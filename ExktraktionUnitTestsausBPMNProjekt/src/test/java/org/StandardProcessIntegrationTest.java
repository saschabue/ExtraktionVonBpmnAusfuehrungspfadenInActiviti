package org;

import java.util.List;

import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.extraktion.TestObjectDesigner;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;

/**
 * @author Sascha Buelles StandardTest to clean up after database after
 *         execution
 */
public class StandardProcessIntegrationTest {

	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	@Before
	public void setUp() {
		User singleResult = activitiRule.getIdentityService().createNativeUserQuery().sql("SELECT * FROM ACT_ID_USER WHERE ID_ ='sascha';").singleResult();
		if (singleResult == null) {
			User user = activitiRule.getIdentityService().newUser("sascha");
			user.setFirstName("Sascha");
			user.setLastName("");
			user.setPassword("sascha");
			user.setEmail("");
			activitiRule.getIdentityService().saveUser(user);
			activitiRule.getIdentityService().createMembership("sascha", "management");
		}
		singleResult = activitiRule.getIdentityService().createNativeUserQuery().sql("SELECT * FROM ACT_ID_USER WHERE ID_ ='kermit';").singleResult();
		if (singleResult == null) {
			User user1 = activitiRule.getIdentityService().newUser("kermit");
			user1.setFirstName("Kermit");
			user1.setLastName("");
			user1.setPassword("kermit");
			user1.setEmail("");
			activitiRule.getIdentityService().saveUser(user1);
			activitiRule.getIdentityService().createMembership("sascha", "management");
		}
		singleResult = activitiRule.getIdentityService().createNativeUserQuery().sql("SELECT * FROM ACT_ID_USER WHERE ID_ ='gonzo';").singleResult();
		if (singleResult == null) {
			User user2 = activitiRule.getIdentityService().newUser("gonzo");
			user2.setFirstName("Gonzo");
			user2.setLastName("");
			user2.setPassword("gonzo");
			user2.setEmail("");
			activitiRule.getIdentityService().saveUser(user2);
			activitiRule.getIdentityService().createMembership("sascha", "management");
		}
		singleResult = activitiRule.getIdentityService().createNativeUserQuery().sql("SELECT * FROM ACT_ID_USER WHERE ID_ ='gem';").singleResult();
		if (singleResult == null) {
			User user3 = activitiRule.getIdentityService().newUser("gem");
			user3.setFirstName("Gem");
			user3.setLastName("");
			user3.setPassword("gem");
			user3.setEmail("");
			activitiRule.getIdentityService().saveUser(user3);
			activitiRule.getIdentityService().createMembership("gem", "management");
		}
	}

	/**
	 * System Clean Up
	 */
	@After
	public void cleanUp() {
		List<Deployment> deployments = activitiRule.getRepositoryService().createDeploymentQuery().list();

		// Alle aktiven Prozess Instanzen stoppen, danach alle Deployments
		// löschen
		List<ProcessInstance> runningInstances = activitiRule.getRuntimeService().createProcessInstanceQuery().list();
		for (ProcessInstance processInstance : runningInstances) {
			activitiRule.getRuntimeService().deleteProcessInstance(processInstance.getId(), "work");
		}
		for (Deployment deployment : deployments) {
			activitiRule.getRepositoryService().deleteDeployment(deployment.getId());
		}
		Session openSession = TestObjectDesigner.getInstance().getHibernateSessionFactory().openSession();
		openSession.getTransaction().begin();

		openSession.createNativeQuery("DELETE FROM EXTRAKTION").executeUpdate();

		openSession.close();
		TestObjectDesigner.getInstance().clearAndClose();

	}
}
