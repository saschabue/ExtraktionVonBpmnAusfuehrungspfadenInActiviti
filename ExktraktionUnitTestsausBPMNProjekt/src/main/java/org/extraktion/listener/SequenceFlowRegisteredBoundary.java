package org.extraktion.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.extraktion.TestObjectDesigner;
import org.extraktion.templatesCreation.behaviour.TimerCatchEventObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * @author Sascha Buelles
 * Klasse erweitern zum Erstellen neuer Typen von Boundary Events, die vom SequenceFlow Listener als Source des Flusses registriert wurden. 
 */
public class SequenceFlowRegisteredBoundary {
	
	/**
	 * Boundary Event Ausführung registriert durch Sequenzfluss. 
	 * @param execution Deligierte Ausführung
	 * @param sourceElement Boundary Event aus Prozessdefinition
	 */
	public SequenceFlowRegisteredBoundary(final DelegateExecution execution, final TransitionImpl sourceElement) {
		this.boundaryTimerElementExecuted(execution, sourceElement);
	}
	
	/**
	 * Timer Boundary Event Logik zur Extraktion.
	 * @param execution siehe Konstruktor
	 * @param source siehe Konstruktor
	 * 
	 */
	private void boundaryTimerElementExecuted(final DelegateExecution execution, final TransitionImpl source) {
		TimerCatchEventObject tceO = new TimerCatchEventObject(execution);
		tceO.setelementName("TimerEvent");
		tceO.setElementDefinitionKey(source.getSource().getId());
		
		SessionFactory hibernateSessionFactory = TestObjectDesigner.getInstance().getHibernateSessionFactory();
		Session session = hibernateSessionFactory.openSession();
		session.beginTransaction();
		tceO.setId(session.createNativeQuery("SELECT * FROM EXTRAKTION;").getResultList().size() + 1);
		session.save(tceO);
		session.getTransaction().commit();
		session.close();
		
		tceO.writeTemplateToModuleFile();
		tceO.writeImportTemplateToFile();
			
	}
}
