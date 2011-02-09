package experimentGUI.util.settingsComponents.components;

import javax.swing.JFileChooser;



@SuppressWarnings("serial")
public class SettingsDirectoryPathChooser extends SettingsFilePathChooser {	
	public SettingsDirectoryPathChooser() {
		setMode(JFileChooser.DIRECTORIES_ONLY);
	}
}
