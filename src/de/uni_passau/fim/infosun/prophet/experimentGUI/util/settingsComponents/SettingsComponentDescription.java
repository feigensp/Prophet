package de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTreeNode.QuestionTreeNode;


public class SettingsComponentDescription {
	private Class<? extends SettingsComponent> myClass;
	private String key;
	private String caption;
	private SettingsComponentDescription next;

	public SettingsComponentDescription(Class<? extends SettingsComponent> myClass, String key, String caption) {
		this.myClass=myClass;
		this.key=key;
		this.caption=caption;
		this.next=null;
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
		SettingsComponent result;
		try {
			result = myClass.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		result.setCaption(caption);
		if (key!=null) {
			QuestionTreeNode myNode = treeNode.getAddAttribute(key);
			result.setTreeNode(myNode);
		}
		return result;
	}
	public void addNextComponent(SettingsComponentDescription next) {
		if (this.next==null) {
			this.next = next;
		} else {
			this.next.addNextComponent(next);
		}
	}
	public SettingsComponentDescription getNextComponentDescription() {
		return next;
	}
}
