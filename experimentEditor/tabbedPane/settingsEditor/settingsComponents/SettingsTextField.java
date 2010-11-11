package experimentEditor.tabbedPane.settingsEditor.settingsComponents;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JTextField;

import experimentEditor.tabbedPane.settingsEditor.SettingsComponent;


@SuppressWarnings("serial")
public class SettingsTextField extends SettingsComponent{
	private String optionKey;
	private JLabel caption;
	private JTextField textField; 
	
	public SettingsTextField() {
		setLayout(new BorderLayout());
		caption = new JLabel();
		add(caption, BorderLayout.NORTH);
		textField = new JTextField();
		add(textField, BorderLayout.CENTER);
	}
	public void setOptionKey(String oK) {
		optionKey=oK;
	}	
	public String getOptionKey() {
		return optionKey;
	}
	public void setValue(String value) {
		textField.setText(value);
	}
	public String getValue() {
		return textField.getText();
	}
	public SettingsComponent newInstance() {
		return new SettingsTextField();
	}
	public void setCaption(String cap) {
		caption.setText(cap);
	}
	public void addActionListener(ActionListener l) {
		textField.addActionListener(l);
	}
	public void removeActionListener(ActionListener l) {
		textField.removeActionListener(l);
	}
}
