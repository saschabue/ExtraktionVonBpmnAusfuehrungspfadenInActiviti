package org.extraktion.templatesCreation.behaviour.helper;

/**
 * Das Objekt wird benötigt, da StringTemplate nicht über Maps iterieren kann.
 * Die Variablen werden StringTemplate mit Objekten und Attributen dieser Klasse
 * zugänglich gemacht.
 * 
 * @author Sascha Buelles
 */
public class VariablesObject {
	/**
	 * Name einer Variablen.
	 */
	private String variableName;
	/**
	 * Content einer Variablen.
	 */
	private Object variableContent;

	/**
	 * Konstruktor.
	 * 
	 * @param variableNameM
	 *            Name
	 * @param variableContentM
	 *            Inhalt
	 */
	public VariablesObject(final String variableNameM, final Object variableContentM) {
		this.variableName = variableNameM;
		this.variableContent = variableContentM;
	}

	/**
	 * Liefert Variablennamen.
	 * 
	 * @return Name.
	 */
	public final String getVariableName() {
		return variableName;
	}

	/**
	 * Liefert Variablen Inhalt.
	 * 
	 * @return Inhalt
	 */
	public final Object getVariableContent() {
		return variableContent;
	}
}
