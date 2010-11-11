package experimentEditor.tabbedPane.settingsEditor;



public class SettingsOption {
	private String key;
	private String caption;
	private SettingsComponent example;
	
	public SettingsOption(String key, SettingsComponent example, String caption) {
		this.key=key;
		this.example=example;
		this.caption=caption;
	}

	public String getKey() {
		return key;
	}

	public String getCaption() {
		return caption;
	}
	
	public SettingsComponent newInstance(String value) {
		SettingsComponent result = example.newInstance();
		result.setOptionKey(getKey());
		result.setCaption(getCaption());
		result.setValue(value);
		return result;
	}
}
