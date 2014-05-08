package de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components;

import javax.swing.JFileChooser;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;

@SuppressWarnings("serial")
public class SettingsDirectoryPathChooser extends SettingsFilePathChooser {

    public SettingsDirectoryPathChooser(Attribute attribute) {
        super(attribute);

        setMode(JFileChooser.DIRECTORIES_ONLY);
    }
}
