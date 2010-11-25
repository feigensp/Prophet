package experimentGUI.experimentEditor.tabbedPane.settingsEditor;

import java.util.Vector;

import experimentGUI.util.questionTreeNode.QuestionTreeNode;


public class SettingsPluginComponentDescription extends SettingsComponentDescription {
	private Vector<SettingsComponentDescription> subComponents = new Vector<SettingsComponentDescription>();

	public SettingsPluginComponentDescription(String key, String caption) {
		super(SettingsPluginComponent.class,key,caption);
	}
	public void addSubComponent(SettingsComponentDescription descr) {
		subComponents.add(descr);
	}
	public SettingsComponent build(QuestionTreeNode treeNode) {
		SettingsPluginComponent result = (SettingsPluginComponent)super.build(treeNode);
		QuestionTreeNode myNode = treeNode.getAddAttribute(getKey());
		for (SettingsComponentDescription desc : subComponents) {
			result.addComponent(desc.build(myNode));
		}
		return result;
	}
}
