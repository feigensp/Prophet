package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Vector;
import javax.swing.JOptionPane;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsCheckBox;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsTextField;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type.CATEGORY;

public class MaxTimePlugin implements Plugin {

    public class TimeOut implements Runnable {

        private long startTime;
        private long duration;

        private boolean started;
        private boolean stopped;

        private Thread myThread;

        private String message;
        private boolean disable;
        private boolean submit;
        private QuestionTreeNode node;

        private TimeOut(QuestionTreeNode node, long duration, String message, boolean disable, boolean submit) {
            this.node = node;
            this.duration = duration;
            this.message = message;
            this.disable = disable;
            this.submit = submit;

            stopped = false;
        }

        public boolean start() {
            if (!started && duration > 0) {
                started = true;
                startTime = System.currentTimeMillis();
                myThread = new Thread(this);
                myThread.start();
                return true;
            }
            return false;
        }

        public boolean stop() {
            if (started && !stopped) {
                stopped = true;
                duration = Math.max(0, duration - (System.currentTimeMillis() - startTime));
                myThread.interrupt();
                if (duration == 0) {
                    action();
                }
                return true;
            }
            return false;
        }

        public boolean resume() {
            if (started && stopped && duration > 0) {
                stopped = false;
                startTime = System.currentTimeMillis();
                myThread = new Thread(this);
                myThread.start();
                return true;
            }
            return false;
        }

        private void action() {
            boolean affected = node == null || isTimeOutAffectedBy(currentNode, node);

            if (message != null && message.length() > 0) {
                if (affected) {
                    JOptionPane.showMessageDialog(experimentViewer, message);
                } else {
                    allMessages.put(node, message);
                }
            }

            if (node != null && disable) {
                timeOuts.add(node);
                if (submit && affected) {
                    experimentViewer.forceNext(true);
                }
            }
        }

        @Override
        public void run() {
            try {
                Thread.sleep(duration);
            } catch (InterruptedException e) {
                return;
            }
            stop();
        }
    }

    private static final String KEY = "max_time";
    private static final String KEY_MAX_TIME = "time";
    private static final String KEY_HARD_EXIT = "hard_exit";
    private static final String KEY_HARD_EXIT_WARNING = "show_warning";
    private static final String KEY_HARD_EXIT_WARNING_TIME = "warning_time";
    private static final String KEY_HARD_EXIT_WARNING_MESSAGE = "warning_message";
    private static final String KEY_IGNORE_TIMEOUT = "ignore_timeout";
    private static final String KEY_MESSAGE = "message";

    private ExperimentViewer experimentViewer;

    private HashMap<QuestionTreeNode, String> allMessages = new HashMap<>();
    private HashMap<QuestionTreeNode, Vector<TimeOut>> allClocks = new HashMap<>();
    private HashSet<QuestionTreeNode> timeOuts = new HashSet<>();

    private QuestionTreeNode experimentNode;
    private QuestionTreeNode currentNode;
    private boolean activateForExperiment;

    @Override
    public Setting getSetting(QTreeNode node) {

        Attribute resultAttribute = node.getAttribute(KEY);
        PluginSettings result = new PluginSettings(resultAttribute, getClass().getSimpleName(), true);
        result.setCaption(UIElementNames.MENU_TAB_SETTINGS_TIME_OUT);

        Attribute subAttribute = resultAttribute.getSubAttribute(KEY_MAX_TIME);
        Setting subSetting = new SettingsTextField(subAttribute, null);

        switch (node.getType()) {

            case EXPERIMENT:
                subSetting.setCaption(UIElementNames.MENU_TAB_SETTINGS_MAX_TIME_EXPERIMENT);
                break;
            case CATEGORY:
                subSetting.setCaption(UIElementNames.MENU_TAB_SETTINGS_MAX_TIME_EXPERIMENT);
                break;
            case QUESTION:
                subSetting.setCaption(UIElementNames.MENU_TAB_SETTINGS_MAX_TIME_EXPERIMENT);
                break;
        }
        result.addSetting(subSetting);

        Attribute hardExitAttribute = resultAttribute.getSubAttribute(KEY_HARD_EXIT);
        PluginSettings hardExit = new PluginSettings(hardExitAttribute, null, true);
        hardExit.setCaption(UIElementNames.MENU_TAB_SETTINGS_HARD_TIME_OUT);

        Attribute warningAttribute = hardExitAttribute.getSubAttribute(KEY_HARD_EXIT_WARNING);
        PluginSettings warning = new PluginSettings(warningAttribute, null, true);
        warning.setCaption(UIElementNames.MENU_TAB_SETTINGS_TIME_OUT_WARN_SUBJECTS);

        subAttribute = warningAttribute.getSubAttribute(KEY_HARD_EXIT_WARNING_TIME);
        subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(UIElementNames.MENU_TAB_SETTINGS_TIME_OUT_WARNING_TIME + ":");
        warning.addSetting(subSetting);

        subAttribute = warningAttribute.getSubAttribute(KEY_HARD_EXIT_WARNING_MESSAGE);
        subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(UIElementNames.MENU_TAB_SETTINGS_TIME_OUT_WARNING_MESSAGE + ":");
        warning.addSetting(subSetting);

        hardExit.addSetting(warning);
        result.addSetting(hardExit);

        subAttribute = resultAttribute.getSubAttribute(KEY_MESSAGE);
        subSetting = new SettingsTextField(subAttribute, null);
        subSetting.setCaption(UIElementNames.MENU_TAB_SETTINGS_TIME_OUT_MESSAGE + ":");
        result.addSetting(subSetting);

        if (node.getType() == CATEGORY) {
            subAttribute = resultAttribute.getSubAttribute(KEY_IGNORE_TIMEOUT);
            subSetting = new SettingsCheckBox(subAttribute, null);
            subSetting.setCaption(UIElementNames.MENU_TAB_SETTINGS_IGNORE_TIME_OUT);
            result.addSetting(subSetting);
        }

        return result;
    }

    @Override
    public void experimentViewerRun(ExperimentViewer experimentViewer) {
        this.experimentViewer = experimentViewer;
    }

    private boolean isTimeOuted(QuestionTreeNode node) {
        while (node != null) {
            if (timeOuts.contains(node)) {
                return true;
            }
            if (Boolean.parseBoolean(node.getAttributeValue(KEY_IGNORE_TIMEOUT))) {
                return false;
            }
            node = (QuestionTreeNode) node.getParent();
        }
        return false;
    }

    private boolean isTimeOutAffectedBy(QuestionTreeNode node, QuestionTreeNode timeOutNode) {
        while (node != null) {
            if (node == timeOutNode) {
                return true;
            }
            if (Boolean.parseBoolean(node.getAttributeValue(KEY_IGNORE_TIMEOUT))) {
                System.out.println("<-- false (hero: " + node.getName() + ")");
                return false;
            }
            node = (QuestionTreeNode) node.getParent();
        }
        return false;
    }

    @Override
    public boolean denyEnterNode(QuestionTreeNode node) {
        if (allMessages.size() > 0) {
            Iterator<Entry<QuestionTreeNode, String>> it = allMessages.entrySet().iterator();
            while (it.hasNext()) {
                Entry<QuestionTreeNode, String> entry = it.next();
                if (isTimeOutAffectedBy(node, entry.getKey())) {
                    JOptionPane.showMessageDialog(experimentViewer, entry.getValue());
                    it.remove();
                }
            }
        }
        return isTimeOuted(node);
    }

    private void startTimers(QuestionTreeNode node) {
        Vector<TimeOut> nodeClocks = allClocks.get(node);
        if (nodeClocks == null) {
            boolean enabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
            if (!enabled) {
                return;
            }
            QuestionTreeNode pluginNode = node.getAttribute(KEY);

            nodeClocks = new Vector<>();
            allClocks.put(node, nodeClocks);

            long maxTime = Long.parseLong(pluginNode.getAttributeValue(KEY_MAX_TIME));
            if (node.isExperiment()) {
                maxTime *= 60;
            }
            String message = pluginNode.getAttributeValue(KEY_MESSAGE);

            if (maxTime <= 0) {
                return;
            }
            boolean hard = Boolean.parseBoolean(pluginNode.getAttributeValue(KEY_HARD_EXIT));
            TimeOut mainTimeOut = new TimeOut(node, maxTime * 1000, message, true, hard);
            nodeClocks.add(mainTimeOut);
            mainTimeOut.start();

            if (!hard) {
                return;
            }
            QuestionTreeNode hardNode = pluginNode.getAttribute(KEY_HARD_EXIT);
            boolean hardWarning = Boolean.parseBoolean(hardNode.getAttributeValue(KEY_HARD_EXIT_WARNING));

            if (!hardWarning) {
                return;
            }

            QuestionTreeNode hardWarningNode = hardNode.getAttribute(KEY_HARD_EXIT_WARNING);

            String hardWarningTimeString = hardWarningNode.getAttributeValue(KEY_HARD_EXIT_WARNING_TIME);
            String hardWarningMessage = hardWarningNode.getAttributeValue(KEY_HARD_EXIT_WARNING_MESSAGE);

            if (hardWarningTimeString == null || hardWarningTimeString.length() > 0 ||
                    hardWarningMessage == null || hardWarningMessage.length() > 0) {
                return;
            }
            long hardWarningTime = maxTime - Long.parseLong(hardWarningTimeString);

            if (hardWarningTime <= 0) {
                return;
            }

            TimeOut warningTimeOut = new TimeOut(node, hardWarningTime * 1000, hardWarningMessage, false, false);
            nodeClocks.add(warningTimeOut);
            warningTimeOut.start();
        } else {
            for (TimeOut clock : nodeClocks) {
                clock.resume();
            }
        }
    }

    @Override
    public void enterNode(QuestionTreeNode node) {
        currentNode = node;
        if (node.isExperiment()) {
            experimentNode = node;
            activateForExperiment = true;
            return;
        }
        if (activateForExperiment) {
            startTimers(experimentNode);
            activateForExperiment = false;
        }
        startTimers(node);
    }

    @Override
    public String denyNextNode(QuestionTreeNode currentNode) {
        return null;
    }

    @Override
    public void exitNode(QuestionTreeNode node) {
        Vector<TimeOut> nodeClocks = allClocks.get(node);
        if (nodeClocks != null) {
            for (TimeOut clock : nodeClocks) {
                clock.stop();
            }
        }
    }

    @Override
    public String finishExperiment() {
        return null;
    }
}
