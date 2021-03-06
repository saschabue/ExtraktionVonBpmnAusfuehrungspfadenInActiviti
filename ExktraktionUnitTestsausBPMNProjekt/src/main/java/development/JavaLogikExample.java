package development;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * Implementierung einer Beispiellogik f�r Service Tasks.
 * 
 * @author Sascha Buelles
 *
 */
public class JavaLogikExample implements JavaDelegate {

	@Override
	public final void execute(final DelegateExecution execution) throws Exception {
		System.out.println("Start Logik");
		System.out.println("ProzessInstanz der Testausf�hrung " + execution.getProcessInstanceId());
		System.out.println("Jepp ich mache Java Logik und bekomme die Execution: " + execution.getId());
		System.out.println("In diesem Prozess existieren Variablen:" + execution.getVariables());

		execution.setVariable("variable1", true);
		System.out.println("Weiter");
	}

}
