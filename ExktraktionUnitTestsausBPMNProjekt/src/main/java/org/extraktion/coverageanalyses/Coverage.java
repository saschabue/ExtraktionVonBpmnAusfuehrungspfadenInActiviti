package org.extraktion.coverageanalyses;

import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.extraktion.TestObjectDesigner;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Analyse auf Basis der Zweigueberdeckung.
 * 
 * @author Sascha Buelles
 *
 */
public class Coverage {

	/**
	 * Neue Engine zum Zugriff auf Prozessmodelle auch ausserhalb einer
	 * manuellen Extraktion.
	 */
	private ProcessEngine processEngine;
	/**
	 * Zu analysierende Modelle.
	 */
	private List<BpmnModel> models = new ArrayList<BpmnModel>();

	/**
	 * Standardkonstruktur zur Initialisierung des Objekts.
	 */
	public Coverage() {
		this.initialize();
	}

	/**
	 * Initialisierung: Abfrage der Prozessmodelle.
	 */
	private void initialize() {
		SessionFactory hibernateSessionFactory = TestObjectDesigner.getInstance().getHibernateSessionFactory();
		Session openSession = hibernateSessionFactory.openSession();

		ProcessEngineConfiguration createProcessEngineConfigurationFromResource = ProcessEngineConfiguration
				.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
		createProcessEngineConfigurationFromResource.setJobExecutorActivate(false);
		processEngine = createProcessEngineConfigurationFromResource.buildProcessEngine();

		// Laden aller Modelle die Referenziert werden zum Zeitpunkt der
		// Initialisierung
		@SuppressWarnings("unchecked")
		List<String> resultList = openSession
				.createNativeQuery("Select processDefinitionID FROM EXTRAKTION WHERE ELEMENTNAME = 'StartElement'")
				.getResultList();
		for (String processkey : resultList) {
			BpmnModel bpmnModel = processEngine.getRepositoryService().getBpmnModel(processkey);
			if (bpmnModel != null) {
				models.add(bpmnModel);
			}
		}
		openSession.close();
		TestObjectDesigner.getInstance().closeSessionfactory();

	}

	/**
	 * Anzahl der ausgefuehrten Sequenzfluesse in aktueller Extraktion.
	 * 
	 * @return Double ausgefuehrten Sequenzfluesse
	 */
	private double getExecutedProcessSequenceFlows() {
		SessionFactory hibernateSessionFactory = TestObjectDesigner.getInstance().getHibernateSessionFactory();
		Session openSession = hibernateSessionFactory.openSession();

		double amountExecutedSequenceFlows = (double) openSession
				.createNativeQuery(
						"Select ELEMENTDEFINITIONKEY FROM EXTRAKTION WHERE ELEMENTNAME ='SequenceFlowElement'")
				.getResultList().size();

		openSession.close();
		TestObjectDesigner.getInstance().closeSessionfactory();
		return amountExecutedSequenceFlows;

	}

	/**
	 * Anzahl aller Sequenzfluesse aller genutzten Modelle.
	 * 
	 * @return Double alle Sequenzfluesse
	 */
	private double getExistingProcessSequenceFlows() {
		int amountOfSequenceFlows = 0;
		for (BpmnModel model : models) {
			List<Process> processes = model.getProcesses();
			for (Process process : processes) {
				amountOfSequenceFlows += process.findFlowElementsOfType(SequenceFlow.class).size();
			}
		}

		return (double) amountOfSequenceFlows;
	}

	/**
	 * Sequenzflussanalyse, Pfadanalyse.
	 * 
	 * @return Double Kennzahl
	 */
	public final double getPathCoverage() {
		double existingProcessSequenceFlows = this.getExistingProcessSequenceFlows();
		if (existingProcessSequenceFlows == 0) {
			return 0;
		}

		return (getExecutedProcessSequenceFlows() / existingProcessSequenceFlows);
	}

	/**
	 * Anzahl aller UserTasks aller Modelle.
	 * 
	 * @return Double alle User Tasks
	 */
	private double getExistingUserTasks() {
		int amountOfUserTasks = 0;
		for (BpmnModel model : models) {
			List<Process> processes = model.getProcesses();
			for (Process process : processes) {
				amountOfUserTasks += process.findFlowElementsOfType(UserTask.class).size();
			}
		}
		return (double) amountOfUserTasks;
	}

	/**
	 * Anzahl der ausgefuehrten User Tasks in aktueller Extraktion.
	 * 
	 * @return Double ausgefuehrte User Tasks
	 */
	private double getExecutedProcessUserTaskElements() {
		SessionFactory hibernateSessionFactory = TestObjectDesigner.getInstance().getHibernateSessionFactory();
		Session openSession = hibernateSessionFactory.openSession();

		double amountExecutedSequenceFlows = (double) openSession
				.createNativeQuery("Select ELEMENTDEFINITIONKEY FROM EXTRAKTION WHERE ELEMENTNAME ='UserTaskElement'")
				.getResultList().size();

		openSession.close();
		TestObjectDesigner.getInstance().closeSessionfactory();
		return amountExecutedSequenceFlows;

	}

	/**
	 * UserTask Analyse, Taskueberdeckung.
	 * 
	 * @return Double Kennzahl
	 */
	public final double getUserTaskCoverage() {
		double existingProcessUserTasks = this.getExistingUserTasks();
		if (existingProcessUserTasks == 0) {
			return 0;
		}
		double analyse = (getExecutedProcessUserTaskElements() / existingProcessUserTasks);
		// Groesser als eins z.B. bei MultiInstanz.
		// Loesung: Anfragen an Multiinstanzen nach Anzahl und hinzufuegen zur
		// maximalen Anzahl.
		if (analyse > 1) {
			return 1;
		} else {
			return analyse;
		}
	}

	/**
	 * Schliessen der Engine.
	 */
	public final void closeContext() {
		processEngine.getProcessEngineConfiguration().getJobExecutor().shutdown();
		processEngine.close();

	}
}
