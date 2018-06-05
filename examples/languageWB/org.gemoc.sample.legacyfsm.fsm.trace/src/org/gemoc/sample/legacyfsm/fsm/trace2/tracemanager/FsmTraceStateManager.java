package org.gemoc.sample.legacyfsm.fsm.trace2.tracemanager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.gemoc.executionframework.engine.core.CommandExecution;

import org.eclipse.gemoc.trace.commons.model.trace.State;
import org.eclipse.gemoc.trace.commons.model.trace.TracedObject;
import org.eclipse.gemoc.trace.gemoc.api.IStateManager;
import org.eclipse.gemoc.trace.gemoc.api.IModelAccessor;

public class FsmTraceStateManager implements IStateManager<State<?, ?>> {

	private final Resource modelResource;

	private final Map<TracedObject<?>, EObject> tracedToExe;

	IModelAccessor modelAccessor;

	public FsmTraceStateManager(Resource modelResource, Map<TracedObject<?>, EObject> tracedToExe,
			IModelAccessor modelAccessor) {
		this.modelResource = modelResource;
		this.tracedToExe = tracedToExe;
		this.modelAccessor = modelAccessor;
	}

	@Override
	public void restoreState(State<?, ?> state) {
		if (modelResource != null && state instanceof fsmTrace.States.SpecificState) {
			try {
				final TransactionalEditingDomain ed = TransactionUtil.getEditingDomain(modelResource);
				if (ed != null) {
					final RecordingCommand command = new RecordingCommand(ed, "") {
						protected void doExecute() {
							restoreStateExecute((fsmTrace.States.SpecificState) state);
						}
					};
					CommandExecution.execute(ed, command);
				}
			} catch (Exception e) {
				throw e;
			}
		}
	}

	private EObject getTracedToExe(EObject tracedObject) {
		return tracedToExe.get(tracedObject);
	}

	private Collection<? extends EObject> getTracedToExe(Collection<? extends EObject> tracedObjects) {
		Collection<EObject> result = new ArrayList<EObject>();
		for (EObject tracedObject : tracedObjects) {
			result.add(tracedToExe.get(tracedObject));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private void restoreStateExecute(fsmTrace.States.SpecificState state) {
		for (fsmTrace.States.SpecificValue value : state.getValues()) {
			if (value instanceof fsmTrace.States.StateMachine_consummedString_Value) {
				final fsmTrace.States.fsm.TracedStateMachine tracedObject = (fsmTrace.States.fsm.TracedStateMachine) value
						.eContainer().eContainer();
				org.gemoc.sample.legacyfsm.fsm.StateMachine exeObject = (org.gemoc.sample.legacyfsm.fsm.StateMachine) getTracedToExe(
						tracedObject);
				modelAccessor.setValue(exeObject, "consummedString",
						((fsmTrace.States.StateMachine_consummedString_Value) value).getConsummedString());
			} else if (value instanceof fsmTrace.States.StateMachine_currentState_Value) {
				final fsmTrace.States.fsm.TracedStateMachine tracedObject = (fsmTrace.States.fsm.TracedStateMachine) value
						.eContainer().eContainer();
				org.gemoc.sample.legacyfsm.fsm.StateMachine exeObject = (org.gemoc.sample.legacyfsm.fsm.StateMachine) getTracedToExe(
						tracedObject);
				modelAccessor.setValue(exeObject, "currentState",
						((fsmTrace.States.StateMachine_currentState_Value) value).getCurrentState());
			} else if (value instanceof fsmTrace.States.StateMachine_producedString_Value) {
				final fsmTrace.States.fsm.TracedStateMachine tracedObject = (fsmTrace.States.fsm.TracedStateMachine) value
						.eContainer().eContainer();
				org.gemoc.sample.legacyfsm.fsm.StateMachine exeObject = (org.gemoc.sample.legacyfsm.fsm.StateMachine) getTracedToExe(
						tracedObject);
				modelAccessor.setValue(exeObject, "producedString",
						((fsmTrace.States.StateMachine_producedString_Value) value).getProducedString());
			} else if (value instanceof fsmTrace.States.StateMachine_unprocessedString_Value) {
				final fsmTrace.States.fsm.TracedStateMachine tracedObject = (fsmTrace.States.fsm.TracedStateMachine) value
						.eContainer().eContainer();
				org.gemoc.sample.legacyfsm.fsm.StateMachine exeObject = (org.gemoc.sample.legacyfsm.fsm.StateMachine) getTracedToExe(
						tracedObject);
				modelAccessor.setValue(exeObject, "unprocessedString",
						((fsmTrace.States.StateMachine_unprocessedString_Value) value).getUnprocessedString());
			}
		}
	}
}
