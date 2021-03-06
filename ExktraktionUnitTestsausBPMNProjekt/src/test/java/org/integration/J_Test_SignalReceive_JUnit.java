package org.integration;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.PathsettingController;
import org.StandardProcessIntegrationTest;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

public class J_Test_SignalReceive_JUnit extends StandardProcessIntegrationTest {

	private String filename = new PathsettingController().getOriginalTest() + "/processId_vortrag_2906.bpmn20.xml";

	@Test
	public void executeProcess() throws Exception {

		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		IdentityService identityService = activitiRule.getIdentityService();
		ProcessInstance processInstance = null;

		Deployment deploy = repositoryService.createDeployment()
				.addInputStream("processId_vortrag_2906.bpmn20.xml", new FileInputStream(filename)).deploy();

		Map<String, Object> variableMap = new HashMap<String, Object>();

		variableMap.put("variable1", false);
		identityService.setAuthenticatedUserId("kermit");
		processInstance = runtimeService.startProcessInstanceByKey("processId_vortrag_2906", variableMap);

		// Take Sequence Flow: Vorangeganenes Objekt:startid
		// ___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("processId_vortrag_2906")
				.singleResult();
		Task task_idut1 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("idut1").singleResult();
		taskService.setAssignee(task_idut1.getId(), "kermit");
		variableMap.put("variable1", false);
		taskService.complete(task_idut1.getId(), variableMap);
		// ___User Task___

		// Take Sequence Flow: Vorangeganenes Objekt:idut1

		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-7A0FB917-CE00-4572-A069-0DEB8FA2EE54

		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-7A0FB917-CE00-4572-A069-0DEB8FA2EE54

		// ________________ServiceTask_Elementname:_S-Task___________________
		System.out.println("Ausf?hrung Service Task: S-Task. Variablenwerte:");
		System.out.println("Variable variable1 : true");
		// ________________ServiceTask____________________

		// Take Sequence Flow: Vorangeganenes Objekt:idst1

		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-7A0FB917-CE00-4572-A069-0DEB8FA2EE54

		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-7A0FB917-CE00-4572-A069-0DEB8FA2EE54

		Execution intermediate = runtimeService.createExecutionQuery().activityId("signalintermediatecatchevent1")
				.singleResult();
		runtimeService.signalEventReceived("SignalEins", intermediate.getId());

		// Take Sequence Flow: Vorangeganenes
		// Objekt:signalintermediatecatchevent1

		// ___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("processId_vortrag_2906")
				.singleResult();
		Task task_usertask1 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("usertask1").singleResult();
		taskService.setAssignee(task_usertask1.getId(), "kermit");
		variableMap.put("variable1", true);
		taskService.complete(task_usertask1.getId(), variableMap);
		// ___User Task___

		// Take Sequence Flow: Vorangeganenes Objekt:usertask1

		// ___Paralleles Gateway___
		System.out.println("Paralleles Gateway 1210001 wurde ausgef?hrt");
		System.out.println("Incoming Flows: 2, Outgoing Flows: 1");
		// ___Paralleles Gateway___

		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-997DFB95-D961-489C-965E-9CEFDECE6977

		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-226585A9-AE41-409E-91E7-A65EC0C14CBB

		// ___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("processId_vortrag_2906")
				.singleResult();
		Task task_idut3 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("idut3").singleResult();
		taskService.setAssignee(task_idut3.getId(), "kermit");
		variableMap.put("variable1", true);
		taskService.complete(task_idut3.getId(), variableMap);
		// ___User Task___

		// Take Sequence Flow: Vorangeganenes Objekt:idut3

		// "variable1", true;
		System.out.println("Teilprozess (1210001) wurde beendet");
		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-3DD4A732-452D-4AE6-A903-2FAADC2D30EB

		// "variable1", true;
		System.out.println("Teilprozess (1210001) wurde beendet");

		// _____Testausf?hrung_beendet_____________________________________________
		repositoryService.deleteDeployment(deploy.getId(), true);
		// _____Deployment_wird_gel?scht___________________________________________
	}

}