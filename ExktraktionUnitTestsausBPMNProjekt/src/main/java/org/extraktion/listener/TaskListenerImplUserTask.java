package org.extraktion.listener;

import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.extraktion.TestObjectDesigner;
import org.extraktion.templatesCreation.SuperElementObject;
import org.extraktion.templatesCreation.behaviour.UserTaskElementObject;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TaskListener zur Extraktion.
 * @author Sascha Buelles
 *
 */
public class TaskListenerImplUserTask extends ActivitiListener 
											implements TaskListener {

	/**
	 * Logger TaskListenerImplUserTask.
	 */
	private static Logger log = LoggerFactory
			.getLogger(TaskListenerImplUserTask.class);

	private static final long serialVersionUID = 1L;

	@Override
	public final void notify(final DelegateTask delegateTask) {
		log.info("User Task Listener wurde aufgerufen");
		TestObjectDesigner.getInstance();
		SuperElementObject userTaskElementObject =
				new UserTaskElementObject(delegateTask);
		userTaskElementObject.setelementName("UserTaskElement");

		Session session = TestObjectDesigner.getInstance()
				.getHibernateSessionFactory().openSession();
		session.beginTransaction();
		// Generierung eines Primary Key zum Speichern des Elements in der
		// Ablauftabelle
		userTaskElementObject.setId(
				session.createNativeQuery("SELECT * FROM EXTRAKTION;")
				.getResultList().size() + 1);

		session.save(userTaskElementObject);
		session.getTransaction().commit();
		session.close();
		TestObjectDesigner.getInstance().closeSessionfactory();
		
		userTaskElementObject.writeTemplateToModuleFile();
		log.info("User Task Modul Tempalte als Datei gespeichert");
		userTaskElementObject.writeImportTemplateToFile();
		log.info("User Task Importe als Datei gespeichert");
	}
}
