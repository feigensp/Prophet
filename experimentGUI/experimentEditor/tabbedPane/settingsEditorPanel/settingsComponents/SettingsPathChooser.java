package experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponent;


@SuppressWarnings("serial")
public class SettingsPathChooser extends SettingsComponent {	
	private JLabel caption;
	private JTextField textField;
	private JButton pathButton;

	public SettingsPathChooser() {
		setLayout(new BorderLayout());
		caption = new JLabel();
		add(caption, BorderLayout.NORTH);;

		textField = new JTextField();
		textField.setEditable(false);
		add(textField,BorderLayout.CENTER);
		textField.setColumns(20);
		textField.addActionListener(getDefaultActionListener());
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
