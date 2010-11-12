package experimentEditor.tabbedPane.settingsEditor;

import util.QuestionTreeNode;

public class SettingsComponentFactory {
	private QuestionTreeNode selected;
	private SettingsComponent component;
	private String key;
	private String caption;
	private String value;
	
	public SettingsComponentFactory(QuestionTreeNode selected, SettingsComponent component, String key, String caption) {
		this.selected=selected;
		this.component=component;
		this.key=key;
		this.caption=caption;
	}
	
	public SettingsComponent build() {
		SettingsComponent result;
		try {
			result = component.getClass().newInstance();
			result.setSelected(selected);
			result.setKey(key);
			result.setCaption(caption);
			if (selected!=null) {
				result.setValue(selected.getAttribute(key));
			}
			return result;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
}
