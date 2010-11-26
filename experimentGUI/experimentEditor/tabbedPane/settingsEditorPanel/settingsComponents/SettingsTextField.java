package experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponent;


@SuppressWarnings("serial")
public class SettingsTextField extends SettingsComponent{	
	private JLabel caption;
	private JTextField textField; 
	
	public SettingsTextField() {
		setLayout(new BorderLayout());
		caption = new JLabel();
		add(caption, BorderLayout.NORTH);
		textField = new JTextField();
		add(textField, BorderLayout.CENTER);
		textField.addActionListener(getDefaultActionListener());
	}
	
	public void setCaption(String cap) {
		caption.setText(cap);
	}
	public String getCaption() {
		return caption.getText();
	}

	public void loadValue() {
		textField.setText(getTreeNode().getValue());
	}

	public void saveValue() {
		getTreeNode().setValue(textField.getText());
	}
}
