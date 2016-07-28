package org;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Laden der zentral definierten Pfadangaben für das Extraktionssystem
 * gespeichert in pfadsetting.xml. Achtung! Die pathsettings.xml Datei im
 * Workspace ist nur für Anpassungen gedacht und sollte nicht referenziert
 * werden, sondern eine Kopie in der Verzeichnisstruktur im lokalen System.
 * Dadurch wird der allgemeine Zugriff von verschiedenen Laufzeitinstanzen
 * sichergestellt.
 * 
 * @author Sascha Buelles
 *
 */
public class PathsettingController {

	/**
	 * Pfad der Konfigurationsdatei für Pfade, siehe Klassenbeschreibung!
	 */
	private String filename = "C:/ExtraktionTemplateFiles/pathsettings.xml";
	/**
	 * Original Prozessdefintionen.
	 */
	private String originalProcesses = "";
	/**
	 * Erweiterte Prozessdefinitionen.
	 */
	private String extendedProcesses = "";
	/**
	 * Zur Extraktion erstellte Module.
	 */
	private String modulTemplates = "";
	/**
	 * Generierter JUnit Test.
	 */
	private String generatedJUnitTest = "";
	/**
	 * Vorlagen für String Templates des Klassen- und Methodenmoduls.
	 */
	private String stringtemplates = "";
	/**
	 * Templates für Imports, benötigt im JUnit Test.
	 */
	private String importTemplates = "";

	/**
	 * Pfad zu den Original Workflow Defintionen von Tests.
	 */
	private String originalTest = "";

	/**
	 * Pfad zu den Extended Workflow Defintionen von Tests.
	 */
	private String extendedTest = "";

	/**
	 * Konstruktor des PathRecordingController und automatische Initialisierung.
	 */
	public PathsettingController() {
		this.initPfadObjekt();
	}

	/**
	 * Initialisierung der Pfade.
	 */
	private void initPfadObjekt() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		Document doc = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		try {
			doc = db.parse(new File(filename));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		loadPaths(doc);
	}

	/**
	 * Laden der Pfadangaben aus Datei.
	 * 
	 * @param doc
	 *            Verwendetes Dokument.
	 */
	private void loadPaths(final Document doc) {
		originalProcesses = doc.getElementsByTagName("originalProcesses").item(0).getTextContent();
		extendedProcesses = doc.getElementsByTagName("extendedProcesses").item(0).getTextContent();
		modulTemplates = doc.getElementsByTagName("modulTemplates").item(0).getTextContent();
		generatedJUnitTest = doc.getElementsByTagName("generatedJUnitTest").item(0).getTextContent();
		stringtemplates = doc.getElementsByTagName("stringtemplates").item(0).getTextContent();
		importTemplates = doc.getElementsByTagName("importtemplates").item(0).getTextContent();
		extendedTest = doc.getElementsByTagName("extendedTest").item(0).getTextContent();
		originalTest = doc.getElementsByTagName("originalTest").item(0).getTextContent();
	}

	/**
	 * @return Pfad zu Original Prozessen
	 */
	public final String getOriginalProcesses() {
		return originalProcesses;
	}

	/**
	 * @return Pfad zu erweiterten Prozessen.
	 */
	public final String getExtendedProcesses() {
		return extendedProcesses;
	}

	/**
	 * @return Pfad zu Modultemplates.
	 */
	public final String getModulTemplates() {
		return modulTemplates;
	}

	/**
	 * @return Pfad zu JUnit Test.
	 */
	public final String getGeneratedJUnitTest() {
		return generatedJUnitTest;
	}

	/**
	 * @return Pfad zu StringTemplate Vorlagen.
	 */
	public final String getStringtemplates() {
		return stringtemplates;
	}

	/**
	 * @return Pfad zu Import Templates.
	 */
	public final String getImportTemplates() {
		return importTemplates;
	}

	/**
	 * @return Pfad zu Tests Extended WF
	 */
	public final String getExtendedTest() {
		return extendedTest;
	}

	/**
	 * @return Pfad zu Tests Original WF
	 */
	public final String getOriginalTest() {
		return originalTest;
	}

}
