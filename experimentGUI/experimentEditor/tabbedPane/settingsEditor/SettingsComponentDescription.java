package experimentGUI.experimentEditor.tabbedPane.settingsEditor;

import experimentGUI.util.questionTreeNode.QuestionTreeNode;


public class SettingsComponentDescription {
	private Class<? extends SettingsComponent> myClass;
	private String key;
	private String caption;

	public SettingsComponentDescription(Class<? extends SettingsComponent> myClass, String key, String caption) {
		this.myClass=myClass;
		this.key=key;
		this.caption=caption;
	}
	public Class<? extends SettingsComponent> getMyClass() {
		return myClass;
	}
	public String getKey() {
		return key;
	}
	public String getCaption() {
		return caption;
	}
	public SettingsComponent build(QuestionTreeNode treeNode) {
		QuestionTreeNode myNode = treeNode.getAddAttribute(key);
		SettingsComponent result;
		try {
			result = myClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		result.setCaption(caption);
		result.setTreeNode(myNode);
		return result;
	}
}
