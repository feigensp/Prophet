package experimentGUI.util.settingsComponents;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import experimentGUI.util.VerticalLayout;


@SuppressWarnings("serial")
public class SettingsPluginComponent extends SettingsComponent {
	private Vector<SettingsComponent> subSettingsComponents = new Vector<SettingsComponent>();
	private JPanel optionPanel;
	private JCheckBox activatedCheckBox;

	public SettingsPluginComponent() {
		setLayout(new BorderLayout());		
		activatedCheckBox = new JCheckBox("");
		activatedCheckBox.setOpaque(false);
		activatedCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveValue();
				loadValue();				
			}
		});
		add(activatedCheckBox,BorderLayout.NORTH);
		optionPanel = new JPanel();
		optionPanel.setOpaque(false);
		optionPanel.setBorder(BorderFactory.createTitledBorder(""));
		optionPanel.setLayout(new VerticalLayout(5,VerticalLayout.LEFT,VerticalLayout.TOP));
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
		optionPanel.add(component);
		component.loadValue();
	}

	public void loadValue() {
		boolean active = Boolean.parseBoolean(getTreeNode().getValue());
		activatedCheckBox.setSelected(active);
		optionPanel.setVisible(active);
		if (active) {		
			for (SettingsComponent component : subSettingsComponents) {
				component.loadValue();
			}
		}
	}

	public void saveValue() {
		getTreeNode().setValue(""+activatedCheckBox.isSelected());
	}
}
