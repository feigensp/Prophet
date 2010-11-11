package experimentEditor.tabbedPane.settingsEditor.settingsComponents;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.border.EmptyBorder;

import experimentEditor.tabbedPane.settingsEditor.SettingsComponent;


@SuppressWarnings("serial")
public class SettingsCheckBox extends SettingsComponent {
	private String optionKey;
	private JCheckBox myCheckBox;
	
	public SettingsCheckBox() {
		setLayout(new BorderLayout());
		myCheckBox = new JCheckBox();
		myCheckBox.setBorder(new EmptyBorder(0,0,0,0));
		add(myCheckBox,BorderLayout.CENTER);
	}
	
	public void setOptionKey(String oK) {
		optionKey=oK;
	}	
	
	public String getOptionKey() {
		return optionKey;
	}
	
	public String getValue() {
		return ""+myCheckBox.isSelected();
	}
	
	public SettingsComponent newInstance() {
		return new SettingsCheckBox();
	}
	
	public void setValue(String value) {
		myCheckBox.setSelected(Boolean.parseBoolean(value));
	}
	
	public void setCaption(String cap) {
		myCheckBox.setText(cap);		
	}

	public void addActionListener(ActionListener l) {
		myCheckBox.addActionListener(l);
	}

	public void removeActionListener(ActionListener l) {
		myCheckBox.removeActionListener(l);
	}
}
