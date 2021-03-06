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
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.extraktion.TestObjectDesigner;
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

public class J_Test_servicetaskAndTimerCatchAndSignalCatch_JUnit extends StandardProcessIntegrationTest {

	private String filename = "./src/test/resources/extended/servicetaskAndTimerCatchAndSignalCatch.bpmn20.xml";

	@Test
	public void executeProcess() throws Exception {
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
		BpmnModel model = handleDocs.loadDocument("servicetaskAndTimerCatchAndSignalCatch.bpmn20.xml", true);
		handleDocs.saveModelAsDocument(model, "servicetaskAndTimerCatchAndSignalCatch.bpmn20.xml", true);

		File assertF = new File("./src/test/resources/extended/servicetaskAndTimerCatchAndSignalCatch.bpmn20.xml");
		Assert.assertTrue(assertF.exists());

		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		IdentityService identityService = activitiRule.getIdentityService();

		ProcessInstance processInstance = null;

		Deployment deploy = repositoryService.createDeployment()
				.addInputStream("servicetaskAndTimerCatchAndSignalCatch.bpmn20.xml", new FileInputStream(filename))
				.deploy();

		Map<String, Object> variableMap = new HashMap<String, Object>();

		variableMap.put("entscheidung", 1);
		identityService.setAuthenticatedUserId("kermit");
		processInstance = runtimeService.startProcessInstanceByKey("servicetaskAndTimerCatchAndSignalCatch",
				variableMap);

		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-BEF3AD88-3A74-42FA-B3F1-D880AFCF16F9

		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-4CEC201D-9A27-4565-A96E-CD8E1A119430

		// ________________ServiceTask_Elementname:_ST1___________________
		System.out.println("Ausf?hrung Service Task: ST1. Variablenwerte:");
		System.out.println("Variable variable1 : true");
		System.out.println("Variable entscheidung : 1");
		// ________________ServiceTask____________________

		// Take Sequence Flow: Vorangeganenes Objekt:idst1

		// ___User Task___
		processInstance = runtimeService.createProcessInstanceQuery()
				.processDefinitionKey("servicetaskAndTimerCatchAndSignalCatch").singleResult();
		Task task_idut1 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("idut1").singleResult();
		taskService.setAssignee(task_idut1.getId(), "kermit");
		variableMap.put("variable1", true);
		variableMap.put("entscheidung", 1);
		taskService.complete(task_idut1.getId(), variableMap);
		// ___User Task___

		// Take Sequence Flow: Vorangeganenes Objekt:idut1

		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-87030A65-235E-40AE-9B8C-37A5DDFB2C98

		// "variable1", true;
		// "entscheidung", 1;
		System.out.println("Teilprozess (1757527) wurde beendet");

		// _____Testausf?hrung_beendet_____________________________________________
		repositoryService.deleteDeployment(deploy.getId(), true);
		// _____Deployment_wird_gel?scht___________________________________________
		TestObjectDesigner.createJUnitTestContent("Test_servicetaskAndTimerCatchAndSignalCatch");

		Assert.assertTrue(new File(new PathsettingController().getGeneratedJUnitTest() + "ProcessTest_"
				+ "Test_servicetaskAndTimerCatchAndSignalCatch" + "_JUnit" + ".java").exists());
	}

}