package de.uni_passau.fim.infosun.prophet.util.qTree.handlers;

import java.io.*;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.swing.tree.TreePath;

import de.uni_passau.fim.infosun.prophet.Constants;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeModel;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Tag;

/**
 * Handles HTML operations for the <code>QTree</code>.
 */
public final class QTreeHTMLHandler extends QTreeFormatHandler {

    /**
     * Utility class.
     */
    private QTreeHTMLHandler() {
    }

    private static int highestID = 0;
    private static final Set<String> returnedIDs = new HashSet<>();

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

    /**
     * Saves the given <code>QTreeNode</code> (and thereby the whole tree under it) as a single HTML page to the given
     * file. This will overwrite <code>saveFile</code>. Neither <code>root</code> nor <code>saveFile</code> may
     * be <code>null</code>. <code>root</code> must be of type {@link QTreeNode.Type#EXPERIMENT}.
     *
     * @param root
     *         the root of the tree to save
     * @param saveFile
     *         the file to save the html to
     *
     * @throws IOException
     *         if the file can not be written to
     * @throws IllegalArgumentException
     *         if root is not of type {@link QTreeNode.Type#EXPERIMENT}
     */
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
            body.appendElement("h" + (Math.max(node.getType().ordinal() + 1, 6))).text(node.getName());
            body.append("<br>").append(node.getHtml());

            if (node.getType() == QTreeNode.Type.EXPERIMENT) {
                String subjCodeDesc;

                if (!node.containsAttribute(Constants.KEY_SUBJECT_CODE_CAP)) {
                    subjCodeDesc = UIElementNames.getLocalized("FOOTER_SUBJECT_CODE_CAPTION");
                } else {
                    subjCodeDesc = node.getAttribute(Constants.KEY_SUBJECT_CODE_CAP).getValue();
                }

                body.appendChild(input("hidden", Constants.KEY_EXPERIMENT_CODE, experimentCode));
                body.appendChild(table(null, new Object[] {subjCodeDesc, input(null, Constants.KEY_SUBJECT_CODE, null)}));
            }

            body.append(divider);
        }

        CharsetEncoder utf8encoder = StandardCharsets.UTF_8.newEncoder();

        try (Writer writer = new OutputStreamWriter(new FileOutputStream(saveFile), utf8encoder)) {
            writer.write(doc.outerHtml());
        }
    }

    /**
     * Creates a 'table' <code>Element</code> (using {@link Object#toString()} from the given data.
     * Any <code>null</code> values in <code>header</code> or <code>rows</code> (and its sub-arrays) will be ignored.
     * Any cells of the table that should be interpreted as HTML must be given as <code>Node</code> instances. Otherwise
     * HTML in the <code>String</code> returned by {@link Object#toString()} will be escaped.
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
}
