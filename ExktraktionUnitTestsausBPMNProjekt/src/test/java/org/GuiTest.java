package org;

import org.extraktion.controlUI.RecordingControlWindow;
import org.junit.Test;

import javafx.application.Application;

/**
 * Test des Nutzerinterfaces zum Erzeugen und Ueberwachen einer Extraktion.
 * 
 * @author Sascha Buelles
 *
 */
public class GuiTest {

	/**
	 * Das Erstellen einer effektiven GUI ist nicht Teil dieser Arbeit. Die GUI
	 * wird daher nur in darauf getestet, ob sie tatsaechlich laedt.
	 * Testmethode.
	 * @throws InterruptedException Unterbrechung im GUI Test
	 */
	@Test
	public void testMethod() throws InterruptedException {

		Thread t = new Thread() {
			@Override
			public void run() {
				Application.launch(RecordingControlWindow.class);
			}
		};
		t.start();
		Thread.sleep(5000);
		RecordingControlWindow.close();

	}
}
