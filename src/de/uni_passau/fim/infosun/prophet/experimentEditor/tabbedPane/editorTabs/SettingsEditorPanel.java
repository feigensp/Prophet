package de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.editorTabs;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpinnerNumberModel;

import de.uni_passau.fim.infosun.prophet.Constants;
import de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.ExperimentEditorTab;
import de.uni_passau.fim.infosun.prophet.experimentEditor.tabbedPane.ExperimentEditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.plugin.PluginList;
import de.uni_passau.fim.infosun.prophet.util.Pair;
import de.uni_passau.fim.infosun.prophet.util.VerticalLayout;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.components.SettingsCheckBox;
import de.uni_passau.fim.infosun.prophet.util.settings.components.SettingsComboBox;
import de.uni_passau.fim.infosun.prophet.util.settings.components.SettingsSpinner;
import de.uni_passau.fim.infosun.prophet.util.settings.components.SettingsTextField;

import static de.uni_passau.fim.infosun.prophet.util.language.UIElementNames.getLocalized;
import static de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode.Type.CATEGORY;
import static de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode.Type.EXPERIMENT;

/**
 * Displays a list of <code>Setting</code>s. There are both default settings and settings contributed by
 * <code>Plugin</code>s.
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
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(5, 50, 5, 50));
        settingsPanel.setLayout(new VerticalLayout(5, VerticalLayout.STRETCH, VerticalLayout.TOP));

        scrollPane = new JScrollPane(settingsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        componentList = new LinkedList<>();
        settings.put(selected, componentList);

        // add the standard options for experiments and categories
        QTreeNode.Type selectedType = selected.getType();
        if (selectedType == EXPERIMENT) {

            Attribute attribute = selected.getAttribute(Constants.KEY_EXPERIMENT_CODE);
            Setting setting = new SettingsTextField(attribute, null);
            setting.setCaption(getLocalized("EXPERIMENT_CODE") + ":");

            componentList.add(setting);
            settingsPanel.add(setting);

            boolean initial = !selected.containsAttribute(Constants.KEY_SUBJECT_CODE_CAP);
            attribute = selected.getAttribute(Constants.KEY_SUBJECT_CODE_CAP);

            if (initial) {
                attribute.setValue(getLocalized("FOOTER_SUBJECT_CODE_CAPTION"));
            }

            setting = new SettingsTextField(attribute, null);
            setting.setCaption(getLocalized("SETTING_SUBJECT_CODE"));

            componentList.add(setting);
            settingsPanel.add(setting);

            List<Pair<String, String>> items = new ArrayList<>();
            items.add(new Pair<>(Constants.KEY_VIEWER_LANGUAGE_SYSTEM, getLocalized("LANGUAGE_SYSTEM")));
            items.add(new Pair<>(Locale.GERMAN.toLanguageTag(), getLocalized("LANGUAGE_GERMAN")));
            items.add(new Pair<>(Locale.ENGLISH.toLanguageTag(), getLocalized("LANGUAGE_ENGLISH")));

            attribute = selected.getAttribute(Constants.KEY_VIEWER_LANGUAGE);
            setting = new SettingsComboBox(attribute, null, items);
            setting.setCaption(getLocalized("VIEWER_LANGUAGE"));

            componentList.add(setting);
            settingsPanel.add(setting);

            attribute = selected.getAttribute(Constants.KEY_TIMING);
            setting = new SettingsCheckBox(attribute, null);
            setting.setCaption(getLocalized("STOPWATCH_CAPTION"));

            componentList.add(setting);
            settingsPanel.add(setting);
        }

        if (selectedType == CATEGORY) {
            Attribute attribute = selected.getAttribute(Constants.KEY_DONOTSHOWCONTENT); //TODO apply this setting to all children when saved
            Setting setting = new SettingsCheckBox(attribute, null);
            setting.setCaption(getLocalized("MENU_TAB_SETTINGS_DONT_SHOW_CONTENT"));

            componentList.add(setting);
            settingsPanel.add(setting);

            attribute = selected.getAttribute(Constants.KEY_QUESTIONSWITCHING);
            setting = new SettingsCheckBox(attribute, null);
            setting.setCaption(getLocalized("MENU_TAB_SETTINGS_ALLOW_BACK_AND_FORTH"));

            componentList.add(setting);
            settingsPanel.add(setting);
        }

        if (selectedType == EXPERIMENT || selectedType == CATEGORY) {
            Attribute attribute = selected.getAttribute(Constants.KEY_ONLY_SHOW_X_CHILDREN);
            PluginSettings setting = new PluginSettings(attribute, null, true);
            setting.setCaption(getLocalized("MENU_TAB_SETTINGS_ONLY_SHOW_X_CHILDREN"));

            Attribute subAttribute = attribute.getSubAttribute(Constants.KEY_SHOW_NUMBER_OF_CHILDREN);
            SpinnerNumberModel model = new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1);
            Setting subSetting = new SettingsSpinner(subAttribute, null, model);

            subSetting.setCaption(getLocalized("MENU_TAB_SETTINGS_SHOW_NUMBER_OF_CHILDREN"));
            setting.addSetting(subSetting);

            componentList.add(setting);
            settingsPanel.add(setting);
        }

        if (selectedType == EXPERIMENT || selectedType == CATEGORY) {
            Attribute attribute = selected.getAttribute(Constants.KEY_RANDOMIZE_CHILDREN);
            Setting setting = new SettingsCheckBox(attribute, null);
            setting.setCaption(getLocalized("MENU_TAB_SETTINGS_RANDOMIZE_CHILDREN"));

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
