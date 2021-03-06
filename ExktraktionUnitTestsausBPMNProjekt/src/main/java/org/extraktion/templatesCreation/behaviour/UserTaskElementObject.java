package org.extraktion.templatesCreation.behaviour;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.repository.ProcessDefinition;
import org.extraktion.templatesCreation.SuperElementObject;
import org.extraktion.templatesCreation.behaviour.helper.VariablesObject;
import org.stringtemplate.v4.ST;

/**
 * Temple erstellendes UserTask Objekt.
 * 
 * @author Sascha Buelles
 *
 */
@Entity
@Table(name = "EXTRAKTION")
public class UserTaskElementObject extends SuperElementObject {

	@Override
	protected void createTemplate(final DelegateExecution execution) {
		// UserTask bekommt keine DelegateExecution sondern einen DelegateTask
	}

	/**
	 * @param execution Deligiertes Objekt.
	 */
	public UserTaskElementObject(final DelegateTask execution) {
		super(execution);
	}

	@Override
	public final void createTemplateForUserTask(final DelegateTask execution) {
		String instanceCount = "";
		
		ProcessDefinition singleResult = execution.getExecution()
				.getEngineServices().getRepositoryService()
				.createNativeProcessDefinitionQuery()
				.sql("Select * FROM ACT_RE_PROCDEF WHERE ID_ ='" 
								+ execution.getProcessDefinitionId() + "'")
				.singleResult();
		
		// Wenn Task mehrere Instanzen hat muss eindeutige ID fuer
		// Extraktionsteablle erweitert werden
		if (execution.getVariable("loopCounter") != null) {
			instanceCount = execution.getVariableInstance("loopCounter")
					.getTextValue();
		}
		
		setElementDefinitionKey(execution.getTaskDefinitionKey() 
				+ instanceCount);
		String executionUser = execution.getAssignee();
		List<VariablesObject> listOfProzessVariables =
								mapToObject(execution.getVariables());
		String processKey = singleResult.getKey();

		ST templateLocal = templateImpl();
		templateLocal.add("taskDefinitionKey", execution
				.getTaskDefinitionKey());
		templateLocal.add("executionUser", executionUser);
		templateLocal.add("processVariables", listOfProzessVariables);
		templateLocal.add("processKey", processKey);
		templateLocal.add("instanceCount", instanceCount);
		setTemplate(templateLocal);
	}

	@Override
	protected final void addImportsForElemente() {
		setImportTemplate(new ST("import org.activiti.engine.task.Task;\n"
				+ "import org.junit.Assert;"));
	}

	@Override
	protected final ST templateImpl() {
		return new ST("\n//___User Task___\n" 
				+ "processInstance = runtimeService.createProcessInstanceQuery()"
				+ ".processDefinitionKey(\"<processKey>\").singleResult();\n"
				
				+ "Task task_<taskDefinitionKey><instanceCount> = "
				+ "(taskService.createTaskQuery()"
				+ 		".processInstanceId(processInstance.getId())).active()\n"
				+ "		.taskDefinitionKey(\"<taskDefinitionKey>\")"
				+ 		".singleResult();\n"
				+ "Assert.assertNotNull(\"Der Originalprozess wurde "
				+ 							"veraendert. Task nicht gefunden\", "
				+ 					"task_<taskDefinitionKey><instanceCount>);\n"
				
				+ "taskService.setAssignee("
				+ "task_<taskDefinitionKey><instanceCount>.getId(),"
				+ "\"<executionUser>\");\n"
				
				+ "<processVariables:{ pv | variableMap.put(\"<pv.variableName>\", "
				+ "<if(pv.isString)>\"<endif> <pv.variableContent>"
				+ "<if(pv.isString)>\"<endif>);\n}>"
				
				+ "taskService.complete"
				+ "(task_<taskDefinitionKey><instanceCount>.getId(), variableMap);\n"
				
				+ "//___User Task___\n");
	}

}
