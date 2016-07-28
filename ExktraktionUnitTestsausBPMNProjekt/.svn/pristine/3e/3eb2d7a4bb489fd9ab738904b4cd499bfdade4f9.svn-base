package org.extraktion.templatesCreation.behaviour;

import java.util.List;

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
public class SkriptTaskElementObject extends SuperElementObject {

	/**
	 * Skript Task.
	 * 
	 * @param execution
	 *            Element
	 */
	public SkriptTaskElementObject(final DelegateExecution execution) {
		super(execution);
	}

	@Override
	protected final void createTemplate(final DelegateExecution execution) {
		List<VariablesObject> listOfProzessVariables = mapToObject(execution.getVariables());
		ST templateLocal = templateImpl();
		templateLocal.add("processVariables", listOfProzessVariables);
		templateLocal.add("actualElement", execution.getCurrentActivityName());
		setTemplate(templateLocal);

	}

	@Override
	protected final void addImportsForElemente() {
		setImportTemplate(new ST(""));
	}

	@Override
	protected final ST templateImpl() {
		return new ST("//________________SkriptTask_Elementname:_<actualElement>___________________\n"
				+ "System.out.println(\"Ausführung ´Skript Task: <actualElement>. Variablenwerte:\");\n"
				+ "<processVariables:{pv| System.out.println(\"Variable <pv.variableName> : <if(pv.isString)>\"<endif><pv.variableContent><if(pv.isString)>\"<endif>\");\n}>"
				+ "//________________SkriptTask____________________\n");
	}

}
