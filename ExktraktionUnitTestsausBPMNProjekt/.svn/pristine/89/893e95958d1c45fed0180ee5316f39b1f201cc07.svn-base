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
  


public class ProcessTest_Test_servicetaskAndTimerCatchAndSignalCatch1_JUnit{

	private String filename = "./src/main/resources/original/servicetaskAndTimerCatchAndSignalCatch.bpmn20.xml";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void executeProcess() throws Exception {
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
		Execution intermediate = runtimeService.createExecutionQuery().activityId("sid-C7BCF5EC-42E4-4E25-BD79-EE7941F52C82").singleResult();
		runtimeService.signalEventReceived("SignalFuerTestProzess", intermediate.getId());
		//___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("servicetaskAndTimerCatchAndSignalCatch").singleResult();
		Task task_idut2 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("idut2").singleResult();
		Assert.assertNotNull("Der Originalprozess wurde ver?ndert. Task nicht gefunden", task_idut2);
		taskService.setAssignee(task_idut2.getId(),"kermit");
		variableMap.put("entscheidung",  2);
		taskService.complete(task_idut2.getId(), variableMap);
		//___User Task___
		//"entscheidung", 2;
		System.out.println("Teilprozess (2890335) wurde beendet");

		//_____Testausf?hrung_beendet_____________________________________________
		repositoryService.deleteDeployment(deploy.getId(),true);
		//_____Deployment_wird_gel?scht___________________________________________
	}

}