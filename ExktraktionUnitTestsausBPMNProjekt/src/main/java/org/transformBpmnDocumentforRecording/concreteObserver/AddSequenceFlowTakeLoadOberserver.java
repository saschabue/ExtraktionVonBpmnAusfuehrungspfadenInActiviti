package org.transformBpmnDocumentforRecording.concreteObserver;

import java.util.List;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.transformBpmnDocumentforRecording.transformationLogik.AbstaractLoadObserver;
import org.transformBpmnDocumentforRecording.transformationLogik.HandleBPMNDocuments;

/**
 * Verbinden einer Listeners für Sequenzfluss Elemente mit einem Prozess.
 * 
 * @author Sascha Buelles
 *
 */
public class AddSequenceFlowTakeLoadOberserver extends AbstaractLoadObserver {
	/**
	 * Logger fuer AddSequenceFlowTakeLoadOberserver.
	 */
	private static Logger log = LoggerFactory.getLogger(AddProcessStartLoadObserver.class);

	/**
	 * Standardkonstrutor zum Anmelden in der HandleDokument Klasse.
	 * 
	 * @param handleDocs
	 *            Zentrales Verwaltungdokument
	 */
	public AddSequenceFlowTakeLoadOberserver(final HandleBPMNDocuments handleDocs) {
		super(handleDocs);
	}

	@Override
	protected final void addListerToElement(final BpmnModel model) {
		List<Process> processes = model.getProcesses();
		ActivitiListener mySequenceTakeListener = createListenerReferenceforXML();
		for (Process process : processes) {

			List<SequenceFlow> findFlowElementsOfType = process.findFlowElementsOfType(SequenceFlow.class);
			for (SequenceFlow sequenceFlowTake : findFlowElementsOfType) {
				List<ActivitiListener> executionListeners = sequenceFlowTake.getExecutionListeners();
				boolean listenerExists = false;
				// check existing Listeners, if this type already exists
				for (ActivitiListener activitiListener : executionListeners) {
					if (activitiListener.getImplementation().equals(mySequenceTakeListener.getImplementation())) {
						listenerExists = true;
						if (listenerExists) {
							log.error("Prozessdefinition has StartEvent recording listeners already implemented");
						}
					}
				}
				if (!listenerExists) {
					sequenceFlowTake.getExecutionListeners().add(mySequenceTakeListener);
				}
			}
		}
	}

	@Override
	protected final ActivitiListener createListenerReferenceforXML() {
		ActivitiListener tli = new ActivitiListener();
		tli.setEvent("take");
		tli.setImplementationType("class");
		tli.setImplementation("org.extraktion.listener.ExecutionListenerImplSequenceFlowElement");
		return tli;
	}

}
