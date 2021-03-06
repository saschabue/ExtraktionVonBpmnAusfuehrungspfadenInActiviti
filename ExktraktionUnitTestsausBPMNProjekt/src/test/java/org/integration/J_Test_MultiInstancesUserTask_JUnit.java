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
import org.transformBpmnDocumentforRecording.concreteObserver.AddProcessEndLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddProcessStartLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddSequenceFlowTakeLoadOberserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddUserTaskLoadObserver;
import org.transformBpmnDocumentforRecording.transformationLogik.HandleBPMNDocuments;

public class J_Test_MultiInstancesUserTask_JUnit extends StandardProcessIntegrationTest {

	@Test
	public void executeProcess() throws Exception {
		String filename = "./src/test/resources/extended/called_process.bpmn20.xml";

		HandleBPMNDocuments handleDocs = new HandleBPMNDocuments();
		new AddUserTaskLoadObserver(handleDocs);
		new AddSequenceFlowTakeLoadOberserver(handleDocs);
		new AddProcessStartLoadObserver(handleDocs);
		new AddProcessEndLoadObserver(handleDocs);
		
		
		BpmnModel model = handleDocs.loadDocument("called_process.bpmn20.xml", true);
		handleDocs.saveModelAsDocument(model, "called_process.bpmn20.xml", true);
		
		File assertF = new File("./src/test/resources/extended/called_process.bpmn20.xml");
		Assert.assertTrue(assertF.exists());
		
		
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		IdentityService identityService = activitiRule.getIdentityService();

		ProcessInstance processInstance = null;

		Deployment deploy = repositoryService.createDeployment()
				.addInputStream("called_process.bpmn20.xml", new FileInputStream(filename)).deploy();

		Map<String, Object> variableMap = new HashMap<String, Object>();

		variableMap.put("variableFromCalledProcess", true);
		identityService.setAuthenticatedUserId("kermit");
		processInstance = runtimeService.startProcessInstanceByKey("called_process", variableMap);

		// Take Sequence Flow: Vorangeganenes Objekt:calledstartelement

		// ___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("called_process")
				.singleResult();
		Task task_calledut10 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("calledut1").singleResult();
		taskService.setAssignee(task_calledut10.getId(), "sascha");
		variableMap.put("nrOfActiveInstances", 1);
		variableMap.put("variableFromCalledProcess", true);
		variableMap.put("loopCounter", 0);
		variableMap.put("nrOfInstances", 3);
		variableMap.put("nrOfCompletedInstances", 0);
		taskService.complete(task_calledut10.getId(), variableMap);
		// ___User Task___
		// ___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("called_process")
				.singleResult();
		Task task_calledut11 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("calledut1").singleResult();
		taskService.setAssignee(task_calledut11.getId(), "sascha");
		variableMap.put("nrOfActiveInstances", 1);
		variableMap.put("variableFromCalledProcess", true);
		variableMap.put("loopCounter", 1);
		variableMap.put("nrOfInstances", 3);
		variableMap.put("nrOfCompletedInstances", 1);
		taskService.complete(task_calledut11.getId(), variableMap);
		// ___User Task___
		// ___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("called_process")
				.singleResult();
		Task task_calledut12 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("calledut1").singleResult();
		taskService.setAssignee(task_calledut12.getId(), "sascha");
		variableMap.put("nrOfActiveInstances", 1);
		variableMap.put("variableFromCalledProcess", true);
		variableMap.put("loopCounter", 2);
		variableMap.put("nrOfInstances", 3);
		variableMap.put("nrOfCompletedInstances", 2);
		taskService.complete(task_calledut12.getId(), variableMap);
		// ___User Task___

		// Take Sequence Flow: Vorangeganenes Objekt:calledut1

		// "variableFromCalledProcess", true;
		System.out.println("Teilprozess (1505001) wurde beendet");
		Assert.assertTrue(runtimeService.createProcessInstanceQuery().active().count() == 0);
		Assert.assertNull(processInstance.getActivityId());
		// _____Testausf?hrung_beendet_____________________________________________
		repositoryService.deleteDeployment(deploy.getId(), true);
		// _____Deployment_wird_gel?scht___________________________________________
		
		TestObjectDesigner.createJUnitTestContent("");

		Assert.assertTrue(new File(new PathsettingController().getGeneratedJUnitTest() + "ProcessTest_"
				+ "StandardTestFileName" + "_JUnit" + ".java").exists());
	}

}