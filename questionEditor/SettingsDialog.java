package questionEditor;

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
	 * Create the dialog.
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
		{
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
		}
		{
			featurePanel = new JPanel();
			contentPanel.add(featurePanel, BorderLayout.CENTER);
			featurePanel
					.setLayout(new BoxLayout(featurePanel, BoxLayout.Y_AXIS));
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
		}
		{
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
		}
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setSettings(String path, Boolean[] checkings) {
		pathTextField.setText(path);
		int i = 0;
		for(Component comp : featurePanel.getComponents()) {
			if(comp instanceof JCheckBox) {
				if(i < checkings.length) {
					((JCheckBox)comp).setSelected(checkings[i]);
					//setting aktualisieren
					for(ElementAttribute<Boolean> setting : settings) {
						if(setting.getName().equals(((JCheckBox)comp).getText())) {
							setting.setContent(checkings[i]);
						}
					}
					i++;
				}
			}
		}
	}

	public void saveSettings() {
		DataTreeNode node = EditorData.getNode(id);
		if (node != null) {
			boolean exist;
			for (ElementAttribute setting : settings) {
				exist = false;
				// schauen ob Attribut bereits existiert
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
			// Pfad speichern
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
	
	public String getSettingsString() {
		String ret = "Pfad : " + pathTextField.getText();
		for (ElementAttribute setting : settings) {
			ret += "\n" + setting.getName() + ": " + setting.getContent();
		}
		return ret;
	}

	@Override
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
