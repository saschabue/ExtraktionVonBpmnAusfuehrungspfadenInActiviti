package test.generatedUnitTests; 

   
 import org.activiti.engine.runtime.ProcessInstance;
  
 import org.activiti.engine.task.Task;
 import org.junit.Assert; 
  
 import org.activiti.engine.runtime.Job;
 import org.activiti.engine.ManagementService; 
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
  


public class ProcessTest_processId_vortrag_29061_JUnit{

	private String filename = "./src/main/resources/original/processId_vortrag_2906.bpmn20.xml";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void executeProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		IdentityService identityService = activitiRule.getIdentityService();

		ProcessInstance processInstance =null; 

		Deployment deploy = repositoryService.createDeployment().addInputStream("processId_vortrag_2906.bpmn20.xml",
					new FileInputStream(filename)).deploy();

		Map<String, Object> variableMap = new HashMap<String, Object>();

		variableMap.put("variable1", false);
		identityService.setAuthenticatedUserId("kermit");
		processInstance = runtimeService.startProcessInstanceByKey("processId_vortrag_2906",variableMap);
		//________________Timer fired:_U-Task1___________________
		ManagementService managementService = activitiRule.getManagementService();
		Job singleResult = managementService.createJobQuery().processInstanceId(processInstance.getId()).singleResult();
		managementService.executeJob(singleResult.getId());// Neues Start Element ausgef?hrt___

		variableMap.put("variable1", false);
		identityService.setAuthenticatedUserId("kermit");
		//___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("processId_vortrag_2906").singleResult();
		Task task_idut3 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("idut3").singleResult();
		Assert.assertNotNull("Der Originalprozess wurde ver?ndert. Task nicht gefunden", task_idut3);
		taskService.setAssignee(task_idut3.getId(),"kermit");
		variableMap.put("variable1",  false);
		taskService.complete(task_idut3.getId(), variableMap);
		//___User Task___
		//"variable1", false;
		System.out.println("Teilprozess (2890424) wurde beendet");//"variable1", false;
		System.out.println("Teilprozess (2890424) wurde beendet");

		//_____Testausf?hrung_beendet_____________________________________________
		repositoryService.deleteDeployment(deploy.getId(),true);
		//_____Deployment_wird_gel?scht___________________________________________
	}

}