package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.uni_passau.fim.infosun.prophet.experimentGUI.Constants;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTab;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.PluginList;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.VerticalLayout;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsCheckBox;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsTextField;

import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type.CATEGORY;
import static de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.QTreeNode.Type.EXPERIMENT;

/**
 * A tab for changing settings for nodes. Settings are defined by plugins, some are hard-coded.
 */
public class SettingsEditorPanel extends ExperimentEditorTab {

    /**
     * Caches for the various layout components to preserve their state when the user switches between nodes.
     */
    private Map<QTreeNode, JScrollPane> scrollPanes = new HashMap<>();
    private Map<QTreeNode, List<Setting>> settings = new HashMap<>();

    private QTreeNode selected;

    /**
     * Constructs a new empty <code>SettingsEditorPanel</code>.
     */
    public SettingsEditorPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
    }

    @Override
    public void load(QTreeNode selected) {

        if (selected == null) {
            return;
        }

        this.selected = selected;
        this.removeAll();
        this.updateUI();

        JScrollPane scrollPane = scrollPanes.get(this.selected);

        if (scrollPane == null) {
            scrollPane = buildOptionScrollPane();
            scrollPanes.put(this.selected, scrollPane);
        }

        List<Setting> settingsList = settings.get(selected);

        if (settingsList != null) {
            settingsList.stream().forEach(Setting::loadValue);
        }

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Builds a <code>JScrollPane</code> containing all the <code>Setting</code> objects
     * appropriate for the currently selected node.
     *
     * @return the JScrollPane containing all options
     */
    private JScrollPane buildOptionScrollPane() {
        JScrollPane scrollPane;
        JPanel settingsPanel;
        List<Setting> componentList;

        settingsPanel = new JPanel();
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        settingsPanel.setLayout(new VerticalLayout(5, VerticalLayout.LEFT, VerticalLayout.TOP));

        scrollPane = new JScrollPane(settingsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        componentList = new LinkedList<>();
        settings.put(selected, componentList);

        // add the standard options for experiments and categories
        if (selected.getType() == EXPERIMENT) {

            Attribute attribute = selected.getAttribute(Constants.KEY_EXPERIMENT_CODE);
            Setting setting = new SettingsTextField(attribute, null);
            setting.setCaption(UIElementNames.get("EXPERIMENT_CODE") + ":");

            componentList.add(setting);
            settingsPanel.add(setting);
        }

        if (selected.getType() == CATEGORY) {
            Attribute attribute = selected.getAttribute(Constants.KEY_DONOTSHOWCONTENT);
            Setting setting = new SettingsCheckBox(attribute, null);
            setting.setCaption(UIElementNames.get("MENU_TAB_SETTINGS_DONT_SHOW_CONTENT"));

            componentList.add(setting);
            settingsPanel.add(setting);

            attribute = selected.getAttribute(Constants.KEY_QUESTIONSWITCHING);
            setting = new SettingsCheckBox(attribute, null);
            setting.setCaption(UIElementNames.get("MENU_TAB_SETTINGS_ALLOW_BACK_AND_FORTH"));

            componentList.add(setting);
            settingsPanel.add(setting);
        }

        // add the options contributed by plugins
        List<Setting> pluginSettings = PluginList.getAllSettings(selected);

        pluginSettings.stream().forEach(p -> {
            componentList.add(p);
            settingsPanel.add(p);
        });

        ExperimentEditorTabbedPane.recursiveSetOpaque(scrollPane);

        return scrollPane;
    }

    @Override
    public void save() {
        List<Setting> comps;

        if (selected == null) {
            return;
        }

        comps = settings.get(selected);
        if (comps != null) {
            comps.stream().forEach(Setting::saveValue);
        }
    }
}
