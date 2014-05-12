package de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;

/**
 * Handles XML operations for the <code>QTree</code>.
 */
public class QTreeXMLHandler {

    private static XStream xStream;

    static {
        xStream = new XStream();

        // QTreeNode XML adjustments
        xStream.alias("QTreeNode", QTreeNode.class);
        xStream.useAttributeFor(QTreeNode.class, "type");
        xStream.useAttributeFor(QTreeNode.class, "name");
        xStream.addImplicitCollection(QTreeNode.class, "children", "child", QTreeNode.class);
        xStream.addImplicitMap(QTreeNode.class, "attributes", "attribute", Attribute.class, "key");

        // Attribute XML adjustments
        xStream.alias("Attribute", Attribute.class);
        xStream.useAttributeFor(Attribute.class, "key");
        xStream.useAttributeFor(Attribute.class, "value");
        xStream.addImplicitMap(Attribute.class, "subAttributes", "attribute", Attribute.class, "key");
    }

    /**
     * Loads the <code>QTreeNode</code> from an XML file.
     *
     * @param xmlFile the XML file
     * @return the
     */
    public static QTreeNode loadFromXML(File xmlFile) {
        Objects.requireNonNull(xmlFile, "xmlFile must not be null!");

        QTreeNode node = null;

        try {
            node = (QTreeNode) xStream.fromXML(xmlFile);
        } catch (XStreamException | ClassCastException e) {
            System.err.println("Could not de-serialise " + xmlFile.getName() + " to a QTreeNode. " + e);
        }

        return node;
    }

    /**
     * Saves the given <code>QTreeNode</code> (and thereby the whole tree under it) to the given file.
     * This will overwrite <code>saveFile</code>.
     *
     * @param root the root of the tree to save
     * @param saveFile the file to save the tree to
     * @throws IOException if the file can not be written to
     */
    public static void saveToXML(QTreeNode root, File saveFile) throws IOException {
        xStream.toXML(root, new FileWriter(saveFile));
    }
}
