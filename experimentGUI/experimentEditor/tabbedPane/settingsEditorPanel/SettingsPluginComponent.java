package experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;


@SuppressWarnings("serial")
public class SettingsPluginComponent extends SettingsComponent {
	private Vector<SettingsComponent> subSettingsComponents = new Vector<SettingsComponent>();
	private JPanel optionPanel;
	private JCheckBox activatedCheckBox;

	public SettingsPluginComponent() {
		setLayout(new BorderLayout());		
		activatedCheckBox = new JCheckBox("");
		activatedCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveValue();
				loadValue();				
			}
		});
		add(activatedCheckBox,BorderLayout.NORTH);
		optionPanel = new JPanel();
		optionPanel.setBorder(BorderFactory.createTitledBorder(""));
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
