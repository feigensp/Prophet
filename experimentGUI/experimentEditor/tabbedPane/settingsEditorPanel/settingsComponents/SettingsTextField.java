package experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

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
		textField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				saveValue();
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				saveValue();
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				saveValue();
			}			
		});
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
