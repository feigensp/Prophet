package experimentEditor.tabbedPane.settingsEditor.settingsComponents;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

import experimentEditor.tabbedPane.settingsEditor.SettingsComponent;


@SuppressWarnings("serial")
public class SettingsPathChooser extends SettingsComponent {
	private String optionKey;
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

	public void setOptionKey(String oK) {
		optionKey = oK;
	}

	public String getOptionKey() {
		return optionKey;
	}

	public String getValue() {
		return textField.getText();
	}

	public void addActionListener(ActionListener l) {
		textField.addActionListener(l);
	}
	
	public void removeActionListener(ActionListener l) {
		textField.removeActionListener(l);
	}

	public SettingsComponent newInstance() {
		return new SettingsPathChooser();
	}

	@Override
	public void setCaption(String cap) {
		caption.setText(cap);
	}

	@Override
	public void setValue(String value) {
		textField.setText(value);
	}
}
