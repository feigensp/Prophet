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
 * @author Markus Köppen, Andreas Hasselberg
 * 
 */
public class HTMLFileView extends JPanel {

	//constants for moving for- or backward
	public static final int FORWARD = 0;
	public static final int BACKWARD = 1;
	//constants for the html navigation
	public static final String FOOTER_FORWARD = "<br><br><input name ='nextQuestion' type='submit' value='Weiter'>";
	public static final String FOOTER_BACKWARD_FORWARD = "<br><br><input name ='previousQuestion' type='submit' value='Zurück'><input name ='nextQuestion' type='submit' value='Weiter'>";
	public static final String FOOTER_BACKWARD_END_CATEGORY = "<br><br><input name ='previousQuestion' type='submit' value='Zurück'><input name ='nextQuestion' type='submit' value='Kategorie Abschließen'>";
	public static final String FOOTER_END_CATEGORIE = "<br><br><input name ='nextQuestion' type='submit' value='Kategorie Abschließen'>";
	//the data store for everything
	private QuestionData data;
	//overview for the user
	private CategorieQuestionListsPanel cqlp;
	//the textpane (one is one question)
	private ArrayList<JScrollPane> textPanes;
	//current and total size of the panes
	private int maxPos, curPos;

	/**
	 * With the call of the Constructor the data is loaded and everything is
	 * initialized. The first question is showed.
	 * 
	 * @param path
	 *            path of the xml file with the data
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

	/**
	 * starts the questionnaire with the first available Question
	 */
	private void start() {
		QuestionInfos q = getQuestion(0, 0);
		maxPos = curPos = -1;
		addQuestion(q);
		//starting the editor
		MyTestMain frame = new MyTestMain();
		frame.setVisible(true);
	}

	/**
	 * creates a JTextPane with the question and adds it
	 * @param q Question which should be added
	 */
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
					.getLastCategoryIndex()) - 1) {
				textPane.setText("<form>" + q.getValue()
						+ FOOTER_BACKWARD_END_CATEGORY + "</form>");
			} else {
				textPane.setText("<form>" + q.getValue()
						+ FOOTER_BACKWARD_FORWARD + "</form>");
			}
		} else {
			if (data.getLastQuestionIndex() == data.getQuestionCount(data
					.getLastCategoryIndex()) - 1) {
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
		cqlp.selectQuestion(data.getLastCategoryIndex(),
				data.getLastQuestionIndex());
	}

	/**
	 * returns the question in category with the index cat and the index quest
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
	 * moves to the next available question
	 */
	private void nextQuestion() {
		if (curPos < maxPos) {
			data.incLastQuestionIndex();
			curPos++;
			this.removeAll();
			this.add(textPanes.get(curPos), BorderLayout.CENTER);
			cqlp.selectQuestion(data.getLastCategoryIndex(),
					data.getLastQuestionIndex());
		} else {
			QuestionInfos q = getQuestion(data.getLastCategoryIndex(),
					(data.getLastQuestionIndex() + 1));
			if (q == null) {
				endQuestionnaire();
			} else {
				addQuestion(q);
			}
		}
		this.updateUI();
		data.saveAnswersToXML("answers");
	}

	/**
	 * trys to move to the previous question
	 */
	private void previousQuestion() {
		data.decLastQuestionIndex();
		cqlp.selectQuestion(data.getLastCategoryIndex(),
				data.getLastQuestionIndex());
		curPos--;
		this.removeAll();
		this.add(textPanes.get(curPos), BorderLayout.CENTER);
		this.updateUI();
		data.saveAnswersToXML("answers");
	}

	/**
	 * saves the answers
	 * returns an integer to know which button was pressed
	 * @param data data which should be stored an consist the info about which button was pressed
	 * @return FORWARD if forward button, BACKWARD if backward button, -1 if neither
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
		cqlp.selectQuestion(data.getLastCategoryIndex(),
				data.getLastQuestionIndex());
	}

}
