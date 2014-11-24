package de.uni_passau.fim.infosun.prophet.plugin.plugins;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFrame;

import de.uni_passau.fim.infosun.prophet.experimentViewer.EViewer;
import de.uni_passau.fim.infosun.prophet.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.util.VerticalLayout;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.components.SettingsTextArea;

import static de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode.Type.CATEGORY;

public class ExternalProgramsPlugin implements Plugin {

    private static final String KEY = "start_external_progs";
    private static final String KEY_COMMANDS = "commands";

    private static class ProgramList extends JFrame {

        private final Map<File, Process> programs;
        private final Map<JButton, File> buttons;

        private final ActionListener listener;

        /**
         * Constructs a new <code>ProgramList</code>.
         */
        public ProgramList() {
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            setTitle(UIElementNames.getLocalized("MENU_TAB_SETTINGS_EXTERNAL_PROGRAMS_TITLE"));

            setLayout(new VerticalLayout(5, VerticalLayout.STRETCH, VerticalLayout.TOP));

            programs = new HashMap<>();
            buttons = new HashMap<>();

            listener = event -> {
                System.out.println(buttons.get(event.getSource()));
            };
        }

        public void clear() {

            buttons.clear();
            programs.clear();
        }

        public void load(Attribute attribute) {
            Scanner scanner = new Scanner(attribute.getValue());
            JButton button;
            File program;

            clear();

            while (scanner.hasNextLine()) {
                program = new File(scanner.nextLine());
                button = new JButton(program.getName());
                button.addActionListener(listener);

                buttons.put(button, program);
                programs.put(program, null);

                add(button);
            }

            revalidate();
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
        PluginSettings pluginSettings = new PluginSettings(mainAttribute, getClass().getSimpleName(), true);
        pluginSettings.setCaption(UIElementNames.getLocalized("MENU_TAB_SETTINGS_EXTERNAL_PROGRAMS"));

        Attribute subAttribute = mainAttribute.getSubAttribute(KEY_COMMANDS);
        Setting subSetting = new SettingsTextArea(subAttribute, null);
        subSetting.setCaption(UIElementNames.getLocalized("MENU_TAB_SETTINGS_PATH_OF_EXTERNAL_PROGRAMS"));
        pluginSettings.addSetting(subSetting);

        return pluginSettings;
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

    private void addButton(String caption, final String command, final int id) {
//        processes.add(id, null);
//        JButton button = new JButton(caption);
//        button.addActionListener(event -> {
//            Process p = processes.get(id);
//            try {
//                if (p == null) {
//                    p = Runtime.getRuntime().exec(command);
//                    processes.add(id, p);
//                } else {
//                    try {
//                        p.exitValue();
//                        p = Runtime.getRuntime().exec(command);
//                        processes.add(id, p);
//                    } catch (Exception e1) {
//                        JOptionPane.showMessageDialog(null, UIElementNames.getLocalized("MESSAGE_ONLY_ONE_INSTANCE"));
//                    }
//                }
//            } catch (IOException e1) {
//                JOptionPane.showMessageDialog(null,
//                        UIElementNames.getLocalized("MESSAGE_COULD_NOT_START_PROGRAM") + ": " + e1.getMessage());
//            }
//        });
//        panel.add(button);
    }

    @Override
    public String denyNextNode(QTreeNode currentNode) {
        return null;
    }

    @Override
    public void exitNode(QTreeNode node) {
//
//        if (enabled) {
//            location = frame.getLocation();
//            frame.setVisible(false);
//            frame.dispose();
//
//            if (node.getType() == CATEGORY) {
//                processes.stream().filter(process -> process != null).forEach(Process::destroy);
//                processes.clear();
//            }
//        }
//
//        enabled = false;
    }

    @Override
    public String finishExperiment() {
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
