package org.extraktion.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.extraktion.templatesCreation.SuperElementObject;
import org.extraktion.templatesCreation.behaviour.TimerCatchEventObject;

/**
 * Derzeit nicht genutzt. Vorlage f�r Intermediate Events siehe SignalReceive.
 * Boundary Timer siehe SequenceFlowElement/SequenceFlowRegisteredBoundary.
 * @author Sascha Buelles
 *
 */
public class ExecutionListenerImplTimerCatchEvent extends StandardExecutionListenerImplementation {

	private static final long serialVersionUID = 1L;

	@Override
	protected final SuperElementObject createExecutionObjectWithType(final DelegateExecution execution) {
		SuperElementObject timerCatchEventObject = new TimerCatchEventObject(execution);
		timerCatchEventObject.setelementName("TimerBoundaryEvent");
		timerCatchEventObject.setElementDefinitionKey(execution.getCurrentActivityId());
		
		return timerCatchEventObject;
	}

}
