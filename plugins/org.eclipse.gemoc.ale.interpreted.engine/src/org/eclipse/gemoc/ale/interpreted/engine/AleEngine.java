package org.eclipse.gemoc.ale.interpreted.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.acceleo.query.runtime.IService;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecoretools.ale.ALEInterpreter;
import org.eclipse.emf.ecoretools.ale.core.interpreter.services.EvalBodyService;
import org.eclipse.emf.ecoretools.ale.core.interpreter.services.ServiceCallListener;
import org.eclipse.emf.ecoretools.ale.core.parser.Dsl;
import org.eclipse.emf.ecoretools.ale.core.parser.DslBuilder;
import org.eclipse.emf.ecoretools.ale.core.parser.visitor.ParseResult;
import org.eclipse.emf.ecoretools.ale.implementation.Method;
import org.eclipse.emf.ecoretools.ale.implementation.ModelUnit;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gemoc.execution.sequential.javaengine.K3RunConfiguration;
import org.eclipse.gemoc.execution.sequential.javaengine.SequentialModelExecutionContext;
import org.eclipse.gemoc.executionframework.engine.commons.DslHelper;
import org.eclipse.gemoc.executionframework.engine.core.AbstractSequentialExecutionEngine;
import org.eclipse.gemoc.executionframework.extensions.sirius.services.IModelAnimator;
import org.eclipse.gemoc.trace.commons.model.generictrace.GenericDimension;
import org.eclipse.gemoc.trace.commons.model.generictrace.GenericTracedObject;
import org.eclipse.gemoc.trace.commons.model.trace.Step;
import org.eclipse.gemoc.trace.gemoc.api.IMultiDimensionalTraceAddon;
import org.eclipse.gemoc.trace.gemoc.api.ITraceViewListener;
import org.eclipse.gemoc.xdsmlframework.api.engine_addon.IEngineAddon;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreterWithDiagnostic.IEvaluationResult;

import com.google.common.collect.Lists;

public class AleEngine extends AbstractSequentialExecutionEngine<SequentialModelExecutionContext<K3RunConfiguration>, K3RunConfiguration> {

	/**
	 * Root of the model
	 */
	EObject caller;
	
	/**
	 * The semantic from .ale files
	 */
	List<ParseResult<ModelUnit>> parsedSemantics;
	
	List<Object> args;
	
	ALEInterpreter interpreter;

	private String mainOp;

	private String initOp;
	
	private SequentialModelExecutionContext<K3RunConfiguration> executionContext;
	
	@Override
	public String engineKindName() {
		return "ALE Engine";
	}

	@Override
	protected void executeEntryPoint() {
		K3RunConfiguration runConf = (K3RunConfiguration) executionContext.getRunConfiguration();
		IResourceChangeListener modelChange = new IResourceChangeListener() {
			@Override
			public void resourceChanged(IResourceChangeEvent event) {
				if (event.getDelta().findMember(new Path(runConf.getExecutedModelURI().toPlatformString(true))) != null) {
					if (executionContext.getRunConfiguration() instanceof K3RunConfiguration) {
						executionContext.initializeResourceModel();
						K3RunConfiguration runConf = (K3RunConfiguration) executionContext.getRunConfiguration();
							
						String rootPath = runConf.getModelEntryPoint();
						EObject newModel = executionContext.getResourceModel().getEObject(rootPath);
						Object instruction = newModel.eGet(newModel.eClass().getEStructuralFeature("instruction"));
						
						TransactionalEditingDomain domain = TransactionUtil.getEditingDomain(caller.eResource().getResourceSet());
						Command cmd = new RecordingCommand(domain) {
							@Override
							protected void doExecute() {
								try {
								    caller.eSet(caller.eClass().getEStructuralFeature("instruction"), instruction);
								} catch (Exception e) {
									//e.printStackTrace();
								}
							}
						};
						domain.getCommandStack().execute(cmd);
					}
					executeEntryPoint2();
				}
			}
		};
		ResourcesPlugin.getWorkspace().addResourceChangeListener(modelChange, IResourceChangeEvent.POST_CHANGE);

		while (true) {
			Scanner sc = new Scanner(System.in);
			String read = sc.nextLine();
			if (read.equals("q"))
			    break;
		}
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(modelChange);
	}
	
	protected void executeEntryPoint2() {
		if(interpreter != null && parsedSemantics != null) {
			interpreter.addListener(new ServiceCallListener() {
				
				@Override
				public void preCall(IService service, Object[] arguments) {
					if(service instanceof EvalBodyService) {
						boolean isStep = ((EvalBodyService)service).getImplem().getTags().contains("step");
						if(isStep) {
							if (arguments[0] instanceof EObject) {
								EObject currentCaller = (EObject) arguments[0];
								String className = currentCaller.eClass().getName();
								String methodName = service.getName();
								beforeExecutionStep(currentCaller, className, methodName);
							}
						}
					}
				}
				
				@Override
				public void postCall(IService service, Object[] arguments, Object result) {
					if(service instanceof EvalBodyService) {
						boolean isStep = ((EvalBodyService)service).getImplem().getTags().contains("step");
						if(isStep) {
							afterExecutionStep();
						}
					}
				}
			});
			
			//Register animation updater
			IMultiDimensionalTraceAddon traceCandidate = null;
			List<IModelAnimator> animators = new ArrayList<>();
			for (IEngineAddon addon : AleEngine.this.getExecutionContext().getExecutionPlatform().getEngineAddons()) {
				if(addon instanceof IMultiDimensionalTraceAddon) {
					traceCandidate = (IMultiDimensionalTraceAddon) addon;
				}
				else if(addon instanceof IModelAnimator) {
					animators.add((IModelAnimator) addon);
				}
			}
			
			final IMultiDimensionalTraceAddon traceAddon = traceCandidate;
			ITraceViewListener diagramUpdater = new ITraceViewListener() {
				@Override
				public void update() {
					for (IModelAnimator addon : animators) {
						try {
							if(traceAddon != null) {
								Step<?> nextStep = (Step<?>) traceAddon.getTraceExplorer().getCurrentState().getStartedSteps().get(0);
								addon.activate(caller,nextStep);
							}
						} catch (Exception exception) {
							// Update failed
						}
					}
				}
			};
			if(traceAddon != null) {
				traceAddon.getTraceExplorer().registerCommand(diagramUpdater, () -> diagramUpdater.update());
			}
			
			Method entryPoint = getMainOp().orElse(null);
			if(interpreter.getCurrentEngine() != null) { //We ran @init method
				interpreter.getCurrentEngine().eval(caller, entryPoint, Arrays.asList());
			}
			else {
				IEvaluationResult res = interpreter.eval(caller, entryPoint, Arrays.asList(), parsedSemantics);
			}
			
			if(traceAddon != null) {
				traceAddon.getTraceExplorer().removeListener(diagramUpdater);
			}
		
			if (traceAddon != null) {
				for (Object obj : traceAddon.getTrace().getTracedObjects()) {
					if (obj instanceof GenericTracedObject) {
						GenericTracedObject traced = (GenericTracedObject) obj;
						
						for (Object obj2 : traced.getAllDimensions()) {
							GenericDimension dimension = (GenericDimension) obj2;
							System.out.println(dimension.getValues());
						}
					}
				}
			}
		}
	}

	@Override
	protected void initializeModel() {
		Optional<Method> init = getInitOp();
		
		if(interpreter != null && parsedSemantics != null && init.isPresent()) {
			IEvaluationResult res = interpreter.eval(caller, init.get(), args, parsedSemantics);
		}
	}

	@Override
	protected void prepareEntryPoint(SequentialModelExecutionContext<K3RunConfiguration> executionContext) {
		
	}

	@Override
	protected void prepareInitializeModel(SequentialModelExecutionContext<K3RunConfiguration> executionContext) {
		this.executionContext = executionContext;
		if(executionContext.getRunConfiguration() instanceof K3RunConfiguration) {
			K3RunConfiguration runConf = (K3RunConfiguration) executionContext.getRunConfiguration();
			
			// caller
			Resource inputModel = executionContext.getResourceModel();
			String rootPath = runConf.getModelEntryPoint();
			caller = inputModel.getEObject(rootPath);
			
			// dslFile
			org.eclipse.gemoc.dsl.Dsl language = DslHelper.load(runConf.getLanguageName());

			// arguments
			args = Lists.newArrayList(runConf.getModelInitializationArguments().split("\n"));
			
			mainOp = runConf.getExecutionEntryPoint();
			initOp = runConf.getModelInitializationMethod();
			
			Dsl environment = Helper.gemocDslToAleDsl(language);
			interpreter = new ALEInterpreter();
			parsedSemantics = (new DslBuilder(interpreter.getQueryEnvironment(),
					caller.eResource().getResourceSet())).parse(environment);
			
			/*
			 * Init interpreter
			 */
			Set<String> projects = new HashSet<String>();
			Set<String> plugins = new HashSet<String>();

			if(language.eResource().getURI().isPlatformPlugin()) {
				URI dslUri = language.eResource().getURI();
				String dslPlugin = dslUri.segmentsList().get(1);
				plugins.add(dslPlugin);
				
				List<String> ecoreUris = Helper.getEcoreUris(language);
				for(String ecoreURI : ecoreUris) {
					URI uri = URI.createURI(ecoreURI);
					String plugin = uri.segmentsList().get(1);
					plugins.add(plugin);
				}
				
				List<String> aleUris = Helper.getAleUris(language);
				for(String aleURI : aleUris) {
					URI uri = URI.createURI(aleURI);
					String plugin = uri.segmentsList().get(1);
					plugins.add(plugin);
				}
			}
			interpreter.javaExtensions.updateScope(plugins,projects);
			interpreter.javaExtensions.reloadIfNeeded();
		}
	}
	
	public List<ModelUnit> getModelUnits() {
		if(parsedSemantics != null) {
			return 
				parsedSemantics
				.stream()
				.map(p -> p.getRoot())
				.filter(elem -> elem != null)
				.collect(Collectors.toList());
		}
		return Lists.newArrayList();
	}
	
	public ALEInterpreter getInterpreter() {
		return interpreter;
	}
	
	public Optional<Method> getMainOp() {
		if(mainOp != null) {
			List<String> segments = Lists.newArrayList(mainOp.split("::"));
			if(segments.size() >= 2) {
				String opName = segments.get(segments.size() - 1);
				String typeName = segments.get(segments.size() - 2);
				
				return
					getModelUnits()
					.stream()
					.flatMap(unit -> unit.getClassExtensions().stream())
					.filter(xtdCls -> xtdCls.getBaseClass().getName().equals(typeName))
					.flatMap(cls -> cls.getMethods().stream())
					.filter(op -> op.getTags().contains("main"))
					.filter(op -> op.getOperationRef().getName().equals(opName))
					.findFirst();
			}
		}
		return Optional.empty();
	}
	
	public Optional<Method> getInitOp() {
		if(initOp != null) {
			List<String> segments = Lists.newArrayList(initOp.split("::"));
			if(segments.size() >= 2) {
				String opName = segments.get(segments.size() - 1);
				String typeName = segments.get(segments.size() - 2);
				
				return
					getModelUnits()
					.stream()
					.flatMap(unit -> unit.getClassExtensions().stream())
					.filter(xtdCls -> xtdCls.getBaseClass().getName().equals(typeName))
					.flatMap(cls -> cls.getMethods().stream())
					.filter(op -> op.getTags().contains("init"))
					.filter(op -> op.getOperationRef().getName().equals(opName))
					.findFirst();
			}
		}
		return Optional.empty();
	}
}
