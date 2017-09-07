
package org.gemoc.sample.legacyfsm.fsm.trace2.tracemanager;

import java.util.List;
import org.eclipse.gemoc.trace.gemoc.api.IStepFactory;

public class FsmTraceStepFactory implements IStepFactory {

	@Override
	public org.eclipse.gemoc.trace.commons.model.trace.Step<?> createStep(
			org.eclipse.gemoc.trace.commons.model.trace.MSE mse, List<Object> parameters, List<Object> result) {

		org.eclipse.gemoc.trace.commons.model.trace.Step<?> step = null;
		org.eclipse.emf.ecore.EClass ec = mse.getCaller().eClass();
		String stepRule = org.eclipse.gemoc.trace.commons.EcoreCraftingUtil.getFQN(ec, ".") + "."
				+ mse.getAction().getName();

		if (stepRule.equalsIgnoreCase("fsm.State.step")) {
			step = fsmTrace.Steps.StepsFactory.eINSTANCE.createFsm_State_Step();
		} else

		if (stepRule.equalsIgnoreCase("fsm.Transition.fire")) {
			step = fsmTrace.Steps.StepsFactory.eINSTANCE.createFsm_Transition_Fire();
		}

		else {
			step = org.eclipse.gemoc.trace.commons.model.generictrace.GenerictraceFactory.eINSTANCE
					.createGenericSequentialStep();
		}

		org.eclipse.gemoc.trace.commons.model.trace.MSEOccurrence mseocc = org.eclipse.gemoc.trace.commons.model.trace.TraceFactory.eINSTANCE
				.createMSEOccurrence();
		mseocc.setMse(mse);
		mseocc.getParameters().addAll(parameters);
		mseocc.getResult().addAll(result);
		step.setMseoccurrence(mseocc);

		return step;
	}
}
