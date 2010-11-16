package experimentEditor.tabbedPane.settingsEditor.settingsComponents;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.border.EmptyBorder;

import experimentEditor.tabbedPane.settingsEditor.SettingsComponent;


@SuppressWarnings("serial")
public class SettingsCheckBox extends SettingsComponent {
	private JCheckBox myCheckBox;
	
	public SettingsCheckBox() {
		setLayout(new BorderLayout());
		myCheckBox = new JCheckBox();
		myCheckBox.addActionListener(getDefaultActionListener());
		//myCheckBox.setBorder(new EmptyBorder(0,0,0,0));
		add(myCheckBox,BorderLayout.CENTER);
	}
	
	public void setCaption(String cap) {
		myCheckBox.setText(cap);		
	}
	public String getCaption() {
		return myCheckBox.getText();
	}
	public void loadValue() {
		myCheckBox.setSelected(Boolean.parseBoolean(getTreeNode().getValue()));
	}

	public void saveValue() {
		getTreeNode().setValue(""+myCheckBox.isSelected());
	}
}
