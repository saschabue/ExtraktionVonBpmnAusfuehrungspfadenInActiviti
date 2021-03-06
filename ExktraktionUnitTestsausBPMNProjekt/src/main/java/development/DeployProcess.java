package development;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.test.ActivitiRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Deploy von Prozessen �ber Eclipse. Upload via Explorer f�hrt teilweise zu Datenverlusten.
 * @author Sascha Buelles
 * 
 */
public class DeployProcess {
	/**
	 * Pfad zur Prozessdatei.
	 */
	private String filename = "./src/main/resources/extended/processFloh.bpmn20.xml";
	
	/**
	 * Activiti Rule.
	 */
	@Rule
	public ActivitiRule activitiRule = new ActivitiRule();

	/**
	 * Helfermethode zum deployen von Prozessen.
	 */
	@Test
	public final void deployProcess() {
		RepositoryService repositoryService = activitiRule.getRepositoryService();

		try {
			repositoryService.createDeployment()
					.addInputStream("processFloh.bpmn20.xml", new FileInputStream(filename)).deploy();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
