package org.transformBpmnDocumentforRecording.transformationLogik;

import org.activiti.bpmn.model.BpmnModel;

/**
 * @author Sascha Buelles
 *
 */
public interface IHandleDocuments {

	/**
	 * @param fileName
	 *            name of bpmn or bpmn.xml file
	 * @return Das erweiterte Prozessmodell
	 * @param isTest
	 *            Speicherung in zentral definiertem Verzeichnis fuer Test oder
	 *            manuelle Extraktionen
	 */
	BpmnModel loadDocument(String fileName, boolean isTest);

	/**
	 * Speichert das Modell als Datei.
	 * 
	 * @param model
	 *            erweitertes Prozessmodell
	 * @param fileName
	 *            Name der Datei, in der das Modell abgelegt werden soll
	 * @param isTest
	 *            Speicherung in zentral definiertem Verzeichnis fuer Test oder
	 *            manuelle Extraktionen
	 */
	void saveModelAsDocument(BpmnModel model, String fileName, boolean isTest);

	/**
	 * Load the created model to directory
	 */
	/**
	 * Direktes deployen des Modells auf im Workspace definierte Engine.
	 * 
	 * @param fileName
	 *            Name der zu deployenden Prozessdefinition
	 * @param filePath
	 *            Pfad der zu deployenden Prozessdefinition
	 */
	void deployModelToEngine(String fileName, String filePath);

}
