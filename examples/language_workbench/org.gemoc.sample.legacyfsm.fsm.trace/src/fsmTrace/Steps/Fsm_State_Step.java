/**
 */
package fsmTrace.Steps;

import fsmTrace.States.SpecificState;

import org.eclipse.gemoc.trace.commons.model.trace.SequentialStep;

import org.gemoc.sample.legacyfsm.fsm.State;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fsm State Step</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see fsmTrace.Steps.StepsPackage#getFsm_State_Step()
 * @model
 * @generated
 */
public interface Fsm_State_Step extends SpecificStep, SequentialStep<Fsm_State_Step_AbstractSubStep, SpecificState> {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return (org.gemoc.sample.legacyfsm.fsm.State) this.getMseoccurrence().getMse().getCaller();'"
	 * @generated
	 */
	State getCaller();

} // Fsm_State_Step
