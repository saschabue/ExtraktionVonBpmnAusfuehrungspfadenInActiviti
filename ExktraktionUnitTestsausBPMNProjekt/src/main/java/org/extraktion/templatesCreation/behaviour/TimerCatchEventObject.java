package org.extraktion.templatesCreation.behaviour;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.activiti.engine.delegate.DelegateExecution;
import org.extraktion.templatesCreation.SuperElementObject;
import org.stringtemplate.v4.ST;

/**
 * Temple erstellendes Objekt.
 * 
 * @author Sascha Buelles
 *
 */
@Entity
@Table(name = "EXTRAKTION")
public class TimerCatchEventObject extends SuperElementObject {

	/**
	 * Timer Catch Event.
	 * 
	 * @param execution
	 *            Element.
	 */
	public TimerCatchEventObject(final DelegateExecution execution) {
		super(execution);
	}

	@Override
	protected final void createTemplate(final DelegateExecution execution) {
		ST templateLocal = templateImpl();
		templateLocal.add("actualElement", execution.getCurrentActivityName());
		setTemplate(templateLocal);
	}

	@Override
	protected final void addImportsForElemente() {
		setImportTemplate(new ST(
				"import org.activiti.engine.runtime.Job;\n" + "import org.activiti.engine.ManagementService;"));
	}

	@Override
	protected final ST templateImpl() {
		return new ST("//________________Timer fired:_<actualElement>___________________\n"
				+ "ManagementService managementService = activitiRule.getManagementService();\n"
				+ "Job singleResult = managementService.createJobQuery().processInstanceId(processInstance.getId()).singleResult();\n"
				+ "managementService.executeJob(singleResult.getId());");
	}

}
