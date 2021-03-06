package org.extraktion.templatesCreation.behaviour;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.extraktion.TestObjectDesigner;
import org.extraktion.templatesCreation.SuperElementObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.stringtemplate.v4.ST;

/**
 * Temple erstellendes Objekt.
 * 
 * @author Sascha Buelles
 *
 */
@Entity
@Table(name = "EXTRAKTION")
public class StartElementObject extends SuperElementObject {

	/**
	 * 
	 */
	private String processDefinitionID;
	
	/**
	 * @return processInstanceID
	 */
	@Column(name = "processDefinitionID", nullable = true)
	public final String getProcessDefinitionID() {
		return processDefinitionID;
	}

	/**
	 * Setzen der ID.
	 * @param setProcessInstanceID InstanzID fuer Analyse
	 */
	public final void setProcessDefinitionID(final String setProcessInstanceID) {
		this.processDefinitionID = setProcessInstanceID;
	}

	/**
	 * Start Element.
	 * 
	 * @param execution
	 *            Element.
	 */
	public StartElementObject(final DelegateExecution execution) {
		super(execution);
	}

	@Override
	public final void createTemplate(final DelegateExecution execution) {

		// Anfrage von Variablen aus der aktuellen Prozessinstanz
		ProcessDefinition processdefinition = execution.getEngineServices().getRepositoryService()
				.createNativeProcessDefinitionQuery()
				.sql("Select * FROM ACT_RE_PROCDEF WHERE ID_ ='" + execution.getProcessDefinitionId() + "'")
				.singleResult();
		SessionFactory hibernateSessionFactory = TestObjectDesigner.getInstance().getHibernateSessionFactory();
		Session session = hibernateSessionFactory.openSession();
		int size = session.createNativeQuery("Select * FROM EXTRAKTION").getResultList().size();
		
		session.close();
		
		//todo 
		setProzesskey(processdefinition.getResourceName());
		setProcessDefinitionID(processdefinition.getId());
		
		
		String executionUser = "";
		HistoricProcessInstance singleResult = null;
		try {
			singleResult = execution.getEngineServices().getHistoryService().createHistoricProcessInstanceQuery()
					.processInstanceId(execution.getProcessInstanceId()).singleResult();
			executionUser = singleResult.getStartUserId();
		} catch (Exception e) {
			executionUser = "kermit";
		}

		// Erstellen des Templates
		if (size == 0) {
			ST templateLocal = templateImpl();
			templateLocal.add("processKey", processdefinition.getKey());
			templateLocal.add("executionUser", executionUser);
			templateLocal.add("processVariables", mapToObject(execution.getVariables()));
			setTemplate(templateLocal);
		} else {
			ST templateLocal = templateImpMultibleElementTemplate();
			templateLocal.add("processKey", processdefinition.getKey());
			templateLocal.add("executionUser", executionUser);
			templateLocal.add("processVariables", mapToObject(execution.getVariables()));
			setTemplate(templateLocal);
		}

	}

	/**
	 * Sollte mehr als ein Start Element exisitieren, darf keine 2.
	 * Prozessinstanz gestartet werden. Activiti macht das automatisch. F�r z.B.
	 * Lanes, Subprozesse, CallActivitys
	 * 
	 * @return Stringtemplate f�r StartElement
	 */
	private ST templateImpMultibleElementTemplate() {
		return new ST("// Neues Start Element ausgef�hrt___\n"
				+ "\n<processVariables:{ pv | variableMap.put(\"<pv.variableName>\", <if(pv.isString)>\"<endif><pv.variableContent><if(pv.isString)>\"<endif>);\n}>"
				+ "identityService.setAuthenticatedUserId(\"<executionUser>\");\n");
	}

	@Override
	protected final void addImportsForElemente() {
		setImportTemplate(new ST("import org.activiti.engine.runtime.ProcessInstance;\n"));
	}

	@Override
	protected final ST templateImpl() {
		return new ST(
				"\n<processVariables:{ pv | variableMap.put(\"<pv.variableName>\", <if(pv.isString)>\"<endif><pv.variableContent><if(pv.isString)>\"<endif>);\n}>"
						+ "identityService.setAuthenticatedUserId(\"<executionUser>\");\n"
						+ "processInstance = runtimeService.startProcessInstanceByKey(\"<processKey>\",variableMap);\n");
	}

}
