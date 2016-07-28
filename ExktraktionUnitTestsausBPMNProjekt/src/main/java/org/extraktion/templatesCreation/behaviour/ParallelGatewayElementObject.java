package org.extraktion.templatesCreation.behaviour;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.ParallelGateway;
import org.activiti.engine.delegate.DelegateExecution;
import org.extraktion.templatesCreation.SuperElementObject;
import org.stringtemplate.v4.ST;

/**
 * Temple erstellendes Objekt.
 * @author Sascha Buelles
 *
 */
@Entity
@Table(name = "EXTRAKTION")
public class ParallelGatewayElementObject extends SuperElementObject {

	/**
	 * Paralleles Gateway.
	 * @param execution Element.
	 */
	public ParallelGatewayElementObject(final DelegateExecution execution) {
		super(execution);
	}

	@Override
	protected final void createTemplate(final DelegateExecution execution) {
		// Keine Veränderung der Prozessvariablen (siehe BPMN Definition) möglich
		FlowElement flowElement = execution.getEngineServices().getRepositoryService()
				.getBpmnModel(execution.getProcessDefinitionId()).getFlowElement(execution.getCurrentActivityId());
		ParallelGateway pg = (ParallelGateway) flowElement;
		int inc = pg.getIncomingFlows().size();
		int out = pg.getOutgoingFlows().size();
		
		
		ST templateLocal = templateImpl();
		templateLocal.add("actualElement", execution.getId());
		templateLocal.add("incomingFlows", inc);
		templateLocal.add("outgoingFlows", out);
		setTemplate(templateLocal);
	}

	@Override
	protected final void addImportsForElemente() {
		setImportTemplate(new ST(" "));

	}

	@Override
	protected final ST templateImpl() {
		return new ST("//___Paralleles Gateway___\n"
				+ "System.out.println(\"Paralleles Gateway <actualElement> wurde ausgeführt\");\n"
				+ "System.out.println(\"Incoming Flows: <incomingFlows>, Outgoing Flows: <outgoingFlows>\");\n"
				+ "//___Paralleles Gateway___\n");
	}


}
