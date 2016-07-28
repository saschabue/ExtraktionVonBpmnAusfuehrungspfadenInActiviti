package org.extraktion.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.bpmn.behavior.BoundaryEventActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.PvmProcessElement;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.extraktion.templatesCreation.SuperElementObject;
import org.extraktion.templatesCreation.behaviour.SequenceFlowElementObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Gruppen Listener für Sequenzflüsse.
 * 
 * @author Sascha Buelles
 *
 */
public class ExecutionListenerImplSequenceFlowElement extends StandardExecutionListenerImplementation {

	/**
	 * Log für Sequenceflow Listener.
	 */
	private static Logger log = LoggerFactory.getLogger(ExecutionListenerImplSequenceFlowElement.class);
	private static final long serialVersionUID = 1L;

	@Override
	protected final SuperElementObject createExecutionObjectWithType(final DelegateExecution execution) {
		// Ist das vorangegangene Element ein Boundary Element, verweist
		// execution auf das Element, dem das Boundary Element zugeordnet ist.
		// Nicht das Boundary Element selbst => boundaryElementExecuted() !!
		PvmProcessElement eventSource = ((ExecutionEntity) execution).getEventSource();
		TransitionImpl sourceElement = (TransitionImpl) eventSource;

		if (sourceElement.getSource().getActivityBehavior() instanceof BoundaryEventActivityBehavior) {
			new SequenceFlowRegisteredBoundary(execution, sourceElement);
		}

		SuperElementObject sequenceFlowElementObject = new SequenceFlowElementObject(execution);

		// DefinitionKey des SequenceFlow wird nicht gesetzt, das dieser auf das
		// vorangeagnegene Element verweist.
		sequenceFlowElementObject.setElementDefinitionKey("Seq_" + execution.getCurrentActivityId());
		sequenceFlowElementObject.setelementName("SequenceFlowElement");
		

		log.info("Flow taken");
		return sequenceFlowElementObject;
	}

}
