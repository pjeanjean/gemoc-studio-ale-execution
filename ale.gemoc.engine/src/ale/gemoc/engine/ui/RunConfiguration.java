package ale.gemoc.engine.ui;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

public class RunConfiguration extends org.eclipse.gemoc.executionframework.engine.ui.commons.RunConfiguration {

	public static final String ALE_DSL_FILE = "ALE_DSL_FILE";
	
	String _dslFile;
	
	public RunConfiguration(ILaunchConfiguration launchConfiguration) throws CoreException {
		super(launchConfiguration);
	}

	@Override
	protected void extractInformation() throws CoreException {
		super.extractInformation();
		_dslFile = getAttribute(ALE_DSL_FILE, "");
	}
	
	public String getDslFile() {
		return _dslFile;
	}
}
