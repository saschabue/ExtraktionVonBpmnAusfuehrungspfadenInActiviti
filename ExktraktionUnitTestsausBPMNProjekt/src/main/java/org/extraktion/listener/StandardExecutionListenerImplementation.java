package org.extraktion.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.extraktion.TestObjectDesigner;
import org.extraktion.templatesCreation.SuperElementObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Standard Listener als allgemeiner Zugriffspunkt fuer Listener 
 * der BPMN Elementgruppen.
 * 
 * @author Sascha Buelles
 */
public abstract class StandardExecutionListenerImplementation 
											implements ExecutionListener {
	/**
	 * Logger StandardExecutionListenerImplementation.
	 */
	private static Logger log = LoggerFactory
			.getLogger(StandardExecutionListenerImplementation.class);
	private static final long serialVersionUID = 1L;

	@Override
	public final void notify(final DelegateExecution execution) {
		TestObjectDesigner.getInstance();
		log.info("ExecutionListener wurde aufgerufen");
		
		// Ruft die abstrakte Methode fuer jedes Element auf 
		// und speichert anschliessend element-abhaengig das 
		// erstellte Template als Datei
		SuperElementObject createExecutionObjectWithType = 
				createExecutionObjectWithType(execution);
		
		SessionFactory hibernateSessionFactory = TestObjectDesigner
				.getInstance().getHibernateSessionFactory();
		Session session = hibernateSessionFactory.openSession();
		session.beginTransaction();
		createExecutionObjectWithType.setId(session
				.createNativeQuery("SELECT * FROM EXTRAKTION;")
				.getResultList().size() + 1);
		session.save(createExecutionObjectWithType);
		session.getTransaction().commit();
		session.close();
		
		createExecutionObjectWithType.setElementDefinitionKey(
				execution.getCurrentActivityId());
		createExecutionObjectWithType.writeTemplateToModuleFile();
		
		
		log.info("Modul Template eines Elements wurde "
				+ "als Datei gespeichert");
		createExecutionObjectWithType.writeImportTemplateToFile();
		log.info("Modul Template der Importe eines Elements wurde "
				+ "als Datei gespeichert");

		TestObjectDesigner.getInstance().closeSessionfactory();
	}

	/**
	 * Erstellt ein Objekt fuer die jeweiligen BPMN Elemente
	 * (org.analyse.templatesCreation.behaviour.*) und speichert 
	 * die Referenz in der Extraktionsdatenbank.
	 * 
	 * @param execution  Referenz zum Objekt der aktuellen Ausfuehrung
	 * @return Abstraktes Superelement
	 */
	protected abstract SuperElementObject 
						createExecutionObjectWithType(DelegateExecution execution);
}
