package org.extraktion.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.extraktion.templatesCreation.SuperElementObject;
import org.extraktion.templatesCreation.behaviour.ParallelGatewayElementObject;

/**
 * Gruppen Listener für parallele Gateways.
 * 
 * @author Sascha Buelles
 *
 */
public class ExecutionListenerImplParallelGateway extends StandardExecutionListenerImplementation {

	/**
	 * ID.
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected final SuperElementObject createExecutionObjectWithType(final DelegateExecution execution) {
		SuperElementObject parallelGatewayElementObject = new ParallelGatewayElementObject(execution);

		parallelGatewayElementObject.setelementName("ParallelGatewayElement");

		return parallelGatewayElementObject;
	}
}
