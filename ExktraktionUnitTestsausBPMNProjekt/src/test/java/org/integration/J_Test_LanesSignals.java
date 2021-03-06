package org.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.extraktion.TestObjectDesigner;
import org.extraktion.coverageanalyses.Coverage;
import org.junit.Assert;
import org.junit.Test;
import org.transformBpmnDocumentforRecording.concreteObserver.AddCallActivitiLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddParallelGateWayLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddProcessEndLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddProcessStartLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddSequenceFlowTakeLoadOberserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddServiceTaskLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddSignalReceiveLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddSkriptTaskLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddUserTaskLoadObserver;
import org.transformBpmnDocumentforRecording.transformationLogik.HandleBPMNDocuments;

public class J_Test_LanesSignals extends StandardProcessIntegrationTest {
	private String filename = "./src/test/resources/extended/LanesSignals.bpmn20.xml";

	@Test
	public void testCompleteWorkflow() {
		
		HandleBPMNDocuments handleDocs = new HandleBPMNDocuments();
		new AddUserTaskLoadObserver(handleDocs);
		new AddSequenceFlowTakeLoadOberserver(handleDocs);
		new AddProcessStartLoadObserver(handleDocs);
		new AddProcessEndLoadObserver(handleDocs);
		new AddCallActivitiLoadObserver(handleDocs);
		new AddParallelGateWayLoadObserver(handleDocs);
		new AddSkriptTaskLoadObserver(handleDocs);
		new AddServiceTaskLoadObserver(handleDocs);
		new AddSignalReceiveLoadObserver(handleDocs);

		// Automatisches hinzufuegen der Listener, welche als Observer
		// angehaengt
		// wurden, sobald das Dokument geladen wird
		BpmnModel model = handleDocs.loadDocument("LanesSignals.bpmn20.xml", true);
		handleDocs.saveModelAsDocument(model, "LanesSignals.bpmn20.xml", true);

		File assertF = new File("./src/test/resources/extended/LanesSignals.bpmn20.xml");
		Assert.assertTrue(assertF.exists());

		// DO THE WORKFLOW
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		IdentityService identityService = activitiRule.getIdentityService();
		ProcessInstance processInstance = null;

		Deployment deploy = null;
		try {
			deploy = repositoryService.createDeployment()
					.addInputStream("LanesSignals.bpmn20.xml", new FileInputStream(filename)).deploy();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Map<String, Object> variableMap = new HashMap<String, Object>();

		identityService.setAuthenticatedUserId("kermit");
		processInstance = runtimeService.startProcessInstanceByKey("process_pool2", variableMap);
		Coverage coverage = new Coverage();
		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-C4EB1A47-9F07-4597-849A-0B1FFE329DDA

		// ___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("process_pool2")
				.singleResult();
		Task task_usertask1 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("usertask1").singleResult();
		taskService.setAssignee(task_usertask1.getId(), "kermit");
		taskService.complete(task_usertask1.getId(), variableMap);
		// ___User Task___
		
		Assert.assertTrue(coverage.getUserTaskCoverage() == (double)1/(double)3);
		// Take Sequence Flow: Vorangeganenes Objekt:usertask1

		// ___Paralleles Gateway___
		System.out.println("Paralleles Gateway 1342509 wurde ausgef?hrt");
		System.out.println("Incoming Flows: 1, Outgoing Flows: 2");
		// ___Paralleles Gateway___

		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-38967529-5B93-4FF3-BE25-268AB9E0C39D

		// Neues Start Element ausgef?hrt___

		identityService.setAuthenticatedUserId("kermit");

		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-949291EA-E64E-44BA-A6CC-4378D7D08ABB

		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-07D2DF8C-F3EA-4C9A-A55B-EB50B8143F6E

		// ___Paralleles Gateway___
		System.out.println("Paralleles Gateway 1342509 wurde ausgef?hrt");
		System.out.println("Incoming Flows: 1, Outgoing Flows: 2");
		// ___Paralleles Gateway___

		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-38967529-5B93-4FF3-BE25-268AB9E0C39D

		// ________________SkriptTask_Elementname:_ST1___________________
		System.out.println("Ausf?hrung ?Skript Task: ST1. Variablenwerte:");
		// ________________SkriptTask____________________

		// Take Sequence Flow: Vorangeganenes Objekt:scripttask1

		// ___Paralleles Gateway___
		System.out.println("Paralleles Gateway 1342501 wurde ausgef?hrt");
		System.out.println("Incoming Flows: 2, Outgoing Flows: 1");
		// ___Paralleles Gateway___

		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-15B3305E-28EC-4B81-84BA-0A391E23EF10

		// ___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("process_pool")
				.singleResult();
		Task task_usertask3 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("usertask3").singleResult();
		taskService.setAssignee(task_usertask3.getId(), "kermit");
		taskService.complete(task_usertask3.getId(), variableMap);
		// ___User Task___

		// Take Sequence Flow: Vorangeganenes Objekt:usertask3

		System.out.println("Teilprozess (1342511) wurde beendet");
		// ___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("process_pool2")
				.singleResult();
		Task task_usertask2 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("usertask2").singleResult();
		taskService.setAssignee(task_usertask2.getId(), "kermit");
		taskService.complete(task_usertask2.getId(), variableMap);
		// ___User Task___

		// Take Sequence Flow: Vorangeganenes Objekt:usertask2

		System.out.println("Teilprozess (1342501) wurde beendet");

		// _____Testausf?hrung_beendet_____________________________________________
		repositoryService.deleteDeployment(deploy.getId(), true);
		// _____Deployment_wird_gel?scht___________________________________________

		TestObjectDesigner.createJUnitTestContent("Test_Test_LanesSignals");

		Assert.assertTrue(new File(new PathsettingController().getGeneratedJUnitTest() + "ProcessTest_"
				+ "Test_Test_LanesSignals" + "_JUnit" + ".java").exists());
	}
}
