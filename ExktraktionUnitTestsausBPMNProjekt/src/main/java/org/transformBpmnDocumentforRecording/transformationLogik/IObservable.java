package org.transformBpmnDocumentforRecording.transformationLogik;

/**
 * Interface fuer Funktionen zur Erweiterung einer Prozessdefinition.
 * @author Sascha Buelles
 *
 */
public interface IObservable {
	
	/**
	 * Fuege einen benutzerdefinierten Observer hinzu.
	 * @param obs Benutzerdefinierter Observer
	 */
	void addOberserver(Observer obs); 
	/**
	 * Entferne einen benutzerdefinierten Observer.
	 * @param obs Benutzerdefinierter Observer
	 */
	void removeObserver(Observer obs);
	/**
	 * Benachrichtige alle Observer.
	 */
	void notifyObservers();
}
