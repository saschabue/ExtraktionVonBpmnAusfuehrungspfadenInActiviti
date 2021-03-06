package development;

import java.util.List;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.test.ActivitiRule;
import org.extraktion.TestObjectDesigner;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Rule;
import org.junit.Test;

/**
 * L�schen aller Depoyments in der Activiti Datenbank.
 * 
 * @author Sascha Buelles
 *
 */
public class DeleteAllDeploymentsAndInstances {

	/**
	 * Activiti Rule.
	 */
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	/**
	 * L�sche alle Deployments und Extrkationseintr�ge in DB! Moduldateien
	 * werden nicht gel�scht. Ausf�hrung als Test f�r schnellen Zugriff bei der
	 * Entwicklung.
	 */
	@Test
	public final void deleteDeployments() {
		List<Deployment> deployments = activitiRule.getRepositoryService().createDeploymentQuery().list();

		// Alle aktiven Prozess Instanzen stoppen, danach alle Deployments
		// l�schen
		List<ProcessInstance> runningInstances = activitiRule.getRuntimeService().createProcessInstanceQuery().list();
		for (ProcessInstance processInstance : runningInstances) {
			activitiRule.getRuntimeService().deleteProcessInstance(processInstance.getId(), "work");
		}
		for (Deployment deployment : deployments) {
			activitiRule.getRepositoryService().deleteDeployment(deployment.getId());
		}

		SessionFactory hibernateSessionFactory = TestObjectDesigner.getInstance().getHibernateSessionFactory();
		Session openSession = hibernateSessionFactory.openSession();
		try {
			openSession.beginTransaction();
			openSession.createNativeQuery("DELETE FROM EXTRAKTION").executeUpdate();
			openSession.close();
		} catch (Exception e) {
			System.out.println("Tabelle fuer Extraktionen existiert gedroppt");
			e.printStackTrace();
		}
		TestObjectDesigner.getInstance().clearAndClose();
	}

}
