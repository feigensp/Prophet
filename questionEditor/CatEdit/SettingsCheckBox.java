package questionEditor.CatEdit;

import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class SettingsCheckBox extends JCheckBox implements SettingsComponent {
	private String optionKey;
	
	public void setOptionKey(String oK) {
		optionKey=oK;
	}	
	public String getOptionKey() {
		return optionKey;
	}
	public String getValue() {
		return ""+isSelected();
	}
	@Override
	public SettingsComponent newInstance() {
		return new SettingsCheckBox();
	}
	@Override
	public void setValue(String value) {
		setSelected(Boolean.parseBoolean(value));
	}
	@Override
	public void setCaption(String cap) {
		this.setText(cap);		
	}
}
