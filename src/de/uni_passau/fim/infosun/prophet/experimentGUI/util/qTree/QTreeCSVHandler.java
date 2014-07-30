package de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import au.com.bytecode.opencsv.CSVWriter;
import de.uni_passau.fim.infosun.prophet.experimentGUI.Constants;
import org.cdmckay.coffeedom.Document;
import org.cdmckay.coffeedom.Element;
import org.cdmckay.coffeedom.input.SAXBuilder;

/**
 * Handles CSV operations for the <code>QTree</code>
 */
public class QTreeCSVHandler {

    private static final String PATH_SEPARATOR = ":";

    /**
     * Recursively searches the given <code>directory</code> for files named <code>fileName</code> and
     * collects them in a list. If the given <code>File</code> is not a directory an empty list will be returned.
     *
     * @param directory
     *         the directory to be searched
     *@param fileName
     *         the filename to be searched for
     *
     * @return the list of files
     */
    public static List<File> getFilesByName(File directory, String fileName) {
        Objects.requireNonNull(directory, "directory must not be null!");
        Objects.requireNonNull(fileName, "fileName must not be null!");

        List<File> xmlFiles = new LinkedList<>();

        if (!directory.isDirectory()) {
            return xmlFiles;
        }

        File[] files = directory.listFiles();

        if (files == null) {
            return xmlFiles;
        }

        for (File file : files) {

            if (file.isDirectory()) {
                xmlFiles.addAll(getFilesByName(file, fileName));
            } else {
                if (file.getName().equals(fileName)) {
                    xmlFiles.add(file);
                }
            }
        }

        return xmlFiles;
    }

    /**
     * Exports all files named {@value Constants#FILE_ANSWERS} in CSV format to the specified <code>saveFile</code>.
     *
     * @param answerDir the directory containing the {@value Constants#FILE_ANSWERS} files; subdirectories will be searched
     * @param saveFile the file to save the resulting CSV to
     */
    public static void exportCSV(File answerDir, File saveFile) {
        List<File> files = getFilesByName(answerDir, Constants.FILE_ANSWERS);
        List<String[]> lines = new LinkedList<>();

        for (File file : files) {
            try {
            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(file);


                if (lines.isEmpty()) {
                    List<String> line = new ArrayList<>();
                    makeLine(line, document.getRootElement(), QTreeCSVHandler::headerFor);

                    lines.add(line.toArray(new String[line.size()]));
                }

                List<String> line = new ArrayList<>();
                makeLine(line, document.getRootElement(), QTreeCSVHandler::contentFor);

                lines.add(line.toArray(new String[line.size()]));
            } catch (IOException e) {
                System.err.println("Could not read " + Constants.FILE_ANSWERS + " file " + file.getAbsolutePath());
            }
        }

        try (FileWriter fileWriter = new FileWriter(saveFile)) {
            CSVWriter csvWriter = new CSVWriter(fileWriter, ';', '"');
            csvWriter.writeAll(lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Makes one line of the CSV file and stores the generated CSV values in the given <code>List line</code>.
     * Can be configured (using <code>extractor</code>) to generate the header or a content line of the CSV file.
     *
     * @param line the <code>List</code> to store the line in
     * @param element the XML <code>Element</code> for whose line is to be created; must be of type QTreeNode
     * @param extractor a function producing the CSV field value for the element itself and all its answers
     */
    private static void makeLine(List<String> line, Element element, Function<Element, String> extractor) {
        Element answers = element.getChild("answers");

        if (answers != null) {
            List<Element> answerEntries = answers.getChildren("entry");

            for (Element answerEntry : answerEntries) {
                line.add(extractor.apply(answerEntry));
            }
        }

        line.add(extractor.apply(element));

        List<Element> children = element.getChildren(QTreeNode.class.getSimpleName());
        for (Element child : children) {
            makeLine(line, child, extractor);
        }
    }

    /**
     * Produces the header <code>String</code> for the given <code>element</code>.
     *
     * @param element the <code>Element</code>; must be either 'QTreeNode' or 'entry'
     * @return the header or an empty <code>String</code> if the type of the <code>Element</code> is not supported
     */
    private static String headerFor(Element element) {
        String name = element.getName();

        if (name.equals(QTreeNode.class.getSimpleName())) {
            List<String> path = pathTo(element);
            path.add(PATH_SEPARATOR);
            path.add("answerTime");

            return path.stream().reduce(String::concat).get();
        } else if (name.equals("entry")) {
            List<String> path = pathTo(element);
            path.add(PATH_SEPARATOR);
            path.add(element.getChildText("string"));

            return path.stream().reduce(String::concat).get();
        } else {
            System.err.println("Unrecognized element " + name);
            return "";
        }
    }

    /**
     * Produces the content <code>String</code> for the given <code>element</code>.
     *
     * @param element the <code>Element</code>; must be either 'QTreeNode' or 'entry'
     * @return the header or an empty <code>String</code> if the type of the <code>Element</code> is not supported
     */
    private static String contentFor(Element element) {
        String name = element.getName();

        if (name.equals(QTreeNode.class.getSimpleName())) {
            return element.getAttributeValue("answerTime");
        } else if (name.equals("entry")) {
            List<Element> answerElements = element.getChild("string-array").getChildren("string");

            if (answerElements.size() == 1) {
                return answerElements.get(0).getText();
            } else {
                StringWriter csv = new StringWriter();
                CSVWriter writer = new CSVWriter(csv, ',', '\'', "");
                List<String> answers = answerElements.stream().map(Element::getText).collect(Collectors.toList());

                writer.writeNext(answers.toArray(new String[answers.size()]));
                return csv.toString();
            }
        } else {
            System.err.println("Unrecognized element " + name);
            return "";
        }
    }

    /**
     * Gives the path from the root QTreeNode to the given <code>Element</code> (or the QTreeNode father of the
     * <code>Element</code>) separated by {@value #PATH_SEPARATOR}.

     * @param element the <code>Element</code> whose path is to be constructed
     * @return the path
     */
    private static List<String> pathTo(Element element) {
        List<String> path = new LinkedList<>();
        Element parent = element;

        while (parent != null && !parent.getName().equals(QTreeNode.class.getSimpleName())) {
            parent = parent.getParentElement();
        }

        while (parent != null) {
            Element parentElement = parent.getParentElement();

            path.add(0, parent.getAttributeValue("name"));
            if (parentElement != null) {
                path.add(0, PATH_SEPARATOR);
            }
            parent = parentElement;
        }

        return path;
    }
}
