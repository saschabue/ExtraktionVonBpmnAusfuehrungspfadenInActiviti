package org.extraktion.templatesCreation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.PathsettingController;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.apache.commons.io.FileUtils;
import org.extraktion.templatesCreation.behaviour.helper.VariablesObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupDir;

/**
 * Standard Element als allgemeiner Zugriffspunkt.
 * 
 * @author Sascha Buelles
 *
 */
@MappedSuperclass
@Table(name = "EXTRAKTION")
public abstract class SuperElementObject {
	/**
	 * Logger f�r diese Klasse.
	 */
	@Transient
	private static Logger log = LoggerFactory.getLogger(SuperElementObject.class);

	/**
	 * Id des ausgef�hrten Elements.
	 */
	private long id;
	/**
	 * Name des ausgef�hrten Elements.
	 */
	private String elementName;
	/**
	 * 
	 */
	private String elementDefinitionKey;
	/**
	 * Prozesskey des ausgef�hrten Elements.
	 */
	private String prozesskey;

	/**
	 * Template des ausgef�hrten Elements.
	 */
	@Transient
	private ST template = null;

	/**
	 * Importe des ausgef�hrten Elements.
	 */
	@Transient
	private ST importTemplate = null;

	/**
	 * Die Stringtemplates f�r den Aufbau der JUnit Testklasse werden aus
	 * externen Dateien genommen.
	 */
	@Transient
	private STGroup group = new STGroupDir(new PathsettingController().getStringtemplates());

	/**
	 * Konstruktor f�r das KlassenStruktur Objekt.
	 */
	public SuperElementObject() {
		addImportsForElemente();
	}

	/**
	 * Konstruktor f�r Elemente, die ExecutionListener des Recording Frameworks
	 * aufrufen.
	 * 
	 * @param execution
	 *            Ausgef�hrtes Element
	 */
	public SuperElementObject(final DelegateExecution execution) {
		createTemplate(execution);
		addImportsForElemente();
	}

	/**
	 * Konstruktor f�r UserTask Elemente, die TaskListener des Recording
	 * Frameworks aufrufen.
	 * 
	 * @param execution
	 *            Ausgef�hrter Task
	 */
	public SuperElementObject(final DelegateTask execution) {
		createTemplateForUserTask(execution);
		addImportsForElemente();
	}

	/**
	 * @return Element Prozesskey
	 */
	public String getProzesskey() {
		return prozesskey;
	}

	/**
	 * Setzt Prozesskey.
	 * 
	 * @param prozesskeyM Key
	 */
	public void setProzesskey(final String prozesskeyM) {
		this.prozesskey = prozesskeyM;
	}

	/**
	 * Nullable for SequenceFlows. Speicherung in Datenbanktabelle f�r
	 * Extraktionsverlauf.
	 * 
	 * @return ElementKey
	 */
	@Column(name = "ElementDefinitionKey", nullable = true)
	public String getElementDefinitionKey() {
		return elementDefinitionKey;
	}

	/**
	 * Setzt Elementkey.
	 * 
	 * @param elementDefinitionKeyM Uebergeben des Element Definition Keys
	 */
	public void setElementDefinitionKey(final String elementDefinitionKeyM) {
		this.elementDefinitionKey = elementDefinitionKeyM;
	}

	/**
	 * @return Id des Elements.
	 */
	@Id
	public long getId() {
		return id;
	}

	/**
	 * Setzt Id des Elements.
	 * Hibernatefeld. 
	 * @param idM
	 *            des Elements
	 */
	public void setId(final long idM) {
		this.id = idM;
	}

	/**
	 * Speicherung in Datenbanktabelle f�r Extraktionsverlauf.
	 * 
	 * @return ElementName
	 */
	@Column(name = "ElementName", nullable = true)
	public String getelementName() {
		return elementName;
	}

	/**
	 * Setze ElementNamen.
	 * 
	 * @param elementNameM
	 *            Name des ausgef�hrten Elements
	 */
	public void setelementName(final String elementNameM) {
		this.elementName = elementNameM;
	}

	/**
	 * Setzt das Template.
	 * 
	 * @param templateM
	 *            Template des ausgef�hrten Elements
	 */
	@Transient
	public final void setTemplate(final ST templateM) {
		this.template = templateM;
	}

	/**
	 * Templatevorlagen f�r das Klassen-, Methoden- und Importetemplate.
	 * 
	 * @return Gruppe der Templatevorlagen
	 */
	@Transient
	public final STGroup getGroup() {
		return group;
	}

	/**
	 * Setzt Gruppe der String Templates.
	 * 
	 * @param groupM
	 *            Gruppe der StringTemplates
	 */
	@Transient
	public final void setGroup(final STGroup groupM) {
		this.group = groupM;
	}

	/**
	 * Nicht abstract, da nur das UserTask Element diese Methode implementiert.
	 * Overhead vermeiden.
	 * 
	 * @param execution
	 *            Parameter kommt aus dem delegateTask Paramenter des
	 *            TaskListeners
	 */
	protected void createTemplateForUserTask(final DelegateTask execution) {
	}

	/**
	 * Erstellen der Templates der Elemente.
	 * 
	 * @param execution
	 *            Paramenter kommt aus den ExecutionListenern der Elemente
	 */
	protected abstract void createTemplate(DelegateExecution execution);

	/**
	 * Die ben�tigten Imports f�r Abfragen im JUnit Test, die durch das Template
	 * generiert werden, werden als Textdokumente gespeichert. Siehe
	 * Dateipfade.java.
	 */
	protected abstract void addImportsForElemente();

	/**
	 * StringTemplate Framework kann Listen verarbeiten, aber keine Maps. Daher
	 * Umwandlung in dieser Methode.
	 * 
	 * @param variables
	 *            Variablen vom Typ Map<String, Object>, Variablen aus einer
	 *            Ausf�hrung
	 * @return �bergebe Variablen als Liste mit Typ VariablesObjekt, lesbar f�r
	 *         StringTemplate
	 */
	@Transient
	protected final List<VariablesObject> mapToObject(final Map<String, Object> variables) {
		final List<VariablesObject> variablesObjects = new ArrayList<VariablesObject>();
		variables.forEach((k, v) -> variablesObjects.add(new VariablesObject(k, v)));
		return variablesObjects;
	}

	/**
	 * Wird vom TestObjectDesigner aufgerufen um die Templates der Elemente zu
	 * bekommen.
	 * 
	 * @return gef�lltes String Template
	 */
	@Transient
	public final ST getTemplate() {
		return template;
	}

	/**
	 * Erstellen des Templates.
	 * 
	 * @return Template gef�llt mit Variablen
	 */
	@Transient
	protected abstract ST templateImpl();

	/**
	 * Schreibt das Temlate Elements in eine korrespondierede Datei. Der Pfad
	 * wird in der XML Datei: /src/main/java/pfadsettings.xml gesetzt.
	 */
	public final void writeTemplateToModuleFile() {
		String fileName = id + "_" + elementName + "_" + elementDefinitionKey + "_" + "Modultemplatefile.txt";
		File destinationFile = new File(new PathsettingController().getModulTemplates() + fileName);
		if (destinationFile.exists()) {
			log.error("The destination file already exists");
		}
		try {
			FileUtils.writeStringToFile(destinationFile, template.render());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Ben�tige Templates der Importe werden als Datei gespeichert.
	 */
	public final void writeImportTemplateToFile() {
		String fileName = elementName + "_importTemplate.txt";
		String importTemplates = new PathsettingController().getImportTemplates();
		File destinationFile = new File(importTemplates + fileName);
		if (destinationFile.exists()) {
			log.info("Existierendes Import Template aktualisiert.");
		}
		try {
			FileUtils.writeStringToFile(destinationFile, importTemplate.render());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return Template der Importe
	 */
	@Transient
	public final ST getImportTemplate() {
		return importTemplate;
	}

	/**
	 * Template der Importe.
	 * 
	 * @param importTemplateM Uebergeben eines Templates.
	 */
	@Transient
	public final void setImportTemplate(final ST importTemplateM) {
		this.importTemplate = importTemplateM;
	}

}
