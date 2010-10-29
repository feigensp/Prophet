package questionEditor;

/**
 * A Dialog to adjust some settings.
 * One is a path an the others are predefined (Settings.java) and could be true oder false
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class SettingsDialog extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private JTextField pathTextField;
	private JPanel featurePanel;

	private Vector<ElementAttribute<Boolean>> settings;

	private String id;

	/**
	 * Constructor which defines the appearance and do the initialisation of the
	 * variables etc.
	 * 
	 * @param id
	 *            String identifier of this Dialog
	 */
	public SettingsDialog(String id) {
		this.id = id;

		settings = new Vector<ElementAttribute<Boolean>>();

		setTitle("Feature Einstellungen");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel pathPanel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) pathPanel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		contentPanel.add(pathPanel, BorderLayout.NORTH);
		pathTextField = new JTextField();
		pathPanel.add(pathTextField);
		pathTextField.setColumns(10);
		JButton pathButton = new JButton("Durchsuchen");
		pathPanel.add(pathButton);
		pathButton.setActionCommand("path");
		pathButton.addActionListener(this);

		featurePanel = new JPanel();
		contentPanel.add(featurePanel, BorderLayout.CENTER);
		featurePanel.setLayout(new BoxLayout(featurePanel, BoxLayout.Y_AXIS));

		// adding checkboxes for the given possible settings in Settings.java
		for (String ea : Settings.settings) {
			JCheckBox check = new JCheckBox(ea);
			check.setSelected(true);
			featurePanel.add(check);
			settings.add(new ElementAttribute<Boolean>(ea, true));
			check.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					String feature = ((JCheckBox) ae.getSource()).getText();
					for (ElementAttribute<Boolean> ea : settings) {
						if (ea.getName().equals(feature)) {
							ea.setContent(((JCheckBox) ae.getSource())
									.isSelected());
							return;
						}
					}
				}
			});
		}

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		okButton.addActionListener(this);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		cancelButton.addActionListener(this);

		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	/**
	 * Returns the String-identifier of the Object
	 * @return string identifier
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the String identifier of the Object
	 * @param id new string identifier
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * this methods set the settings new
	 * @param path path-setting
	 * @param checkings binary-settings
	 */
	public void setSettings(String path, Boolean[] checkings) {
		//path settings
		pathTextField.setText(path);
		//other settings
		int i = 0;
		for (Component comp : featurePanel.getComponents()) {
			if (comp instanceof JCheckBox) {
				if (i < checkings.length) {
					((JCheckBox) comp).setSelected(checkings[i]);
					// update settings
					for (ElementAttribute<Boolean> setting : settings) {
						if (setting.getName().equals(
								((JCheckBox) comp).getText())) {
							setting.setContent(checkings[i]);
						}
					}
					i++;
				}
			}
		}
	}

	/**
	 * Saves the settings to the corresponding node (in Settings.java)
	 */
	public void saveSettings() {
		DataTreeNode node = EditorData.getNode(id);
		if (node != null) {
			boolean exist;
			for (ElementAttribute setting : settings) {
				exist = false;
				Vector<ElementAttribute> attributes = node.getAttributes();
				for (ElementAttribute attribute : attributes) {
					if (attribute.getName().equals(setting.getName())) {
						exist = true;
						attribute.setContent(setting.getContent());
					}
				}
				if (!exist) {
					node.addAttribute(setting);
				}
			}
			exist = false;
			Vector<ElementAttribute> attributes = node.getAttributes();
			for (ElementAttribute attribute : attributes) {
				if (attribute.getName().equals("path")) {
					exist = true;
					attribute.setContent(pathTextField.getText());
				}
			}
			if (!exist) {
				node.addAttribute(new ElementAttribute("path", pathTextField
						.getText()));
			}

		}
	}

	/**
	 * Return a String which shows the settings of this Dialog
	 * @return settings as String
	 */
	public String getSettingsString() {
		String ret = "Pfad : " + pathTextField.getText();
		for (ElementAttribute setting : settings) {
			ret += "\n" + setting.getName() + ": " + setting.getContent();
		}
		return ret;
	}

	/**
	 * Some action Commands...
	 * @param ae
	 */
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("OK")) {
			saveSettings();
			this.setVisible(false);
		} else if (ae.getActionCommand().equals("Cancel")) {
			this.setVisible(false);
		} else if (ae.getActionCommand().equals("path")) {
			JFileChooser fc = new JFileChooser();
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				pathTextField.setText(fc.getSelectedFile().getPath());
			}
		}
	}

}
