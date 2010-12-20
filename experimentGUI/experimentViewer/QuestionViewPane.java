package experimentGUI.experimentViewer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
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

import experimentGUI.Constants;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;




/**
 * This class shows the html files (questions) creates the navigation and
 * navigates everything...
 * 
 * @author Markus Köppen, Andreas Hasselberg
 * 
 */
@SuppressWarnings("serial")
public class QuestionViewPane extends JScrollPane {
	//constants for the html navigation
	private static final String HTML_START = "<html><body><form>";
	private static final String HTML_DIVIDER = "<br /><br /><hr /><br /><br />";
	private static final String FOOTER_FORWARD_CAPTION = "Weiter";
	private static final String FOOTER_BACKWARD_CAPTION = "Zurück";
	private static final String FOOTER_END_CATEGORY_CAPTION = "Kategorie Abschließen";
	private static final String FOOTER_START_EXPERIMENT_CAPTION = "Experiment starten";
	private static final String FOOTER_FORWARD = "<input name =\""+Constants.KEY_FORWARD+"\" type=\"submit\" value=\""+FOOTER_FORWARD_CAPTION+"\" />";
	private static final String FOOTER_BACKWARD = "<input name =\""+Constants.KEY_BACKWARD+"\" type=\"submit\" value=\""+FOOTER_BACKWARD_CAPTION+"\" />";
	private static final String FOOTER_END_CATEGORY = "<input name =\""+Constants.KEY_FORWARD+"\" type=\"submit\" value=\""+FOOTER_END_CATEGORY_CAPTION+"\" />";
	private static final String FOOTER_START_EXPERIMENT = "<table><tr><td>Probandencode:</td><td><input name=\""+Constants.KEY_SUBJECT+"\" /></td></tr></table>"+HTML_DIVIDER+"<input name =\""+Constants.KEY_FORWARD+"\" type=\"submit\" value=\""+FOOTER_START_EXPERIMENT_CAPTION+"\" />";
	private static final String HTML_END = "</form></body></html>";
	
	private ActionListener actionListener;
	private QuestionTreeNode questionNode;
	private JTextPane textPane;
	
	/**
	 * With the call of the Constructor the data is loaded and everything is
	 * initialized. The first question is showed.
	 * 
	 * @param path
	 *            path of the xml file with the data
	 * @param cqlp
	 *            the categorieQuestionListsPanel where the overview is shown
	 */
	public QuestionViewPane(QuestionTreeNode questionNode) {
		this.questionNode=questionNode;
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
									// What should happen when the buttons are
									// pressed?
									protected void submitData(String data) {
										String action = saveAnswers(data);
										if (action!=null) {
											fireEvent(action);
										}
									}
								};
						}
						return super.create(elem);
					}
				};
			}
		});
		

		URL trueBase = ClassLoader.getSystemResource(".");
		((javax.swing.text.html.HTMLDocument)textPane.getDocument()).setBase(trueBase);
		
		String questionText = HTML_START + questionNode.getValue()+HTML_DIVIDER;
		boolean questionSwitching = false;
		if (questionNode.isQuestion()) {
			questionSwitching = Boolean.parseBoolean(((QuestionTreeNode)questionNode.getParent()).getAttributeValue("questionswitching"));
		}
		if (hasActivePreviousNode(questionNode) && questionSwitching) {
			questionText+=FOOTER_BACKWARD;
		}
		if (questionNode.isExperiment()) {
			questionText+=FOOTER_START_EXPERIMENT;
		} else {
			if (hasActiveNextNode(questionNode)) {
				questionText+=FOOTER_FORWARD;
			} else{
				questionText+=FOOTER_END_CATEGORY;
			}
		}
		questionText+=HTML_END;
		textPane.setText(questionText);
		textPane.setCaretPosition(0);
	}
	
	/**
	 * saves the answers
	 * returns an integer to know which button was pressed
	 * @param data data which should be stored an consist the info about which button was pressed
	 * @return FORWARD if forward button, BACKWARD if backward button, -1 if neither
	 */
	private String saveAnswers(String data) {
		StringTokenizer st = new StringTokenizer(data, "&");
		String result = null;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			String key = null;
			String value = null;
			try {
				key = URLDecoder.decode(
						token.substring(0, token.indexOf("=")), "ISO-8859-1");
				value = URLDecoder
						.decode(token.substring(token.indexOf("=") + 1,
								token.length()), "ISO-8859-1");
			} catch (UnsupportedEncodingException ex) {
				ex.printStackTrace();
			}			
			if (key.equals(Constants.KEY_FORWARD) || key.equals(Constants.KEY_BACKWARD)) {
				result=key;
			} else {
				questionNode.setAnswer(key, value);
			}
		}
		return result;
	}
	private boolean hasActiveNextNode(QuestionTreeNode node) {
		boolean inactive;
		if (node.isExperiment() || node.isCategory()) {
			inactive = Boolean.parseBoolean(node.getAttributeValue(Constants.KEY_INACTIVE));
			if (inactive || node.getChildCount()==0) {
				return false;
			} else {
				node = (QuestionTreeNode)node.getFirstChild();
				inactive = Boolean.parseBoolean(node.getAttributeValue(Constants.KEY_INACTIVE));
				if (!inactive) {
					return true;
				}
				return hasActiveNextNode(node);
			}
		} else if (node.isQuestion()) {
			node = (QuestionTreeNode)node.getNextSibling();
			if (node==null) {
				return false;
			}
			inactive = Boolean.parseBoolean(node.getAttributeValue(Constants.KEY_INACTIVE));
			if (!inactive) {
				return true;
			}
			return hasActiveNextNode(node);
		} else {
			return false;
		}
	}
	private boolean hasActivePreviousNode(QuestionTreeNode node) {
		if (node.isQuestion()) {
			node = (QuestionTreeNode)node.getPreviousSibling();
			if (node==null) {
				return false;
			}
			boolean inactive = Boolean.parseBoolean(node.getAttributeValue(Constants.KEY_INACTIVE));
			if (!inactive) {
				return true;
			}
			return hasActivePreviousNode(node);
		} else {
			return false;
		}
	}
	public void setActionListener(ActionListener l) {
		actionListener=l;
	}
	public ActionListener getActionListener() {
		return actionListener;
	}
	public void fireEvent(String action) {
		if (actionListener != null) {
			ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, action);
			actionListener.actionPerformed(event);
		}
	}
}
