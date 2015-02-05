package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.fileTree.FileEvent;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.fileTree.FileListener;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.fileTree.FileTree;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.Recorder;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;

import static de.uni_passau.fim.infosun.prophet.util.language.UIElementNames.getLocalized;

/**
 * A <code>JFrame</code> implementing the GUI for the <code>CodeViewerPlugin</code>.
 */
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
    private Attribute attribute;

    /**
     * Constructs a new <code>CodeViewer</code> taking its settings from the sub-attributes of the given
     * <code>Attribute</code>.
     *
     * @param cvAttributes
     *         the <code>Attribute</code> obtained from the currently selected <code>QTreeNode</code> by
     *         the <code>CodeViewerPlugin</code>
     * @param saveDir
     *         the directory in which data produced by this <code>CodeViewer</code> should be saved
     *
     * @throws NullPointerException
     *         if <code>cvAttributes</code> or <code>saveDir</code> is <code>null</code>
     */
    public CodeViewer(Attribute cvAttributes, File saveDir) {

        if (cvAttributes == null || saveDir == null) {
            throw new NullPointerException("Neither cvAttributes nor saveDir may be null.");
        }

        setTitle(getLocalized("TITLE_CODE_VIEWER"));
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout());

        this.attribute = cvAttributes;
        this.saveDir = saveDir;

        if (cvAttributes.containsSubAttribute(KEY_PATH)) {
            showDir = new File(cvAttributes.getSubAttribute(KEY_PATH).getValue());

            if (!showDir.exists()) {
                String message = getLocalized("MESSAGE_PATH_DOES_NOT_EXIST");
                String title = getLocalized("MESSAGE_ERROR");
                JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
            }
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

        recorder = new Recorder(cvAttributes);

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

        recorder.onFrameCreate(this);
        CodeViewerPluginList.onCreate(this);

        pack();
    }

    @Override
    public void fileEventOccurred(FileEvent event) {

        if (event.getID() == FileEvent.FILE_OPENED) {
            tabbedPane.openFile(event.getFile());
        }
    }

    /**
     * Returns the <code>Recorder</code> used by this <code>CodeViewer</code>.
     *
     * @return the <code>Recorder</code>
     */
    public Recorder getRecorder() {
        return recorder;
    }

    /**
     * Returns the <code>FileTree</code> that is the left hand side of the <code>CodeViewer</code>.
     *
     * @return the <code>FileTree</code>
     */
    public FileTree getFileTree() {
        return fileTree;
    }

    /**
     * Returns the <code>EditorTabbedPane</code> that is the right hand side of the <code>CodeViewer</code>.
     *
     * @return the <code>EditorTabbedPane</code>
     */
    public EditorTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    /**
     * Returns the <code>File</code> representing the directory whose contents are being displayed in this
     * <code>CodeViewer</code>s <code>FileTree</code>.
     *
     * @return the shown directory
     */
    public File getShowDir() {
        return showDir;
    }

    /**
     * Returns the <code>File</code> representing the directory in which this <code>CodeViewer</code> saves the data
     * it produces.
     *
     * @return the save directory
     */
    public File getSaveDir() {
        return saveDir;
    }

    /**
     * Returns the main <code>Attribute</code> of the <code>CodeViewerPlugin</code> this <code>CodeViewer</code> belongs
     * to. <code>Plugins</code> may take their settings from the sub-attributes of this <code>Attribute</code>.
     * 
     * @return the main <code>Attribute</code> of this <code>CodeViewer</code>
     */
    public Attribute getAttribute() {
        return attribute;
    }
    
    /**
     * Adds a <code>JMenu</code> to the <code>JMenuBar</code> this <code>CodeViewer</code> uses.
     *
     * @param menu
     *         the <code>JMenu</code> to add
     */
    public void addMenu(JMenu menu) {
        menuBar.add(menu);
        menuBar.setVisible(true);
    }

    /**
     * Adds a <code>JMenuItem</code> to the 'File' menu of the <code>JMenuBar</code> this <code>CodeViewer</code> uses.
     *
     * @param item
     *         the <code>JMenuItem</code> to add
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
