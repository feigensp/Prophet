package de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.tree.TreePath;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Handles HTML operations for the <code>QTree</code>.
 */
public class QTreeHTMLHandler {

    /**
     * Checks the HTML contents of all nodes in the tree under <code>root</code> for elements with duplicate names.
     * The returned <code>Map</code> contains mappings from the names that occur multiple times to the
     * <code>TreePath</code>s to where they occur. The index (amongst all named elements in the nodes
     * HTML content) of the element with the duplicate name will be appended as the last component
     * (an <code>Integer</code>) of the <code>TreePath</code> making the <code>QTreeNode</code> whose HTML content
     * contains the element the second to last component of the <code>TreePath</code>.
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
}
