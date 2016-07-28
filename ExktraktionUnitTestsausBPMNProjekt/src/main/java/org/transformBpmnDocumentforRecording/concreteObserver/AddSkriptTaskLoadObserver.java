package org.transformBpmnDocumentforRecording.concreteObserver;

import java.util.List;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.ScriptTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.transformBpmnDocumentforRecording.transformationLogik.AbstaractLoadObserver;
import org.transformBpmnDocumentforRecording.transformationLogik.HandleBPMNDocuments;

/**
 * Verbinden einer Listeners für Skript Task Elemente mit einem Prozess.
 * 
 * @author Sascha Buelles
 *
 */
public class AddSkriptTaskLoadObserver extends AbstaractLoadObserver {
	/**
	 * Logger fuer AddSkriptTaskLoadObserver.
	 */
	private static Logger log = LoggerFactory.getLogger(AddSkriptTaskLoadObserver.class);

	/**
	 * Standardkonstrutor zum Anmelden in der HandleDokument Klasse.
	 * 
	 * @param handleDocs
	 *            Zentrales Verwaltungdokument
	 */
	public AddSkriptTaskLoadObserver(final HandleBPMNDocuments handleDocs) {
		super(handleDocs);
	}

	@Override
	protected final void addListerToElement(final BpmnModel model) {
		List<Process> processes = model.getProcesses();
		ActivitiListener myProcessStartListener = createListenerReferenceforXML();
		for (Process process : processes) {

			List<ScriptTask> findFlowElementsOfType = process.findFlowElementsOfType(ScriptTask.class);
			for (ScriptTask skriptTask : findFlowElementsOfType) {
				List<ActivitiListener> executionListeners = skriptTask.getExecutionListeners();
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
					skriptTask.getExecutionListeners().add(myProcessStartListener);
				}
			}
		}

	}

	@Override
	protected final ActivitiListener createListenerReferenceforXML() {
		ActivitiListener tli = new ActivitiListener();
		tli.setEvent("end");
		tli.setImplementationType("class");
		tli.setImplementation("org.extraktion.listener.ExecutionListenerImplSkriptTask");
		return tli;
	}

}
