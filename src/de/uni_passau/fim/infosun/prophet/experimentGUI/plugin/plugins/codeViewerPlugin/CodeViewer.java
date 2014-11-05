package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.fileTree.FileEvent;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.fileTree.FileListener;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.fileTree.FileTree;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames.getLocalized;

public class CodeViewer extends JFrame implements FileListener {

    public final static String KEY_PATH = "path";
    public final static String KEY_EDITABLE = "editable";

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu editMenu;

    private JSplitPane splitPane;
    private FileTree myTree;
    private EditorTabbedPane tabbedPane;

    private File showDir;
    private File saveDir;

    private Recorder recorder;

    public CodeViewer(Attribute selected, File saveDir) {
        setTitle(getLocalized("TITLE_CODE_VIEWER"));
        setSize(800, 600);
        setLayout(new BorderLayout());

        if (selected == null) {
            selected = new Attribute("", ""); //TODO does this ever happen?
        }

        if (saveDir == null) {
            saveDir = new File(".");
        }
        this.saveDir = saveDir;

        //read settings, store in variables

        String showPath = selected.getSubAttribute(KEY_PATH).getValue()
                .replace('/', System.getProperty("file.separator").charAt(0));
        showDir = new File(showPath.length() == 0 ? "." : showPath);
        if (!showDir.exists()) {
            JOptionPane
                    .showMessageDialog(this, getLocalized("MESSAGE_PATH_DOES_NOT_EXIST"), getLocalized("MESSAGE_ERROR"),
                            JOptionPane.ERROR_MESSAGE);
        }

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.setVisible(false);

        fileMenu = new JMenu(getLocalized("MENU_FILE"));
        menuBar.add(fileMenu);
        fileMenu.setVisible(false);

        editMenu = new JMenu(getLocalized("MENU_EDIT"));
        menuBar.add(editMenu);
        editMenu.setVisible(false);

        splitPane = new JSplitPane();

        myTree = new FileTree(showDir);
        myTree.setBorder(null);
        myTree.setPreferredSize(new Dimension(200, 400));
        myTree.addFileListener(this);
        splitPane.setLeftComponent(new JScrollPane(myTree));

        recorder = new Recorder(selected);

        tabbedPane = new EditorTabbedPane(showDir, recorder);
        tabbedPane.setBorder(null);
        splitPane.setRightComponent(tabbedPane);

        splitPane.setBorder(null);
        for (Component component : splitPane.getComponents()) {
            if (component instanceof BasicSplitPaneDivider) {
                ((BasicSplitPaneDivider) component).setBorder(null);
            }
        }

        add(splitPane, BorderLayout.CENTER);

        CodeViewerPluginList.init(selected);
        recorder.onFrameCreate(this);
        CodeViewerPluginList.onFrameCreate(this);
    }

    @Override
    public void fileEventOccured(FileEvent arg0) {
        if (arg0.getID() == FileEvent.FILE_OPENED) {
            tabbedPane.openFile(arg0.getFilePath());
        }
    }

    public Recorder getRecorder() {
        return recorder;
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

    public FileTree getFileTree() {
        return myTree;
    }

    public EditorTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public File getShowDir() {
        return showDir;
    }

    public File getSaveDir() {
        return saveDir;
    }

    public void addMenu(JMenu menu) {
        menuBar.add(menu);
        menuBar.setVisible(true);
    }

    public void addMenuItemToFileMenu(JMenuItem item) {
        fileMenu.add(item);
        fileMenu.setVisible(true);
        menuBar.setVisible(true);
    }

    public void addMenuItemToEditMenu(JMenuItem item) {
        editMenu.add(item);
        editMenu.setVisible(true);
        menuBar.setVisible(true);
    }
}
