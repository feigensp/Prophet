package experimentQuestionCreator;

import java.awt.AWTEvent;

public class QuestionElementEvent extends AWTEvent {
	QuestionElement questionElement;
	public static final int QELECLOSED = RESERVED_ID_MAX + 1;
	public static final int QELEUP = RESERVED_ID_MAX + 2;
	public static final int QELEDOWN = RESERVED_ID_MAX + 3;

	QuestionElementEvent(QuestionElement source, int id, QuestionElement qe) {
		super(source, id);
		this.questionElement = qe;
	}

	public QuestionElement getQuestionElement() {
		return questionElement;
	}

}