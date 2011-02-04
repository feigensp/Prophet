package experimentGUI.util.settingsComponents.components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import experimentGUI.util.settingsComponents.SettingsComponent;


@SuppressWarnings("serial")
public class SettingsCheckBox extends SettingsComponent {
	private JCheckBox myCheckBox;
	
	public SettingsCheckBox() {
		setLayout(new BorderLayout());
		myCheckBox = new JCheckBox();
		myCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveValue();				
			}
		});
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
