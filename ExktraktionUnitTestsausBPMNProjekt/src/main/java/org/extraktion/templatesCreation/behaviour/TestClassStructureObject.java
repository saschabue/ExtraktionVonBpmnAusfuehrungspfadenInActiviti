package org.extraktion.templatesCreation.behaviour;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.persistence.Entity;

import org.PathsettingController;
import org.activiti.engine.delegate.DelegateExecution;
import org.apache.commons.io.FileUtils;
import org.extraktion.TestObjectDesigner;
import org.extraktion.templatesCreation.SuperElementObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

/**
 * Temple erstellendes Objekt.
 * 
 * @author Sascha Buelles
 *
 */
@Entity
public class TestClassStructureObject extends SuperElementObject {
	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory
			.getLogger(TestClassStructureObject.class);

	/**
	 * Konstruktor des Elements fuer den 
	 * Aufbau der Klasse eines JUnit Tests.
	 */
	public TestClassStructureObject() {
		super();
	}

	/**
	 * Erstellt eine JUnit Klasse.
	 * 
	 * @param destination
	 *            Zieldatei
	 * @param filledExecutionTemplates
	 *            Ausfuehrungsmodule
	 * @param importTemplateContent
	 *            Module der Importe
	 */
	public final void writeFilledJUnitStructure(final File destination, 
			final String filledExecutionTemplates,
			final List<String> importTemplateContent) {
		String processName = "";

		// Abfrage des Prozessdefinitonsnamens,
		// der waehrend der Ausfuehrung in
		// das StartEvent geschrieben wurde
		SessionFactory hibernateSessionFactory = 
				TestObjectDesigner.getInstance().getHibernateSessionFactory();
		Session session = hibernateSessionFactory.openSession();
		session.beginTransaction();

		@SuppressWarnings("unchecked")
		NativeQuery<String> startelementKeyQuery = session
				.createNativeQuery(
						"SELECT PROZESSKEY FROM EXTRAKTION "
						+ "WHERE ELEMENTNAME ='StartElement';");
		
		if (startelementKeyQuery.getResultList().size() > 1) {
			log.error("Es existiert mehr als eine "
					+ "Prozessdefinitionsdatei in der Ausfuehrung");
		}
		
		// StartElement ist per Definition das erste Element 
		// in einer Ausfuehrung
		if (startelementKeyQuery.getResultList().size() == 0) {
			log.info("Es existieren keine Extraktionen im System");
		} else {
			processName = startelementKeyQuery.getResultList()
					.get(0).toString();
			
			/** Hier findet das Erstellen der JUnit Test Klasse statt **/
			
			// Erstellen String Templates fuer den Methodenaufbau 
			ST createTestMethodTemplate = 
					createTestMethodTemplate(processName, filledExecutionTemplates);

			// Erstellen der JUnit Testklasse als String Objekt
			String filledClassContent = 
					(createTestClassTemplate(destination.getName()
							.replace(".java", ""),
					new PathsettingController().getOriginalProcesses() + processName, 
					importTemplateContent,
					createTestMethodTemplate)).render();

			// Schreiben der JUnit Testklasse in Datei
			writeFile(filledClassContent, destination);
		}
		session.close();
	}

	/**
	 * Methode zum Erstellen eines Klassentemplates.
	 * 
	 * @param processName
	 *            Name des Prozesses
	 * @param prozessFilePath
	 *            Pfad zur Prozessdefinition
	 * @param importTemplateContent
	 *            Importe fuer den JUnit Test
	 * @param generatedMethod
	 *            Generierte Methode bestehend aus den Modulen der
	 *            Elementausfuehrungen
	 * @return String Template der Klasse
	 */
	private ST createTestClassTemplate(final String processName, 
			final String prozessFilePath,
			final List<String> importTemplateContent, 
			final ST generatedMethod) {
		
		// Fuege der Liste der Importe die fuer den JUnit Test
		// benoetigten imports hinzu.
		importTemplateContent.add(getImportTemplate().render());

		ST importTemplate = getGroup()
				.getInstanceOf("jUnit_Imports_Template");
		importTemplate.add("imports", importTemplateContent);

		ST classTemplate = getGroup()
				.getInstanceOf("jUnit_ClassFrame_Template");
		classTemplate.add("imports", importTemplate);
		classTemplate.add("processName", processName);
		classTemplate.add("prozessFilePath", prozessFilePath);
		classTemplate.add("generatedMethod", generatedMethod);
		return classTemplate;
	}

	/**
	 * Erstellt die Testmethode fuer den JUnit Test.
	 * 
	 * @param processFile
	 *            Die zu testende Prozessdatei
	 * @param ausfuehrungen
	 *            Module der Elementausfuehrungen als String
	 * @return Stringtemplate der Methode
	 */
	private ST createTestMethodTemplate(final String processFile,
			final String ausfuehrungen) {
		
		ST methodTemplate = getGroup()
				.getInstanceOf("jUnit_TestMethod_Template");
		methodTemplate.add("processFile", processFile);
		methodTemplate.add("ausfuehrungen", ausfuehrungen);
		return methodTemplate;
	}

	/**
	 * Benoetigte Importe fuer leeren JUnit Test. 
	 * Importe fuer Elementausfuehrungen werden
	 * durch Elemente selbst bestimmt.
	 */
	@Override
	protected final void addImportsForElemente() {
		setImportTemplate(new ST("import java.io.FileInputStream;\n" 
				+ "import java.util.HashMap;\n"
				+ "import java.util.Map;\n" 
				+ "import org.activiti.engine.test.ActivitiRule;\n"
				+ "import org.junit.Rule;\n" 
				+ "import org.junit.Test;\n"
				+ "import org.activiti.engine.IdentityService;\n" 
				+ "import org.activiti.engine.RepositoryService;\n"
				+ "import org.activiti.engine.RuntimeService;\n" 
				+ "import org.activiti.engine.TaskService;\n"
				+ "import org.activiti.engine.repository.Deployment;\n"));
	}

	/**
	 * Diese Methode wird hier nicht gebraucht! 
	 * Klassentemplate ist Spezialfall.
	 * 
	 * @param execution
	 *            hier nicht gebraucht.
	 */
	@Override
	protected void createTemplate(final DelegateExecution execution) {
	}

	/**
	 * Schreibt die JUnit Klasse in die angegebene Datei.
	 * 
	 * @param result
	 *            JUnit Testklasse als String Objekt.
	 * @param targetFile
	 *            Schreibt String in eine Zieldatei
	 */
	public final void writeFile(final String result, final File targetFile) {
		try {
			FileUtils.writeStringToFile(targetFile, result);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected final ST templateImpl() {
		// Wird hier nicht gebraucht.
		return null;
	}
}
