package de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.tree.TreePath;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Handles HTML operations for the <code>QTree</code>.
 */
public class QTreeHTMLHandler {

    public static int highestID = 0;
    public static Set<String> returnedIDs = new HashSet<>();

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
    public static List<String> getIDs(QTreeNode root, int number) {
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
}
