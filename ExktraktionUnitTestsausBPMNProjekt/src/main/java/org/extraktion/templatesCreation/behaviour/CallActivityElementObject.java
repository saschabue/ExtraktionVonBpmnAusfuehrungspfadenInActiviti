package org.extraktion.templatesCreation.behaviour;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.PathsettingController;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.bpmn.behavior.CallActivityBehavior;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.PvmProcessElement;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
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
public class CallActivityElementObject extends SuperElementObject {

	/**
	 * Call Activiti Konstruktor.
	 * 
	 * @param execution
	 *            Element
	 */
	public CallActivityElementObject(final DelegateExecution execution) {
		super(execution);
	}

	@Override
	protected final void createTemplate(final DelegateExecution execution) {
		String actualElement = execution.getCurrentActivityName();
		String processkey = "";

		ProcessDefinition processdefinition = null;

		PvmProcessElement eventSource = ((ExecutionEntity) execution).getEventSource();
		ActivityImpl sourceElement = (ActivityImpl) eventSource;
		if (sourceElement.getActivityBehavior() instanceof CallActivityBehavior) {
			CallActivityBehavior a = (CallActivityBehavior) sourceElement.getActivityBehavior();

			processkey = a.getProcessDefinitonKey();
		}
		processdefinition = execution.getEngineServices().getRepositoryService().createNativeProcessDefinitionQuery()
				.sql("Select * FROM ACT_RE_PROCDEF WHERE Key_ ='" + processkey + "'").singleResult();

		ST templateLocal = templateImpl();
		templateLocal.add("actualElement", actualElement);
		templateLocal.add("processkey", processkey);
		templateLocal.add("ressourceName", processdefinition.getResourceName());
		templateLocal.add("filename",
				new PathsettingController().getOriginalProcesses() + processdefinition.getResourceName());
		setTemplate(templateLocal);
	}

	@Override
	protected final void addImportsForElemente() {
		setImportTemplate(new ST(""));
	}

	@Override
	protected final ST templateImpl() {
		return new ST("\nSystem.out.println(\"CallActivity <actualElement> wird deployed\");\n"
				+ "Deployment deploy_<processkey> = repositoryService.createDeployment().addInputStream(\"<ressourceName>\",new FileInputStream(\"<filename>\")).deploy();\n");
	}

}
