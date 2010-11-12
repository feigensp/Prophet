package plugins.codeViewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import plugins.codeViewer.codeViewerPlugins.CodeViewerPlugin;
import plugins.codeViewer.codeViewerPlugins.CodeViewerPluginList;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponent;
import experimentEditor.tabbedPane.settingsEditor.SettingsComponentFactory;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsPathChooser;

@SuppressWarnings("serial")
public class CodeViewerSettingsComponent extends SettingsComponent {
	Vector<SettingsComponent> mySettings;
	JPanel optionPanel;
	JCheckBox activatedCheckBox;

	public CodeViewerSettingsComponent() {
		setLayout(new BorderLayout());		
		activatedCheckBox = new JCheckBox("");
		activatedCheckBox.addActionListener(getDefaultActionListener());
		activatedCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				activate(activatedCheckBox.isSelected());				
			}			
		});
		add(activatedCheckBox,BorderLayout.NORTH);
		optionPanel = new JPanel();
		optionPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		optionPanel.setLayout(new BoxLayout(optionPanel,BoxLayout.Y_AXIS));
		add(optionPanel,BorderLayout.CENTER);
	}

	public void setValue(String value) {
		activatedCheckBox.setSelected(Boolean.parseBoolean(value));
		activate(activatedCheckBox.isSelected());
	}

	public String getValue() {
		return ""+activatedCheckBox.isSelected();
	}

	public void setCaption(String caption) {
		activatedCheckBox.setText(caption);
	}
	
	public String getCaption() {
		return activatedCheckBox.getText();
	}
	public void activate(boolean active) {
		optionPanel.setVisible(active);
		if (active) {
			optionPanel.removeAll();
			optionPanel.updateUI();
		
			if (getSelected()!=null) {
				SettingsComponent pathChooser = new SettingsComponentFactory(getSelected(), new SettingsPathChooser(), "codeviewer_path", "Quelltextpfad").build();
				optionPanel.add(pathChooser);
				// adding checkboxes for the given possible settings in Settings.java
				for (CodeViewerPlugin plugin : CodeViewerPluginList.getPlugins()) {
					List<SettingsComponentFactory> factories = plugin.getSettingsComponentFactories(getSelected());
					if (factories!=null) {
						for (SettingsComponentFactory factory : factories) {
							if (factory!=null) {
								optionPanel.add(factory.build());
							}
						}
					}					
				}
			}
		}
	}
}
