package org.extraktion.templatesCreation.behaviour;

import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.activiti.engine.delegate.DelegateExecution;
import org.extraktion.templatesCreation.SuperElementObject;
import org.extraktion.templatesCreation.behaviour.helper.VariablesObject;
import org.stringtemplate.v4.ST;

/**
 * Temple erstellendes Objekt.
 * 
 * @author Sascha Buelles
 *
 */
@Entity
@Table(name = "EXTRAKTION")
public class EndEventElementObject extends SuperElementObject {

	/**
	 * End Event.
	 * 
	 * @param execution
	 *            Element
	 */
	public EndEventElementObject(final DelegateExecution execution) {
		super(execution);
	}

	@Override
	public final void createTemplate(final DelegateExecution execution) {
		String instanceID = execution.getProcessInstanceId();
		Map<String, Object> variables = execution.getVariables();
		List<VariablesObject> listOfProzessVariables = mapToObject(variables);
		ST templateLocal = templateImpl();
		templateLocal.add("instanceID", instanceID);
		templateLocal.add("processVariables", listOfProzessVariables);
		setTemplate(templateLocal);
	}

	@Override
	protected final void addImportsForElemente() {
		setImportTemplate(new ST(" "));
	}

	@Override
	public final ST templateImpl() {
		return new ST(
				"<processVariables:{ pv |//\"<pv.variableName>\", <if(pv.isString)>\"<endif><pv.variableContent><if(pv.isString)>\"<endif>;\n}>"
						+ "System.out.println(\"Teilprozess (<instanceID>) wurde beendet\");");
	}

}
