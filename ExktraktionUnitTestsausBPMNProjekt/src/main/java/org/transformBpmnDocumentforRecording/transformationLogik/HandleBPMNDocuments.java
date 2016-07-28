package org.transformBpmnDocumentforRecording.transformationLogik;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.PathsettingController;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.exceptions.XMLException;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Zentrale Klasse zur Erweiterung einer Prozessdefinition.
 * 
 * @author Sascha Buelles
 *
 */
public class HandleBPMNDocuments implements IHandleDocuments, IObservable {
	/**
	 * Logger der HandleBPMNDocuments Klasse.
	 */
	private static Logger log = LoggerFactory.getLogger(HandleBPMNDocuments.class);
	/**
	 * Laden und Schreiben der Prozessdefinitionsdatei.
	 */
	private XMLInputFactory factory = XMLInputFactory.newInstance();

	/**
	 * Liste der vom Benutzer hinzugefuegten Observer fuer zu erweiternde
	 * Elemente.
	 */
	private List<Observer> observers = new ArrayList<Observer>();
	/**
	 * Prozessmodell zur Erweiterung.
	 */
	private BpmnModel model = null;

	/**
	 * Standardkonstruktor.
	 */
	public HandleBPMNDocuments() {
	}

	@Override
	public final BpmnModel loadDocument(final String fileName, final boolean isTest) throws NullPointerException {
		File sourceDirectory = null;
		if (isTest) {
			sourceDirectory = new File(new PathsettingController().getOriginalTest());
		} else {
			sourceDirectory = new File(new PathsettingController().getOriginalProcesses());
		}
		File file = null;
		for (File tmp : sourceDirectory.listFiles()) {
			if (tmp.isFile() & tmp.getName().equals(fileName)
					& ((tmp.isFile() & (tmp.getName().endsWith(".bpmn") | tmp.getName().endsWith(".xml"))))) {
				file = tmp;
			}
		}
		if (!file.exists()) {
			log.error("The file does not exist, please check the file name");
			throw new NullPointerException(file.getName());
		}
		try {
			model = this.convertFileToBPMNModel(file);
			notifyObservers();
			return model;
		} catch (XMLException e) {
			log.error("XMLException in Document: " + fileName + " The file is empty or has wrong format: "
					+ e.toString());
		}
		return null;
	}

	/**
	 * Hilfsfunktion zum Laden eines Prozessmodell aus einer Datei. Funktion
	 * nutzt BpmnXMLConverter von Activiti.
	 * 
	 * @param file
	 *            File mit Prozessmodell
	 * @throws XMLException XML Fehler beim Laden
	 * @return BPMN Modell
	 */
	private BpmnModel convertFileToBPMNModel(final File file) throws XMLException {
		XMLStreamReader streamReader = null;

		try {
			streamReader = factory.createXMLStreamReader(new FileReader(file));
			return new BpmnXMLConverter().convertToBpmnModel(streamReader);
		} catch (FileNotFoundException e) {
			log.error("The file does not exist");
			e.printStackTrace();
		} catch (XMLStreamException e) {
			log.error("The file has wrong format" + file.getName());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public final void deployModelToEngine(final String fileName, final String filePath) {
		ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
		try {
			processEngine.getRepositoryService().createDeployment()
					.addInputStream(fileName, new FileInputStream(filePath + fileName)).deploy();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public final void addOberserver(final Observer obs) {
		observers.add(obs);
	}

	@Override
	public final void removeObserver(final Observer obs) {
		observers.remove(obs);
	}

	@Override
	public final void notifyObservers() {
		for (Observer observer : observers) {
			observer.update(model);
		}
	}

	@Override
	public final void saveModelAsDocument(final BpmnModel loadModel, final String fileName, final boolean isTest) {
		File file = null;
		if (isTest) {
			file = new File(new PathsettingController().getExtendedTest() + fileName);
		} else {
			file = new File(new PathsettingController().getExtendedProcesses() + fileName);
		}
		byte[] convertToXML = new BpmnXMLConverter().convertToXML(loadModel);
		System.out.println("check: " + file.getAbsolutePath());
		OutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream(convertToXML.length);
		try {
			baos.write(convertToXML);
			baos.writeTo(outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
