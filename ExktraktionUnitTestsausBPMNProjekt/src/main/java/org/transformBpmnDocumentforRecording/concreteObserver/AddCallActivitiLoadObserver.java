package org.transformBpmnDocumentforRecording.concreteObserver;

import java.util.List;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.CallActivity;
import org.activiti.bpmn.model.Process;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.transformBpmnDocumentforRecording.transformationLogik.AbstaractLoadObserver;
import org.transformBpmnDocumentforRecording.transformationLogik.HandleBPMNDocuments;

/**
 * Verbinden einer Listeners für CallActivity Elemente mit einem Prozess.
 * 
 * @author Sascha Buelles
 *
 */
public class AddCallActivitiLoadObserver extends AbstaractLoadObserver {
	/**
	 * Logger fuer AddCallActivitiLoadObserver.
	 */
	private static Logger log = LoggerFactory.getLogger(AddCallActivitiLoadObserver.class);

	/**
	 * Standardkonstrutor zum Anmelden in der HandleDokument Klasse.
	 * 
	 * @param handleDocs
	 *            Zentrales Verwaltungdokument
	 */
	public AddCallActivitiLoadObserver(final HandleBPMNDocuments handleDocs) {
		super(handleDocs);
	}

	@Override
	protected final void addListerToElement(final BpmnModel model) {
		List<Process> processes = model.getProcesses();
		ActivitiListener myProcessStartListener = createListenerReferenceforXML();
		for (Process process : processes) {

			List<CallActivity> findFlowElementsOfType = process.findFlowElementsOfType(CallActivity.class);
			for (CallActivity callActivity : findFlowElementsOfType) {
				List<ActivitiListener> executionListeners = callActivity.getExecutionListeners();
				boolean listenerExists = false;
				// check existing Listeners, if this type already exists
				for (ActivitiListener activitiListener : executionListeners) {
					if (activitiListener.getImplementation().equals(myProcessStartListener.getImplementation())) {
						listenerExists = true;
						if (listenerExists) {
							log.error("Prozessdefinition has StartEvent recording listeners already implemented");
						}
					}
				}
				if (!listenerExists) {
					callActivity.getExecutionListeners().add(myProcessStartListener);
				}
			}
		}
	}

	@Override
	protected final ActivitiListener createListenerReferenceforXML() {
		ActivitiListener tli = new ActivitiListener();
		tli.setEvent("end");
		tli.setImplementationType("class");
		tli.setImplementation("org.extraktion.listener.ExecutionListenerImplCallActivity");
		return tli;
	}

}
