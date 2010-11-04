package questionViewer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.FormView;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class HTMLFileView extends JScrollPane {

	private JTextPane textPane;
	private QuestionData data;
	private CategorieQuestionListsPanel cqlp;

	public HTMLFileView(String path, CategorieQuestionListsPanel cqlp) {
		super();

		data = new QuestionData(path);

		this.cqlp = cqlp;
		
		textPane = new JTextPane();
		this.setViewportView(textPane);
		textPane.setEditable(false);
		textPane.setEditorKit(new HTMLEditorKit() {
			public ViewFactory getViewFactory() {
				return new HTMLEditorKit.HTMLFactory() {
					public View create(Element elem) {
						Object o = elem.getAttributes().getAttribute(
								StyleConstants.NameAttribute);
						if (o instanceof HTML.Tag) {
							HTML.Tag kind = (HTML.Tag) o;
							if (kind == HTML.Tag.INPUT)
								return new FormView(elem) {
									protected void submitData(String data) {
										saveAnswers(data);
										nextQuestion();
									}
								};
						}
						return super.create(elem);
					}
				};
			}
		});
		
		refreshListData();
		start();
	}
	
	public void refreshListData() {
		cqlp.removeAll();
		for(int i=0; i<data.getCategorieCount(); i++) {
			ArrayList<String> questions = new ArrayList<String>();
			for(int j=0; j<data.getQuestionCount(i); j++) {
				questions.add(data.getQuestion(i, j).getKey());
			}
			cqlp.addCategorie(questions, false);
		}
		cqlp.selectQuestion(data.getLastCategorieIndex(), data.getLastQuestionIndex());
	}

	/**
	 * returns the question in categorie with the index cat and the index quest
	 * itself if it not exist it tries to get the next question if it find no
	 * next question it returns null
	 * 
	 * @param cat
	 * @param quest
	 * @return
	 */
	private QuestionInfos getQuestion(int cat, int quest) {
		for (int i = cat; i < data.getCategorieCount(); i++) {
			if (data.getQuestionCount(i) > quest) {
				return data.getQuestion(i, quest);
			} else {
				quest = 0;
			}
		}
		return null;
	}

	// starts the questionnaire with showing the first question
	public void start() {
		QuestionInfos q = getQuestion(0, 0);
		if (q != null) {
			textPane.setText(q.getValue());
			cqlp.selectQuestion(data.getLastCategorieIndex(), data.getLastQuestionIndex());
		} else {
			endQuestionnaire();
		}
	}

	/**
	 * Moves to the next Question loads the next HTML-File in the list if its a
	 * new categorie it tells the editor to show new files an settings
	 */
	private void nextQuestion() {
		QuestionInfos q = getQuestion(data.getLastCategorieIndex(), data.getLastQuestionIndex()+1);
		if(q != null) {
			textPane.setText(q.getValue());	
			cqlp.selectQuestion(data.getLastCategorieIndex(), data.getLastQuestionIndex());		
		} else {
			endQuestionnaire();
		}
	}

	/**
	 * saves the given answers in Question Data
	 */
	private void saveAnswers(String data) {
		StringTokenizer st = new StringTokenizer(data, "&");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			String name = null;
			String content = null;
			try {
				name = URLDecoder.decode(
						token.substring(0, token.indexOf("=")), "ISO-8859-1");
				content = URLDecoder
						.decode(token.substring(token.indexOf("=") + 1,
								token.length()), "ISO-8859-1");
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}
			this.data.addAnswers(name, content);
		}
	}

	private void endQuestionnaire() {
		System.out.println("Beende Befragung");
	}

}
