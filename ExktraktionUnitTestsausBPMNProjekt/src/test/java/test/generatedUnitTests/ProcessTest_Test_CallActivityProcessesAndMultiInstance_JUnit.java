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
  


public class ProcessTest_Test_CallActivityProcessesAndMultiInstance_JUnit{

	private String filename = "./src/main/resources/original/callingActivitiprocess.bpmn20.xml";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void executeProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		TaskService taskService = activitiRule.getTaskService();
		IdentityService identityService = activitiRule.getIdentityService();

		ProcessInstance processInstance =null; 

		Deployment deploy = repositoryService.createDeployment().addInputStream("callingActivitiprocess.bpmn20.xml",
					new FileInputStream(filename)).deploy();

		Map<String, Object> variableMap = new HashMap<String, Object>();

		System.out.println("CallActivity Callactivititask1Name wird deployed");
		Deployment deploy_called_process = repositoryService.createDeployment().addInputStream("called_process.bpmn20.xml",new FileInputStream("./src/main/resources/original/called_process.bpmn20.xml")).deploy();
		identityService.setAuthenticatedUserId("kermit");
		processInstance = runtimeService.startProcessInstanceByKey("callingActivitiprocess",variableMap);
		//___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("callingActivitiprocess").singleResult();
		Task task_ut1 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("ut1").singleResult();
		Assert.assertNotNull("Der Originalprozess wurde ver?ndert. Task nicht gefunden", task_ut1);
		taskService.setAssignee(task_ut1.getId(),"kermit");
		taskService.complete(task_ut1.getId(), variableMap);
		//___User Task___
		// Neues Start Element ausgef?hrt___

		identityService.setAuthenticatedUserId("kermit");
		//___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("called_process").singleResult();
		Task task_calledut10 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("calledut1").singleResult();
		Assert.assertNotNull("Der Originalprozess wurde ver?ndert. Task nicht gefunden", task_calledut10);
		taskService.setAssignee(task_calledut10.getId(),"sascha");
		variableMap.put("nrOfActiveInstances",  1);
		variableMap.put("loopCounter",  0);
		variableMap.put("nrOfInstances",  3);
		variableMap.put("nrOfCompletedInstances",  0);
		taskService.complete(task_calledut10.getId(), variableMap);
		//___User Task___
		//___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("called_process").singleResult();
		Task task_calledut11 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("calledut1").singleResult();
		Assert.assertNotNull("Der Originalprozess wurde ver?ndert. Task nicht gefunden", task_calledut11);
		taskService.setAssignee(task_calledut11.getId(),"sascha");
		variableMap.put("nrOfActiveInstances",  1);
		variableMap.put("loopCounter",  1);
		variableMap.put("nrOfInstances",  3);
		variableMap.put("nrOfCompletedInstances",  1);
		taskService.complete(task_calledut11.getId(), variableMap);
		//___User Task___
		//___User Task___
		processInstance = runtimeService.createProcessInstanceQuery().processDefinitionKey("called_process").singleResult();
		Task task_calledut12 = (taskService.createTaskQuery().processInstanceId(processInstance.getId())).active()
				.taskDefinitionKey("calledut1").singleResult();
		Assert.assertNotNull("Der Originalprozess wurde ver?ndert. Task nicht gefunden", task_calledut12);
		taskService.setAssignee(task_calledut12.getId(),"sascha");
		variableMap.put("nrOfActiveInstances",  1);
		variableMap.put("loopCounter",  2);
		variableMap.put("nrOfInstances",  3);
		variableMap.put("nrOfCompletedInstances",  2);
		taskService.complete(task_calledut12.getId(), variableMap);
		//___User Task___
		System.out.println("Teilprozess (2890019) wurde beendet");System.out.println("Teilprozess (2890009) wurde beendet");

		//_____Testausf?hrung_beendet_____________________________________________
		repositoryService.deleteDeployment(deploy.getId(),true);
		repositoryService.deleteDeployment(deploy_called_process.getId(), true);
		//_____Deployment_wird_gel?scht___________________________________________
	}

}