package de.uni_passau.fim.infosun.prophet.experimentGUI.util;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.FormView;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import de.uni_passau.fim.infosun.prophet.experimentGUI.Constants;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type.*;
import static javax.swing.text.html.HTML.Attribute.NAME;
import static javax.swing.text.html.HTML.Attribute.TYPE;
import static javax.swing.text.html.HTML.Tag.*;

/**
 * This class shows the html files (questions) creates the navigation and
 * navigates everything...
 *
 * @author Markus Köppen, Andreas Hasselberg
 */
public class QuestionViewPane extends JScrollPane {

    // constants for the html navigation
    public static final String HTML_START = "<html><body><form>";
    public static final String HTML_DIVIDER = "<br /><br /><hr /><br /><br />";
    public static final String HTML_TYPE_SUBMIT = "submit";

    public static final String FOOTER_FORWARD = String.format("<input name =\"%s\" type=\"%s\" value=\"%s\" />",
            Constants.KEY_FORWARD, HTML_TYPE_SUBMIT, UIElementNames.FOOTER_FORWARD_CAPTION);

    public static final String FOOTER_BACKWARD = String.format("<input name =\"%s\" type=\"%s\" value=\"%s\" />",
            Constants.KEY_BACKWARD, HTML_TYPE_SUBMIT, UIElementNames.FOOTER_BACKWARD_CAPTION);

    public static final String FOOTER_END_CATEGORY = String.format("<input name =\"%s\" type=\"%s\" value=\"%s\" />",
            Constants.KEY_FORWARD, HTML_TYPE_SUBMIT, UIElementNames.FOOTER_END_CATEGORY_CAPTION);

    public static final String FOOTER_EXPERIMENT_CODE =
            String.format("<table><tr><td>%s</td><td><input name=\"%s\" /></td></tr></table>",
                    UIElementNames.FOOTER_SUBJECT_CODE_CAPTION, Constants.KEY_SUBJECT);

    public static final String FOOTER_START_EXPERIMENT =
            String.format("%s%s<input name =\"%s\" type=\"%s\" value=\"%s\" />", FOOTER_EXPERIMENT_CODE, HTML_DIVIDER,
                    Constants.KEY_FORWARD, HTML_TYPE_SUBMIT, UIElementNames.FOOTER_START_EXPERIMENT_CAPTION);

    public static final String HTML_END = "</form></body></html>";

    private List<ActionListener> actionListeners;
    private QTreeNode questionNode;
    private JTextPane textPane;

    private FormView submitButton;
    private boolean doNotFire = false;

    /**
     * A <code>HyperlinkListener</code> that will try and open links in the systems standard browser.
     */
    private final HyperlinkListener hyperlinkListener = event -> {

        if (!event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED)) {
            return;
        }

        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();

            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(event.getURL().toURI());
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(textPane, UIElementNames.MESSAGE_COULD_NOT_START_BROWSER);
                } catch (URISyntaxException e) {
                    JOptionPane.showMessageDialog(textPane, UIElementNames.MESSAGE_INVALID_URL);
                }
            } else {
                JOptionPane.showMessageDialog(textPane, UIElementNames.MESSAGE_COULD_NOT_OPEN_STANDARD_BROWSER);
            }
        } else {
            JOptionPane.showMessageDialog(textPane, UIElementNames.MESSAGE_COULD_NOT_OPEN_STANDARD_BROWSER);
        }
    };

    /**
     * A custom <code>HTMLFactory</code> that creates <code>View</code>s for <code>INPUT</code>, <code>TEXTAREA</code>,
     * and <code>SELECT</code> elements that intercept the form data when it is submitted.
     */
    private class CustomFactory extends HTMLEditorKit.HTMLFactory {

        @Override
        public View create(Element elem) {
            AttributeSet eAttribs = elem.getAttributes();
            Object elementName = eAttribs.getAttribute(StyleConstants.NameAttribute);

            if (INPUT.equals(elementName) || TEXTAREA.equals(elementName) || SELECT.equals(elementName)) {

                FormView formView = new FormView(elem) {

                    @Override
                    protected void submitData(String data) {
                        String action = saveAnswers(data);
                        if (action != null) {
                            fireEvent(action);
                        }
                    }
                };

                // if we find a 'nextQuestion' button we save it for use in the clickSubmit and saveAnswers method
                boolean isForwardButton = Constants.KEY_FORWARD.equals(eAttribs.getAttribute(NAME));
                boolean isTypeSubmit = HTML_TYPE_SUBMIT.equals(eAttribs.getAttribute(TYPE));

                if (INPUT.equals(elementName) && isForwardButton && isTypeSubmit) {
                    submitButton = formView;
                }
                return formView;
            } else {
                return super.create(elem);
            }
        }
    }

    /**
     * Constructs a new <code>QuestionViewPane</code> displaying the HTML content of the given <code>QTreeNode</code>.
     * The appropriate buttons for navigating between the parts of the experiment will be added.
     *
     * @param questionNode the <code>QTreeNode</code> whose HTML is to be displayed
     */
    public QuestionViewPane(QTreeNode questionNode) {
        this.actionListeners = new LinkedList<>();
        this.questionNode = questionNode;

        this.textPane = new JTextPane();
        this.setViewportView(textPane);

        textPane.setEditable(false);
        textPane.setEditorKit(new HTMLEditorKit() {

            @Override
            public ViewFactory getViewFactory() {
                return new CustomFactory();
            }
        });
        textPane.addHyperlinkListener(hyperlinkListener);

        URL trueBase = ClassLoader.getSystemResource(".");
        ((HTMLDocument) textPane.getDocument()).setBase(trueBase);

        textPane.setText(getHTMLString(questionNode));
        textPane.setCaretPosition(0);
    }

    /**
     * Assembles the HTML content that will be displayed by the <code>textPane</code>.
     *
     * @param questionNode the <code>QTreeNode</code> whose HTML content will be integrated into the returned HTML
     * @return the HTML content for the <code>textPane</code>
     */
    private String getHTMLString(QTreeNode questionNode) {
        boolean questionSwitching = false;
        StringBuilder sBuilder = new StringBuilder();

        sBuilder.append(HTML_START).append(questionNode.getHtml()).append(HTML_DIVIDER);

        if (questionNode.getType() == QUESTION) {
            Attribute parentIsQSwitching = questionNode.getParent().getAttribute(Constants.KEY_QUESTIONSWITCHING);
            questionSwitching = Boolean.parseBoolean(parentIsQSwitching.getValue());
        }

        if (hasActivePreviousNode(questionNode) && questionSwitching) {
            sBuilder.append(FOOTER_BACKWARD);
        }

        if (questionNode.getType() == EXPERIMENT) {
            sBuilder.append(FOOTER_START_EXPERIMENT);
        } else {
            if (hasActiveNextNode(questionNode)) {
                sBuilder.append(FOOTER_FORWARD);
            } else {
                sBuilder.append(FOOTER_END_CATEGORY);
            }
        }

        sBuilder.append(HTML_END);

        return sBuilder.toString();
    }

    /**
     * Saves the answers contained in the given <code>String</code> to the <code>QTreeNode</code> this
     * <code>QuestionViewPane</code> displays. Returns the name of the button that was clicked or <code>null</code>
     * if the data submission was not caused by a button click.
     *
     * @param data the submitted form data
     * @return {@link Constants#KEY_FORWARD}, {@link Constants#KEY_BACKWARD} or <code>null</code>
     */
    private String saveAnswers(String data) {
        StringTokenizer st = new StringTokenizer(data, "&");
        String result = null;

        System.out.println(data);

        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            String key = null;
            String value = null;

            try {
                key = URLDecoder.decode(token.substring(0, token.indexOf("=")), "ISO-8859-1");
                value = URLDecoder.decode(token.substring(token.indexOf("=") + 1, token.length()), "ISO-8859-1");
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }

            if (Constants.KEY_FORWARD.equals(key) || Constants.KEY_BACKWARD.equals(key)) {
                result = key;
            } else {
                questionNode.setAnswer(key, value);
            }
        }

        return result;
    }

    /**
     * Returns whether the given <code>QTreeNode</code> has an active (meaning a node that may be visited by the
     * <code>ExperimentViewer</code>) that comes after <code>node</code>.
     *
     * @param node the <code>QTreeNode</code> to be searched from
     * @return true iff the given <code>QTreeNode</code> has an active next node
     */
    private boolean hasActiveNextNode(QTreeNode node) {
        if (node.getType() == EXPERIMENT || node.getType() == CATEGORY) {
            if (ExperimentViewer.denyEnterNode(node) || node.getChildCount() == 0) {
                return false;
            } else {
                node = node.getChild(0);
                return !ExperimentViewer.denyEnterNode(node) || hasActiveNextNode(node);
            }
        } else if (node.getType() == QUESTION) {
            node = node.getNextSibling();
            return node != null && (!ExperimentViewer.denyEnterNode(node) || hasActiveNextNode(node));
        } else {
            return false;
        }
    }

    /**
     * Returns whether the given <code>QTreeNode</code> has an active (meaning a node that may be visited by the
     * <code>ExperimentViewer</code>) that comes before <code>node</code>.
     *
     * @param node the <code>QTreeNode</code> to be searched from
     * @return true iff the given <code>QTreeNode</code> has an active previous node
     */
    private boolean hasActivePreviousNode(QTreeNode node) {
        if (node.getType() == QUESTION) {
            node = node.getPreviousSibling();
            return node != null && (!ExperimentViewer.denyEnterNode(node) || hasActivePreviousNode(node));
        } else {
            return false;
        }
    }

    /**
     * Adds an <code>ActionListener</code> to the <code>QuestionViewPane</code>. It will be notified next/previous
     * buttons are clicked. The {@link java.awt.event.ActionEvent#getActionCommand()} method will return
     * either {@link Constants#KEY_FORWARD} or {@link Constants#KEY_BACKWARD} thereby indicating which button was
     * clicked.
     *
     * @param listener the <code>ActionListener</code> to be added
     */
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    /**
     * Removes the given <code>ActionListener</code> from this <code>QuestionViewPane</code>s listeners.
     *
     * @param listener the <code>ActionListener</code> to be removed
     * @return true iff the <code>ActionListener</code> was removed
     */
    public boolean removeActionListener(ActionListener listener) {
        return actionListeners.remove(listener);
    }

    /**
     * Fires an <code>ActionEvent</code> of type <code>ACTION_PERFORMED</code> for the
     * <code>actionListener</code> if there is one.
     *
     * @param action the action <code>String</code> to be passed to the <code>ActionListener</code>
     */
    private void fireEvent(String action) {
        ActionEvent actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, action);

        if (!doNotFire) {
            actionListeners.forEach(l -> l.actionPerformed(actionEvent));
        }
    }

    /**
     * Clicks the submit button of this <code>QuestionViewPane</code> if there is one.
     *
     * @return true iff there was a button to be clicked
     */
    public boolean clickSubmit() {
        if (submitButton != null && submitButton.getComponent() instanceof JButton) {
            ((JButton) submitButton.getComponent()).doClick();
            return true;
        }
        return false;
    }

    /**
     * Saves the answers contained in this <code>QuestionViewPane</code> to the <code>QTreeNode</code> it displays.
     *
     * @return true iff the answers were saved
     */
    public boolean saveCurrentAnswersToNode() {
        if (submitButton != null && submitButton.getComponent() instanceof JButton) {
            doNotFire = true;
            ((JButton) submitButton.getComponent()).doClick();
            doNotFire = false;
            return true;
        }
        return false;
    }
}
