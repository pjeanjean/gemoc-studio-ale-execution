package org.eclipse.gemoc.ale.language.wb.sample.deployer.wizards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.gemoc.ale.language.sample.deployer.Activator;
import org.eclipse.gemoc.sequential.language.wb.sample.deployer.wizards.AbstractExampleWizard;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class AleFsmExampleWizard extends AbstractExampleWizard {

	@Override
	protected AbstractUIPlugin getContainerPlugin() {
		return Activator.getDefault();
	}

	@Override
	protected Collection<ProjectDescriptor> getProjectDescriptors() {
		List<ProjectDescriptor> projects = new ArrayList<ProjectDescriptor>(1);
		// extract project in a compilation friendly way, give the compiler a chance to be more efficient and succeed without a "clean all"
		projects.add(new ProjectDescriptor(Activator.PLUGIN_ID, "zips/org.eclipse.gemoc.sample.ale.fsm.model.zip", 	"org.eclipse.gemoc.sample.ale.fsm.model"));
		projects.add(new ProjectDescriptor(Activator.PLUGIN_ID, "zips/org.eclipse.gemoc.sample.ale.fsm.design.zip", 	"org.eclipse.gemoc.sample.ale.fsm.design"));
		projects.add(new ProjectDescriptor(Activator.PLUGIN_ID, "zips/org.eclipse.gemoc.sample.ale.xfsm.design.zip", 	"org.eclipse.gemoc.sample.ale.xfsm.design"));
return projects;
	}

}
