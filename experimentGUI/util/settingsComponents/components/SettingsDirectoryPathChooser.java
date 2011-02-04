package experimentGUI.util.settingsComponents.components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import experimentGUI.util.settingsComponents.SettingsComponent;


@SuppressWarnings("serial")
public class SettingsDirectoryPathChooser extends SettingsComponent {	
	private JLabel caption;
	private JTextField textField;
	private JButton pathButton;

	public SettingsDirectoryPathChooser() {
		setLayout(new BorderLayout());
		caption = new JLabel();
		add(caption, BorderLayout.NORTH);;

		textField = new JTextField();
		textField.setOpaque(false);
		textField.setEditable(true);
		add(textField,BorderLayout.CENTER);
		textField.setColumns(20);
		textField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent arg0) {
				saveValue();
			}
			public void insertUpdate(DocumentEvent arg0) {
				saveValue();
			}
			public void removeUpdate(DocumentEvent arg0) {
				saveValue();
			}			
		});
		pathButton = new JButton("Durchsuchen");
		add(pathButton,BorderLayout.EAST);
		pathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser(new File("."));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					String currentPath = System.getProperty("user.dir");
					String selectedPath = fc.getSelectedFile()
							.getAbsolutePath();
					if (selectedPath.equals(currentPath)) {
						selectedPath = ".";
					} else if (selectedPath.indexOf(currentPath) == 0) {
						selectedPath = selectedPath.substring(currentPath
								.length() + 1) + "/";
					}
					textField.setText(selectedPath.replace('\\','/'));
				}
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
