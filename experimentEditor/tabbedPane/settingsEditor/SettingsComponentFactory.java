package experimentEditor.tabbedPane.settingsEditor;

import util.QuestionTreeNode;

public class SettingsComponentFactory {
	QuestionTreeNode selected;
	SettingsComponent component;
	String key;
	String caption;
	String value;
	
	public SettingsComponentFactory(QuestionTreeNode selected, SettingsComponent component, String key, String caption, String value) {
		this.selected=selected;
		this.component=component;
		this.key=key;
		this.caption=caption;
		this.value=value;
	}
	
	public SettingsComponent build() {
		SettingsComponent result;
		try {
			result = component.getClass().newInstance();
			result.setSelected(selected);
			result.setKey(key);
			result.setCaption(caption);
			result.setValue(value);
			return result;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
