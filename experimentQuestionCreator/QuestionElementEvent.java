package experimentQuestionCreator;

import java.awt.AWTEvent;

public class QuestionElementEvent extends AWTEvent {
	QuestionElement questionElement;
	public static final int QELECLOSED = RESERVED_ID_MAX - 1;

	QuestionElementEvent(QuestionElement source, int id, QuestionElement qe) {
		super(source, id);
		this.questionElement = qe;
	}

	public QuestionElement getQe() {
		return questionElement;
	}

}