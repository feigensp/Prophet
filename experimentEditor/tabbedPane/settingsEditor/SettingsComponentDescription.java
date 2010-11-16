package experimentEditor.tabbedPane.settingsEditor;

import java.util.Vector;

import util.QuestionTreeNode;
import experimentEditor.tabbedPane.settingsEditor.settingsComponents.SettingsPluginComponent;


public class SettingsComponentDescription {
	private Class<? extends SettingsComponent> myClass;
	private String key;
	private String caption;
	private Vector<SettingsComponentDescription> subComponents = new Vector<SettingsComponentDescription>();

	public SettingsComponentDescription(Class<? extends SettingsComponent> myClass, String key, String caption) {
		this.myClass=myClass;
		this.key=key;
		this.caption=caption;
	}
	public void addSubComponent(SettingsComponentDescription descr) {
		subComponents.add(descr);
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
		if (result instanceof SettingsPluginComponent) {
			for (SettingsComponentDescription desc : subComponents) {
				((SettingsPluginComponent)result).addComponent(desc.build(myNode));
			}
		}
		return result;
	}
}
