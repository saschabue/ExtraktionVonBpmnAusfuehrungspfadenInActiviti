package test.generatedUnitTests; 

 import org.activiti.engine.runtime.Execution; 
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
  


public class ProcessTest_NeuerTest_JUnit{

	private String filename = "./src/main/resources/original/letzterTest.bpmn20.xml";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void executeProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		IdentityService identityService = activitiRule.getIdentityService();

		ProcessInstance processInstance =null; 

		Deployment deploy = repositoryService.createDeployment().addInputStream("letzterTest.bpmn20.xml",
					new FileInputStream(filename)).deploy();

		Map<String, Object> variableMap = new HashMap<String, Object>();

		variableMap.put("variable", true);
		identityService.setAuthenticatedUserId("kermit");
		processInstance = runtimeService.startProcessInstanceByKey("letzterTest",variableMap);
		//___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("letzterTest").singleResult();
		Task task_usertask1 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("usertask1").singleResult();
		Assert.assertNotNull("Der Originalprozess wurde veraendert. Task nicht gefunden", task_usertask1);
		taskService.setAssignee(task_usertask1.getId(),"kermit");
		variableMap.put("variable",  true);
		taskService.complete(task_usertask1.getId(), variableMap);
		//___User Task___
		//___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("letzterTest").singleResult();
		Task task_usertask2 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("usertask2").singleResult();
		Assert.assertNotNull("Der Originalprozess wurde veraendert. Task nicht gefunden", task_usertask2);
		taskService.setAssignee(task_usertask2.getId(),"kermit");
		variableMap.put("variable",  true);
		taskService.complete(task_usertask2.getId(), variableMap);
		//___User Task___
		Execution intermediate = runtimeService.createExecutionQuery().activityId("signaleelementidreceive").singleResult();
		if (intermediate!=null && !intermediate.isEnded()) {
		runtimeService.signalEventReceived("SigName", intermediate.getId());
		}//___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("letzterTest").singleResult();
		Task task_usertask3 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("usertask3").singleResult();
		Assert.assertNotNull("Der Originalprozess wurde veraendert. Task nicht gefunden", task_usertask3);
		taskService.setAssignee(task_usertask3.getId(),"sascha");
		variableMap.put("variable",  true);
		taskService.complete(task_usertask3.getId(), variableMap);
		//___User Task___


		//_____Testausf?hrung_beendet_____________________________________________
		repositoryService.deleteDeployment(deploy.getId(),true);
		//_____Deployment_wird_gel?scht___________________________________________
	}

}