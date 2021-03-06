package org.integration; 

 import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.PathsettingController;
import org.StandardProcessIntegrationTest;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
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
  


public class J_Test_ServicetaskAndTimerCatchAndSignalCatch1_JUnit extends StandardProcessIntegrationTest{

	@Test
	public void executeProcess() throws Exception {
		String filename = "./src/test/resources/extended/servicetaskAndTimerCatchAndSignalCatch.bpmn20.xml";
		
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
		BpmnModel model = handleDocs.loadDocument("servicetaskAndTimerCatchAndSignalCatch.bpmn20.xml", true);
		handleDocs.saveModelAsDocument(model, "servicetaskAndTimerCatchAndSignalCatch.bpmn20.xml", true);
				
		File assertF = new File("./src/test/resources/extended/servicetaskAndTimerCatchAndSignalCatch.bpmn20.xml");
		Assert.assertTrue(assertF.exists());
		
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		IdentityService identityService = activitiRule.getIdentityService();

		ProcessInstance processInstance =null; 

		Deployment deploy = repositoryService.createDeployment().addInputStream("servicetaskAndTimerCatchAndSignalCatch.bpmn20.xml",
					new FileInputStream(filename)).deploy();

		Map<String, Object> variableMap = new HashMap<String, Object>();

		variableMap.put("entscheidung", 2);
		identityService.setAuthenticatedUserId("kermit");
		processInstance = runtimeService.startProcessInstanceByKey("servicetaskAndTimerCatchAndSignalCatch",variableMap);

		//Take Sequence Flow: Vorangeganenes Objekt:sid-BEF3AD88-3A74-42FA-B3F1-D880AFCF16F9


		//Take Sequence Flow: Vorangeganenes Objekt:sid-4CEC201D-9A27-4565-A96E-CD8E1A119430

		Execution intermediate = runtimeService.createExecutionQuery().activityId("sid-C7BCF5EC-42E4-4E25-BD79-EE7941F52C82").singleResult();
		runtimeService.signalEventReceived("SignalFuerTestProzess", intermediate.getId());

		//Take Sequence Flow: Vorangeganenes Objekt:sid-C7BCF5EC-42E4-4E25-BD79-EE7941F52C82

		//___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("servicetaskAndTimerCatchAndSignalCatch").singleResult();
		Task task_idut2 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active().taskDefinitionKey("idut2").singleResult();
		taskService.setAssignee(task_idut2.getId(),"kermit");
		variableMap.put("entscheidung",  2);
		taskService.complete(task_idut2.getId(), variableMap);
		//___User Task___

		//Take Sequence Flow: Vorangeganenes Objekt:idut2


		//Take Sequence Flow: Vorangeganenes Objekt:sid-87030A65-235E-40AE-9B8C-37A5DDFB2C98

		//"entscheidung", 2;
		System.out.println("Teilprozess (1757541) wurde beendet");

		//_____Testausf?hrung_beendet_____________________________________________
		repositoryService.deleteDeployment(deploy.getId(),true);
		//_____Deployment_wird_gel?scht___________________________________________
		
		TestObjectDesigner.createJUnitTestContent("Test_servicetaskAndTimerCatchAndSignalCatch1");
		Assert.assertTrue(new File(new PathsettingController().getGeneratedJUnitTest() + "ProcessTest_"
				+ "Test_servicetaskAndTimerCatchAndSignalCatch1" + "_JUnit" + ".java").exists());
	}

}