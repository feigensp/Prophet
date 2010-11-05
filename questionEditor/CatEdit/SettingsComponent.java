package questionEditor.CatEdit;

import java.awt.event.ActionListener;

public interface SettingsComponent {
	public SettingsComponent newInstance();
	public void setOptionKey(String oK);
	public String getOptionKey();
	public void setValue(String value);
	public String getValue();
	public void setCaption(String cap);
	public void addActionListener(ActionListener l);
}
