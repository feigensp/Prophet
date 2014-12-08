package de.uni_passau.fim.infosun.prophet.util.qTree.handlers;

import java.io.*;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import com.thoughtworks.xstream.XStream;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import nu.xom.*;
import org.xml.sax.SAXException;

/**
 * Handles XML operations for the <code>QTree</code>. This includes:
 * <ul>
 * <li>Checking a file against the XSD Schema for new or legacy XML files</li>
 * <li>Reading files and extracting the root node (and all the children/attributes)</li>
 * <li>Saving the root node and all children/attributes to a file</li>
 * </ul>
 */
public final class QTreeXMLHandler extends QTreeFormatHandler {

    private static final String xmlProlog = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";
    private static final XStream saveLoadStream;
    private static final XStream answerStream;
    private static Validator legacyValidator;
    private static Validator validator;

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
        answerStream.useAttributeFor(QTreeNode.class, "answerTime");

        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema legacySchema = factory.newSchema(QTreeXMLHandler.class.getResource("LegacyExperiment.xsd"));
            legacyValidator = legacySchema.newValidator();
        } catch (SAXException e) {
            System.err.println("Could not create a Validator for the legacy XML format.");
        }

        try {
            Schema schema = factory.newSchema(QTreeXMLHandler.class.getResource("Experiment.xsd"));
            validator = schema.newValidator();
        } catch (SAXException e) {
            System.err.println("Could not create a Validator for the XML format.");
        }
    }

    /**
     * Utility class.
     */
    private QTreeXMLHandler() {}

    /**
     * Saves the given <code>QTreeNode</code> (and thereby the whole tree under it) to the given file using
     * the answers.xml format. This will overwrite <code>saveFile</code>.
     * Neither <code>root</code> nor <code>saveFile</code> may be <code>null</code>.
     *
     * @param root
     *         the root of the tree to save
     * @param saveFile
     *         the file to save the tree to
     *
     * @throws IOException
     *         if the file can not be written to
     */
    public static void saveAnswerXML(QTreeNode root, File saveFile) throws IOException {
        Objects.requireNonNull(root, "root must not be null!");
        Objects.requireNonNull(saveFile, "saveFile must not be null!");
        CharsetEncoder utf8encoder = StandardCharsets.UTF_8.newEncoder();

        checkParent(saveFile);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(saveFile), utf8encoder)) {
            writer.write(xmlProlog);
            answerStream.toXML(root, writer);
        }
    }

    /**
     * Loads the <code>QTreeNode</code> from an XML file.
     * If there is an error de-serialising the <code>xmlFile</code> <code>null</code> will be returned.
     *
     * @param xmlFile
     *         the XML file containing a <code>QTreeNode</code>
     *
     * @return the <code>QTreeNode</code> contained in the file or <code>null</code>
     */
    public static QTreeNode loadExperimentXML(File xmlFile) {
        Objects.requireNonNull(xmlFile, "xmlFile must not be null!");
        CharsetDecoder utf8decoder = StandardCharsets.UTF_8.newDecoder();
        QTreeNode node;

        if (isValidXML(validator, "CurrentValidator" , xmlFile)) {
            try (Reader reader = new InputStreamReader(new FileInputStream(xmlFile), utf8decoder)) {
                node = (QTreeNode) saveLoadStream.fromXML(reader);
            } catch (IOException e) {
                System.err.println("There was an error deserializing " + xmlFile.getName());
                System.err.println(e.getMessage());
                node = null;
            }
        } else if (isValidXML(legacyValidator, "LegacyValidator" , xmlFile)) {
            node = loadOldExperimentXML(xmlFile);
        } else {
            node = null;
        }

        return node;
    }

    /**
     * Checks whether the given XML file conforms to the <code>Validator</code>s schema.
     * This method will return <code>false</code> if <code>validator</code> or
     * <code>xmlFile</code> is <code>null</code>. The <code>validatorID</code> will be used to identify the
     * <code>Validator</code> when the cause of the validation failure is printed to <code>System.err</code>.
     *
     * @param validator
     *         the <code>Validator</code> to use for validation
     * @param validatorID
     *         a <code>String</code> identifying the <code>Validator</code>
     * @param xmlFile
     *         the XML file to validate
     *
     * @return true iff the <code>Validator</code> accepts the <code>xmlFile</code>
     */
    private static boolean isValidXML(Validator validator, String validatorID, File xmlFile) {
        CharsetDecoder utf8decoder = StandardCharsets.UTF_8.newDecoder();

        if (validator == null || xmlFile == null) {
            System.err.println("Validator or XML file is null. Considering the file invalid.");
            return false;
        }

        try (Reader reader = new InputStreamReader(new FileInputStream(xmlFile), utf8decoder)) {
            validator.validate(new StreamSource(reader));
        } catch (SAXException | IOException e) {
            System.err.println("The file " + xmlFile.getName() + " is invalid according to " + validatorID);
            System.err.println("Cause: " + e.getMessage());
            return false;
        }

        return true;
    }

    /**
     * Saves the given <code>QTreeNode</code> (and thereby the whole tree under it) to the given file.
     * This will overwrite <code>saveFile</code>.
     * Neither <code>root</code> nor <code>saveFile</code> may be <code>null</code>.
     *
     * @param root
     *         the root of the tree to save
     * @param saveFile
     *         the file to save the tree to
     *
     * @throws IOException
     *         if the file can not be written to
     */
    public static void saveExperimentXML(QTreeNode root, File saveFile) throws IOException {
        Objects.requireNonNull(root, "root must not be null!");
        Objects.requireNonNull(saveFile, "saveFile must not be null!");
        CharsetEncoder utf8encoder = StandardCharsets.UTF_8.newEncoder();

        checkParent(saveFile);
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(saveFile), utf8encoder)) {
            writer.write(xmlProlog);
            saveLoadStream.toXML(root, writer);
        }
    }

    // Code to handle the old-style XML format below.

    private static final String TYPE_EXPERIMENT = "experiment";
    private static final String TYPE_CATEGORY = "category";
    private static final String TYPE_QUESTION = "question";
    private static final String TYPE_ATTRIBUTE = "attribute";
    private static final String TYPE_ATTRIBUTES = "attributes";
    private static final String TYPE_CHILDREN = "children";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_VALUE = "value";

    /**
     * Loads the <code>QTreeNode</code> from an old-style XML file.
     * If there is an error de-serialising the <code>xmlFile</code> <code>null</code> will be returned.
     *
     * @param xmlFile
     *         the old-style XML file containing a question tree
     *
     * @return the <code>QTreeNode</code> resulting from converting the file or <code>null</code>
     */
    private static QTreeNode loadOldExperimentXML(File xmlFile) {
        Objects.requireNonNull(xmlFile, "xmlFile must not be null!");
        CharsetDecoder utf8decoder = StandardCharsets.UTF_8.newDecoder();
        Builder parser = new Builder();
        Document document;

        try (Reader reader = new InputStreamReader(new FileInputStream(xmlFile), utf8decoder)) {
            document = parser.build(reader);
        } catch (ParsingException | IOException e) {
            System.err.println("Could not parse " + xmlFile.getName());
            System.err.println(e.getMessage());
            return null;
        }

        return loadOldTreeNode(document.getRootElement(), null);
    }

    /**
     * Loads a <code>QTreeNode</code> from the given <code>Element</code>.
     *
     * @param element
     *         the <code>Element</code> containing information to be de-serialised to a <code>QTreeNode</code>
     * @param parent
     *         the parent of the resulting <code>QTreeNode</code>
     *
     * @return the resulting <code>QTreeNode</code>
     */
    private static QTreeNode loadOldTreeNode(Element element, QTreeNode parent) {
        QTreeNode node;
        QTreeNode.Type type;
        String name;
        String html;
        Element attributes;
        Element children;
        Elements attributeNodes;

        switch (element.getLocalName()) {
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
                System.err.println("Unknown node type: " + element.getLocalName());
                type = null;
        }
        name = element.getAttributeValue(ATTRIBUTE_NAME);
        html = element.getAttributeValue(ATTRIBUTE_VALUE);
        node = new QTreeNode(parent, type, name);

        node.setHtml(html);

        attributes = element.getFirstChildElement(TYPE_ATTRIBUTES);
        children = element.getFirstChildElement(TYPE_CHILDREN);

        if (attributes != null) {
            attributeNodes = attributes.getChildElements(TYPE_ATTRIBUTE);

            for (int i = 0; i < attributeNodes.size(); i++) {
                node.addAttribute(loadOldAttributeNode(attributeNodes.get(i)));
            }
        }

        if (children != null) {
            Elements childElements = null;

            switch (node.getType()) {

                case EXPERIMENT:
                    childElements = children.getChildElements(TYPE_CATEGORY);
                    break;
                case CATEGORY:
                    childElements = children.getChildElements(TYPE_QUESTION);
                    break;
                default:
                    System.err.println("No rule to get the children of a node with type " + node.getType());
            }

            if (childElements != null) {
                for (int i = 0; i < childElements.size(); i++) {
                    node.addChild(loadOldTreeNode(childElements.get(i), node));
                }
            }
        }

        return node;
    }

    /**
     * Loads an <code>Attribute</code> from the given <code>Element</code>.
     *
     * @param element
     *         the <code>Element</code> containing information to be de-serialised to an <code>Attribute</code>
     *
     * @return the resulting <code>Attribute</code>
     */
    private static Attribute loadOldAttributeNode(Element element) {
        Attribute attribute;
        Element attributes;
        Elements attributeNodes;
        String key = element.getAttributeValue(ATTRIBUTE_NAME);
        String value = element.getAttributeValue(ATTRIBUTE_VALUE);

        attribute = new Attribute(key, value);
        attributes = element.getFirstChildElement(TYPE_ATTRIBUTES);

        if (attributes != null) {
            attributeNodes = attributes.getChildElements(TYPE_ATTRIBUTE);

            for (int i = 0; i < attributeNodes.size(); i++) {
                attribute.addSubAttribute(loadOldAttributeNode(attributeNodes.get(i)));
            }
        }

        return attribute;
    }
}
