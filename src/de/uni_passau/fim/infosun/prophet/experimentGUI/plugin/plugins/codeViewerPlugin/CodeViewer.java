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

    public static final String KEY_PATH = "path";
    public static final String KEY_EDITABLE = "editable";

    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu editMenu;

    private FileTree fileTree;
    private EditorTabbedPane tabbedPane;

    private File showDir;
    private File saveDir;

    private Recorder recorder;

    public CodeViewer(Attribute selected, File saveDir) {
        setTitle(getLocalized("TITLE_CODE_VIEWER"));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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
        menuBar.setVisible(false);
        setJMenuBar(menuBar);

        fileMenu = new JMenu(getLocalized("MENU_FILE"));
        fileMenu.setVisible(false);
        menuBar.add(fileMenu);

        editMenu = new JMenu(getLocalized("MENU_EDIT"));
        editMenu.setVisible(false);
        menuBar.add(editMenu);

        JSplitPane splitPane = new JSplitPane();

        fileTree = new FileTree(showDir);
        fileTree.setBorder(null);
        fileTree.addFileListener(this);
        splitPane.setLeftComponent(new JScrollPane(fileTree));

        recorder = new Recorder(selected);

        tabbedPane = new EditorTabbedPane(recorder);
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

        pack();
    }

    @Override
    public void fileEventOccurred(FileEvent event) {

        if (event.getID() == FileEvent.FILE_OPENED) {
            tabbedPane.openFile(event.getFile());
        }
    }

    public Recorder getRecorder() {
        return recorder;
    }

    public FileTree getFileTree() {
        return fileTree;
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

    /**
     * Adds a <code>JMenu</code> to the <code>JMenuBar</code> this <code>CodeViewer</code> uses.
     *
     * @param menu the <code>JMenu</code> to add
     */
    public void addMenu(JMenu menu) {
        menuBar.add(menu);
        menuBar.setVisible(true);
    }

    /**
     * Adds a <code>JMenuItem</code> to the 'File' menu of the <code>JMenuBar</code> this <code>CodeViewer</code> uses.
     *
     * @param item the <code>JMenuItem</code> to add
     */
    public void addMenuItemToFileMenu(JMenuItem item) {
        fileMenu.add(item);
        fileMenu.setVisible(true);
        menuBar.setVisible(true);
    }

    /**
     * Adds a <code>JMenuItem</code> to the 'Edit' menu of the <code>JMenuBar</code> this <code>CodeViewer</code> uses.
     *
     * @param item
     *         the <code>JMenuItem</code> to add
     */
    public void addMenuItemToEditMenu(JMenuItem item) {
        editMenu.add(item);
        editMenu.setVisible(true);
        menuBar.setVisible(true);
    }
}
