package ale.gemoc.engine.ui;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecoretools.ale.ALEInterpreter;
import org.eclipse.emf.ecoretools.ale.core.parser.Dsl;
import org.eclipse.emf.ecoretools.ale.core.parser.DslBuilder;
import org.eclipse.emf.ecoretools.ale.core.parser.visitor.ParseResult;
import org.eclipse.emf.ecoretools.ale.ide.WorkbenchDsl;
import org.eclipse.emf.ecoretools.ale.implementation.Method;
import org.eclipse.emf.ecoretools.ale.implementation.ModelUnit;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

public class SelectMainMethodDialog extends ElementListSelectionDialog {

	String dslFile;
	
	/**
	 * Create a selection dialog displaying all available methods with @main
	 * from elements in 'aspects' weaving 'modelElem'.
	 * If 'modelElem' is null, selection dialog displays all @main.
	 */
	public SelectMainMethodDialog(String dslFile, EObject modelElem, ILabelProvider renderer) {
		super(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), renderer);
		
		this.dslFile = dslFile;
		
		if(dslFile != null)
			update(modelElem);
	}
	
	/**
	 * Display only methods with @main from Aspects applied on modelElem
	 */
	public void update(EObject modelElem){
		
		EClass target = null;
		if(modelElem != null){
			target = modelElem.eClass();
		}
		final EClass finalTarget = target;
		
		
		try {
			Dsl environment = new WorkbenchDsl(dslFile);
			ALEInterpreter interpreter = new ALEInterpreter();
			List<ParseResult<ModelUnit>> parsedSemantics = (new DslBuilder(interpreter.getQueryEnvironment())).parse(environment);
			List<Method> mainOperations =
	    		parsedSemantics
		    	.stream()
		    	.filter(sem -> sem.getRoot() != null)
		    	.map(sem -> sem.getRoot())
		    	.flatMap(unit -> unit.getClassExtensions().stream())
		    	.filter(xtdCls -> finalTarget == null || finalTarget == xtdCls.getBaseClass())
		    	.flatMap(xtdCls -> xtdCls.getMethods().stream())
	    		.filter(op -> op.getTags().contains("main"))
	    		.collect(Collectors.toList());
			
			this.setElements(mainOperations.toArray());
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
