package QuestionTree;


public class QuestionNode extends QuestionTreeNode {
	private String content;

	public QuestionNode(String n) {
		super(n);
	}
	
	public boolean isQuestion() {
		return true;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String c) {
		content=c;
	}
}
