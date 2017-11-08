package ale.gemoc.engine;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecoretools.ale.ide.WorkbenchDsl;
import org.eclipse.gemoc.dsl.SimpleValue;

public class Helper {

	public static org.eclipse.emf.ecoretools.ale.core.parser.Dsl gemocDslToAleDsl(org.eclipse.gemoc.dsl.Dsl language) {
		List<String> ecoreUris = 
			language
			.getAbstractSyntax()
			.getValues()
			.stream()
			.filter(v -> v instanceof SimpleValue)
			.map(v -> (SimpleValue) v)
			.filter(v -> v.getName().equals("ecore"))
			.flatMap(ecore -> ecore.getValues().stream())
			.collect(Collectors.toList());
			
		List<String> ecoreFileUris = ecoreUris
	         .stream()
	         .map(elem -> URI.createFileURI(WorkbenchDsl.convertToFile(elem)).toString())
	         .collect(Collectors.toList());
		
		List<String> aleUris = 
				language
				.getSemantic()
				.getValues()
				.stream()
				.filter(v -> v instanceof SimpleValue)
				.map(v -> (SimpleValue) v)
				.filter(v -> v.getName().equals("ale"))
				.flatMap(ecore -> ecore.getValues().stream())
				.collect(Collectors.toList());
		
		return new WorkbenchDsl(ecoreFileUris,aleUris);
	}
	
}
