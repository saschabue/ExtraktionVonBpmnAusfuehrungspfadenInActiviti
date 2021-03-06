package test.generatedUnitTests; 

 import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;
  


public class ProcessTest_Test_SignalAndTimerEvent_JUnit{

	private String filename = "./src/main/resources/original/IntermediateTimerCatch.bpmn20.xml";

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Test
	public void executeProcess() throws Exception {
		RepositoryService repositoryService = activitiRule.getRepositoryService();
		RuntimeService runtimeService = activitiRule.getRuntimeService();
		IdentityService identityService = activitiRule.getIdentityService();

		ProcessInstance processInstance =null; 

		Deployment deploy = repositoryService.createDeployment().addInputStream("IntermediateTimerCatch.bpmn20.xml",
					new FileInputStream(filename)).deploy();

		Map<String, Object> variableMap = new HashMap<String, Object>();

		identityService.setAuthenticatedUserId("kermit");
		processInstance = runtimeService.startProcessInstanceByKey("intermediateTimerCatch",variableMap);
		//________________Timer fired:_TCE___________________
		ManagementService managementService = activitiRule.getManagementService();
		Job singleResult = managementService.createJobQuery().processInstanceId(processInstance.getId()).singleResult();
		managementService.executeJob(singleResult.getId());Execution intermediate = runtimeService.createExecutionQuery().activityId("signalintermediatecatchevent1").singleResult();
		runtimeService.signalEventReceived("Signal", intermediate.getId());
		System.out.println("Teilprozess (2890476) wurde beendet");

		//_____Testausf?hrung_beendet_____________________________________________
		repositoryService.deleteDeployment(deploy.getId(),true);
		//_____Deployment_wird_gel?scht___________________________________________
	}

}