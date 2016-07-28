package org.transformBpmnDocumentforRecording.concreteObserver;

import java.util.List;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.UserTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.transformBpmnDocumentforRecording
			.transformationLogik.AbstaractLoadObserver;
import org.transformBpmnDocumentforRecording
			.transformationLogik.HandleBPMNDocuments;

/**
 * UserTask Observer zur Erweiterung einer Prozessdefinition.
 * 
 * @author Sascha Buelles
 *
 */
public class AddUserTaskLoadObserver extends AbstaractLoadObserver {
	/**
	 * Logger fuer UserTask Observer.
	 */
	private static Logger log = LoggerFactory
			.getLogger(AddUserTaskLoadObserver.class);

	/**
	 * Standard Konstruktor mit Kontroller Objekt.
	 * 
	 * @param handleDocs
	 *            Kontroller Objekt zur Bestimmung der zu ueberwachenden
	 *            Elemente.
	 */
	public AddUserTaskLoadObserver(final HandleBPMNDocuments handleDocs) {
		super(handleDocs);
	}

	@Override
	protected final void addListerToElement(final BpmnModel model) {
		ActivitiListener myTaskListener = createListenerReferenceforXML();
		List<Process> processes = model.getProcesses();

		// Fuer jeden Prozess(Pool) in der Definition werden alle User Tasks
		// herausgesucht und der TaskListener eingefuegt.
		
		for (Process process : processes) {
			List<UserTask> findFlowElementsOfType =
					process.findFlowElementsOfType(UserTask.class);
			
			for (UserTask userTask : findFlowElementsOfType) {
				// Durchsuche Listener des Tasks. Wenn myTaskListener nicht
				// schon existiert fuege ihn hinzu: Darf nur 1x existieren, da
				// er
				// ein neues Testelement erzeugen wird.

				List<ActivitiListener> taskListeners = userTask.getTaskListeners();
				boolean listenerExists = false;
				// Ueberpruefen ob der Listener bereits fuer das Element existiert

				for (ActivitiListener activitiListener : taskListeners) {
					if (activitiListener.getImplementation()
							.equals(myTaskListener.getImplementation())) {
						listenerExists = true;
						if (listenerExists) {
							log.error("Prozessdefinition hat bereits " 
									+ "UserTask Activiti Listener implementiert.");
						}
					}
				}
				if (!listenerExists) {
					userTask.getTaskListeners().add(myTaskListener);
				}
			}
		}
	}

	@Override
	protected final ActivitiListener createListenerReferenceforXML() {
		ActivitiListener tli = new ActivitiListener();
		tli.setEvent("complete");
		tli.setImplementationType("class");
		tli.setImplementation(
				"org.extraktion.listener.TaskListenerImplUserTask");
		return tli;
	}
}
