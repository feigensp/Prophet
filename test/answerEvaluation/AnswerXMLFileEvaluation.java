package test.answerEvaluation;

import java.util.ArrayList;
import java.util.HashMap;

import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class AnswerXMLFileEvaluation {
	
	public static final String ANSWER_CATEGORIE_TAG = "answers";
	
	public static ArrayList<HashMap<String, String>> storeAnswers(QuestionTreeNode qtn) {
		ArrayList<HashMap<String, String>> ret = new ArrayList<HashMap<String, String>>();
		//search child with answers
		QuestionTreeNode answers = null;
		for(int i=0; i<qtn.getChildCount(); i++) {
			if(qtn.getChildAt(i).toString().equals(ANSWER_CATEGORIE_TAG)) {
				answers = (QuestionTreeNode) qtn.getChildAt(i);
				break;
			}
		}
		if(answers != null) {
			for(int i=0; i<answers.getChildCount(); i++) {
				String id = ((QuestionTreeNode)answers.getChildAt(i)).getAttributeValue("id");
				String value = ((QuestionTreeNode)answers.getChildAt(i)).getAttributeValue("value");
				if(id != null && value != null) {
					HashMap<String, String> answer = new HashMap<String, String>();
					answer.put("id", id);
					answer.put("value", value);
					ret.add(answer);
				}
				
			}
		}		
		return ret;
	}

}
