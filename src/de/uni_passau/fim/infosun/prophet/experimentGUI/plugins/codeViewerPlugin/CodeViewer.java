package de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneDivider;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.fileTree.FileEvent;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.fileTree.FileListener;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.fileTree.FileTree;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTreeNode.QuestionTreeNode;

@SuppressWarnings("serial")
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

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    CodeViewer frame = new CodeViewer(null, null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public CodeViewer(QuestionTreeNode selected, File saveDir) {
        setTitle(UIElementNames.TITLE_CODE_VIEWER);
        setSize(800, 600);
        setLayout(new BorderLayout());

        if (selected == null) {
            selected = new QuestionTreeNode();
        }
        if (saveDir == null) {
            saveDir = new File(".");
        }
        this.saveDir = saveDir;

        //read settings, store in variables

        String showPath =
                selected.getAttributeValue(KEY_PATH).replace('/', System.getProperty("file.separator").charAt(0));
        showDir = new File(showPath == null || showPath.length() == 0 ? "." : showPath);
        if (!showDir.exists()) {
            JOptionPane
                    .showMessageDialog(this, UIElementNames.MESSAGE_PATH_DOES_NOT_EXIST, UIElementNames.MESSAGE_ERROR,
                            JOptionPane.ERROR_MESSAGE);
        }

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        menuBar.setVisible(false);

        fileMenu = new JMenu(UIElementNames.MENU_FILE);
        menuBar.add(fileMenu);
        fileMenu.setVisible(false);

        editMenu = new JMenu(UIElementNames.MENU_EDIT);
        menuBar.add(editMenu);
        editMenu.setVisible(false);

        splitPane = new JSplitPane();

        myTree = new FileTree(showDir);
        myTree.setBorder(null);
        myTree.setPreferredSize(new Dimension(200, 400));
        myTree.addFileListener(this);
        splitPane.setLeftComponent(myTree);

        recorder = new Recorder(selected);

        tabbedPane = new EditorTabbedPane(selected, showDir, recorder);
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
