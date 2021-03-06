package org.integration;

import org.PathsettingController;
import org.StandardProcessIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.transformBpmnDocumentforRecording.concreteObserver.AddParallelGateWayLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddProcessEndLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddProcessStartLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddSequenceFlowTakeLoadOberserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddSignalReceiveLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddSkriptTaskLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddUserTaskLoadObserver;
import org.transformBpmnDocumentforRecording.transformationLogik.HandleBPMNDocuments;


public class J_Test_Deploy_JUnit extends StandardProcessIntegrationTest {

	@Test
	public void deploydirectly() throws Exception {
		HandleBPMNDocuments handleDocs = new HandleBPMNDocuments();
		new AddUserTaskLoadObserver(handleDocs);
		new AddSequenceFlowTakeLoadOberserver(handleDocs);
		new AddProcessStartLoadObserver(handleDocs);
		new AddProcessEndLoadObserver(handleDocs);
		new AddParallelGateWayLoadObserver(handleDocs);
		new AddSkriptTaskLoadObserver(handleDocs);
		new AddSignalReceiveLoadObserver(handleDocs);

		handleDocs.loadDocument("called_process.bpmn20.xml", true);
		handleDocs.deployModelToEngine("called_process.bpmn20.xml", new PathsettingController().getOriginalTest());
		Assert.assertTrue(activitiRule.getRepositoryService().createDeploymentQuery().count() == 1);
	}

	

}