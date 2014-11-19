package de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import javax.swing.tree.TreePath;

import de.uni_passau.fim.infosun.prophet.experimentGUI.Constants;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.QuestionViewPane.*;

/**
 * Handles HTML operations for the <code>QTree</code>.
 */
public final class QTreeHTMLHandler extends QTreeFormatHandler {

    /**
     * Utility class.
     */
    private QTreeHTMLHandler() {
    }

    public static int highestID = 0;
    public static Set<String> returnedIDs = new HashSet<>();

    /**
     * Checks the HTML contents of all nodes in the tree under <code>root</code> for elements with duplicate names.
     * The returned <code>Map</code> contains mappings from the names that occur multiple times to the
     * <code>TreePath</code>s to where they occur.
     * Each <code>TreePath</code> has the following structure:
     * Let n be be length of the TreePath. The indices of the TreePath are seperated in two parts:
     * TreePath[0] to TreePath[n-1] | TreePath[n]
     * <ul>
     * <li>Index 0 to n-1 is the TreePath from the root node to the QTreeNode that contains the duplicate name</li>
     * <li>Index n is an <code>Integer</code> index of the &lt;input&gt; within the QTreeNode that contains the
     * duplicate name. All elements in the HTML content of a QTreeNode that contain a <code>name</code> attribute are
     * indexed starting
     * by zero. The Integer at TreePath[n] gives the index within this "named" elements.
     * </li>
     * </ul>
     *
     * @param root
     *         the root of the tree to be checked
     *
     * @return a <code>Map</code> of the described format
     */
    public static Map<String, List<TreePath>> checkNames(QTreeNode root) {
        Map<String, List<TreePath>> names = new HashMap<>();
        Map<String, List<TreePath>> duplicates = new HashMap<>();
        List<TreePath> containingNodes;
        String nameAttr = "name";
        String name;
        Document doc;
        Element body;
        int index;

        for (QTreeNode node : root.preOrder()) {
            doc = Jsoup.parseBodyFragment(node.getHtml());
            body = doc.body();
            index = 0;

            for (Element element : body.getElementsByAttribute(nameAttr)) {
                name = element.attr(nameAttr);

                containingNodes = names.getOrDefault(name, new LinkedList<>());
                containingNodes.add(new TreePath(QTreeModel.buildPath(node, true)).pathByAddingChild(index));
                names.putIfAbsent(name, containingNodes);
                index++;
            }
        }

        names.forEach((key, value) -> {
            if (value.size() > 1) {
                duplicates.put(key, value);
            }
        });

        return duplicates;
    }

    /**
     * Returns a list of <code>String</code> IDs (numbers) that have not yet been used as an ID in any HTML element
     * of a node in the tree under <code>root</code> or returned by this <code>QTreeHTMLHandler</code>.
     *
     * @param root
     *         the root of the tree in which the ids should be unique
     * @param number
     *         the number of ids to be returned
     *
     * @return the ids as <code>String</code>s
     */
    public static List<String> createIDs(QTreeNode root, int number) {
        Set<String> existingIDs = new HashSet<>();
        List<String> newIDs = new LinkedList<>();
        String idAttr = "id";
        String stringID;
        Document doc;

        for (QTreeNode node : root.preOrder()) {
            doc = Jsoup.parseBodyFragment(node.getHtml());
            doc.body().getElementsByAttribute(idAttr).forEach(element -> existingIDs.add(element.attr(idAttr)));
        }
        existingIDs.addAll(returnedIDs);

        for (int i = 0; i < number; i++) {

            do {
                highestID++;
                stringID = String.valueOf(highestID);
            } while (existingIDs.contains(stringID));
            newIDs.add(stringID);
        }
        returnedIDs.addAll(newIDs);

        return newIDs;
    }

    public static void saveExperimentHTML(QTreeNode root, File saveFile) throws IOException {
        Objects.requireNonNull(root);
        Objects.requireNonNull(saveFile);

        if (root.getType() != QTreeNode.Type.EXPERIMENT) {
            throw new IllegalArgumentException("root must be of type EXPERIMENT");
        }

        String divider = "<hr>";

        checkParent(saveFile);

        String experimentName = root.getName();
        String experimentCode = root.getAttribute(Constants.KEY_EXPERIMENT_CODE).getValue();

        Document doc = Document.createShell("");
        doc.head().appendElement("meta").attr("content", "text/html; charset=utf-8");
        doc.title(String.format("%s - ExpCode %s", experimentName, experimentCode));

        Element body = doc.body();
        for (QTreeNode node : root.preOrder()) {
            body.appendElement("h" + (node.getType().ordinal() + 1)).text(node.getName());
            body.append("<br>").append(node.getHtml());

            if (node.getType() == QTreeNode.Type.EXPERIMENT) {
                String subjCodeDesc = UIElementNames.getLocalized("FOOTER_SUBJECT_CODE_CAPTION"); // TODO make this customizable (using an Attribute of the root node)

                body.appendChild(input("hidden", Constants.KEY_EXPERIMENT_CODE, experimentCode));
                body.appendChild(table(null, new Object[] {subjCodeDesc, input(null, Constants.KEY_SUBJECT, null)}));
            }

            body.append(divider);
        }

        System.out.println(doc.outerHtml()); // TODO debug

        try (FileWriter out = new FileWriter(saveFile)) {
            out.write(doc.outerHtml());
        }
    }

    /**
     * Creates a 'table' <code>Element</code> (using {@link Object#toString()} from the given data.
     * Any <code>null</code> values in <code>header</code> or <code>rows</code> (and its sub-arrays) will be ignored.
     * Any cells of the table that should be interpreted as HTML must be given as <code>Node</code> instances.
     *
     * @param header the optional header for the table
     * @param rows the rows for the table
     * @return the 'table' <code>Element</code>
     */
    public static Element table(Object[] header, Object[]... rows) {
        Element table = new Element(Tag.valueOf("table"), "");

        if (header != null) {
            Element headerRowEl = table.appendElement("tr");
            Element headerColEl;

            for (Object headerData : header) {
                if (headerData == null) {
                    continue;
                }

                headerColEl = headerRowEl.appendElement("th");

                if (headerData instanceof Node) {
                    headerColEl.appendChild((Node) headerData);
                } else {
                    headerColEl.text(headerData.toString());
                }
            }
        }

        if (rows != null) {
            Element rowEl;
            Element colEl;

            for (Object[] row : rows) {
                if (row == null) {
                    continue;
                }

                rowEl = table.appendElement("tr");
                for (Object rowData : row) {
                    if (rowData == null) {
                        continue;
                    }

                    colEl = rowEl.appendElement("td");

                    if (rowData instanceof Node) {
                        colEl.appendChild((Node) rowData);
                    } else {
                        colEl.text(rowData.toString());
                    }
                }
            }
        }

        return table;
    }

    /**
     * Creates an 'input' <code>Element</code> with the given (optional) attributes.
     *
     * @param type the value for the attribute 'type'
     * @param name the value for the attribute 'name'
     * @param value the value for the attribute 'value'
     * @return an 'input' <code>Element</code>
     */
    public static Element input(String type, String name, String value) {
        Element element = new Element(Tag.valueOf("input"), "");

        if (type != null) {
            element.attr("type", type);
        }

        if (name != null) {
            element.attr("name", name);
        }

        if (value != null) {
            element.attr("value", value);
        }

        return element;
    }

    /**
     * Saves all the forms and text contained in the given tree to a html file.
     *
     * @param file
     *         the file to write the html to
     * @param rootNode
     *         the root node of the tree
     *
     * @throws java.lang.IllegalArgumentException
     *         if <code>rootNode</code> was not of type <code>Type.EXPERIMENT</code>
     */
    public static void saveAsHTMLFile(File file, QTreeNode rootNode) {
        Objects.requireNonNull(file, "file must not be null!");
        Objects.requireNonNull(rootNode, "rootNode must not be null!");

        if (rootNode.getType() == QTreeNode.Type.EXPERIMENT) {
            String experimentName = rootNode.getName();
            String experimentCode = rootNode.getAttribute(Constants.KEY_EXPERIMENT_CODE).getValue();
            String htmlContent = createHTMLContent(new StringBuffer(), rootNode).toString();
            String newline = System.getProperty("line.separator");

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                String path = file.getAbsolutePath();

                System.out.println("Export HTML to: " + path); // TODO debug

                bw.write("<html>" + newline);
                bw.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">");
                bw.write("<head>" + newline);
                bw.write("<title>" + newline);
                bw.write(String.format("%s - ExpCode %s%n", experimentName, experimentCode));
                bw.write("</title>" + newline);
                bw.write("</head>" + newline);
                bw.write("<body>" + newline);
                bw.write(htmlContent + newline);
                bw.write("</body>" + newline);
                bw.write("</html>" + newline);
            } catch (IOException e) {
                System.err.println("Error while writing questions to HTML file: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("rootNode must be of type Type.EXPERIMENT");
        }
    }

    private static StringBuffer createHTMLContent(StringBuffer htmlContent, QTreeNode node) {
        String nodeName = node.getName();
        String bottomLine = HTML_DIVIDER;
        String headline;

        switch (node.getType()) {
            case EXPERIMENT: {
                String expCode = node.getAttribute(Constants.KEY_EXPERIMENT_CODE).getValue();
                String footerExpCode = String.format(FOOTER_EXPERIMENT_CODE, expCode);

                bottomLine = String.format("%1$s%2$s%3$s%1$s", HTML_DIVIDER, footerExpCode, FOOTER_SUBJECT_CODE);
                headline = String.format("<h1>%s</h1>", nodeName);
            }
            break;
            case CATEGORY:
                headline = String.format("<h2>%s</h2>", nodeName);
                break;
            case QUESTION:
                headline = String.format("<h3>%s</h3>", nodeName);
                break;
            default:
                headline = String.format("<h1>%s</h1>", nodeName);
                System.err.println("Non-exhausting switch for 'Type' while exporting HTML!");
        }

        htmlContent.append(String.format("%n%n%s%n<br><br>%n%s%s", headline, node.getHtml(), bottomLine));

        for (QTreeNode child : node.getChildren()) {
            createHTMLContent(htmlContent, child);
        }

        return htmlContent;
    }
}
