package org.extraktion.controlUI;

import java.util.List;

import org.extraktion.TestObjectDesigner;
import org.extraktion.coverageanalyses.Coverage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * User Interface zur Kontrolle der Extraktion.
 * 
 * @author Sascha Buelles
 */
public class RecordingControlWindow extends Application implements Runnable {
	/**
	 * Observierte Objekte der GUI.
	 */
	private static ObservableList<String> data = FXCollections.observableArrayList();

	/**
	 * Logger des Kontrollinterfaces.
	 */
	private static Logger log = LoggerFactory.getLogger(RecordingControlWindow.class);

	/**
	 * Label f�r Kennzahlen einer Analyse.
	 */
	private static Label aktuelleuberdeckung;
	/**
	 * Label f�r Kennzahlen einer Analyse.
	 */
	private static Label aktuelleUserTaskUeberdeckung;

	/**
	 * Breite des Applicaktionsfensters.
	 */
	private static final int HEIGHT = 600;
	/**
	 * Breite des Applicaktionsfensters.
	 */

	private static final int BUTTONWIDTH = 250;
	/**
	 * Intervall zwischen Abfragen an die Datenbank zum aktuellen Stand.
	 */
	private static final int WIDTH = 850;

	/**
	 * Lokale Referenz zur primaryStage.
	 */
	private static Stage pStage = null;

	/**
	 * Standard Eintrittspunkt in JavaFX Applikation.
	 * 
	 * @param args
	 *            Standard Argumente
	 */
	public static void main(final String[] args) {
		RecordingControlWindow.launch(args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public final void start(final Stage primaryStage) {
		pStage = primaryStage;

		primaryStage.setTitle("Recording Control Panel");
		TextField genClassName = new TextField();
		Label nameTestFile = new Label("Namen Eingeben (ohne Endung/ Dateipfad)");

		Button createTestfileButton = new Button("Create TestFile");
		Button getdata = new Button("Aktualisiere View");
		Button clearButton = new Button("Bereinige System");

		genClassName.setPrefWidth(WIDTH - BUTTONWIDTH);
		nameTestFile.setPrefWidth(BUTTONWIDTH);
		createTestfileButton.setPrefWidth(BUTTONWIDTH);
		clearButton.setPrefWidth(BUTTONWIDTH);
		getdata.setPrefSize(BUTTONWIDTH, BUTTONWIDTH);

		GridPane namingArea = new GridPane();
		namingArea.add(nameTestFile, 0, 0);
		namingArea.add(genClassName, 1, 0);

		GridPane controlButtons = new GridPane();
		controlButtons.add(createTestfileButton, 0, 0);
		controlButtons.add(clearButton, 0, 1);
		controlButtons.add(getdata, 0, 2);

		ListView<String> lw = new ListView<String>();
		lw.setPrefWidth(WIDTH - BUTTONWIDTH);
		lw.setItems(data);

		BorderPane root = new BorderPane();
		root.setLeft(controlButtons);
		root.setTop(namingArea);
		root.setBottom(getAnalysePane());
		root.setRight(lw);

		primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
		primaryStage.show();

		// Schliessen aller offenen Instanzen, wenn Fenster geschlossen wird
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(final WindowEvent event) {
				if (TestObjectDesigner.getInstance().getHibernateSessionFactory().isOpen()) {
					TestObjectDesigner.getInstance().getHibernateSessionFactory().close();
				}
				TestObjectDesigner.getInstance().closeSessionfactory();

			}
		});

		// Button zum Laden der Extraktionsdatenbank und Analysen
		getdata.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						System.out.println("Lade Coverage Analysen. Bitte warten .. ");
						Coverage cover = new Coverage();
						System.out.println("Aktualisiere Tabelle");

						SessionFactory hibernateSessionFactory = TestObjectDesigner.getInstance()
								.getHibernateSessionFactory();
						Session hibernateSession = hibernateSessionFactory.openSession();
						@SuppressWarnings("unchecked")
						List<String> resultList = hibernateSession
								.createNativeQuery("select ELEMENTDEFINITIONKEY from EXTRAKTION").getResultList();
						hibernateSession.beginTransaction().commit();
						data.clear();
						for (String string : resultList) {
							data.add(string);
						}
						aktualisiereUeberdeckungen(Double.toString(cover.getPathCoverage()),
								(Double.toString(cover.getUserTaskCoverage())));

						cover.closeContext();
						cover = null;
						System.out.println("Aktualisierung beenden.");

					}
				});

			}
		});

		// Button zum Erstellen eines JUnit Tests
		createTestfileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				TestObjectDesigner.getInstance();

				TestObjectDesigner.createJUnitTestContent(genClassName.getText());
				TestObjectDesigner.getInstance().clearAndClose();

			}
		});
		clearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				TestObjectDesigner.getInstance();
				SessionFactory hibernateSessionFactory = TestObjectDesigner.getInstance().getHibernateSessionFactory();
				Session openSession = hibernateSessionFactory.openSession();
				openSession.getTransaction().begin();
				try {
					openSession.createNativeQuery("DELETE FROM EXTRAKTION").executeUpdate();
					data.clear();
				} catch (Exception e) {
					log.error("Cleaning failed");
				}
				openSession.close();
				TestObjectDesigner.getInstance().clearAndClose();

			}
		});

	}

	/**
	 * @return Liefert GridPane reserviert f�r Analysekennzahlen
	 */
	private GridPane getAnalysePane() {
		GridPane gp = new GridPane();
		aktuelleuberdeckung = new Label("");
		aktuelleUserTaskUeberdeckung = new Label();
		Label zweigueberdeckung = new Label("C1 Zweig�berdeckung:");
		Label userTaskueberdeckung = new Label("C0 User Task �berdeckung:");

		gp.add(zweigueberdeckung, 0, 0);
		gp.add(aktuelleuberdeckung, 2, 0);
		gp.add(userTaskueberdeckung, 0, 1);
		gp.add(aktuelleUserTaskUeberdeckung, 2, 1);
		gp.setPrefWidth(WIDTH);
		return gp;
	}

	/**
	 * @param pathCoverage
	 *            Neuer Wert der Anaylse der Pfade
	 * @param userTaskCoverage
	 *            Neuer Wert der Anaylse der UserTasks
	 */
	public static void aktualisiereUeberdeckungen(final String pathCoverage, final String userTaskCoverage) {

		aktuelleuberdeckung.setText(pathCoverage);
		aktuelleUserTaskUeberdeckung.setText(userTaskCoverage);

	}

	/**
	 * Methode zum Schlie�en der Applikation von aussen. Spezielle Anwendung
	 * sind Tests.
	 */
	public static void close() {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				pStage.fireEvent(new WindowEvent(pStage, WindowEvent.WINDOW_CLOSE_REQUEST));

			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run() unused
	 */
	@Override
	public void run() {
	}
}
