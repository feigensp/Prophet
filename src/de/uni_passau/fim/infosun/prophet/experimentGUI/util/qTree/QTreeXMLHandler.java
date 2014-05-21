package de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import org.cdmckay.coffeedom.Document;
import org.cdmckay.coffeedom.Element;
import org.cdmckay.coffeedom.input.SAXBuilder;

/**
 * Handles XML operations for the <code>QTree</code>.
 */
public class QTreeXMLHandler {

    private static final XStream saveLoadStream;
    private static final XStream answerStream;

    static {
        saveLoadStream = new XStream();

        // QTreeNode XML adjustments
        String simpleName = QTreeNode.class.getSimpleName();
        saveLoadStream.alias(simpleName, QTreeNode.class);
        saveLoadStream.useAttributeFor(QTreeNode.class, "type");
        saveLoadStream.useAttributeFor(QTreeNode.class, "name");
        saveLoadStream.addImplicitMap(QTreeNode.class, "attributes", "attribute", Attribute.class, "key");
        saveLoadStream.addImplicitCollection(QTreeNode.class, "children", simpleName, QTreeNode.class);
        saveLoadStream.omitField(QTreeNode.class, "answers");
        saveLoadStream.omitField(QTreeNode.class, "answerTime");

        // Attribute XML adjustments
        saveLoadStream.alias("Attribute", Attribute.class);
        saveLoadStream.useAttributeFor(Attribute.class, "key");
        saveLoadStream.useAttributeFor(Attribute.class, "value");
        saveLoadStream.addImplicitMap(Attribute.class, "subAttributes", "attribute", Attribute.class, "key");

        answerStream = new XStream();
        answerStream.alias(simpleName, QTreeNode.class);
        answerStream.useAttributeFor(QTreeNode.class, "type");
        answerStream.useAttributeFor(QTreeNode.class, "name");
        answerStream.omitField(QTreeNode.class, "html");
        answerStream.omitField(QTreeNode.class, "attributes");
        answerStream.omitField(QTreeNode.class, "parent");
        answerStream.addImplicitCollection(QTreeNode.class, "children", simpleName, QTreeNode.class);
        answerStream.addImplicitMap(QTreeNode.class, "answers", "answer", String.class, "name");
        answerStream.useAttributeFor(QTreeNode.class, "answerTime");
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
            System.err.println(e.getClass().getSimpleName() + " while de-serialising. Falling back to old-style XML.");
            node = loadOldExperimentXML(xmlFile);
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

    // Code to handle the old-style XML format below.

    public final static String TYPE_EXPERIMENT = "experiment";
    public final static String TYPE_CATEGORY = "category";
    public final static String TYPE_QUESTION = "question";
    public final static String TYPE_ATTRIBUTE = "attribute";
    public final static String TYPE_ATTRIBUTES = "attributes";
    public final static String TYPE_CHILDREN = "children";
    public final static String ATTRIBUTE_NAME = "name";
    public final static String ATTRIBUTE_VALUE = "value";

    /**
     * Loads the <code>QTreeNode</code> from an old-style XML file.
     * If there is an error de-serialising the <code>xmlFile</code> <code>null</code> will be returned.
     *
     * @param xmlFile the old-style XML file containing a question tree
     * @return the <code>QTreeNode</code> resulting from converting the file or <code>null</code>
     */
    public static QTreeNode loadOldExperimentXML(File xmlFile) {
        Objects.requireNonNull(xmlFile, "xmlFile must not be null!");

        SAXBuilder builder = new SAXBuilder();
        Document document;
        Element element;

        try {
            document = builder.build(xmlFile);
        } catch (IOException e) {
            System.err.println("Could not de-serialise " + xmlFile.getName() + "using old-style XML to a QTreeNode. "
                     + e);
            return null;
        }

        element = document.getRootElement();

        if (!element.getName().equals(TYPE_EXPERIMENT)) {
            System.err.println("Tried to de-serialise an XML file that did not conform to the old-style XML format.");
            return null;
        }

        return loadOldTreeNode(element, null);
    }

    /**
     * Loads a <code>QTreeNode</code> from the given <code>Element</code>.
     *
     * @param element the <code>Element</code> containing information to be de-serialised to a <code>QTreeNode</code>
     * @param parent the parent of the resulting <code>QTreeNode</code>
     * @return the resulting <code>QTreeNode</code>
     */
    private static QTreeNode loadOldTreeNode(Element element, QTreeNode parent) {
        QTreeNode node;
        QTreeNode.Type type;
        String name;
        String html;
        Element attributes;
        Element children;
        List<Element> attributeNodes;
        List<Element> childNodes;

        switch (element.getName()) {
            case TYPE_EXPERIMENT:
                type = QTreeNode.Type.EXPERIMENT;
                break;
            case TYPE_CATEGORY:
                type = QTreeNode.Type.CATEGORY;
                break;
            case TYPE_QUESTION:
                type = QTreeNode.Type.QUESTION;
                break;
            default:
                System.err.println("Unknown node type: " + element.getName());
                type = null;
        }
        name = element.getAttributeValue(ATTRIBUTE_NAME);
        html = element.getAttributeValue(ATTRIBUTE_VALUE);
        node = new QTreeNode(parent, type, name);

        node.setHtml(html);

        attributes = element.getChild(TYPE_ATTRIBUTES);
        children = element.getChild(TYPE_CHILDREN);

        if (attributes != null) {
            attributeNodes = attributes.getChildren(TYPE_ATTRIBUTE);
            attributeNodes.forEach(attElement -> node.addAttribute(loadOldAttributeNode(attElement)));
        }

        if (children != null) {

            childNodes = new LinkedList<>();
            switch (node.getType()) {

                case EXPERIMENT:
                    childNodes.addAll(children.getChildren(TYPE_CATEGORY));
                    break;
                case CATEGORY:
                    childNodes.addAll(children.getChildren(TYPE_QUESTION));
                    break;
            }

            childNodes.forEach(childElement -> node.addChild(loadOldTreeNode(childElement, node)));
        }

        return node;
    }

    /**
     * Loads an <code>Attribute</code> from the given <code>Element</code>.
     *
     * @param element the <code>Element</code> containing information to be de-serialised to an <code>Attribute</code>
     * @return the resulting <code>Attribute</code>
     */
    private static Attribute loadOldAttributeNode(Element element) {
        Attribute attribute;
        Element attributes;
        List<Element> attributeNodes;
        String key = element.getAttributeValue(ATTRIBUTE_NAME);
        String value = element.getAttributeValue(ATTRIBUTE_VALUE);

        attribute = new Attribute(key, value);
        attributes = element.getChild(TYPE_ATTRIBUTES);

        if (attributes != null) {
            attributeNodes = element.getChildren(TYPE_ATTRIBUTE);
            attributeNodes.forEach(attElement -> attribute.addSubAttribute(loadOldAttributeNode(attElement)));
        }

        return attribute;
    }
}
