package org.extraktion.templatesCreation.behaviour;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.activiti.engine.delegate.DelegateExecution;
import org.extraktion.templatesCreation.SuperElementObject;
import org.stringtemplate.v4.ST;

/**
 * Temple erstellendes Objekt.
 * @author Sascha Buelles
 *
 */
@Entity
@Table(name = "EXTRAKTION")
public class SequenceFlowElementObject extends SuperElementObject {

	/**
	 * Sequenzfluss.
	 * @param execution vorheriges Element
	 */
	public SequenceFlowElementObject(final DelegateExecution execution) {
		super(execution);
	}

	@Override
	public final void createTemplate(final DelegateExecution execution) {
		// Execution referenziert nur das vorangegangene Element, nicht den
		// Sequenzfluss. Kein Abfrage bzgl. Target
		// Sequenzflussausfuehrung automatisch ->
		// Im Testlauf aufgezeichnete Asserts werden im Unittest dazu nicht
		// synchron verarbeitet.
		// Automatische Elemente

		ST templateLocal = templateImpl();

		templateLocal.add("currentActivityID", execution.getCurrentActivityId());
		setTemplate(templateLocal);
	}

	@Override
	protected final void addImportsForElemente() {
		setImportTemplate(new ST(""));
	}

	/*
	 * Asserts k�nnen nur in Elemente, die in einem Wait-State sind, eingebracht
	 * werden. Eine Synchronisation von Asserts zu anderen Elementen, die nicht
	 * warten, ist in einem JUnit Test nicht m�glich. Wird aus einem Wait-State
	 * ein Prozess in einem JUnit Test fortgesetzt, dh. es existieren keine
	 * synchronisierenden Listener mehr, werden die Elemente bis zum n�chsten
	 * W-S bearbeitet. Wann Ver�nderungen durch z.B. Service oder Skript Tasks
	 * ausgef�hrt werden, kann nicht bestimmt werden. Asserts k�nnen also
	 */
	@Override
	public final ST templateImpl() {
		return new ST("\n\n//Take Sequence Flow: Vorangeganenes Objekt:<currentActivityID>\n\n");
	}



}
