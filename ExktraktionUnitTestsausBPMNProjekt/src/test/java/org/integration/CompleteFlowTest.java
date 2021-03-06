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
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.extraktion.TestObjectDesigner;
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

public class CompleteFlowTest extends StandardProcessIntegrationTest {

	@Test
	public void testCompleteWorkflow() {
		String filename = new PathsettingController().getExtendedTest() + "/processId_vortrag_2906.bpmn20.xml";

		HandleBPMNDocuments handleDocs = new HandleBPMNDocuments();
		new AddUserTaskLoadObserver(handleDocs);
		new AddSequenceFlowTakeLoadOberserver(handleDocs);
		new AddProcessStartLoadObserver(handleDocs);
		new AddProcessEndLoadObserver(handleDocs);
		new AddParallelGateWayLoadObserver(handleDocs);
		new AddSkriptTaskLoadObserver(handleDocs);
		new AddSignalReceiveLoadObserver(handleDocs);

		BpmnModel model = handleDocs.loadDocument("processId_vortrag_2906.bpmn20.xml", true);

		// Pfad zur Datei wird in zentraler pfadsettings.xml Datei angelegt
		handleDocs.saveModelAsDocument(model, "processId_vortrag_2906.bpmn20.xml", true);
		File assertF = new File("./src/test/resources/extended/processId_vortrag_2906.bpmn20.xml");
		Assert.assertTrue(assertF.exists());

		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		IdentityService identityService = activitiRule.getIdentityService();

		ProcessInstance processInstance = null;

		Deployment deploy = null;
		try {
			deploy = repositoryService.createDeployment()
					.addInputStream("processId_vortrag_2906.bpmn20.xml", new FileInputStream(filename)).deploy();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Map<String, Object> variableMap = new HashMap<String, Object>();

		variableMap.put("variable1", false);
		identityService.setAuthenticatedUserId("kermit");
		processInstance = runtimeService.startProcessInstanceByKey("processId_vortrag_2906", variableMap);

		// Take Sequence Flow: Vorangeganenes Objekt:startid

		// ________________Timer fired:_U-Task1___________________
		ManagementService managementService = activitiRule.getManagementService();
		Job singleResult = managementService.createJobQuery().processInstanceId(processInstance.getId()).singleResult();
		managementService.executeJob(singleResult.getId());
		// Take Sequence Flow: Vorangeganenes Objekt:idut1

		// Neues Start Element ausgef?hrt___

		variableMap.put("variable1", false);
		identityService.setAuthenticatedUserId("kermit");

		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-226585A9-AE41-409E-91E7-A65EC0C14CBB

		// ___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("processId_vortrag_2906")
				.singleResult();
		Task task_idut3 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("idut3").singleResult();
		taskService.setAssignee(task_idut3.getId(), "kermit");
		variableMap.put("variable1", false);
		taskService.complete(task_idut3.getId(), variableMap);
		// ___User Task___

		// Take Sequence Flow: Vorangeganenes Objekt:idut3

		// "variable1", false;
		System.out.println("Teilprozess (1305001) wurde beendet");
		// Take Sequence Flow: Vorangeganenes
		// Objekt:sid-3DD4A732-452D-4AE6-A903-2FAADC2D30EB

		// "variable1", false;
		System.out.println("Teilprozess (1305001) wurde beendet");

		// _____Testausf?hrung_beendet_____________________________________________
		repositoryService.deleteDeployment(deploy.getId(), true);
		// _____Deployment_wird_gel?scht___________________________________________
		
		TestObjectDesigner.createJUnitTestContent("processId_vortrag_2906");
		
		Assert.assertTrue(new File(new PathsettingController().getGeneratedJUnitTest() + "ProcessTest_"
				+ "processId_vortrag_2906" + "_JUnit" + ".java").exists());
		assertF.delete();
		
	}
}
