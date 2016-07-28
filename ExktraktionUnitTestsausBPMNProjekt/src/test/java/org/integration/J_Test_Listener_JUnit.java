package org.integration;

import java.io.File;
import java.io.FileReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.PathsettingController;
import org.StandardProcessIntegrationTest;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.ActivitiListener;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.CallActivity;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.ServiceTask;
import org.activiti.bpmn.model.UserTask;
import org.junit.Assert;
import org.junit.Test;
import org.transformBpmnDocumentforRecording.concreteObserver.AddCallActivitiLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddServiceTaskLoadObserver;
import org.transformBpmnDocumentforRecording.concreteObserver.AddUserTaskLoadObserver;
import org.transformBpmnDocumentforRecording.transformationLogik.HandleBPMNDocuments;

/**
 * Das Model ist nicht lauffaehig. Hier werden lediglich einzelne Komponenten
 * getestet.
 * 
 * @author Sascha Buelles
 * 
 */
public class J_Test_Listener_JUnit extends StandardProcessIntegrationTest {

	/**
	 * Test.
	 * 
	 * @throws Exception fileException
	 */
	@Test
	public final void testMethod() throws Exception {

		HandleBPMNDocuments handleDocs = new HandleBPMNDocuments();
		new AddUserTaskLoadObserver(handleDocs);

		new AddCallActivitiLoadObserver(handleDocs);

		new AddServiceTaskLoadObserver(handleDocs);

		// Das entstehende Model ist nicht ausfuehrbar. Hier wird nur die Logik
		// fuer bereits vorhandene Listener getestet.
		BpmnModel testmodel = handleDocs.loadDocument("listenerTest.bpmn20.xml", true);
		handleDocs.saveModelAsDocument(testmodel, "listenerTest.bpmn20.xml", true);

		File f = new File(new PathsettingController().getExtendedTest() + "listenerTest.bpmn20.xml");
		Assert.assertTrue(f.exists());

		XMLInputFactory factory = XMLInputFactory.newInstance();
		FileReader fileReader = new FileReader(f);
		XMLStreamReader streamReader = factory.createXMLStreamReader(fileReader);
		BpmnXMLConverter bpmnXMLConverter = new BpmnXMLConverter();
		BpmnModel convertToBpmnModel = bpmnXMLConverter.convertToBpmnModel(streamReader);

		Process process = convertToBpmnModel.getProcesses().get(0);

		ServiceTask serviceTask = process.findFlowElementsOfType(ServiceTask.class).get(0);
		ActivitiListener activitiListener = serviceTask.getExecutionListeners().get(0);
		Assert.assertTrue(activitiListener.getImplementation()
				.equals("org.extraktion.listener.ExecutionListenerImplServiceTask"));

		CallActivity callActivity = process.findFlowElementsOfType(CallActivity.class).get(0);
		ActivitiListener activitiListener1 = callActivity.getExecutionListeners().get(0);
		Assert.assertTrue(activitiListener1.getImplementation()
				.equals("org.extraktion.listener.ExecutionListenerImplCallActivity"));

		UserTask userTask = process.findFlowElementsOfType(UserTask.class).get(0);
		ActivitiListener activitiListener2 = userTask.getTaskListeners().get(0);
		Assert.assertTrue(
				activitiListener2.getImplementation().equals("org.extraktion.listener.TaskListenerImplUserTask"));

		streamReader.close();
		fileReader.close();

	}

}