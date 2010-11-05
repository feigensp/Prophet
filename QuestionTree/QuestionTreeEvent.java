package QuestionTree;

import java.awt.AWTEvent;

@SuppressWarnings("serial")
public class QuestionTreeEvent extends AWTEvent{
	public static final int QUESTION_TREE_EVENT = RESERVED_ID_MAX + 1;
	QuestionTreeNode myNode;
	QuestionTreeEvent(Object source, QuestionTreeNode n) {
		super(source, QUESTION_TREE_EVENT);
		myNode=n;
	}
	public QuestionTreeNode getNode() {
		return myNode;
	}
}
