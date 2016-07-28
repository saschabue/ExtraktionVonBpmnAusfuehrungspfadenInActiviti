package org.transformBpmnDocumentforRecording.transformationLogik;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BpmnModel;

/**
 * Abstrakter Observer zur Implementierung der Observer fur das Erweitern einer Prozessdefintion.
 * @author Sascha Buelles
 *
 */
public abstract class AbstaractLoadObserver implements Observer {
	/**
	 * Objekt zur Verwaltung einer Erweiterungsroutine.
	 */
	private HandleBPMNDocuments handleDocs = null;

	/**
	 * Standardkonstruktor des abstrakten Observers zum Laden einer Erweiterung.
	 * 
	 * @param setHandleDocs
	 *            Objekt zur Verwaltung einer Erweiterungsroutine.
	 */
	public AbstaractLoadObserver(final HandleBPMNDocuments setHandleDocs) {
		this.handleDocs = setHandleDocs;
		this.handleDocs.addOberserver(this);
	}

	@Override
	public final void update(final BpmnModel model) {
		this.addListerToElement(model);
	}

	/**
	 * Fuege dem Modell einen AusfuehrungListener hinzu.
	 * 
	 * @param model
	 *            Verwendetes Modell.
	 */
	protected abstract void addListerToElement(BpmnModel model);

	/**
	 * Erstellt die Listener Referenz, die in der XML Datei eingetragen werden
	 * muss.
	 * 
	 * @return Activiti Listener
	 */
	protected abstract ActivitiListener createListenerReferenceforXML();
}
