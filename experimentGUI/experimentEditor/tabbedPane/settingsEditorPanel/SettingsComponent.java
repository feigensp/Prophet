package experimentGUI.experimentEditor.tabbedPane.settingsEditorPanel;

import javax.swing.JPanel;

import experimentGUI.util.questionTreeNode.QuestionTreeNode;


@SuppressWarnings("serial")
public abstract class SettingsComponent extends JPanel {
	private String key;
	private QuestionTreeNode treeNode;

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
