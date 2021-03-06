package org.integration; 

 import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.PathsettingController;
import org.StandardProcessIntegrationTest;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.extraktion.TestObjectDesigner;
import org.junit.Assert;
import org.junit.Test;
import org.transformBpmnDocumentforRecording.concreteObserver.AddCallActivitiLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddProcessEndLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddProcessStartLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddSequenceFlowTakeLoadOberserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddSignalReceiveLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddUserTaskLoadObserver;
import org.transformBpmnDocumentforRecording.transformationLogik.HandleBPMNDocuments;
  


public class J_Test_SignalAndTimerEvent_JUnit extends StandardProcessIntegrationTest{

	@Test
	public void executeProcess() throws Exception {
		String filename = "./src/test/resources/extended/IntermediateTimerCatch.bpmn20.xml";
		
		HandleBPMNDocuments handleDocs = new HandleBPMNDocuments();
		new AddUserTaskLoadObserver(handleDocs);
		new AddSequenceFlowTakeLoadOberserver(handleDocs);
		new AddProcessStartLoadObserver(handleDocs);
		new AddProcessEndLoadObserver(handleDocs);
		new AddCallActivitiLoadObserver(handleDocs);
		new AddSignalReceiveLoadObserver(handleDocs);

		// Automatisches hinzufuegen der Listener, welche als Observer
		// angehaengt werden. Fuer Call Activities muessen alle aufgerufenen
		// Prozesse erweitert werden
		BpmnModel model = handleDocs.loadDocument("IntermediateTimerCatch.bpmn20.xml", true);
		handleDocs.saveModelAsDocument(model, "IntermediateTimerCatch.bpmn20.xml", true);
		
		File assertF = new File("./src/test/resources/extended/IntermediateTimerCatch.bpmn20.xml");
		Assert.assertTrue(assertF.exists());
		
		
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		
		IdentityService identityService = activitiRule.getIdentityService();

		ProcessInstance processInstance =null; 

		Deployment deploy = repositoryService.createDeployment().addInputStream("IntermediateTimerCatch.bpmn20.xml",
					new FileInputStream(filename)).deploy();

		Map<String, Object> variableMap = new HashMap<String, Object>();

		identityService.setAuthenticatedUserId("kermit");
		processInstance = runtimeService.startProcessInstanceByKey("intermediateTimerCatch",variableMap);

		//Take Sequence Flow: Vorangeganenes Objekt:startevent1

		//________________Timer fired:_TCE___________________
		ManagementService managementService = activitiRule.getManagementService();
		Job singleResult = managementService.createJobQuery().processInstanceId(processInstance.getId()).singleResult();
		managementService.executeJob(singleResult.getId());
		//Take Sequence Flow: Vorangeganenes Objekt:timerintermediatecatchevent1

		Execution intermediate = runtimeService.createExecutionQuery().activityId("signalintermediatecatchevent1").singleResult();
		runtimeService.signalEventReceived("Signal", intermediate.getId());

		//Take Sequence Flow: Vorangeganenes Objekt:signalintermediatecatchevent1

		System.out.println("Teilprozess (1910014) wurde beendet");

		//_____Testausf?hrung_beendet_____________________________________________
		repositoryService.deleteDeployment(deploy.getId(),true);
		//_____Deployment_wird_gel?scht___________________________________________
		

		TestObjectDesigner.createJUnitTestContent("Test_SignalAndTimerEvent");
		Assert.assertTrue(new File(new PathsettingController().getGeneratedJUnitTest() + "ProcessTest_"
				+ "Test_SignalAndTimerEvent" + "_JUnit" + ".java").exists());
	}

}