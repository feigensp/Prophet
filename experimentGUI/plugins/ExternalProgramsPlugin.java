package experimentGUI.plugins;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import experimentGUI.PluginInterface;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.SettingsPluginComponentDescription;
import experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel.settingsComponents.SettingsTextArea;
import experimentGUI.experimentViewer.ExperimentViewer;
import experimentGUI.util.VerticalLayout;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class ExternalProgramsPlugin implements PluginInterface {

	public static final String KEY = "startExternalProgs";
	public static final String COMMAND = "commands";
	private ArrayList<Process> processes = new ArrayList<Process>();
	private JFrame frame;
	private JPanel panel;
	private int buttons;
	
	private static Point location = null;

	@Override
	public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
		if (node.isCategory()) {
			SettingsPluginComponentDescription result = new SettingsPluginComponentDescription(KEY,
					"Externe Programme starten");
			result.addSubComponent(new SettingsComponentDescription(SettingsTextArea.class, COMMAND,
					"Programmpfade (durch Zeilenumbruch getrennt)"));
			return result;
		}
		return null;
	}

	@Override
	public void experimentViewerRun(ExperimentViewer experimentViewer) {
		if(location == null) {
			location = experimentViewer.getLocation();
		}
	}

	@Override
	public Object enterNode(QuestionTreeNode node) {
		if (node.isCategory()) {				
			boolean categoryEnabled = Boolean.parseBoolean(node.getAttributeValue(KEY));
			if (categoryEnabled) {
				QuestionTreeNode attributes = node.getAttribute(KEY);
				String[] commands = attributes.getAttributeValue(COMMAND).split("\n");
				createWindow();
				for(int i=0; i<commands.length; i++) {
					if(!commands[i].equals("")) {
						int lastSep = commands[i].lastIndexOf(System.getProperty("file.separator"));
						String caption = commands[i];
//						if(lastSep!= -1) {
//							caption = caption.substring(lastSep+1);
//						}
						addButton(caption, commands[i]);
					}
				}
				frame.pack();
			}
		}
		return null;
	}
	
	private void createWindow() {
		frame = new JFrame("Liste nutzbarer Programme");
		frame.setLayout(new BorderLayout());
		panel = new JPanel();
		panel.setLayout(new VerticalLayout(0, 0));
		frame.add(new JScrollPane(panel), BorderLayout.CENTER);
		frame.setLocation(location);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setVisible(true);
		buttons = 0;
	}
	
	private void addButton(String caption, final String command) {
		JButton button = new JButton(caption);
		button.setSize(400, (int)button.getPreferredSize().getHeight());
		int buttonHeight = (int) button.getPreferredSize().getHeight();
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Process p = Runtime.getRuntime().exec(command);
					processes.add(p);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel.add(button);
		buttons++;
		if(buttons < 5) {
			frame.setSize(150, buttons*buttonHeight+50);
		} else {
			frame.setSize(150, 5*buttonHeight+50);			
		}
	}

	@Override
	public void exitNode(QuestionTreeNode node, Object pluginData) {
		location = frame.getLocation();
		frame.setVisible(false);
		frame.dispose();
		if (node.isCategory()) {
			for(int i=0; i<processes.size(); i++) {
				if(processes.get(i) != null) {
					processes.get(i).destroy();
				}
			}
			processes.clear();
		}
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String finishExperiment() {
		return null;
	}

}
