package development;

import org.activiti.bpmn.model.BpmnModel;

import org.transformBpmnDocumentforRecording
	.concreteObserver.AddCallActivitiLoadObserver;
import org.transformBpmnDocumentforRecording
	.concreteObserver.AddParallelGateWayLoadObserver;
import org.transformBpmnDocumentforRecording
	.concreteObserver.AddProcessEndLoadObserver;
import org.transformBpmnDocumentforRecording
	.concreteObserver.AddProcessStartLoadObserver;
import org.transformBpmnDocumentforRecording
	.concreteObserver.AddSequenceFlowTakeLoadOberserver;
import org.transformBpmnDocumentforRecording
	.concreteObserver.AddServiceTaskLoadObserver;
import org.transformBpmnDocumentforRecording
	.concreteObserver.AddSignalReceiveLoadObserver;
import org.transformBpmnDocumentforRecording
	.concreteObserver.AddSkriptTaskLoadObserver;
import org.transformBpmnDocumentforRecording
	.concreteObserver.AddUserTaskLoadObserver;
import org.transformBpmnDocumentforRecording
	.transformationLogik.HandleBPMNDocuments;

/**
 * Diese Klasse ist eine Beispielklasse fuer Anwender, um benutzerdefinierte
 * Listener an die BPMN Elemente einer Prozessdefinition anzufuegen.
 * 
 * @author Sascha Buelles
 *
 */
 public class ExampleClient {
	/**
	 * Der Prozessname kann fuer original und extended veraendert werden. Der
	 * Example Client vereinfacht diesen Sachverhalt. Die Dokumente liegen in
	 * unterschiedlichen Verzeichnissen.
	 */
	private static final String DOCUMENT = "letzterTest.bpmn20.xml";

	/**
	 * Auswahl der Verzeichnisse. s.o. True = TestVerzeichnis
	 */
	private static final boolean ISTEST = false;

	/**
	 * Standard Getter.
	 * 
	 * @return documentname
	 */
	public static String getDocument() {
		return DOCUMENT;
	}

	/**
	 * Standard Getter.
	 * 
	 * @return Wenn Testverzeichnis genutzt wird
	 */
	public static boolean isIstest() {
		return ISTEST;
	}

	/**
	 * Beispiel Client Implementierung.
	 * 
	 * @author Sascha Buelles
	 * @param args Standard Arguments
	 */
	public static void main(final String[] args) {
		HandleBPMNDocuments handleDocs = new HandleBPMNDocuments();
		new AddSequenceFlowTakeLoadOberserver(handleDocs);
		new AddProcessStartLoadObserver(handleDocs);
		new AddProcessEndLoadObserver(handleDocs);
		new AddSignalReceiveLoadObserver(handleDocs);
		new AddCallActivitiLoadObserver(handleDocs);
		new AddParallelGateWayLoadObserver(handleDocs);
		new AddSkriptTaskLoadObserver(handleDocs);
		new AddServiceTaskLoadObserver(handleDocs);
		new AddUserTaskLoadObserver(handleDocs);

		// Automatisches hinzufuegen der Listener, welche die Observer
		// angehaengt wurden, sobald das Dokument geladen wird
		BpmnModel model = handleDocs.loadDocument(DOCUMENT, ISTEST);

		// Pfad zur Datei wird in zentraler pfadsettings.xml Datei angelegt
		handleDocs.saveModelAsDocument(model, DOCUMENT, ISTEST);
	}

}
