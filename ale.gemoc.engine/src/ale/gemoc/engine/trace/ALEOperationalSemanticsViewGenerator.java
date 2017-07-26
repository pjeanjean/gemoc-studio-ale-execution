package ale.gemoc.engine.trace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.acceleo.query.ast.Call;
import org.eclipse.acceleo.query.ast.Expression;
import org.eclipse.acceleo.query.parser.AstValidator;
import org.eclipse.acceleo.query.runtime.impl.ValidationServices;
import org.eclipse.acceleo.query.validation.type.EClassifierType;
import org.eclipse.acceleo.query.validation.type.IType;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecoretools.ale.ALEInterpreter;
import org.eclipse.emf.ecoretools.ale.core.parser.visitor.ParseResult;
import org.eclipse.emf.ecoretools.ale.core.validation.BaseValidator;
import org.eclipse.emf.ecoretools.ale.ide.resource.AleResourceFactory;
import org.eclipse.emf.ecoretools.ale.implementation.BehavioredClass;
import org.eclipse.emf.ecoretools.ale.implementation.ExtendedClass;
import org.eclipse.emf.ecoretools.ale.implementation.Method;
import org.eclipse.emf.ecoretools.ale.implementation.ModelUnit;
import org.eclipse.emf.ecoretools.ale.implementation.RuntimeClass;
import org.eclipse.gemoc.opsemanticsview.gen.OperationalSemanticsViewGenerator;

import com.google.common.collect.Lists;

import fr.inria.diverse.melange.metamodel.melange.Language;
import opsemanticsview.OperationalSemanticsView;
import opsemanticsview.OpsemanticsviewFactory;
import opsemanticsview.Rule;

public class ALEOperationalSemanticsViewGenerator implements OperationalSemanticsViewGenerator {

	Map<Method,Rule> methodToRule = new HashMap<>();
	BaseValidator aleValidator;
	List<Method> allMethods;

	@Override
	public boolean canHandle(Language language, IProject melangeProject) {
		return language.getAle() != null && !language.getAle().isEmpty();
	}

	@Override
	public OperationalSemanticsView generate(Language language, IProject melangeProject) {
		
		OperationalSemanticsView result = OpsemanticsviewFactory.eINSTANCE.createOperationalSemanticsView();
		
		ResourceSet rs = new ResourceSetImpl();
		Resource executionMetamodelResource = rs.getResource(URI.createURI(language.getSyntax().getEcoreUri()), true);
		EPackage executionMetamodel = 
			executionMetamodelResource
			.getContents()
			.stream()
			.filter(o -> o instanceof EPackage)
			.map(o -> (EPackage) o)
			.findFirst()
			.get();
		result.setExecutionMetamodel(executionMetamodel);
		result.setAbstractSyntax(null);
		
		List<ModelUnit> units = loadModelUnits(language.getAle(),rs);
		allMethods = getAllMethod(units);
		
		findDynamicParts(units, result);		
		allMethods.forEach(mtd -> inspectForBigStep(mtd, result));
		
		return result;
	}
	
	private List<ModelUnit> loadModelUnits(String dslFile, ResourceSet rs) {
		
		ALEInterpreter interpreter = new ALEInterpreter();
		rs.getResourceFactoryRegistry()
			.getExtensionToFactoryMap()
			.put("dsl", new AleResourceFactory(interpreter.getQueryEnvironment(),rs));
		
		
		Resource dslResource = rs.getResource(URI.createURI(dslFile),true);
		List<ModelUnit> res = 
			dslResource
			.getContents()
			.stream()
			.filter(o -> o instanceof ModelUnit)
			.map(o -> (ModelUnit) o)
			.collect(Collectors.toList());
		
		aleValidator = new BaseValidator(interpreter.getQueryEnvironment(), Arrays.asList());
		List<ParseResult<ModelUnit>> validationInput = new ArrayList<>();
		for(ModelUnit unit : res) {
			ParseResult<ModelUnit> mockParseRes = new ParseResult<ModelUnit>();
			mockParseRes.setRoot(unit);
			validationInput.add(mockParseRes);
		}
		aleValidator.validate(validationInput);
		
		return res;
//		List<ParseResult<ModelUnit>> parsedSemantics = null;
//		try {
//			Dsl environment = new WorkbenchDsl(dslFile);
//			ALEInterpreter interpreter = new ALEInterpreter();
//			parsedSemantics = (new DslBuilder(interpreter.getQueryEnvironment(),rs)).parse(environment);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
//		
//		if(parsedSemantics != null) {
//			return 
//				parsedSemantics
//				.stream()
//				.map(p -> p.getRoot())
//				.filter(elem -> elem != null)
//				.collect(Collectors.toList());
//		}
//		return Lists.newArrayList();
	}
	
	private void findDynamicParts(List<ModelUnit> units, OperationalSemanticsView view) {
		for(ModelUnit unit : units) {
			for(ExtendedClass xtdCls : unit.getClassExtensions()) {
				
				EClass clsFragment = xtdCls.getFragment();
				
				//Put the EClass into a Resource to be able to save model
				if(clsFragment.eResource() == null) {
					clsFragment = EcoreUtil.copy(clsFragment);
					xtdCls.eResource().getContents().add(clsFragment);
				}
				
				List<EStructuralFeature> movedFeature = Lists.newArrayList(clsFragment.getEStructuralFeatures());
				for (EStructuralFeature feature : movedFeature) {
					xtdCls.getBaseClass().getEStructuralFeatures().add(feature); //FIXME: dirty hack
					view.getDynamicProperties().add(feature);
				}
			}
			
			for(RuntimeClass newCls : unit.getClassDefinitions()) {
				
				EClass clsFragment = newCls.getFragment();
				
				//Put the EClass into a Resource to be able to save model
				if(clsFragment.eResource() == null) {
					clsFragment = EcoreUtil.copy(clsFragment);
					newCls.eResource().getContents().add(clsFragment);
				}
				
				view.getDynamicClasses().add(clsFragment);
				
				for (EStructuralFeature feature : clsFragment.getEStructuralFeatures()) {
					view.getDynamicProperties().add(feature);
				}
			}
		}
	}
	
	private Rule getRuleOfMethod(Method method, OperationalSemanticsView view) {
		if (methodToRule.containsKey(method))
			return methodToRule.get(method);
		else {
			Rule rule = OpsemanticsviewFactory.eINSTANCE.createRule();
			rule.setOperation(method.getOperationRef());
			view.getRules().add(rule);
			
			if(method.eContainer() instanceof ExtendedClass) {
				EClass eClass = ((ExtendedClass)method.eContainer()).getBaseClass();
				rule.setContainingClass(eClass);
			}
			else {
				EClass eClass = ((BehavioredClass)method.eContainer()).getFragment();
				rule.setContainingClass(eClass);
			}

			rule.setStepRule(method.getTags().contains("step"));
			rule.setMain(method.getTags().contains("main"));
			methodToRule.put(method, rule);
			return rule;
		}
	}
	
	private void inspectForBigStep(Method method, OperationalSemanticsView view) {
		Rule rule = getRuleOfMethod(method, view);
		
		//Called methods
		List<Method> calledMethods = findCalledMethods(method);
		for (Method calledMethod : calledMethods) {
			Rule calledRule = getRuleOfMethod(calledMethod, view);
			rule.getCalledRules().add(calledRule);
		}
		
		//Overrides
		List<Method> overridedMethods = findOverridedMethods(method);
		for (Method overridedMethod : overridedMethods) {
			Rule overidedRule = getRuleOfMethod(overridedMethod, view);
			overidedRule.getOverridenBy().add(rule); //TODO: double check :/
		}
	}

	private List<Method> findCalledMethods(Method method) {
		List<Method> calledMethods = new ArrayList<>();
		// TODO Auto-generated method stub
		
		TreeIterator<Object> allBodyContent = EcoreUtil.getAllContents(method.getBody(), true);
		allBodyContent.forEachRemaining(elem -> {
			if(elem instanceof Call) {
				Call call = (Call) elem;
				Expression caller = call.getArguments().get(0);
				Set<IType> types = aleValidator.getPossibleTypes(caller);
				
				//search for the best method
				Method candidate = null;
				for(Method mtd : allMethods) {
					EClass cls = getContainingClass(mtd);
					EClassifierType callerType = new EClassifierType(aleValidator.getQryEnv(),cls);  
					boolean isMatching = 
							call.getServiceName().equals(mtd.getOperationRef().getName()) &&
							mtd.getOperationRef().getEParameters().size() == call.getArguments().size() - 1 &&
							types.stream().anyMatch(type -> callerType.isAssignableFrom(type));
					if(candidate == null && isMatching) {
						candidate = mtd;
					}
					else if(isMatching && getContainingClass(candidate).isSuperTypeOf(getContainingClass(mtd))){
						candidate = mtd;
					}
				}
				if(candidate != null) {
					calledMethods.add(candidate);
				}
			}
		});
		
		return calledMethods;
	}

	private List<Method> findOverridedMethods(Method method) {
		List<Method> overridedMethods = new ArrayList<>();
		
		for(Method mtd : allMethods) {
			EClass methodClass = getContainingClass(method);
			EClass mtdClass = getContainingClass(mtd);
			boolean isMatching = 
					method != mtd &&
					method.getOperationRef().getName().equals(mtd.getOperationRef().getName()) &&
					method.getOperationRef().getEParameters().size() == mtd.getOperationRef().getEParameters().size() &&
					methodClass.isSuperTypeOf(mtdClass);
			if(isMatching) {
				overridedMethods.add(mtd);
			}
		}
		
		return overridedMethods;
	}
	
	private List<Method> getAllMethod(List<ModelUnit> units) {
		List<Method> allMethods =
				units
				.stream()
				.flatMap(unit -> unit.getClassExtensions().stream())
				.flatMap(cls -> cls.getMethods().stream())
				.collect(Collectors.toList());
			allMethods.addAll(
				units
				.stream()
				.flatMap(unit -> unit.getClassDefinitions().stream())
				.flatMap(cls -> cls.getMethods().stream())
				.collect(Collectors.toList())
				);
		return allMethods;
	}
	
	private EClass getContainingClass(Method mtd) {
		if(mtd.eContainer() instanceof ExtendedClass) {
			return ((ExtendedClass)mtd.eContainer()).getBaseClass();
		}
		else {
			return mtd.getOperationRef().getEContainingClass();
		}
	}
}
