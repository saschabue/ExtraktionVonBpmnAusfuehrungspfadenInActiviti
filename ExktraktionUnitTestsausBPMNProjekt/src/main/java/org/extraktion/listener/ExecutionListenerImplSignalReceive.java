package org.extraktion.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.extraktion.templatesCreation.SuperElementObject;
import org.extraktion.templatesCreation.behaviour.SignalReceiveElementObject;

/**
 * Gruppen Listener für Intermediate Signals Receive Elemente.
 * @author Sascha Buelles
 *
 */
public class ExecutionListenerImplSignalReceive extends StandardExecutionListenerImplementation {

	private static final long serialVersionUID = 1L;

	@Override
	protected final SuperElementObject createExecutionObjectWithType(final DelegateExecution execution) {
		SuperElementObject signalReceiveElementObject = new SignalReceiveElementObject(execution);
		
		signalReceiveElementObject.setelementName("SignalReceiveElement");
		signalReceiveElementObject.setElementDefinitionKey(execution.getCurrentActivityId());
		
		return signalReceiveElementObject;
	}

}
