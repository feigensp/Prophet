package experimentQuestionCreator;


public interface QuestionElementListener {
	void questionElementClose(QuestionElementEvent e);

	void questionElementUp(QuestionElementEvent e);
	
	void questionElementDown(QuestionElementEvent e);
}
