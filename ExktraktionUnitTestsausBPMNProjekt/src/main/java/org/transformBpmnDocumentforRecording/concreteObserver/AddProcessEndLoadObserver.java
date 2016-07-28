package org.transformBpmnDocumentforRecording.concreteObserver;

import java.util.List;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.transformBpmnDocumentforRecording.transformationLogik.AbstaractLoadObserver;
import org.transformBpmnDocumentforRecording.transformationLogik.HandleBPMNDocuments;


/**
 * Verbinden einer Listeners für Parallele GateWay Elemente mit einem Prozess.
 * 
 * @author Sascha Buelles
 *
 */
public class AddProcessEndLoadObserver extends AbstaractLoadObserver {
	/**
	 * Logger fuer AddProcessEndLoadObserver.
	 */
	private static Logger log = LoggerFactory.getLogger(AddProcessEndLoadObserver.class);

	/**
	 * Standardkonstrutor zum Anmelden in der HandleDokument Klasse.
	 * 
	 * @param handleDocs
	 *            Zentrales Verwaltungdokument
	 */
	public AddProcessEndLoadObserver(final HandleBPMNDocuments handleDocs) {
		super(handleDocs);
	}

	@Override
	protected final void addListerToElement(final BpmnModel model) {
		List<Process> processes = model.getProcesses();
		ActivitiListener myProcessEndListener = createListenerReferenceforXML();
		for (Process process : processes) {

			List<EndEvent> findFlowElementsOfType = process.findFlowElementsOfType(EndEvent.class);
			for (EndEvent endEvent : findFlowElementsOfType) {
				List<ActivitiListener> executionListeners = endEvent.getExecutionListeners();
				boolean listenerExists = false;
				// check existing Listeners, if this type already exists
				for (ActivitiListener activitiListener : executionListeners) {
					if (activitiListener.getImplementation().equals(myProcessEndListener.getImplementation())) {
						listenerExists = true;
						if (listenerExists) {
							log.error("Prozessdefinition has StartEvent recording listeners already implemented");
						}
					}
				}
				if (!listenerExists) {
					endEvent.getExecutionListeners().add(myProcessEndListener);
				}
			}
		}

	}

	@Override
	protected final ActivitiListener createListenerReferenceforXML() {
		ActivitiListener tli = new ActivitiListener();
		tli.setEvent("end");
		tli.setImplementationType("class");
		tli.setImplementation("org.extraktion.listener.ExecutionListenerImplEndElement");
		return tli;
	}

}
