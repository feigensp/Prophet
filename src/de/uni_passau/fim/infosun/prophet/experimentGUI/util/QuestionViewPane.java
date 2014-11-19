package de.uni_passau.fim.infosun.prophet.experimentGUI.util;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.*;
import javax.swing.text.html.FormSubmitEvent;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import de.uni_passau.fim.infosun.prophet.experimentGUI.Constants;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.PluginList;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames.getLocalized;
import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type.*;
import static javax.swing.text.html.HTML.Attribute.NAME;
import static javax.swing.text.html.HTML.Attribute.TYPE;
import static javax.swing.text.html.HTML.Tag.*;

/**
 * Displays the rendered HTML content of a <code>QTreeNode</code>, saves the answers the user input to its
 * <code>QTreeNode</code> and adds the proper navigation buttons (next, previous, ...) for the node.
 * <code>ActionListener</code>s for navigation events can be added.
 *
 * @author Georg Seibt
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public class QuestionViewPane extends JScrollPane {

    private static Font f;

    static {
        InputStream fontInput = QuestionViewPane.class.getResourceAsStream("VERDANAB.TTF");

        try {
            f = Font.createFont(Font.TRUETYPE_FONT, fontInput);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(f);
        } catch (FontFormatException | IOException e) {
            f = UIManager.getDefaults().getFont("TextPane.font");
        }
    }

    // constants for the html navigation
    public static final String HTML_START = "<html><body style=\"font-family: " + f.getFamily() + "\"><form>";
    public static final String HTML_DIVIDER = "<br /><br /><hr /><br /><br />";
    public static final String HTML_TYPE_SUBMIT = "submit";

    public static final String FOOTER_FORWARD =
            String.format("<input name =\"%s\" type=\"%s\" value=\"%s\" />", Constants.KEY_FORWARD, HTML_TYPE_SUBMIT,
                    getLocalized("FOOTER_FORWARD_CAPTION"));

    public static final String FOOTER_BACKWARD =
            String.format("<input name =\"%s\" type=\"%s\" value=\"%s\" />", Constants.KEY_BACKWARD, HTML_TYPE_SUBMIT,
                    getLocalized("FOOTER_BACKWARD_CAPTION"));

    public static final String FOOTER_END_CATEGORY =
            String.format("<input name =\"%s\" type=\"%s\" value=\"%s\" />", Constants.KEY_FORWARD, HTML_TYPE_SUBMIT,
                    getLocalized("FOOTER_END_CATEGORY_CAPTION"));

    public static final String FOOTER_EXPERIMENT_CODE =
            "<input type=\"hidden\" name=" + Constants.KEY_EXPERIMENT_CODE + " value=\"%s\">";

    public static final String FOOTER_SUBJECT_CODE =
            String.format("<table><tr><td>%s</td><td><input name=\"%s\" /></td></tr></table>",
                    getLocalized("FOOTER_SUBJECT_CODE_CAPTION"), Constants.KEY_SUBJECT_CODE);

    public static final String FOOTER_START_EXPERIMENT =
            String.format("%s%s<input name =\"%s\" type=\"%s\" value=\"%s\" />", FOOTER_SUBJECT_CODE, HTML_DIVIDER,
                    Constants.KEY_FORWARD, HTML_TYPE_SUBMIT, getLocalized("FOOTER_START_EXPERIMENT_CAPTION"));

    public static final String HTML_END = "</form></body></html>";

    private List<ActionListener> actionListeners;
    private QTreeNode questionNode;
    private JTextPane textPane;

    private ComponentView submitButton;
    private AtomicBoolean doNotFire;

    /**
     * A <code>HyperlinkListener</code> that will react to <code>FormSubmitEvent</code>s by
     * storing the data the user entered in the <code>questionNode</code> and firing the appropriate event (if any).
     */
    private final HyperlinkListener formSubmitListener = event -> {

        if (event instanceof FormSubmitEvent) {
            FormSubmitEvent fse = (FormSubmitEvent) event;

            String action = saveAnswers(fse.getData());
            if (action != null) {
                fireEvent(action);
            }
        }
    };

    /**
     * A <code>HyperlinkListener</code> that will try and open links in the systems standard browser.
     */
    private final HyperlinkListener hyperlinkListener = event -> {

        if (!event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED) || event instanceof FormSubmitEvent) {
            return;
        }

        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();

            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(event.getURL().toURI());
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(textPane, getLocalized("MESSAGE_COULD_NOT_START_BROWSER"));
                } catch (URISyntaxException e) {
                    JOptionPane.showMessageDialog(textPane, getLocalized("MESSAGE_INVALID_URL"));
                }
            } else {
                JOptionPane.showMessageDialog(textPane, getLocalized("MESSAGE_COULD_NOT_OPEN_STANDARD_BROWSER"));
            }
        } else {
            JOptionPane.showMessageDialog(textPane, getLocalized("MESSAGE_COULD_NOT_OPEN_STANDARD_BROWSER"));
        }
    };

    /**
     * A custom <code>HTMLEditorKit.HTMLFactory</code> that stores the view produced for the 'nextQuestion'
     * button in the variable <code>submitButton</code>. The <code>JButton</code> contained in the
     * <code>ComponentView</code> is later used to submit the HTML form from code.
     */
    private class CustomFactory extends HTMLEditorKit.HTMLFactory {

        @Override
        public View create(javax.swing.text.Element elem) {
            ComponentView view;
            AttributeSet eAttribs = elem.getAttributes();
            Object elementName = eAttribs.getAttribute(StyleConstants.NameAttribute);

            if (INPUT.equals(elementName) || TEXTAREA.equals(elementName) || SELECT.equals(elementName)) {
                view = (ComponentView) super.create(elem);

                // if we find a 'nextQuestion' button we save it for use in the clickSubmit and saveAnswers method
                boolean isForwardButton = Constants.KEY_FORWARD.equals(eAttribs.getAttribute(NAME));
                boolean isTypeSubmit = HTML_TYPE_SUBMIT.equals(eAttribs.getAttribute(TYPE));

                if (INPUT.equals(elementName) && isForwardButton && isTypeSubmit) {
                    submitButton = view;
                }
                return view;
            } else {
                return super.create(elem);
            }
        }
    }

    /**
     * Constructs a new <code>QuestionViewPane</code> displaying the HTML content of the given <code>QTreeNode</code>.
     * The appropriate buttons for navigating between the parts of the experiment will be added.
     *
     * @param questionNode
     *         the <code>QTreeNode</code> whose HTML is to be displayed
     */
    public QuestionViewPane(QTreeNode questionNode) {
        this.actionListeners = new LinkedList<>();
        this.questionNode = questionNode;
        this.doNotFire = new AtomicBoolean(false);

        this.textPane = new JTextPane();
        this.setViewportView(textPane);

        textPane.setContentType("text/html");
        textPane.setEditable(false);
        textPane.setEditorKit(new HTMLEditorKit() {

            @Override
            public ViewFactory getViewFactory() {
                return new CustomFactory();
            }
        });
        textPane.addHyperlinkListener(hyperlinkListener);
        textPane.addHyperlinkListener(formSubmitListener);
        ((HTMLEditorKit) textPane.getEditorKit()).setAutoFormSubmission(false);

        URL trueBase = ClassLoader.getSystemResource(".");
        ((HTMLDocument) textPane.getDocument()).setBase(trueBase);

        textPane.setText(getHTMLString(questionNode));
        textPane.setCaretPosition(0);

        Timer bTimer = new Timer(0, null);
        bTimer.addActionListener(new ActionListener() {

            private int times = 0;
            private String oldText;

            @Override
            public void actionPerformed(ActionEvent e) {
                JButton button = ((JButton) submitButton.getComponent());
                int maxTimes = 5;

                if (times == 0) {
                    oldText = button.getText();
                    button.setEnabled(false);
                }

                if (times < maxTimes) {
                    button.setText(String.format("%s (%d)", oldText, maxTimes - times));
                    times++;
                } else {
                    button.setText(oldText);
                    button.setEnabled(true);
                    bTimer.stop();
                }
            }
        });

        bTimer.setDelay(1000);
        bTimer.setRepeats(true);
        bTimer.start();
    }

    /**
     * Assembles the HTML content that will be displayed by the <code>textPane</code>.
     *
     * @param questionNode
     *         the <code>QTreeNode</code> whose HTML content will be integrated into the returned HTML
     *
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
            sBuilder.append(String.format(FOOTER_EXPERIMENT_CODE,
                    questionNode.getAttribute(Constants.KEY_EXPERIMENT_CODE).getValue()));
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
     * @param data
     *         the submitted form data
     *
     * @return {@link Constants#KEY_FORWARD}, {@link Constants#KEY_BACKWARD} or <code>null</code>
     */
    private String saveAnswers(String data) {
        Map<String, ArrayList<String>> answers = new HashMap<>();
        StringTokenizer st = new StringTokenizer(data, "&");
        String result = null;

        Document doc = Jsoup.parse(textPane.getText());
        Element form = doc.body().getElementsByTag("form").first();
        Set<String> tags = new HashSet<>(Arrays.asList("input", "textarea", "select"));
        String htmlName = "name";

        for (Element namedElement : form.getElementsByAttribute(htmlName)) {
            String name = namedElement.attr(htmlName);

            boolean isNotButton = !Constants.KEY_BACKWARD.equals(name) && !Constants.KEY_FORWARD.equals(name);

            if (tags.contains(namedElement.tagName()) && isNotButton) {
                answers.put(name, new ArrayList<>());
            }
        }

        System.out.println(data); // TODO debug

        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            String key;
            String value;

            try {
                key = URLDecoder.decode(token.substring(0, token.indexOf("=")), "ISO-8859-1");
                value = URLDecoder.decode(token.substring(token.indexOf("=") + 1, token.length()), "ISO-8859-1").trim();
            } catch (UnsupportedEncodingException e) {
                System.err.println("Invalid encoding in HTML form. " + e.getMessage());
                continue;
            }

            if (Constants.KEY_FORWARD.equals(key) || Constants.KEY_BACKWARD.equals(key)) {
                result = key;
            } else {
                ArrayList<String> answer = answers.get(key);

                if (answer != null && !value.isEmpty()) {
                    answer.add(value);
                }
            }
        }

        for (Map.Entry<String, ArrayList<String>> entry : answers.entrySet()) {
            ArrayList<String> value = entry.getValue();
            questionNode.setAnswers(entry.getKey(), value.toArray(new String[value.size()]));
        }

        return result;
    }

    /**
     * Returns whether the given <code>QTreeNode</code> has an active (meaning a node that may be visited by the
     * <code>ExperimentViewer</code>) that comes after <code>node</code>.
     *
     * @param node
     *         the <code>QTreeNode</code> to be searched from
     *
     * @return true iff the given <code>QTreeNode</code> has an active next node
     */
    private boolean hasActiveNextNode(QTreeNode node) {
        if (node.getType() == EXPERIMENT || node.getType() == CATEGORY) {
            if (PluginList.denyEnterNode(node) || node.getChildCount() == 0) {
                return false;
            } else {
                node = node.getChild(0);
                return !PluginList.denyEnterNode(node) || hasActiveNextNode(node);
            }
        } else if (node.getType() == QUESTION) {
            node = node.getNextSibling();
            return node != null && (!PluginList.denyEnterNode(node) || hasActiveNextNode(node));
        } else {
            return false;
        }
    }

    /**
     * Returns whether the given <code>QTreeNode</code> has an active (meaning a node that may be visited by the
     * <code>ExperimentViewer</code>) that comes before <code>node</code>.
     *
     * @param node
     *         the <code>QTreeNode</code> to be searched from
     *
     * @return true iff the given <code>QTreeNode</code> has an active previous node
     */
    private boolean hasActivePreviousNode(QTreeNode node) {
        if (node.getType() == QUESTION) {
            node = node.getPreviousSibling();
            return node != null && (!PluginList.denyEnterNode(node) || hasActivePreviousNode(node));
        } else {
            return false;
        }
    }

    /**
     * Adds an <code>ActionListener</code> to the <code>QuestionViewPane</code>. It will be notified when next/previous
     * buttons are clicked. The {@link java.awt.event.ActionEvent#getActionCommand()} method will return
     * either {@link Constants#KEY_FORWARD} or {@link Constants#KEY_BACKWARD} thereby indicating which button was
     * clicked.
     *
     * @param listener
     *         the <code>ActionListener</code> to be added
     */
    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    /**
     * Removes the given <code>ActionListener</code> from this <code>QuestionViewPane</code>s listeners.
     *
     * @param listener
     *         the <code>ActionListener</code> to be removed
     *
     * @return true iff the <code>ActionListener</code> was removed
     */
    public boolean removeActionListener(ActionListener listener) {
        return actionListeners.remove(listener);
    }

    /**
     * Fires an <code>ActionEvent</code> of type <code>ACTION_PERFORMED</code> for the
     * <code>actionListener</code> if there is one.
     *
     * @param action
     *         the action <code>String</code> to be passed to the <code>ActionListener</code>
     */
    private void fireEvent(String action) {
        ActionEvent actionEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, action);

        if (!doNotFire.compareAndSet(true, false)) {
            actionListeners.forEach(l -> l.actionPerformed(actionEvent));
        }
    }

    /**
     * Clicks the submit button of this <code>QuestionViewPane</code> if there is one.
     * An event will be fired because of this click.
     *
     * @return true iff there was a button to be clicked
     */
    public boolean clickSubmit() {
        return clickSubmit(true);
    }

    /**
     * Clicks the submit button of this <code>QuestionViewPane</code> if there is one.
     *
     * @param fireEvent
     *         whether to fire an event because of this click
     *
     * @return true iff there was a button to be clicked
     */
    public boolean clickSubmit(boolean fireEvent) {
        if (submitButton != null && submitButton.getComponent() instanceof JButton) {
            doNotFire.set(!fireEvent);
            ((JButton) submitButton.getComponent()).doClick();
            return true;
        }

        return false;
    }
}
