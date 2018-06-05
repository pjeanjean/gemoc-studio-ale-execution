/**
 */
package fsmTrace.Steps;

import fsmTrace.States.SpecificState;

import org.eclipse.gemoc.trace.commons.model.trace.SmallStep;

import org.gemoc.sample.legacyfsm.fsm.Transition;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Fsm Transition Fire</b></em>'.
 * <!-- end-user-doc -->
 *
 *
 * @see fsmTrace.Steps.StepsPackage#getFsm_Transition_Fire()
 * @model
 * @generated
 */
public interface Fsm_Transition_Fire extends Fsm_State_Step_AbstractSubStep, SpecificStep, SmallStep<SpecificState> {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model kind="operation" required="true"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='return (org.gemoc.sample.legacyfsm.fsm.Transition) this.getMseoccurrence().getMse().getCaller();'"
	 * @generated
	 */
	Transition getCaller();

} // Fsm_Transition_Fire
