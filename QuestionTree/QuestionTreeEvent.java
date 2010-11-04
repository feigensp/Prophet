package QuestionTree;

import java.awt.AWTEvent;
import java.io.File;

@SuppressWarnings("serial")
public class QuestionTreeEvent extends AWTEvent{
	public static final int CATEGORY_OPENED = RESERVED_ID_MAX + 1;
	public static final int QUESTION_OPENED = CATEGORY_OPENED + 1;
	QuestionTreeNode myNode;
	QuestionTreeEvent(Object source, CategoryNode n) {
		super(source, CATEGORY_OPENED);
		myNode=n;
	}
	QuestionTreeEvent(Object source, QuestionNode n) {
		super(source, QUESTION_OPENED);
		myNode=n;
	}
	public QuestionTreeNode getNode() {
		return myNode;
	}
}
