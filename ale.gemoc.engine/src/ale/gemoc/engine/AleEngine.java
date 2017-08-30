package ale.gemoc.engine;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.acceleo.query.runtime.IService;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecoretools.ale.ALEInterpreter;
import org.eclipse.emf.ecoretools.ale.core.interpreter.services.EvalBodyService;
import org.eclipse.emf.ecoretools.ale.core.interpreter.services.ServiceCallListener;
import org.eclipse.emf.ecoretools.ale.core.parser.Dsl;
import org.eclipse.emf.ecoretools.ale.core.parser.DslBuilder;
import org.eclipse.emf.ecoretools.ale.core.parser.visitor.ParseResult;
import org.eclipse.emf.ecoretools.ale.ide.WorkbenchDsl;
import org.eclipse.emf.ecoretools.ale.implementation.ModelUnit;
import org.eclipse.gemoc.executionframework.engine.core.AbstractSequentialExecutionEngine;
import org.eclipse.gemoc.executionframework.extensions.sirius.services.IModelAnimator;
import org.eclipse.gemoc.trace.commons.model.trace.Step;
import org.eclipse.gemoc.trace.gemoc.api.IMultiDimensionalTraceAddon;
import org.eclipse.gemoc.trace.gemoc.api.ITraceViewListener;
import org.eclipse.gemoc.xdsmlframework.api.core.IExecutionContext;
import org.eclipse.gemoc.xdsmlframework.api.engine_addon.IEngineAddon;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreterWithDiagnostic.IEvaluationResult;

import com.google.common.collect.Lists;

import ale.gemoc.engine.ui.RunConfiguration;

public class AleEngine extends AbstractSequentialExecutionEngine {

	/**
	 * Root of the model
	 */
	EObject caller;
	
	/**
	 * The semantic from .ale files
	 */
	List<ParseResult<ModelUnit>> parsedSemantics;
	
	String args;
	
	ALEInterpreter interpreter;
	
	@Override
	public String engineKindName() {
		return "ALE Engine";
	}

	@Override
	protected void executeEntryPoint() {
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
							Step<?> nextStep = (Step<?>) traceAddon.getTraceExplorer().getCurrentState().getStartedSteps().get(0);
							addon.activate(caller,nextStep);
						} catch (Exception exception) {
							// Update failed
						}
					}
				}
			};
			traceAddon.getTraceExplorer().registerCommand(diagramUpdater, () -> diagramUpdater.update());
			
			IEvaluationResult res = interpreter.eval(caller, Arrays.asList(), parsedSemantics);
			
			traceAddon.getTraceExplorer().removeListener(diagramUpdater);
		}

	}

	@Override
	protected void initializeModel() {
		System.out.println("DEBUG:initModel");
		// TODO run @init
		
	}

	@Override
	protected void prepareEntryPoint(IExecutionContext executionContext) {
		
	}

	@Override
	protected void prepareInitializeModel(IExecutionContext executionContext) {
		if(executionContext.getRunConfiguration() instanceof RunConfiguration) {
			RunConfiguration runConf = (RunConfiguration) executionContext.getRunConfiguration();
			
			// caller
			Resource inputModel = executionContext.getResourceModel();
			String rootPath = runConf.getModelEntryPoint();
			caller = inputModel.getEObject(rootPath);
			
			// dslFile
			String dslFile = runConf.getDslFile();

			// arguments
			args = runConf.getModelInitializationArguments();
			
			try {
				Dsl environment = new WorkbenchDsl(dslFile);
				interpreter = new ALEInterpreter();
				parsedSemantics = (new DslBuilder(interpreter.getQueryEnvironment(),
						caller.eResource().getResourceSet())).parse(environment);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
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
	
}
