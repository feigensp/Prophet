package experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import experimentGUI.util.questionTreeNode.QuestionTreeNode;


@SuppressWarnings("serial")
public abstract class SettingsComponent extends JPanel {
	String key;
	QuestionTreeNode treeNode;
	
	private ActionListener defaultActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			saveValue();
			loadValue();
		}		
	};
	
	public ActionListener getDefaultActionListener() {
		return defaultActionListener;
	}	

	public QuestionTreeNode getTreeNode() {
		return treeNode;
	}
	public void setTreeNode(QuestionTreeNode treeNode) {
		this.treeNode = treeNode;
		loadValue();
		saveValue();
	}
	
	public abstract void setCaption(String caption);
	public abstract String getCaption();
	
	public abstract void loadValue();
	public abstract void saveValue();
}
