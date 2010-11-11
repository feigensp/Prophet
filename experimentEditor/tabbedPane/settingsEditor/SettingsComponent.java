package experimentEditor.tabbedPane.settingsEditor;

import java.awt.event.ActionListener;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public abstract class SettingsComponent extends JPanel {
	public abstract SettingsComponent newInstance();
	public abstract void setOptionKey(String oK);
	public abstract String getOptionKey();
	public abstract void setValue(String value);
	public abstract String getValue();
	public abstract void setCaption(String cap);
	public abstract void addActionListener(ActionListener l);
	public abstract void removeActionListener(ActionListener l);
}
