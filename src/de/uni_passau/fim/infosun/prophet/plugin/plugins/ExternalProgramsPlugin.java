package de.uni_passau.fim.infosun.prophet.plugin.plugins;

import java.awt.BorderLayout;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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

    private List<Process> processes;
    private JFrame frame;
    private JPanel panel;

    private Point location;
    private EViewer experimentViewer;
    private boolean enabled;

    /**
     * Constructs a new <code>ExternalProgramsPlugin</code>.
     */
    public ExternalProgramsPlugin() {
        processes = new ArrayList<>();
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
        this.experimentViewer = experimentViewer;
    }

    @Override
    public boolean denyEnterNode(QTreeNode node) {
        return false;
    }

    @Override
    public void enterNode(QTreeNode node) {

        if (node.getType() == CATEGORY) {
            enabled = Boolean.parseBoolean(node.getAttribute(KEY).getValue());
            if (enabled) {
                Attribute attributes = node.getAttribute(KEY);
                String[] commands = attributes.getSubAttribute(KEY_COMMANDS).getValue().split("\n");
                createWindow();
                for (int i = 0; i < commands.length; i++) {
                    if (!commands[i].equals("")) {
                        int lastSep = commands[i].lastIndexOf(System.getProperty("file.separator"));
                        String caption = commands[i];
                        if (lastSep != -1) {
                            caption = caption.substring(lastSep + 1);
                        }
                        addButton(caption, commands[i], i);
                    }
                }
                frame.pack();
                if (location == null) {
                    frame.setLocationRelativeTo(experimentViewer);
                } else {
                    frame.setLocation(location);
                }
            }
        }
    }

    private void createWindow() {
        frame = new JFrame(UIElementNames.getLocalized("MENU_TAB_SETTINGS_EXTERNAL_PROGRAMS_TITLE"));
        frame.setLayout(new BorderLayout());
        panel = new JPanel();
        panel.setLayout(new VerticalLayout(0, 0));
        frame.add(new JScrollPane(panel), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
    }

    private void addButton(String caption, final String command, final int id) {
        processes.add(id, null);
        JButton button = new JButton(caption);
        button.addActionListener(event -> {
            Process p = processes.get(id);
            try {
                if (p == null) {
                    p = Runtime.getRuntime().exec(command);
                    processes.add(id, p);
                } else {
                    try {
                        p.exitValue();
                        p = Runtime.getRuntime().exec(command);
                        processes.add(id, p);
                    } catch (Exception e1) {
                        JOptionPane.showMessageDialog(null, UIElementNames.getLocalized("MESSAGE_ONLY_ONE_INSTANCE"));
                    }
                }
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(null,
                        UIElementNames.getLocalized("MESSAGE_COULD_NOT_START_PROGRAM") + ": " + e1.getMessage());
            }
        });
        panel.add(button);
    }

    @Override
    public String denyNextNode(QTreeNode currentNode) {
        return null;
    }

    @Override
    public void exitNode(QTreeNode node) {
        if (enabled) {
            location = frame.getLocation();
            frame.setVisible(false);
            frame.dispose();
            if (node.getType() == CATEGORY) {
                processes.stream().filter(process -> process != null).forEach(Process::destroy);
                processes.clear();
            }
        }
        enabled = false;
    }

    @Override
    public String finishExperiment() {
        return null;
    }
}
