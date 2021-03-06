package de.uni_passau.fim.infosun.prophet.util.settings;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;

/**
 * A <code>Settings</code> implementation that represents the collected settings for a plugin.
 * Optionally these settings can be 'enableable' meaning they can be hidden or shown using a checkbox.
 */
public class SettingsList extends Setting {

    private JPanel settingsPanel;
    private JCheckBox enableCheckBox;
    private List<Setting> settingsList;

    /**
     * Constructs a new <code>SettingsList</code> object.
     *
     * @param attribute
     *         the <code>Attribute</code> for this <code>Setting</code>
     * @param pluginName
     *         the name of the plugin
     * @param enableable
     *         whether the settings are enableable
     */
    public SettingsList(Attribute attribute, String pluginName, boolean enableable) {
        super(attribute, pluginName);

        settingsList = new ArrayList<>();

        settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        add(settingsPanel, BorderLayout.CENTER);

        if (enableable) {
            enableCheckBox = new JCheckBox();
            enableCheckBox.addActionListener(action -> settingsPanel.setVisible(enableCheckBox.isSelected()));
            add(enableCheckBox, BorderLayout.NORTH);
        }
    }

    /**
     * Adds a <code>Setting</code> to this <code>SettingsList</code> object.
     *
     * @param setting
     *         the <code>Setting</code> to be added
     */
    public void addSetting(Setting setting) {
        
        if (setting != null) {
            settingsList.add(setting);
            settingsPanel.add(setting);    
        }
    }

    /**
     * Adds all given <code>Setting</code> objects to this <code>SettingsList</code> object.
     *
     * @param settings
     *         the <code>Setting</code> objects to be added
     */
    public void addAllSettings(Collection<? extends Setting> settings) {
        settingsList.addAll(settings);
        settings.stream().forEach(settingsPanel::add);
    }

    @Override
    public void setCaption(String caption) {
        if (enableCheckBox != null) {
            enableCheckBox.setText(caption);
        }
    }

    @Override
    public void loadValue() {
        if (enableCheckBox != null) {
            boolean active = Boolean.parseBoolean(attribute.getValue());
            enableCheckBox.setSelected(active);
            settingsPanel.setVisible(active);
        }

        for (Setting s : settingsList) {
            s.loadValue();
        }
    }

    @Override
    public void saveValue() {
        if (enableCheckBox != null) {
            attribute.setValue(String.valueOf(enableCheckBox.isSelected()));
        }

        for (Setting s : settingsList) {
            s.saveValue();
        }
    }
}
