package de.uni_passau.fim.infosun.prophet.experimentGUI.plugins;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.uni_passau.fim.infosun.prophet.experimentGUI.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.VerticalLayout;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTreeNode.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsComponentDescription;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsPluginComponentDescription;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.components.SettingsTextArea;

public class ExternalProgramsPlugin extends Thread implements Plugin {

    private static final String KEY = "start_external_progs";
    private static final String KEY_COMMANDS = "commands";
    private ArrayList<Process> processes = new ArrayList<>();
    private JFrame frame;
    private JPanel panel;

    private Point location;
    private ExperimentViewer experimentViewer;
    private boolean enabled;

    @Override
    public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
        if (node.isCategory()) {
            SettingsPluginComponentDescription result =
                    new SettingsPluginComponentDescription(KEY, UIElementNames.MENU_TAB_SETTINGS_EXTERNAL_PROGRAMS,
                            true);
            result.addSubComponent(new SettingsComponentDescription(SettingsTextArea.class, KEY_COMMANDS,
                    UIElementNames.MENU_TAB_SETTINGS_PATH_OF_EXTERNAL_PROGRAMS));
            return result;
        }
        return null;
    }

    @Override
    public void experimentViewerRun(ExperimentViewer experimentViewer) {
        this.experimentViewer = experimentViewer;
    }

    @Override
    public boolean denyEnterNode(QuestionTreeNode node) {
        return false;
    }

    @Override
    public void enterNode(QuestionTreeNode node) {
        if (node.isCategory()) {
            enabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
            if (enabled) {
                QuestionTreeNode attributes = node.getAttribute(KEY);
                String[] commands = attributes.getAttributeValue(KEY_COMMANDS).split("\n");
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
        frame = new JFrame(UIElementNames.MENU_TAB_SETTINGS_EXTERNAL_PROGRAMS_TITLE);
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
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
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
                            JOptionPane.showMessageDialog(null, UIElementNames.MESSAGE_ONLY_ONE_INSTANCE);
                        }
                    }
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null,
                            UIElementNames.MESSAGE_COULD_NOT_START_PROGRAM + ": " + e1.getMessage());
                }
            }
        });
        panel.add(button);
    }

    @Override
    public String denyNextNode(QuestionTreeNode currentNode) {
        return null;
    }

    @Override
    public void exitNode(QuestionTreeNode node) {
        if (enabled) {
            location = frame.getLocation();
            frame.setVisible(false);
            frame.dispose();
            if (node.isCategory()) {
                for (Process process : processes) {
                    if (process != null) {
                        process.destroy();
                    }
                }
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
