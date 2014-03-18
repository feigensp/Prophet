package de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.Pair;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.QuestionViewPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;

public class QuestionTreeHTMLHandler {

    public static final int EXPERIMENT_LEVEL = 0;
    public static final int CATEGORY_LEVEL = 1;
    public static final int QUESTION_LEVEL = 2;

    public static final String HEAER_EXPCODE = "expCode";
    public static final String HEADER_PROBCODE = "probCode";

    public static List<String> headerFromQuestionTree(QuestionTreeNode root) {
        List<String> header = new LinkedList<>();
        HTML.Tag[] tags = {HTML.Tag.INPUT, HTML.Tag.SELECT, HTML.Tag.TEXTAREA};
        QuestionTreeNode currentNode = root;

        header.add(HEAER_EXPCODE);
        header.add(HEADER_PROBCODE);

        while (currentNode != null) {

            header.add(currentNode.getName() + ":time");

            String htmlContent = currentNode.getValue();
            HTMLEditorKit editorKit = new HTMLEditorKit();
            StringReader reader = new StringReader(htmlContent);
            HTMLDocument.Iterator iterator;
            HTMLDocument htmlDocument = (HTMLDocument) editorKit.createDefaultDocument();

            try {
                editorKit.read(reader, htmlDocument, 0);
            } catch (IOException | BadLocationException e) {
                System.err.println("Could not read a nodes value as html. Returning an empty list.");
                return new LinkedList<>();
            }

            for (HTML.Tag tag : tags) {
                iterator = htmlDocument.getIterator(tag);

                while (iterator.isValid()) {
                    AttributeSet attributeSet = iterator.getAttributes();
                    String formName = (String) attributeSet.getAttribute(HTML.Attribute.NAME);

                    if (!header.contains(formName)) { // TODO use set?
                        header.add(formName);
                    }

                    iterator.next();
                }
            }

            currentNode = (QuestionTreeNode) currentNode.getNextNode();
        }

        return header;
    }

    public static ArrayList<Pair<QuestionTreeNode, ArrayList<Pair<String, String>>>> getForms(
            QuestionTreeNode startNode) {
        ArrayList<Pair<QuestionTreeNode, ArrayList<Pair<String, String>>>> ret = new ArrayList<>();
        QuestionTreeNode node = startNode;
        HTML.Tag[] tags = {HTML.Tag.INPUT, HTML.Tag.SELECT, HTML.Tag.TEXTAREA};

        try {
            while (node != null) {
                String content = node.getValue();
                ArrayList<Pair<String, String>> forms = new ArrayList<>();
                HTMLEditorKit editKit = new HTMLEditorKit();
                StringReader reader = new StringReader(content);
                HTMLDocument.Iterator iterator;

                HTMLDocument htmlDoc = (HTMLDocument) editKit.createDefaultDocument();
                editKit.read(reader, htmlDoc, 0);

                for (HTML.Tag tag : tags) {
                    iterator = htmlDoc.getIterator(tag);

                    while (iterator.isValid()) {
                        AttributeSet attributes = iterator.getAttributes();
                        String formName = (String) attributes.getAttribute(HTML.Attribute.NAME);
                        String formValue = (String) attributes.getAttribute(HTML.Attribute.VALUE);

                        forms.add(new Pair<>(formName, formValue));
                        iterator.next();
                    }
                }

                ret.add(new Pair<>(node, forms));
                node = (QuestionTreeNode) node.getNextNode();
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, UIElementNames.QUESTION_TREE_HTML_HANDLER_MESSAGE_ERROR_WHILE_READING);
            return null;
        } catch (BadLocationException e) {
            JOptionPane.showMessageDialog(null, UIElementNames.QUESTION_TREE_HTML_HANDLER_MESSAGE_ERROR_WHILE_READING);
            return null;
        }

        return ret;
    }

    public static void saveAsHTMLFile(File file, QuestionTreeNode node) {
        String nodeName = node.getName();
        String htmlContent = createHTMLContent(new StringBuffer(), node).toString();

        if (node.isExperiment()) {
            FileWriter fw;
            BufferedWriter bw;
            String newline = System.getProperty("line.separator");
            try {
                String path = file.getAbsolutePath();
                System.out.println(path);
                if (!path.endsWith(".htm") && !path.endsWith(".html")) {
                    file = new File(file.getAbsolutePath() + ".html");
                }
                fw = new FileWriter(file);
                bw = new BufferedWriter(fw);
                bw.write("<HTML>" + newline);
                bw.write("<HEAD>" + newline);
                bw.write("<TITLE>" + newline);
                bw.write(nodeName + newline);
                bw.write("</TITLE>" + newline);
                bw.write("</HEAD>" + newline);
                bw.write("<BODY>" + newline);
                bw.write(htmlContent + newline);
                bw.write("</BODY>" + newline);
                bw.write("</HTML>" + newline);
                bw.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private static StringBuffer createHTMLContent(StringBuffer htmlContent, QuestionTreeNode node) {
        String nodeName = node.getName();
        String nodeValue = node.getValue();
        if (node.isExperiment()) {
            htmlContent.append(getHTMLString(nodeName, nodeValue, EXPERIMENT_LEVEL));
        } else if (node.isCategory()) {
            htmlContent.append(getHTMLString(nodeName, nodeValue, CATEGORY_LEVEL));
        } else if (node.isQuestion()) {
            htmlContent.append(getHTMLString(nodeName, nodeValue, QUESTION_LEVEL));
        }
        for (int i = 0; i < node.getChildCount(); i++) {
            createHTMLContent(htmlContent, (QuestionTreeNode) node.getChildAt(i));
        }
        return htmlContent;
    }

    private static String getHTMLString(String name, String content, int level) {
        String headline = "";
        String newline = System.getProperty("line.separator");
        String bottomLine = QuestionViewPane.HTML_DIVIDER;
        if (name != null && content != null) {
            switch (level) {
                case EXPERIMENT_LEVEL:
                    headline = "<h1>" + name + "</h1>";
                    bottomLine = QuestionViewPane.HTML_DIVIDER + QuestionViewPane.FOOTER_EXPERIMENT_CODE + bottomLine;
                    break;
                case CATEGORY_LEVEL:
                    headline = "<h2>" + name + "</h2>";
                    break;
                case QUESTION_LEVEL:
                    headline = "<h3>" + name + "</h3>";
                    break;
            }
            return newline + newline + headline + newline + "<br><br>" + newline + content + bottomLine;
        }
        return "";
    }
}
