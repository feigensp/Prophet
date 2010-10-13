/**
 * Interface für den QuestionElementListener
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package experimentQuestionCreator;


public interface QuestionElementListener {
	/**
	 * Methode für das Schliessen
	 * @param e
	 */
	void questionElementClose(QuestionElementEvent e);
	
	/**
	 * Methode für das Vorrücken
	 * @param e
	 */
	void questionElementUp(QuestionElementEvent e);
	
	/**
	 * Methode für das Zurückfallen
	 * @param e
	 */
	void questionElementDown(QuestionElementEvent e);
}
