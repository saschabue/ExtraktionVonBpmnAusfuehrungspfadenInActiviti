package org.extraktion.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.extraktion.templatesCreation.SuperElementObject;
import org.extraktion.templatesCreation.behaviour.StartElementObject;

/**
 * Gruppen Listener f�r Start Elemente.
 * 
 * @author Sascha B�lles
 */
public class ExecutionListenerImplStartElement extends StandardExecutionListenerImplementation {

	private static final long serialVersionUID = 1L;

	@Override
	protected final SuperElementObject createExecutionObjectWithType(final DelegateExecution execution) {
		SuperElementObject startElementObject = new StartElementObject(execution);
		
		startElementObject.setelementName("StartElement");
		startElementObject.setElementDefinitionKey(execution.getCurrentActivityId());
		
		return startElementObject;
	}
}
