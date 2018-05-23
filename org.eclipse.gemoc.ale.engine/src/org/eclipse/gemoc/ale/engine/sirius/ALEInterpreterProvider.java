package org.eclipse.gemoc.ale.engine.sirius;

import org.eclipse.gemoc.ale.engine.AleEngine;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreter;
import org.eclipse.sirius.common.tools.api.interpreter.IInterpreterProvider;

public class ALEInterpreterProvider implements IInterpreterProvider {

	AleEngine engine;
	
	public ALEInterpreterProvider(AleEngine engine) {
		this.engine = engine;
	}
	
	@Override
	public IInterpreter createInterpreter() {
		return new ALESiriusInterpreter(engine);
	}

	@Override
	public boolean provides(String expression) {
		if (expression != null) {
            return expression.startsWith("ale:");
        }
        return false;
	}

}
