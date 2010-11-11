/**
 * Event für ein QuestionElement
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package test.experimentQuestionCreator;

import java.awt.AWTEvent;

public class QuestionElementEvent extends AWTEvent {
	QuestionElement questionElement;	//Element welches das Event ausgelöst hat
	public static final int QELECLOSED = RESERVED_ID_MAX + 1; 	//Event für das Schliessen
	public static final int QELEUP = RESERVED_ID_MAX + 2;		//Vorwärts-Event
	public static final int QELEDOWN = RESERVED_ID_MAX + 3;		//Rückwärts-Event
	
	/**
	 * Konstruktor für ein neues Event
	 * @param source 
	 * @param id
	 * @param qe
	 */
	QuestionElementEvent(QuestionElement source, int id, QuestionElement qe) {
		super(source, id);
		this.questionElement = qe;
	}
	
	/**
	 * Liefert das Element zurück, welches das event ausgelöst hat
	 * @return QuestionElement, welches das Event auslöste
	 */
	public QuestionElement getQuestionElement() {
		return questionElement;
	}

}