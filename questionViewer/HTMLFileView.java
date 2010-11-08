package questionViewer;

import java.awt.BorderLayout;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.FormView;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

import test.MyTestMain;

/**
 * This class shows the html files (questions) creates the navigation and
 * navigates everything...
 * 
 * @author hasselbe
 * 
 */
public class HTMLFileView extends JPanel {

	public static final int FORWARD = 0;
	public static final int BACKWARD = 1;

	public static final String FOOTER_FORWARD = "<br><br><input name ='nextQuestion' type='submit' value='Weiter'>";
	public static final String FOOTER_BACKWARD_FORWARD = "<br><br><input name ='previousQuestion' type='submit' value='Zurück'><input name ='nextQuestion' type='submit' value='Weiter'>";
	public static final String FOOTER_BACKWARD_END_CATEGORIE = "<br><br><input name ='previousQuestion' type='submit' value='Zurück'><input name ='nextQuestion' type='submit' value='Kategorie Abschließen'>";
	public static final String FOOTER_END_CATEGORIE = "<br><br><input name ='nextQuestion' type='submit' value='Kategorie Abschließen'>";

	private QuestionData data;
	private CategorieQuestionListsPanel cqlp;
	ArrayList<JScrollPane> textPanes;
	private int maxPos, curPos;

	/**
	 * With the call of the Construktor the data ist loaded and everything is
	 * initialised. The first question is showed.
	 * 
	 * @param path
	 *            path of the html file with the data
	 * @param cqlp
	 *            the categorieQuestionListsPanel where the overview is shown
	 */
	public HTMLFileView(String path, CategorieQuestionListsPanel cqlp) {
		super();
		data = new QuestionData(path);
		this.cqlp = cqlp;

		textPanes = new ArrayList<JScrollPane>();
		this.setLayout(new BorderLayout());

		refreshListData();
		start();
	}

	public void addQuestion(QuestionInfos q) {
		// initialize textPane
		JTextPane textPane = new JTextPane();
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
									// What should happen when the buttons are
									// pressed?
									protected void submitData(String data) {
										int action = saveAnswers(data);
										if (action == FORWARD) {
											nextQuestion();
										} else if (action == BACKWARD) {
											previousQuestion();
										} else {
										}
									}
								};
						}
						return super.create(elem);
					}
				};
			}
		});

		// set content to TextPane
		String questSwitch = data.getCategorieSetting("allowswitching");
		questSwitch = questSwitch == null ? "false" : questSwitch;
		if (data.getLastQuestionIndex() > 0 && questSwitch.equals("true")) {
			if (data.getLastQuestionIndex() == data.getQuestionCount(data
					.getLastCategorieIndex()) - 1) {
				textPane.setText("<form>" + q.getValue()
						+ FOOTER_BACKWARD_END_CATEGORIE + "</form>");
			} else {
				textPane.setText("<form>" + q.getValue()
						+ FOOTER_BACKWARD_FORWARD + "</form>");
			}
		} else {
			if (data.getLastQuestionIndex() == data.getQuestionCount(data
					.getLastCategorieIndex()) - 1) {
				textPane.setText("<form>" + q.getValue() + FOOTER_END_CATEGORIE
						+ "</form>");
			} else {
				textPane.setText("<form>" + q.getValue() + FOOTER_FORWARD
						+ "</form>");
			}
		}

		// various settings
		JScrollPane scrollPane = new JScrollPane(textPane);
		textPanes.add(scrollPane);
		this.removeAll();
		this.add(scrollPane, BorderLayout.CENTER);
		maxPos++;
		curPos++;
		cqlp.selectQuestion(data.getLastCategorieIndex(),
				data.getLastQuestionIndex());
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

	/**
	 * starts the questionnaire with the first available Question
	 */
	private void start() {
		QuestionInfos q = getQuestion(0, 0);
		maxPos = curPos = -1;
		addQuestion(q);

		MyTestMain frame = new MyTestMain();
		frame.setVisible(true);
	}

	/**
	 * moves to the next available question
	 */
	private void nextQuestion() {
		if (curPos < maxPos) {
			data.incLastQuestionIndex();
			curPos++;
			this.removeAll();
			this.add(textPanes.get(curPos), BorderLayout.CENTER);
			updateUI();
			cqlp.selectQuestion(data.getLastCategorieIndex(),
					data.getLastQuestionIndex());
		} else {
			QuestionInfos q = getQuestion(data.getLastCategorieIndex(),
					(data.getLastQuestionIndex() + 1));
			if (q == null) {
				endQuestionnaire();
			} else {
				addQuestion(q);
			}
		}
		this.revalidate();
		data.saveAnswersToXML("answers");
	}

	/**
	 * trys to move to the previous question
	 */
	private void previousQuestion() {
		data.decLastQuestionIndex();
		cqlp.selectQuestion(data.getLastCategorieIndex(),
				data.getLastQuestionIndex());
		curPos--;
		this.removeAll();
		this.add(textPanes.get(curPos), BorderLayout.CENTER);
		this.revalidate();
		data.saveAnswersToXML("answers");
	}

	/**
	 * saves the given answers in QuestionData
	 */
	private int saveAnswers(String data) {
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
			if (!name.equals("nextQuestion")
					&& !name.equals("previousQuestion")) {
				System.out.println("bli");
				this.data.addAnswers(name, content);
			} else if (name.equals("nextQuestion")) {
				return FORWARD;
			} else if (name.equals("previousQuestion")) {
				return BACKWARD;
			}
		}
		return -1;
	}

	/**
	 * method which is called when the last question is answered
	 */
	private void endQuestionnaire() {
		System.out.println("Beende Befragung");
	}

	/**
	 * Refreshes the view of the CategorieQuestionListsPanel
	 */
	private void refreshListData() {
		cqlp.removeAll();
		for (int i = 0; i < data.getCategorieCount(); i++) {
			ArrayList<String> questions = new ArrayList<String>();
			for (int j = 0; j < data.getQuestionCount(i); j++) {
				questions.add(data.getQuestion(i, j).getKey());
			}
			cqlp.addCategorie(questions);
		}
		cqlp.selectQuestion(data.getLastCategorieIndex(),
				data.getLastQuestionIndex());
	}

}
