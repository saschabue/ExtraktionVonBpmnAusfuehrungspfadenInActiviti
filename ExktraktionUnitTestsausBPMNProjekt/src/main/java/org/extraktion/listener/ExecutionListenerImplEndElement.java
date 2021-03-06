package org.extraktion.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.extraktion.templatesCreation.SuperElementObject;
import org.extraktion.templatesCreation.behaviour.EndEventElementObject;

/**
 *  Gruppen Listener f�r End Elemente.
 * @author Sascha Buelles
 */
public class ExecutionListenerImplEndElement extends StandardExecutionListenerImplementation {

	/**
	 * ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected final SuperElementObject createExecutionObjectWithType(final DelegateExecution execution) {
		SuperElementObject endElementObject = new EndEventElementObject(execution);

		endElementObject.setelementName("EndElement");
		endElementObject.setElementDefinitionKey(execution.getCurrentActivityId()); 

		
		
		return endElementObject;
	}
}
