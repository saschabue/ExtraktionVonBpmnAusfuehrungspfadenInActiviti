package test.generatedUnitTests; 

   
 import org.activiti.engine.runtime.ProcessInstance;
  
 import org.activiti.engine.task.Task;
 import org.junit.Assert; 
  
  
   
 import java.io.FileInputStream;
 import java.util.HashMap;
 import java.util.Map;
 import org.activiti.engine.test.ActivitiRule;
 import org.junit.Rule;
 import org.junit.Test;
 import org.activiti.engine.IdentityService;
 import org.activiti.engine.RepositoryService;
 import org.activiti.engine.RuntimeService;
 import org.activiti.engine.TaskService;
 import org.activiti.engine.repository.Deployment;
  


public class ProcessTest_Test_Test_LanesSignals_JUnit{

	private String filename = "./src/main/resources/original/LanesSignals.bpmn20.xml";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void executeProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		IdentityService identityService = activitiRule.getIdentityService();

		ProcessInstance processInstance =null; 

		Deployment deploy = repositoryService.createDeployment().addInputStream("LanesSignals.bpmn20.xml",
					new FileInputStream(filename)).deploy();

		Map<String, Object> variableMap = new HashMap<String, Object>();

		identityService.setAuthenticatedUserId("kermit");
		processInstance = runtimeService.startProcessInstanceByKey("process_pool2",variableMap);
		//___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("process_pool2").singleResult();
		Task task_usertask1 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("usertask1").singleResult();
		Assert.assertNotNull("Der Originalprozess wurde ver?ndert. Task nicht gefunden", task_usertask1);
		taskService.setAssignee(task_usertask1.getId(),"kermit");
		taskService.complete(task_usertask1.getId(), variableMap);
		//___User Task___
		// Neues Start Element ausgef?hrt___

		identityService.setAuthenticatedUserId("kermit");
		//________________SkriptTask_Elementname:_ST1___________________
		System.out.println("Ausf?hrung ?Skript Task: ST1. Variablenwerte:");
		//________________SkriptTask____________________
		//___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("process_pool").singleResult();
		Task task_usertask3 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("usertask3").singleResult();
		Assert.assertNotNull("Der Originalprozess wurde ver?ndert. Task nicht gefunden", task_usertask3);
		taskService.setAssignee(task_usertask3.getId(),"kermit");
		taskService.complete(task_usertask3.getId(), variableMap);
		//___User Task___
		System.out.println("Teilprozess (2890458) wurde beendet");//___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("process_pool2").singleResult();
		Task task_usertask2 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("usertask2").singleResult();
		Assert.assertNotNull("Der Originalprozess wurde ver?ndert. Task nicht gefunden", task_usertask2);
		taskService.setAssignee(task_usertask2.getId(),"kermit");
		taskService.complete(task_usertask2.getId(), variableMap);
		//___User Task___
		System.out.println("Teilprozess (2890448) wurde beendet");

		//_____Testausf?hrung_beendet_____________________________________________
		repositoryService.deleteDeployment(deploy.getId(),true);
		//_____Deployment_wird_gel?scht___________________________________________
	}

}