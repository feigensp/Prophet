package de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.components;

import javax.swing.JFileChooser;

@SuppressWarnings("serial")
public class SettingsDirectoryPathChooser extends SettingsFilePathChooser {

    public SettingsDirectoryPathChooser() {
        setMode(JFileChooser.DIRECTORIES_ONLY);
    }
}
