/**
 */
package fsmTrace.States;

import org.gemoc.sample.legacyfsm.fsm.State;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>State Machine current State Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link fsmTrace.States.StateMachine_currentState_Value#getCurrentState <em>Current State</em>}</li>
 * </ul>
 *
 * @see fsmTrace.States.StatesPackage#getStateMachine_currentState_Value()
 * @model annotation="http://www.modelexecution.org/trace/XMOF executionMetamodelElement='null'"
 * @generated
 */
public interface StateMachine_currentState_Value extends SpecificReferenceValue<State> {
	/**
	 * Returns the value of the '<em><b>Current State</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Current State</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Current State</em>' reference.
	 * @see #setCurrentState(State)
	 * @see fsmTrace.States.StatesPackage#getStateMachine_currentState_Value_CurrentState()
	 * @model
	 * @generated
	 */
	State getCurrentState();

	/**
	 * Sets the value of the '{@link fsmTrace.States.StateMachine_currentState_Value#getCurrentState <em>Current State</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Current State</em>' reference.
	 * @see #getCurrentState()
	 * @generated
	 */
	void setCurrentState(State value);

} // StateMachine_currentState_Value
