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

    private static XStream saveLoadStream;
    private static XStream answerStream;

    static {
        saveLoadStream = new XStream();

        // QTreeNode XML adjustments
        saveLoadStream.alias("QTreeNode", QTreeNode.class);
        saveLoadStream.useAttributeFor(QTreeNode.class, "type");
        saveLoadStream.useAttributeFor(QTreeNode.class, "name");
        saveLoadStream.addImplicitCollection(QTreeNode.class, "children", "child", QTreeNode.class);
        saveLoadStream.addImplicitMap(QTreeNode.class, "attributes", "attribute", Attribute.class, "key");
        saveLoadStream.omitField(QTreeNode.class, "answers");
        saveLoadStream.omitField(QTreeNode.class, "answerTime");

        // Attribute XML adjustments
        saveLoadStream.alias("Attribute", Attribute.class);
        saveLoadStream.useAttributeFor(Attribute.class, "key");
        saveLoadStream.useAttributeFor(Attribute.class, "value");
        saveLoadStream.addImplicitMap(Attribute.class, "subAttributes", "attribute", Attribute.class, "key");

        answerStream = new XStream();
        //TODO configure the answerStream to produce the old answers.xml format
    }

    /**
     * Saves the given <code>QTreeNode</code> (and thereby the whole tree under it) to the given file using
     * the answers.xml format. This will overwrite <code>saveFile</code>.
     * Neither <code>root</code> nor <code>saveFile</code> may be <code>null</code>.
     *
     * @param root the root of the tree to save
     * @param saveFile the file to save the tree to
     * @throws IOException if the file can not be written to
     */
    public static void saveAnswerXML(QTreeNode root, File saveFile) throws IOException {
        Objects.requireNonNull(root, "root must not be null!");
        Objects.requireNonNull(saveFile, "saveFile must not be null!");

        answerStream.toXML(root, new FileWriter(saveFile));
    }

    /**
     * Loads the <code>QTreeNode</code> from an XML file.
     * If there is an error de-serialising the <code>xmlFile</code> <code>null</code> will be returned.
     *
     * @param xmlFile the XML file containing a <code>QTreeNode</code>
     * @return the <code>QTreeNode</code> contained in the file or <code>null</code>
     */
    public static QTreeNode loadExperimentXML(File xmlFile) {
        Objects.requireNonNull(xmlFile, "xmlFile must not be null!");

        QTreeNode node = null;

        try {
            node = (QTreeNode) saveLoadStream.fromXML(xmlFile);
        } catch (XStreamException | ClassCastException e) {
            System.err.println("Could not de-serialise " + xmlFile.getName() + " to a QTreeNode. " + e); //TODO logging?
        }

        return node;
    }

    /**
     * Saves the given <code>QTreeNode</code> (and thereby the whole tree under it) to the given file.
     * This will overwrite <code>saveFile</code>.
     * Neither <code>root</code> nor <code>saveFile</code> may be <code>null</code>.
     *
     * @param root the root of the tree to save
     * @param saveFile the file to save the tree to
     * @throws IOException if the file can not be written to
     */
    public static void saveExperimentXML(QTreeNode root, File saveFile) throws IOException {
        Objects.requireNonNull(root, "root must not be null!");
        Objects.requireNonNull(saveFile, "saveFile must not be null!");

        saveLoadStream.toXML(root, new FileWriter(saveFile));
    }
}
