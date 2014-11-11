package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.codeViewerPlugins;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.stream.Collectors;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewerPlugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.codeViewerPlugins
        .showCIDECodePlugin.Triple;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsTextField;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextAreaHighlighter;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ShowCIDECodePlugin implements CodeViewerPlugin {

    public static final String KEY = "show_cide_code";
    public static final String CIDE_INFO_PATH = "info_path";

    public static final String NODE_PROJECT = "project";
    public static final String ATTRIBUE_NAME = "name";
    public static final String NODE_FILE = "file";
    public static final String NODE_FOLDER = "folder";
    public static final String NODE_FRAGMENT = "fragment";
    public static final String ATTRIBUE_LENGTH = "length";
    public static final String ATTRIBUE_OFFSET = "offset";
    public static final String NODE_FEATURE = "feature";

    final static int rectWidth = 5;

    //Pfad --> Liste(offset, length, features)
    HashMap<String, ArrayList<Triple<Integer, Integer, ArrayList<String>>>> coloringInfos;
    JPanel drawPanel;
    String whitespaces;
    int addToOffset = 0;
    String path;

    private boolean enabled;

    @Override
    public Setting getSetting(Attribute mainAttribute) {

        Attribute attribute = mainAttribute.getSubAttribute(KEY);
        PluginSettings pluginSettings = new PluginSettings(attribute, getClass().getSimpleName(), true);
        pluginSettings.setCaption(UIElementNames.getLocalized("CIDE_HIGHLIGHT_SOURCE_CODE"));

        Attribute subAttribute = attribute.getSubAttribute(CIDE_INFO_PATH);
        Setting subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption("annotations.xml");
        pluginSettings.addSetting(subSetting);

        return pluginSettings;
    }

    @Override
    public void init(Attribute selected) {
        enabled = Boolean.parseBoolean(selected.getSubAttribute(KEY).getValue());
        if (enabled) {
            try {
                Attribute attributes = selected.getSubAttribute(KEY);
                path = attributes.getSubAttribute(CIDE_INFO_PATH).getValue()
                        .replace('/', System.getProperty("file.separator").charAt(0));
                coloringInfos = loadXMLTree(path);
            } catch (FileNotFoundException e) {
                //TODO: Fehler
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFrameCreate(CodeViewer viewer) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEditorPanelCreate(EditorPanel editorPanel) {
        if (enabled) {
            Color myColor = JColorChooser.showDialog(editorPanel, "bla", Color.WHITE);

            RSyntaxTextAreaHighlighter hilit = new RSyntaxTextAreaHighlighter();
            DefaultHighlightPainter painterYellow = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
            editorPanel.getTextArea().setHighlighter(hilit);
            ArrayList<Triple<Integer, Integer, ArrayList<String>>> fileColoringInfos =
                    coloringInfos.get(editorPanel.getFile().getName());
            //wenn infos existieren, nutze sie
            if (fileColoringInfos != null) {
                //anzahl der meisten features die gleichzeitig vorhanden sind holen
                int maxParallelFeatures = 0;
                for (Triple<Integer, Integer, ArrayList<String>> fileInfos : fileColoringInfos) {
                    maxParallelFeatures = Math.max(maxParallelFeatures, fileInfos.getValue2().size());
                }
                whitespaces = createWhitespaceString(maxParallelFeatures);
                fileColoringInfos = correctXMLOffset(fileColoringInfos, editorPanel.getTextArea(), whitespaces);
                addWhitespaces(whitespaces, editorPanel.getTextArea());
                for (Triple<Integer, Integer, ArrayList<String>> infos : fileColoringInfos) {
                    int offset = infos.getKey();
                    int length = infos.getValue1();
                    ArrayList<String> features = infos.getValue2();
                    colorFeatures(offset, length, features, editorPanel.getTextArea(), hilit, painterYellow);
                }
            }
        }
    }

    @Override
    public void onEditorPanelClose(EditorPanel editorPanel) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClose() {
        // TODO Auto-generated method stub

    }

    private String createWhitespaceString(int length) {
        String ret = "";
        for (int i = 0; i < length; i++) {
            ret += " ";
        }
        return ret + "| ";
    }

    private void createColorMenu(int x, int y) {
        //Get Features
        Iterator<ArrayList<Triple<Integer, Integer, ArrayList<String>>>> colorInfosIterator =
                coloringInfos.values().iterator();
        HashSet<String> features = new HashSet<>();
        while (colorInfosIterator.hasNext()) {
            for (Triple<Integer, Integer, ArrayList<String>> fragmentInfos : colorInfosIterator.next()) {
                features.addAll(fragmentInfos.getValue2().stream().collect(Collectors.toList()));
            }
        }

        JFrame colorFrame = new JFrame(UIElementNames.getLocalized("CIDE_COLOR_SELECTION"));
        JPanel contentPane = new JPanel();
        colorFrame.getContentPane().add(contentPane);
        colorFrame.setLocation(x, y);
    }

    // //////////////////
    private void addWhitespaces(String whitespaces, RSyntaxTextArea textArea) {
        int lineCount = textArea.getLineCount();
        int offset;
        for (int i = 0; i < lineCount; i++) {
            try {
                offset = textArea.getLineStartOffset(i);
                textArea.getDocument().insertString(offset, whitespaces, null);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    private ArrayList<Triple<Integer, Integer, ArrayList<String>>> correctXMLOffset(
            ArrayList<Triple<Integer, Integer, ArrayList<String>>> infos, RSyntaxTextArea textArea,
            String whitespaces) {
        for (int i = 0; i < infos.size(); i++) {
            Triple<Integer, Integer, ArrayList<String>> triple = infos.get(i);
            try {
                int startLine = textArea.getLineOfOffset(triple.getKey());
                int endLine = textArea.getLineOfOffset(triple.getKey() + triple.getValue1());
                int newOffset = triple.getKey() + startLine * whitespaces.length();
                int newLength = ((endLine - startLine) * whitespaces.length()) + triple.getValue1();
//				System.out.println("startline="+startLine+" : endLine="+endLine);
//				System.out.println("old Data: offset="+triple.getKey() + " : length="+triple.getValue1());
//				System.out.println("new Data: offset="+newOffset+" : length="+newLength);
//				System.out.println();
                infos.set(i, new Triple<>(newOffset, newLength, triple.getValue2()));
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
        return infos;
    }

    private void colorFeatures(int offset, int length, ArrayList<String> features, RSyntaxTextArea textArea,
            RSyntaxTextAreaHighlighter hilit, DefaultHighlightPainter painterYellow) {
        try {
            System.out.println("color");
            int startLine = textArea.getLineOfOffset(offset);
            int endLine = textArea.getLineOfOffset(offset + length);
            for (int i = startLine; i <= endLine; i++) {
                System.out.println(i);
                int colorOffset = textArea.getLineStartOffset(i);
                for (int j = 0; j < features.size(); j++) {
                    System.out.println("_____" + (colorOffset + j - textArea.getLineStartOffset(i)));
                    hilit.addHighlight(colorOffset + j, colorOffset + j + 1, painterYellow);
                }
            }
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }

    // ///////////////////

    private HashMap<String, ArrayList<Triple<Integer, Integer, ArrayList<String>>>> loadXMLTree(String path)
            throws FileNotFoundException {
        HashMap<String, ArrayList<Triple<Integer, Integer, ArrayList<String>>>> infos = new HashMap<>();
        File file = new File(path);
        if (!file.exists()) {
            throw new FileNotFoundException();
        }
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
            // projekte
            NodeList projectList = doc.getChildNodes();
            for (int i = 0; i < projectList.getLength(); i++) {
                // dateien und ordner
                if (projectList.item(i).getChildNodes().getLength() > 0) {
                    filesAndDirs(projectList.item(i).getChildNodes(), "", infos);
                }
            }
        } catch (SAXException | ParserConfigurationException | IOException e) {
            e.printStackTrace();
        }
        return infos;
    }

    private void filesAndDirs(NodeList list, String path,
            HashMap<String, ArrayList<Triple<Integer, Integer, ArrayList<String>>>> infos) {
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i).getNodeName().equals(NODE_FOLDER)) {
                String folder = list.item(i).getAttributes().getNamedItem(ATTRIBUE_NAME).getNodeValue();
                filesAndDirs(list.item(i).getChildNodes(), path + System.getProperty("file.separator") + folder, infos);
            } else if (list.item(i).getNodeName().equals(NODE_FILE)) {
                String file = list.item(i).getAttributes().getNamedItem(ATTRIBUE_NAME).getNodeValue();
                fragments(list.item(i).getChildNodes(), path + System.getProperty("file.separator") + file, infos);
            }
        }
    }

    private void fragments(NodeList list, String path,
            HashMap<String, ArrayList<Triple<Integer, Integer, ArrayList<String>>>> infos) {
        for (int i = 0; i < list.getLength(); i++) {
            Node fragment = list.item(i);
            if (!fragment.getNodeName().equals("#text")) {
                int offset = -1;
                int length = -1;
                NamedNodeMap fragAttributes = fragment.getAttributes();
                if (fragAttributes.getLength() > 0) {
                    try {
                        offset = Integer.parseInt(fragAttributes.getNamedItem(ATTRIBUE_OFFSET).getNodeValue());
                        length = Integer.parseInt(fragAttributes.getNamedItem(ATTRIBUE_LENGTH).getNodeValue());
                    } catch (NumberFormatException e0) {
                        System.err.print("corrupt xml file");
                    }
                    // Features
                    ArrayList<String> features = new ArrayList<>();
                    NodeList featureList = fragment.getChildNodes();
                    for (int j = 0; j < featureList.getLength(); j++) {
                        if (!featureList.item(j).getNodeName().equals("#text")) {
                            features.add(featureList.item(j).getTextContent());
                        }
                    }
                    if (infos.get(path) == null) {
                        infos.put(path, new ArrayList<>());
                    }
                    infos.get(path).add(new Triple<>(offset, length, features));
                }
            }
        }
    }

//	private class myComboBoxModel
}
