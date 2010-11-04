package questionViewer;

import java.util.HashMap;

public class QuestionInfos {
	String key;
	String value;
	
	HashMap<String, String> answers;
	
	public QuestionInfos() {
		key = "";
		value = "";
		
		answers = new HashMap<String, String>();
	}
	
	public QuestionInfos(String key, String value) {
		this.key = key;
		this.value = value;	
		
		answers = new HashMap<String, String>();	
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public void addAnswer(String name, String answer) {
		answers.put(name, answer);
	}
	public HashMap<String, String> getAnswers() {
		return answers;
	}
}