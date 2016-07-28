package org.extraktion.templatesCreation.behaviour;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.IntermediateCatchEvent;
import org.activiti.bpmn.model.Signal;
import org.activiti.bpmn.model.SignalEventDefinition;
import org.activiti.engine.delegate.DelegateExecution;
import org.extraktion.templatesCreation.SuperElementObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

/**
 * Temple erstellendes Intermediate Signal Receive Element.
 * @author Sascha Buelles
 *
 */
@Entity
@Table(name = "EXTRAKTION")
public class SignalReceiveElementObject extends SuperElementObject {
	/**
	 * Logger.
	 */
	@Transient
	private static Logger log = LoggerFactory
					.getLogger(SignalReceiveElementObject.class);

	/**
	 * Template erstellendes Intermediate Signal Receive Objekt.
	 * @param execution Element.
	 */
	public SignalReceiveElementObject(final DelegateExecution execution) {
		super(execution);
	}

	@Override
	protected final void createTemplate(final DelegateExecution execution) {
		// Abfrage des SignalNamens zur Ausfuehrung im Unittest
		FlowElement flowElement = execution.getEngineServices()
				.getRepositoryService()
				.getBpmnModel(execution.getProcessDefinitionId())
				.getFlowElement(execution.getCurrentActivityId());
		
		IntermediateCatchEvent ice = (IntermediateCatchEvent) flowElement;
		SignalEventDefinition eventDefinition = (SignalEventDefinition) ice
				.getEventDefinitions().get(0);
		
		if (ice.getEventDefinitions().size() > 1) {
			log.warn("Bitte ueberpruefen, "
					+ "dass das richtige Signal abgefangen wurde");
		}
		
		String signalRef = eventDefinition.getSignalRef();
		String sigName = ((Signal) execution.getEngineServices()
				.getRepositoryService()
				.getBpmnModel(execution.getProcessDefinitionId())
				.getSignal(signalRef)).getName();

		ST templateLocal = templateImpl();
		templateLocal.add("SignalElementId", execution.getCurrentActivityId());
		templateLocal.add("SigName", sigName);
		setTemplate(templateLocal);
		log.info("Template erstellt");
	}

	@Override
	protected final void addImportsForElemente() {
		setImportTemplate(
				new ST("import org.activiti.engine.runtime.Execution;"));
	}

	@Override
	protected final ST templateImpl() {
		return new ST(
				"Execution intermediate = "
				+ "runtimeService.createExecutionQuery().activityId(\""
				+ "<SignalElementId>\").singleResult();\n"
				+ "if (intermediate!=null && !intermediate.isEnded()) {\n"
				+ "runtimeService.signalEventReceived(\"<SigName>\", "
				+ "intermediate.getId());\n"
				+ "}");
	}
}
