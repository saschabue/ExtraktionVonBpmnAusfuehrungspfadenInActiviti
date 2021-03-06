package org.integration;


import java.io.File;

import org.PathsettingController;
import org.StandardProcessIntegrationTest;
import org.activiti.bpmn.model.BpmnModel;
import org.extraktion.TestObjectDesigner;
import org.junit.Assert;
import org.junit.Test;
import org.transformBpmnDocumentforRecording.concreteObserver.AddProcessEndLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddProcessStartLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddSequenceFlowTakeLoadOberserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddSignalReceiveLoadObserver;
import org.transformBpmnDocumentforRecording.transformationLogik.HandleBPMNDocuments;

public class J_Test_WriteNonExistingExtraktion extends StandardProcessIntegrationTest {

//	private String filename = "./src/test/resources/extended/callingActivitiprocess.bpmn20.xml";
//	private String filename1 = "./src/test/resources/extended/called_process.bpmn20.xml";

	@Test
	public void executeProcess() throws Exception {

		HandleBPMNDocuments handleDocs = new HandleBPMNDocuments();
		
		new AddSequenceFlowTakeLoadOberserver(handleDocs);
		new AddProcessStartLoadObserver(handleDocs);
		new AddProcessEndLoadObserver(handleDocs);
		new AddSignalReceiveLoadObserver(handleDocs);
		

		// Automatisches hinzufuegen der Listener, welche als Observer
		// angehaengt werden. Fuer Call Activities muessen alle aufgerufenen
		// Prozesse erweitert werden
		BpmnModel model = handleDocs.loadDocument("IntermediateTimerCatch.bpmn20.xml", true);
		handleDocs.saveModelAsDocument(model, "IntermediateTimerCatch.bpmn20.xml", true);
		
		TestObjectDesigner.createJUnitTestContent("Not_existing");
		//Es existieren keine Extraktionselemente. Die Datei darf nicht geschrieben werden
		Assert.assertFalse(new File(new PathsettingController().getGeneratedJUnitTest() + "ProcessTest_"
				+ "Not_existing" + "_JUnit" + ".java").exists());
		
	}

}