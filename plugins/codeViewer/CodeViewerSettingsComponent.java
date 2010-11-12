package plugins.codeViewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPluginList;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponent;

@SuppressWarnings("serial")
public class CodeViewerSettingsComponent extends SettingsComponent {
	Vector<SettingsComponent> mySettings;
	JTabbedPane tabbedPane;
	JPanel myPanel;
	JPanel optionPanel;
	JCheckBox activatedCheckBox;

	public CodeViewerSettingsComponent() {
		setLayout(new BorderLayout());
		tabbedPane = new JTabbedPane();
		add(tabbedPane,BorderLayout.CENTER);
		myPanel = new JPanel();
		tabbedPane.addTab("", myPanel);
		myPanel.setLayout(new BorderLayout());		
		activatedCheckBox = new JCheckBox("Codeviewer aktivieren");
		activatedCheckBox.addActionListener(getDefaultActionListener());
		activatedCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				activate(activatedCheckBox.isSelected());				
			}			
		});
		myPanel.add(activatedCheckBox,BorderLayout.NORTH);
		optionPanel = new JPanel();
		optionPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		optionPanel.setLayout(new BoxLayout(optionPanel,BoxLayout.Y_AXIS));
		myPanel.add(optionPanel,BorderLayout.CENTER);
	}

	public void setValue(String value) {
		activatedCheckBox.setSelected(Boolean.parseBoolean(value));
		activate(activatedCheckBox.isSelected());
	}

	public String getValue() {
		return ""+activatedCheckBox.isSelected();
	}

	public void setCaption(String cap) {
		tabbedPane.setTitleAt(0, cap);
	}
	
	public String getCaption() {
		return tabbedPane.getTitleAt(0);
	}
	public void activate(boolean active) {
		optionPanel.setVisible(active);
		if (active) {
			optionPanel.removeAll();
			optionPanel.updateUI();
		
			if (getSelected()!=null) {
				// adding checkboxes for the given possible settings in Settings.java
				for (CodeViewerPlugin plugin : CodeViewerPluginList.getPlugins()) {
					SettingsComponent settingsOption = plugin.getSettingsComponentFactory(getSelected()).build();
					if (settingsOption!=null) {
						optionPanel.add(settingsOption);
					}						
				}
			}
		}
	}
}
