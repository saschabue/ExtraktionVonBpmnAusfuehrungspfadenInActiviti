package org.extraktion.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.extraktion.templatesCreation.SuperElementObject;
import org.extraktion.templatesCreation.behaviour.CallActivityElementObject;

/**
 * Gruppen Listener für CallActivities.
 * 
 * @author Sascha Buelles
 */
public class ExecutionListenerImplCallActivity extends StandardExecutionListenerImplementation {

	/**
	 * ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected final SuperElementObject createExecutionObjectWithType(final DelegateExecution execution) {
		SuperElementObject callactiviti = new CallActivityElementObject(execution);

		callactiviti.setelementName("CallActiviti");
		callactiviti.setElementDefinitionKey(execution.getCurrentActivityId());
		
		return callactiviti;

	}

}
