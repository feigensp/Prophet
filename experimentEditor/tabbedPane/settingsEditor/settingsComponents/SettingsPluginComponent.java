package experimentEditor.tabbedPane.settingsEditor.settingsComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import experimentEditor.tabbedPane.settingsEditor.SettingsComponent;

@SuppressWarnings("serial")
public class SettingsPluginComponent extends SettingsComponent {
	Vector<SettingsComponent> subSettingsComponents = new Vector<SettingsComponent>();
	JPanel optionPanel;
	JCheckBox activatedCheckBox;

	public SettingsPluginComponent() {
		setLayout(new BorderLayout());		
		activatedCheckBox = new JCheckBox("");
		activatedCheckBox.addActionListener(getDefaultActionListener());
		add(activatedCheckBox,BorderLayout.NORTH);
		optionPanel = new JPanel();
		optionPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		optionPanel.setLayout(new BoxLayout(optionPanel,BoxLayout.Y_AXIS));
		add(optionPanel,BorderLayout.CENTER);
	}

	public void setCaption(String caption) {
		activatedCheckBox.setText(caption);
	}
	
	public String getCaption() {
		return activatedCheckBox.getText();
	}
	public void addComponent(SettingsComponent component) {
		subSettingsComponents.add(component);
		loadValue();
	}

	public void loadValue() {
		boolean active = Boolean.parseBoolean(getTreeNode().getValue());
		activatedCheckBox.setSelected(active);
		optionPanel.setVisible(active);
		if (active) {
			optionPanel.removeAll();
			optionPanel.updateUI();
		
			for (SettingsComponent component : subSettingsComponents) {
				optionPanel.add(component);
				component.loadValue();
			}
		}
	}

	public void saveValue() {
		getTreeNode().setValue(""+activatedCheckBox.isSelected());
	}
}
