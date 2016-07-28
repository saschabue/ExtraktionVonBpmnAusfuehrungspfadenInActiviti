jUnit_TestMethod_Template(processFile, ausfuehrungen) ::= "

@Test
public void executeProcess() throws Exception {
	RepositoryService repositoryService = activitiRule.getRepositoryService();
	RuntimeService runtimeService = activitiRule.getRuntimeService();
	TaskService taskService = activitiRule.getTaskService();
	IdentityService identityService = activitiRule.getIdentityService();
	
	ProcessInstance processInstance =null; 
	
	Deployment deploy = repositoryService.createDeployment().addInputStream(\"<processFile>\",
				new FileInputStream(filename)).deploy();
				
	Map\<String, Object> variableMap = new HashMap\<String, Object>();
	<! Ablauf wird ab hier eingetragen !>
	
	<ausfuehrungen>
	
	//_____Testausführung_beendet_____________________________________________
	repositoryService.deleteDeployment(deploy.getId(),true);
	//_____Deployment_wird_gelöscht___________________________________________
}
"