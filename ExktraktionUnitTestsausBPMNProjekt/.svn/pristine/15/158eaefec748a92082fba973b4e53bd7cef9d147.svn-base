package org.extraktion.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.extraktion.templatesCreation.SuperElementObject;
import org.extraktion.templatesCreation.behaviour.SkriptTaskElementObject;

/**
 * Gruppen Listener für Skript Tasks.
 * @author Sascha Buelles
 *
 */
public class ExecutionListenerImplSkriptTask extends StandardExecutionListenerImplementation {

	private static final long serialVersionUID = 1L;

	@Override
	protected final SuperElementObject createExecutionObjectWithType(final DelegateExecution execution) {
		SuperElementObject skriptTaskElementObject = new SkriptTaskElementObject(execution);

		skriptTaskElementObject.setelementName("SkriptTaskElement");
		skriptTaskElementObject.setElementDefinitionKey(execution.getCurrentActivityId());
		
		return skriptTaskElementObject;
	}

}
