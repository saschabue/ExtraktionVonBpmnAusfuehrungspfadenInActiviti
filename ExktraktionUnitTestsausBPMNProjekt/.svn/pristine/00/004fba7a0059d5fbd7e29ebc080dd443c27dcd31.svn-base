package org.transformBpmnDocumentforRecording.concreteObserver;

import java.util.List;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.IntermediateCatchEvent;
import org.activiti.bpmn.model.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.transformBpmnDocumentforRecording.transformationLogik.AbstaractLoadObserver;
import org.transformBpmnDocumentforRecording.transformationLogik.HandleBPMNDocuments;

/**
 * Verbinden einer Listeners für Signal Receive Elemente mit einem Prozess.
 * 
 * @author Sascha Buelles
 *
 */
public class AddSignalReceiveLoadObserver extends AbstaractLoadObserver {
	/**
	 * Logger fuer AddProcessStartLoadObserver.
	 */
	private static Logger log = LoggerFactory.getLogger(AddSignalReceiveLoadObserver.class);

	/**
	 * Standardkonstrutor zum Anmelden in der HandleDokument Klasse.
	 * 
	 * @param handleDocs
	 *            Zentrales Verwaltungdokument
	 */
	public AddSignalReceiveLoadObserver(final HandleBPMNDocuments handleDocs) {
		super(handleDocs);
	}

	@Override
	protected final void addListerToElement(final BpmnModel model) {
		List<Process> processes = model.getProcesses();
		ActivitiListener signalReceive = createListenerReferenceforXML();
		ActivitiListener timerReceive = createTimerListenerReferenceforXML();

		for (Process process : processes) {

			List<IntermediateCatchEvent> findFlowElementsOfType = process
					.findFlowElementsOfType(IntermediateCatchEvent.class);

			for (IntermediateCatchEvent intermediateCatchEvent : findFlowElementsOfType) {
				if (intermediateCatchEvent.getEventDefinitions().get(0).getClass().getName()
						.equals("org.activiti.bpmn.model.TimerEventDefinition")) {
					List<ActivitiListener> executionListeners = intermediateCatchEvent.getExecutionListeners();
					boolean listenerExists = false;
					// check existing Listeners, if this type already exists
					for (ActivitiListener activitiListener : executionListeners) {
						if (activitiListener.getImplementation().equals(timerReceive.getImplementation())) {
							listenerExists = true;
							if (listenerExists) {
								log.error("Prozessdefinition has StartEvent recording listeners already implemented");
							}
						}
					}
					if (!listenerExists) {
						intermediateCatchEvent.getExecutionListeners().add(timerReceive);
					}
				}

				if (intermediateCatchEvent.getEventDefinitions().get(0).getClass().getName()
						.equals("org.activiti.bpmn.model.SignalEventDefinition")) {
					List<ActivitiListener> executionListeners = intermediateCatchEvent.getExecutionListeners();
					boolean listenerExists = false;
					// check existing Listeners, if this type already exists
					for (ActivitiListener activitiListener : executionListeners) {
						if (activitiListener.getImplementation().equals(signalReceive.getImplementation())) {
							listenerExists = true;
							if (listenerExists) {
								log.error("Prozessdefinition has StartEvent recording listeners already implemented");
							}
						}
					}
					if (!listenerExists) {
						intermediateCatchEvent.getExecutionListeners().add(signalReceive);
					}
				}
			}
		}
	}

	/**
	 * Spezialmethode. Fuer jedes differenzierbare Signal muss ein extra Listener in dieser Klasse angezeigt werden. 
	 * @return Activiti Listener mit TimerCatchEvent Referenz
	 */
	private ActivitiListener createTimerListenerReferenceforXML() {
		ActivitiListener tli = new ActivitiListener();
		tli.setEvent("end");
		tli.setImplementationType("class");
		tli.setImplementation("org.extraktion.listener.ExecutionListenerImplTimerCatchEvent");
		return tli;
	}

	@Override
	protected final ActivitiListener createListenerReferenceforXML() {
		ActivitiListener tli = new ActivitiListener();
		tli.setEvent("end");
		tli.setImplementationType("class");
		tli.setImplementation("org.extraktion.listener.ExecutionListenerImplSignalReceive");
		return tli;
	}

}
