package org.extraktion.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.extraktion.templatesCreation.SuperElementObject;
import org.extraktion.templatesCreation.behaviour.ServiceTaskElementObject;

/**
 * Gruppen Listener für Service Tasks.
 * @author Sascha Buelles
 *
 */
public class ExecutionListenerImplServiceTask extends StandardExecutionListenerImplementation {

	/**
	 * ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected final SuperElementObject createExecutionObjectWithType(final DelegateExecution execution) {
		SuperElementObject serviceTaskObject = new ServiceTaskElementObject(execution);

		serviceTaskObject.setelementName("ServiceTaskElement");
		serviceTaskObject.setElementDefinitionKey(execution.getCurrentActivityId());
		
		return serviceTaskObject;
	}

}
