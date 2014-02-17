package de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTreeNode;

import java.awt.AWTEvent;


@SuppressWarnings("serial")
public class QuestionTreeNodeEvent extends AWTEvent{
	public static final int QUESTION_TREE_EVENT = RESERVED_ID_MAX + 1;
	private QuestionTreeNode myNode;
	public QuestionTreeNodeEvent(Object source, QuestionTreeNode n) {
		super(source, QUESTION_TREE_EVENT);
		myNode=n;
	}
	public QuestionTreeNode getNode() {
		return myNode;
	}
}
