package development;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

/**
 * Beispiellogik.
 * 
 * @author Sascha Buelles
 *
 */
public class ServiceTaskTestLogik implements JavaDelegate {

	@Override
	public final void execute(final DelegateExecution execution) throws Exception {
		execution.setVariable("variable", true);

	}

}
