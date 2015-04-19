package de.uni_passau.fim.infosun.prophet.plugin.plugins;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;

import de.uni_passau.fim.infosun.prophet.experimentViewer.EViewer;
import de.uni_passau.fim.infosun.prophet.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.util.VerticalLayout;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.SettingsList;
import de.uni_passau.fim.infosun.prophet.util.settings.components.TextAreaSetting;

import static de.uni_passau.fim.infosun.prophet.util.language.UIElementNames.getLocalized;
import static de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode.Type.CATEGORY;
import static javax.swing.border.TitledBorder.ABOVE_TOP;
import static javax.swing.border.TitledBorder.CENTER;

/**
 * This <code>Plugin</code> enables the experiment editor to define a list commands to be executed on the command
 * line. The commands will be represented as buttons in a separate <code>JFrame</code> the <code>Plugin</code> displays.
 * Clicking one of the buttons will execute the associated command.
 */
public class ExternalProgramsPlugin implements Plugin {

    private static final String KEY = "start_external_progs";
    private static final String KEY_COMMANDS = "commands";

    /**
     * A <code>JFrame</code> that displays a column of <code>JButton</code>s that start external programs on the
     * command line when clicked.
     */
    public static class ProgramList extends JFrame {

        private final Map<File, Process> processes;
        private final Map<JButton, File> programs;
        private final ActionListener listener;

        private JPanel list;

        /**
         * Constructs a new <code>ProgramList</code>.
         */
        public ProgramList() {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            String title = getLocalized("MENU_TAB_SETTINGS_EXTERNAL_PROGRAMS_TITLE");
            Border emptyBorder = BorderFactory.createEmptyBorder(10, 20, 0, 20);

            list = new JPanel();
            list.setBorder(BorderFactory.createTitledBorder(emptyBorder, title, CENTER, ABOVE_TOP));
            list.setLayout(new VerticalLayout(5, VerticalLayout.STRETCH, VerticalLayout.TOP));
            add(list, BorderLayout.CENTER);

            processes = new HashMap<>();
            programs = new HashMap<>();

            listener = event -> {
                Object btn = event.getSource();

                if (!(btn instanceof JButton && programs.containsKey(btn))) {
                    return;
                }

                File program = programs.get(btn);
                Process process = processes.get(program);

                if (process != null && process.isAlive()) {
                    JOptionPane.showMessageDialog(null, getLocalized("MESSAGE_ONLY_ONE_INSTANCE"));
                } else {

                    try {
                        process = Runtime.getRuntime().exec(program.toString());
                        processes.replace(program, process);
                    } catch (IOException e) {
                        String message = getLocalized("MESSAGE_COULD_NOT_START_PROGRAM") + ": " + e.getMessage();
                        JOptionPane.showMessageDialog(null, message);
                    }
                }
            };
        }

        /**
         * Removes all buttons from this <code>ProgramList</code> and destroys all running sub-processes started
         * previously. The layout of this <code>JFrame</code> must be validated after this method was called.
         */
        public void clear() {
            list.removeAll();
            programs.clear();
            processes.values().stream().filter(p -> p != null).forEach(Process::destroy);
            processes.clear();
        }

        /**
         * Clears this <code>ProgramList</code> and then loads new programs from the given <code>Attribute</code>.
         * The programs (<code>String</code>s to be executed on the command line) are separated by line breaks.
         * The size of the <code>ProgramList</code> will be adjusted to fit the new number of buttons.
         *
         * @param attribute
         *         the <code>Attribute</code> to load from
         */
        public void load(Attribute attribute) {
            Scanner scanner = new Scanner(attribute.getValue());
            JButton button;
            File program;

            clear();

            while (scanner.hasNextLine()) {
                program = new File(scanner.nextLine());
                button = new JButton(program.getName());
                button.addActionListener(listener);

                programs.put(button, program);
                processes.put(program, null);

                list.add(button);
            }

            pack();
        }
    }

    private ProgramList pList;

    private EViewer eViewer;
    private Point eViewerLoc;

    /**
     * Constructs a new <code>ExternalProgramsPlugin</code>.
     */
    public ExternalProgramsPlugin() {
        pList = new ProgramList();
    }

    @Override
    public Setting getSetting(QTreeNode node) {

        if (node.getType() != CATEGORY) {
            return null;
        }

        Attribute mainAttribute = node.getAttribute(KEY);
        SettingsList settingsList = new SettingsList(mainAttribute, getClass().getSimpleName(), true);
        settingsList.setCaption(getLocalized("MENU_TAB_SETTINGS_EXTERNAL_PROGRAMS"));

        Attribute subAttribute = mainAttribute.getSubAttribute(KEY_COMMANDS);
        Setting subSetting = new TextAreaSetting(subAttribute, null);
        subSetting.setCaption(getLocalized("MENU_TAB_SETTINGS_PATH_OF_EXTERNAL_PROGRAMS"));
        settingsList.addSetting(subSetting);

        return settingsList;
    }

    @Override
    public void experimentViewerRun(EViewer experimentViewer) {
        this.eViewer = experimentViewer;
        this.eViewerLoc = experimentViewer.getLocation();

        this.eViewer.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentMoved(ComponentEvent e) {
                super.componentMoved(e);

                eViewerLoc.setLocation(e.getComponent().getLocation());
            }
        });
    }

    @Override
    public boolean denyEnterNode(QTreeNode node) {
        return false;
    }

    @Override
    public void enterNode(QTreeNode node) {

        if (!enabled(node)) {
            return;
        }

        pList.load(node.getAttribute(KEY).getSubAttribute(KEY_COMMANDS));

        Rectangle eViewerDim = eViewer.getBounds();

        pList.setLocation(eViewerLoc.x + eViewerDim.width + 10, eViewerLoc.y);
        pList.setVisible(true);
    }

    @Override
    public String denyNextNode(QTreeNode currentNode) {
        return null;
    }

    @Override
    public void exitNode(QTreeNode node) {

        if (enabled(node)) {
            pList.clear();
            pList.setVisible(false);
        }
    }

    @Override
    public String finishExperiment() {
        pList.dispose();
        return null;
    }

    /**
     * Returns whether this <code>Plugin</code> is enabled for the given <code>node</code>.
     *
     * @param node
     *         the <code>QTreeNode</code> to check
     *
     * @return <code>true</code> iff this <code>Plugin</code> is enabled for <code>node</code>
     */
    private boolean enabled(QTreeNode node) {

        if (node.getType() != CATEGORY) {
            return false;
        }

        return node.containsAttribute(KEY) && Boolean.parseBoolean(node.getAttribute(KEY).getValue());
    }
}
