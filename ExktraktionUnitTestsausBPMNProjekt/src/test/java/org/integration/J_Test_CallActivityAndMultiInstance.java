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
import org.extraktion.coverageanalyses.Coverage;
import org.junit.Assert;
import org.junit.Test;
import org.transformBpmnDocumentforRecording.concreteObserver.AddCallActivitiLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddProcessEndLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddProcessStartLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddSequenceFlowTakeLoadOberserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddUserTaskLoadObserver;
import org.transformBpmnDocumentforRecording.transformationLogik.HandleBPMNDocuments;

public class J_Test_CallActivityAndMultiInstance extends StandardProcessIntegrationTest {

	private String filename = "./src/test/resources/extended/callingActivitiprocess.bpmn20.xml";
	private String filename1 = "./src/test/resources/extended/called_process.bpmn20.xml";

	@Test
	public void executeProcess() throws Exception {
		
		
		HandleBPMNDocuments handleDocs = new HandleBPMNDocuments();
		new AddUserTaskLoadObserver(handleDocs);
		new AddSequenceFlowTakeLoadOberserver(handleDocs);
		new AddProcessStartLoadObserver(handleDocs);
		new AddProcessEndLoadObserver(handleDocs);
		new AddCallActivitiLoadObserver(handleDocs);


		// Automatisches hinzufuegen der Listener, welche als Observer
		// angehaengt werden. Fuer Call Activities muessen alle aufgerufenen
		// Prozesse erweitert werden
		BpmnModel model = handleDocs.loadDocument("callingActivitiprocess.bpmn20.xml", true);
		handleDocs.saveModelAsDocument(model, "callingActivitiprocess.bpmn20.xml", true);
		BpmnModel model2 = handleDocs.loadDocument("called_process.bpmn20.xml", true);
		handleDocs.saveModelAsDocument(model2, "called_process.bpmn20.xml", true);

		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		IdentityService identityService = activitiRule.getIdentityService();

		ProcessInstance processInstance = null;

		Deployment deploy = repositoryService.createDeployment()
				.addInputStream("callingActivitiprocess.bpmn20.xml", new FileInputStream(filename)).deploy();
		Deployment deploy1 = repositoryService.createDeployment()
				.addInputStream("called_process.bpmn20.xml", new FileInputStream(filename1)).deploy();

		Map<String, Object> variableMap = new HashMap<String, Object>();

		identityService.setAuthenticatedUserId("kermit");
		processInstance = runtimeService.startProcessInstanceByKey("callingActivitiprocess", variableMap);
		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-068FEC9C-C5A9-4C10-90DC-B0F2F589C4D3
		
		
		// ___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("callingActivitiprocess")
				.singleResult();
		Task task_ut1 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("ut1").singleResult();
		taskService.setAssignee(task_ut1.getId(), "kermit");
		taskService.complete(task_ut1.getId(), variableMap);
		// ___User Task___

		// Take Sequence Flow: Vorangeganenes Objekt:ut1

		// Take Sequence Flow: Vorangeganenes Objekt:mt1

		// Neues Start Element ausgef?hrt___

		identityService.setAuthenticatedUserId("kermit");

		// Take Sequence Flow: Vorangeganenes Objekt:calledstartelement
		
		// Fuer Call Activities werden bei Aufruf alle Pfade hinzugerechnet
		Coverage coverageValues = new Coverage();
		coverageValues = new Coverage();
		Assert.assertTrue(coverageValues.getPathCoverage() == ((double)4/(double)6));

		// ___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("called_process")
				.singleResult();
		Task task_calledut10 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("calledut1").singleResult();
		taskService.setAssignee(task_calledut10.getId(), "sascha");
		variableMap.put("nrOfActiveInstances", 1);
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
		variableMap.put("loopCounter", 2);
		variableMap.put("nrOfInstances", 3);
		variableMap.put("nrOfCompletedInstances", 2);
		taskService.complete(task_calledut12.getId(), variableMap);
		// ___User Task___

		// Take Sequence Flow: Vorangeganenes Objekt:calledut1

		System.out.println("Teilprozess (1555041) wurde beendet");
		System.out.println("CallActivity Callactivititask1Name Element wurde ausgef?hrt ");

		//  Sequence Flow: Vorangeganenes Objekt:callactivititask1

		System.out.println("Teilprozess (1555030) wurde beendet");

		// _____Testausf?hrung_beendet_____________________________________________
		repositoryService.deleteDeployment(deploy.getId(), true);
		repositoryService.deleteDeployment(deploy1.getId(), true);
		// _____Deployment_wird_gel?scht___________________________________________
		Assert.assertTrue(coverageValues.getPathCoverage() == (double)1);
		
		TestObjectDesigner.createJUnitTestContent("Test_CallActivityProcessesAndMultiInstance");
		Assert.assertTrue(new File(new PathsettingController().getGeneratedJUnitTest() + "ProcessTest_"
				+ "Test_CallActivityProcessesAndMultiInstance" + "_JUnit" + ".java").exists());
	}

}