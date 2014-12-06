package de.uni_passau.fim.infosun.prophet.util.qTree.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import au.com.bytecode.opencsv.CSVWriter;
import de.uni_passau.fim.infosun.prophet.Constants;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.ParentNode;
import nu.xom.ParsingException;

/**
 * Handles CSV operations for the <code>QTree</code>
 */
public final class QTreeCSVHandler extends QTreeFormatHandler {

    private QTreeCSVHandler() {}

    private static final String PATH_SEPARATOR = ":";

    /**
     * Recursively searches the given <code>directory</code> for files named <code>fileName</code> and
     * collects them in a list. If the given <code>File</code> is not a directory an empty list will be returned.
     *
     * @param directory
     *         the directory to be searched
     * @param fileName
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
     * @param answerDir
     *         the directory containing the {@value Constants#FILE_ANSWERS} files; subdirectories will be searched
     * @param saveFile
     *         the file to save the resulting CSV to
     */
    public static void exportCSV(File answerDir, File saveFile) {
        List<File> files = getFilesByName(answerDir, Constants.FILE_ANSWERS);
        List<String[]> lines = new LinkedList<>();
        CharsetDecoder utf8decoder;    
        
        for (File file : files) {
            utf8decoder = StandardCharsets.UTF_8.newDecoder();
            
            try (Reader reader = new InputStreamReader(new FileInputStream(file), utf8decoder)) {
                Builder builder = new Builder();
                Document document = builder.build(reader);

                if (lines.isEmpty()) {
                    List<String> line = new ArrayList<>();
                    makeLine(line, document.getRootElement(), QTreeCSVHandler::headerFor);

                    lines.add(line.toArray(new String[line.size()]));
                }

                List<String> line = new ArrayList<>();
                makeLine(line, document.getRootElement(), QTreeCSVHandler::contentFor);

                lines.add(line.toArray(new String[line.size()]));
            } catch (IOException | ParsingException e) {
                System.err.println("Could not parse " + Constants.FILE_ANSWERS + " file " + file.getAbsolutePath());
                System.err.println(e.getMessage());
            }
        }

        CharsetEncoder utf8encoder = StandardCharsets.UTF_8.newEncoder();
        
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(saveFile), utf8encoder)) {
            CSVWriter csvWriter = new CSVWriter(writer, ';', '"');
            csvWriter.writeAll(lines);
        } catch (IOException e) {
            System.err.println("Could not write the CSV export file " + saveFile.getAbsolutePath());
            System.err.println(e.getMessage());
        }
    }

    /**
     * Makes one line of the CSV file and stores the generated CSV values in the given <code>List line</code>.
     * Can be configured (using <code>extractor</code>) to generate the header or a content line of the CSV file.
     *
     * @param line
     *         the <code>List</code> to store the line in
     * @param element
     *         the XML <code>Element</code> for whose line is to be created; must be of type QTreeNode
     * @param extractor
     *         a function producing the CSV field value for the element itself and all its answers
     */
    private static void makeLine(List<String> line, Element element, Function<Element, String> extractor) {
        Element answers = element.getFirstChildElement("answers");

        if (answers != null) {
            Elements answerEntries = answers.getChildElements("entry");

            for (int i = 0; i < answerEntries.size(); i++) {
                line.add(extractor.apply(answerEntries.get(i)));
            }
        }
        line.add(extractor.apply(element));

        Elements children = element.getChildElements(QTreeNode.class.getSimpleName());

        for (int i = 0; i < children.size(); i++) {
            makeLine(line, children.get(i), extractor);
        }
    }

    /**
     * Produces the header <code>String</code> for the given <code>element</code>.
     *
     * @param element
     *         the <code>Element</code>; must be either 'QTreeNode' or 'entry'
     *
     * @return the header or an empty <code>String</code> if the type of the <code>Element</code> is not supported
     */
    private static String headerFor(Element element) {
        String name = element.getLocalName();

        if (name.equals(QTreeNode.class.getSimpleName())) {
            List<String> path = pathTo(element);
            path.add(PATH_SEPARATOR);
            path.add("answerTime");

            return path.stream().reduce(String::concat).get();
        } else if (name.equals("entry")) {
            List<String> path = pathTo(element);
            path.add(PATH_SEPARATOR);
            path.add(element.getFirstChildElement("string").getValue());

            return path.stream().reduce(String::concat).get();
        } else {
            System.err.println("Unrecognized element " + name);
            return "";
        }
    }

    /**
     * Produces the content <code>String</code> for the given <code>element</code>.
     *
     * @param element
     *         the <code>Element</code>; must be either 'QTreeNode' or 'entry'
     *
     * @return the header or an empty <code>String</code> if the type of the <code>Element</code> is not supported
     */
    private static String contentFor(Element element) {
        String name = element.getLocalName();

        if (name.equals(QTreeNode.class.getSimpleName())) {
            return element.getAttributeValue("answerTime");
        } else if (name.equals("entry")) {
            Elements answerElements = element.getFirstChildElement("string-array").getChildElements("string");

            if (answerElements.size() == 1) {
                return answerElements.get(0).getValue();
            } else {
                StringWriter csv = new StringWriter();
                CSVWriter writer = new CSVWriter(csv, ',', '\'', "");

                String[] answers = new String[answerElements.size()];
                for (int i = 0; i < answerElements.size(); i++) {
                    answers[i] = answerElements.get(i).getValue();
                }
                writer.writeNext(answers);

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
     *
     * @param target
     *         the <code>Element</code> whose path is to be constructed
     *
     * @return the path
     */
    private static List<String> pathTo(Element target) {
        List<String> path = new LinkedList<>();
        boolean done = false;
        Element element = target;

        while (!element.getLocalName().equals(QTreeNode.class.getSimpleName())) {
            element = (Element) element.getParent();
        }

        while (!done) {
            ParentNode parentNode = element.getParent();

            path.add(0, element.getAttributeValue("name"));
            if (!(parentNode instanceof Document)) {
                path.add(0, PATH_SEPARATOR);
                element = (Element) parentNode;
            } else {
                done = true;
            }
        }

        return path;
    }
}
