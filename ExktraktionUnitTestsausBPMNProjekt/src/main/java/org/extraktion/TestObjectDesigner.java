package org.extraktion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.PathsettingController;
import org.apache.commons.io.FileUtils;
import org.extraktion.templatesCreation.behaviour.TestClassStructureObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Zentrale Schnittstelle zur Verwaltung des Benutzer Interfaces und Erstellung
 * einer JUnit Testklasse.
 * 
 * @author Sascha Buelles
 */
public class TestObjectDesigner {
	/**
	 * Logger des TestObjectDesigner.
	 */
	private static Logger log = LoggerFactory.getLogger(TestObjectDesigner.class);

	/**
	 * Instanz des TestObjectDesigner.
	 */
	private static TestObjectDesigner testobjectdesignerInstance;

	/**
	 * Hibernate Session f�r Speicherungsablauf.
	 */
	private SessionFactory sessionFactory = null;

	/**
	 * Eine Instanz pro Applikation.
	 * 
	 * @return TestObjectDesigner Instanz TestObjectDesigner.
	 * @author Sascha Buelles
	 */
	public static TestObjectDesigner getInstance() {
		try {
			if (TestObjectDesigner.testobjectdesignerInstance == null) {
				testobjectdesignerInstance = new TestObjectDesigner();
				log.info("Neue TestObjectDesigner-Instanz wird erzeugt");
			}
		} catch (Exception e) {
			log.error("TestObjectDesigner Instanz Fehler");
			e.printStackTrace();
		}
		return TestObjectDesigner.testobjectdesignerInstance;
	}

	/**
	 * �berf�hrt die ausfgef�llten Templates der Ausf�hrungsobjekte in das JUnit
	 * Klassentemplate, rendert das Template und schreibt die JUnit Datei.
	 * Zieldatei f�r JUnit Testklasse
	 * 
	 * @param userEingabeZielDateiName
	 *            Usereingabe des Dateinames
	 */
	public static void createJUnitTestContent(final String userEingabeZielDateiName) {
		String filename = "";
		if (userEingabeZielDateiName.equals("")) {
			filename = "StandardTestFileName";
		} else {
			filename = userEingabeZielDateiName;
		}
		// DateiObjekt erstellen im Zielverzeichnis
		File destinationFile = new File(
				new PathsettingController().getGeneratedJUnitTest() + "ProcessTest_" + filename + "_JUnit" + ".java");

		// Pr�fe ob Datei existiert. Wenn ja, ver�ndere Namen
		for (int fileNameExtension = 1; destinationFile.exists(); fileNameExtension++) {
			log.warn("The destination file already exists! Try to create file of version number: " + fileNameExtension);
			destinationFile = new File(new PathsettingController().getGeneratedJUnitTest() + "ProcessTest_" + filename
					+ fileNameExtension + "_JUnit" + ".java");
		}
		// Schreibe die JUnit Testklasse
		TestClassStructureObject testClassStructureObject = new TestClassStructureObject();
		testClassStructureObject.writeFilledJUnitStructure(destinationFile, transferExecutionModuleTemplateFiles(),
				transferExecutionImportTemplateFiles());
		log.info("Created File in: " + destinationFile.getAbsolutePath());
	}

	/**
	 * Holt die bereits erstellten Templates aus den einzelnen
	 * Ausf�hrungsmodulen in er Reihenfolge der Datenbankeintr�ge. F�gt sie dem
	 * resultString hinzu.
	 * 
	 * @return Module der Extraktion
	 * @author Sascha Buelles
	 */
	private static String transferExecutionModuleTemplateFiles() {
		String result = "";
		// Logger Warnung bei StringL�nge = 2/3*maxStringL�nge, Logger Error bei
		// 9/10
		final int resultStringWarningLog = 1431655764;
		final int resultStringWarningErr = 1932735282;

		// Elemente in der Reihenfolge der Ausf�hrung aus Datenbank lesen
		// Reihenfolge der IDs
		// (Dateien stellen Reihenfolge durch Bennenung nicht sicher
		// BSP: Statt 1 < 2 < 10 : 1 < 10 < 2)

		SessionFactory hibernateSessionFactory = TestObjectDesigner.getInstance().getHibernateSessionFactory();
		Session session = hibernateSessionFactory.openSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<String> executionList = session.createNativeQuery("SELECT ELEMENTDEFINITIONKEY FROM EXTRAKTION;")
				.getResultList();

		@SuppressWarnings("unchecked")
		List<String> callActivitiList = session
				.createNativeQuery("SELECT ELEMENTDEFINITIONKEY FROM EXTRAKTION WHERE ELEMENTNAME ='CallActiviti';")
				.getResultList();
		session.close();

		// Laden der Modulinhalte und zu String adden
		FileInputStream fis = null;
		byte[] data = null;
		File[] listFiles = new File(new PathsettingController().getModulTemplates()).listFiles();

		for (String rl : executionList) {
			for (File file : listFiles) {
				if (file.getName() != null & rl != null) {
					// "_"+rl+"_" Pattern stellt sicher, dass der Name des
					// Elements genau rl ist
					if (file.getName().contains("_" + rl + "_")) {
						try {
							fis = new FileInputStream(file);
							data = new byte[(int) file.length()];
							fis.read(data);
							fis.close();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// F�ge iterativ die Moduldatei-Inhalte zum Result
						// String hinzu. Logger speichert Gr��e f�r jeden
						// Durchlauf
						try {
							log.info("Die Gr�sse des result String ist " + result.length());
							if (result.length() > resultStringWarningLog) {
								log.warn(
										"Die Gr�sse des result String hat �ber 2/3 der maximalen L�nge angenommen. Bitte �berwachen");
							}
							if (result.length() > resultStringWarningErr) {
								log.error("Kritische Gr�sse erreicht f�r L�nge des Result String");
							}
							if (callActivitiList.contains(rl)) {
								result = new String(data, "UTF-8") + result;
							} else {
								result += new String(data, "UTF-8");
							}
						} catch (UnsupportedEncodingException e) {
							log.error("Hinzuf�gen von Content zur erzeugten JUnit Testklasse nicht m�glich");
							e.printStackTrace();
						}
						break;
					}
				}
			}
		}
		return result;
	}

	/**
	 * Lade Templates f�r die Imports.
	 * 
	 * @return Ben�tigte Imports als Liste von Strings
	 * @author Sascha Buelles
	 */
	private static List<String> transferExecutionImportTemplateFiles() {
		List<String> importElemente = new ArrayList<String>();

		// Suche die Typen der erstellten Elemente aus der Datenbank
		Session session = TestObjectDesigner.getInstance().getHibernateSessionFactory().openSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<String> distinctElementListRecorded = session
				.createNativeQuery("SELECT DISTINCT ELEMENTNAME FROM EXTRAKTION;").getResultList();
		session.getTransaction().commit();
		session.close();

		File[] listFiles = new File(new PathsettingController().getImportTemplates()).listFiles();
		FileInputStream fis = null;
		byte[] data = null;

		// F�r jedes Element, das in der Liste der erstellten Elemente liegt,
		// suche das passende Import Template, hinterlegt als Datei
		for (String element : distinctElementListRecorded) {
			for (File file : listFiles) {
				if (file.getName().contains(element)) {
					try {
						fis = new FileInputStream(file);
						data = new byte[(int) file.length()];
						fis.read(data);
						fis.close();
					} catch (FileNotFoundException e) {
						log.error("Import Datei nicht gefunden");
						e.printStackTrace();
					} catch (IOException e) {
						log.error("Import Datei nicht lesbar");
						e.printStackTrace();
					}
					try {
						// Adde die Templates in die Liste
						importElemente.add(new String(data, "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						log.error("Hinzuf�gen von Content zur Imports-Liste f�r JUnit Testklasse nicht m�glich");
						e.printStackTrace();
					}
					break;
				}
			}
		}
		return importElemente;
	}

	/**
	 * @return Main Thread of the Application
	 * @author Sascha Buelles
	 */
	public final Thread getMainThread() {
		return Thread.currentThread();
	}

	/**
	 * Initialisiere Hibernate Session Factory.
	 * 
	 * @author Sascha Buelles
	 */
	private void initSessionFactory() {
		Configuration cfg = new Configuration().configure();
		sessionFactory = cfg.buildSessionFactory();

	}

	/**
	 * @return Hibernate Session zum Speichern der Referenzierung f�r Templates
	 * @author Sascha Buelles
	 */
	public final SessionFactory getHibernateSessionFactory() {
		if (sessionFactory == null) {
			this.initSessionFactory();
		}
		return sessionFactory;
	}

	/**
	 * Schlie�e SessionFactory and l�sche Modulaufzeichnungen in DB und lokalen
	 * Verzeichnissen.
	 * 
	 * @author Sascha Buelles
	 */
	public final void clearAndClose() {

		try {
			FileUtils.cleanDirectory(new File(new PathsettingController().getModulTemplates()));
			FileUtils.cleanDirectory(new File(new PathsettingController().getImportTemplates()));
		} catch (IOException e) {
			log.error("Probleme beim L�schen der Module beim Aufr�umen");
			e.printStackTrace();
		}

		getHibernateSessionFactory().close();
		sessionFactory = null;
		testobjectdesignerInstance = null;

	}

	/**
	 * Hibernate SessionFactory komplett schliessen und Objekt vernichten.
	 */
	public final void closeSessionfactory() {
		sessionFactory.close();
		sessionFactory = null;

	}

}
