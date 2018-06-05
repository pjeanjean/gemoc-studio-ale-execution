/**
 */
package fsmTrace.States.fsm;

import fsmTrace.States.StatesPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see fsmTrace.States.fsm.FsmFactory
 * @model kind="package"
 * @generated
 */
public interface FsmPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "fsm";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "fsmTrace_fsm";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	FsmPackage eINSTANCE = fsmTrace.States.fsm.impl.FsmPackageImpl.init();

	/**
	 * The meta object id for the '{@link fsmTrace.States.fsm.impl.TracedNamedElementImpl <em>Traced Named Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see fsmTrace.States.fsm.impl.TracedNamedElementImpl
	 * @see fsmTrace.States.fsm.impl.FsmPackageImpl#getTracedNamedElement()
	 * @generated
	 */
	int TRACED_NAMED_ELEMENT = 0;

	/**
	 * The feature id for the '<em><b>Dimensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACED_NAMED_ELEMENT__DIMENSIONS = StatesPackage.SPECIFIC_TRACED_OBJECT__DIMENSIONS;

	/**
	 * The number of structural features of the '<em>Traced Named Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACED_NAMED_ELEMENT_FEATURE_COUNT = StatesPackage.SPECIFIC_TRACED_OBJECT_FEATURE_COUNT + 0;

	/**
	 * The meta object id for the '{@link fsmTrace.States.fsm.impl.TracedStateMachineImpl <em>Traced State Machine</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see fsmTrace.States.fsm.impl.TracedStateMachineImpl
	 * @see fsmTrace.States.fsm.impl.FsmPackageImpl#getTracedStateMachine()
	 * @generated
	 */
	int TRACED_STATE_MACHINE = 1;

	/**
	 * The feature id for the '<em><b>Dimensions</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACED_STATE_MACHINE__DIMENSIONS = TRACED_NAMED_ELEMENT__DIMENSIONS;

	/**
	 * The feature id for the '<em><b>Original Object</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACED_STATE_MACHINE__ORIGINAL_OBJECT = TRACED_NAMED_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>State Machine consummed String Dimension</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACED_STATE_MACHINE__STATE_MACHINE_CONSUMMED_STRING_DIMENSION = TRACED_NAMED_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>State Machine current State Dimension</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACED_STATE_MACHINE__STATE_MACHINE_CURRENT_STATE_DIMENSION = TRACED_NAMED_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>State Machine produced String Dimension</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACED_STATE_MACHINE__STATE_MACHINE_PRODUCED_STRING_DIMENSION = TRACED_NAMED_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>State Machine unprocessed String Dimension</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACED_STATE_MACHINE__STATE_MACHINE_UNPROCESSED_STRING_DIMENSION = TRACED_NAMED_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Traced State Machine</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACED_STATE_MACHINE_FEATURE_COUNT = TRACED_NAMED_ELEMENT_FEATURE_COUNT + 5;


	/**
	 * Returns the meta object for class '{@link fsmTrace.States.fsm.TracedNamedElement <em>Traced Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Traced Named Element</em>'.
	 * @see fsmTrace.States.fsm.TracedNamedElement
	 * @generated
	 */
	EClass getTracedNamedElement();

	/**
	 * Returns the meta object for class '{@link fsmTrace.States.fsm.TracedStateMachine <em>Traced State Machine</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Traced State Machine</em>'.
	 * @see fsmTrace.States.fsm.TracedStateMachine
	 * @generated
	 */
	EClass getTracedStateMachine();

	/**
	 * Returns the meta object for the reference '{@link fsmTrace.States.fsm.TracedStateMachine#getOriginalObject <em>Original Object</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Original Object</em>'.
	 * @see fsmTrace.States.fsm.TracedStateMachine#getOriginalObject()
	 * @see #getTracedStateMachine()
	 * @generated
	 */
	EReference getTracedStateMachine_OriginalObject();

	/**
	 * Returns the meta object for the containment reference '{@link fsmTrace.States.fsm.TracedStateMachine#getStateMachine_consummedString_Dimension <em>State Machine consummed String Dimension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>State Machine consummed String Dimension</em>'.
	 * @see fsmTrace.States.fsm.TracedStateMachine#getStateMachine_consummedString_Dimension()
	 * @see #getTracedStateMachine()
	 * @generated
	 */
	EReference getTracedStateMachine_StateMachine_consummedString_Dimension();

	/**
	 * Returns the meta object for the containment reference '{@link fsmTrace.States.fsm.TracedStateMachine#getStateMachine_currentState_Dimension <em>State Machine current State Dimension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>State Machine current State Dimension</em>'.
	 * @see fsmTrace.States.fsm.TracedStateMachine#getStateMachine_currentState_Dimension()
	 * @see #getTracedStateMachine()
	 * @generated
	 */
	EReference getTracedStateMachine_StateMachine_currentState_Dimension();

	/**
	 * Returns the meta object for the containment reference '{@link fsmTrace.States.fsm.TracedStateMachine#getStateMachine_producedString_Dimension <em>State Machine produced String Dimension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>State Machine produced String Dimension</em>'.
	 * @see fsmTrace.States.fsm.TracedStateMachine#getStateMachine_producedString_Dimension()
	 * @see #getTracedStateMachine()
	 * @generated
	 */
	EReference getTracedStateMachine_StateMachine_producedString_Dimension();

	/**
	 * Returns the meta object for the containment reference '{@link fsmTrace.States.fsm.TracedStateMachine#getStateMachine_unprocessedString_Dimension <em>State Machine unprocessed String Dimension</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>State Machine unprocessed String Dimension</em>'.
	 * @see fsmTrace.States.fsm.TracedStateMachine#getStateMachine_unprocessedString_Dimension()
	 * @see #getTracedStateMachine()
	 * @generated
	 */
	EReference getTracedStateMachine_StateMachine_unprocessedString_Dimension();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	FsmFactory getFsmFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link fsmTrace.States.fsm.impl.TracedNamedElementImpl <em>Traced Named Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see fsmTrace.States.fsm.impl.TracedNamedElementImpl
		 * @see fsmTrace.States.fsm.impl.FsmPackageImpl#getTracedNamedElement()
		 * @generated
		 */
		EClass TRACED_NAMED_ELEMENT = eINSTANCE.getTracedNamedElement();

		/**
		 * The meta object literal for the '{@link fsmTrace.States.fsm.impl.TracedStateMachineImpl <em>Traced State Machine</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see fsmTrace.States.fsm.impl.TracedStateMachineImpl
		 * @see fsmTrace.States.fsm.impl.FsmPackageImpl#getTracedStateMachine()
		 * @generated
		 */
		EClass TRACED_STATE_MACHINE = eINSTANCE.getTracedStateMachine();

		/**
		 * The meta object literal for the '<em><b>Original Object</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACED_STATE_MACHINE__ORIGINAL_OBJECT = eINSTANCE.getTracedStateMachine_OriginalObject();

		/**
		 * The meta object literal for the '<em><b>State Machine consummed String Dimension</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACED_STATE_MACHINE__STATE_MACHINE_CONSUMMED_STRING_DIMENSION = eINSTANCE.getTracedStateMachine_StateMachine_consummedString_Dimension();

		/**
		 * The meta object literal for the '<em><b>State Machine current State Dimension</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACED_STATE_MACHINE__STATE_MACHINE_CURRENT_STATE_DIMENSION = eINSTANCE.getTracedStateMachine_StateMachine_currentState_Dimension();

		/**
		 * The meta object literal for the '<em><b>State Machine produced String Dimension</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACED_STATE_MACHINE__STATE_MACHINE_PRODUCED_STRING_DIMENSION = eINSTANCE.getTracedStateMachine_StateMachine_producedString_Dimension();

		/**
		 * The meta object literal for the '<em><b>State Machine unprocessed String Dimension</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACED_STATE_MACHINE__STATE_MACHINE_UNPROCESSED_STRING_DIMENSION = eINSTANCE.getTracedStateMachine_StateMachine_unprocessedString_Dimension();

	}

} //FsmPackage
