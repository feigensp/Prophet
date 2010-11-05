package CategorySettingsDialog;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class SettingsTextField extends JPanel implements SettingsComponent{
	private String optionKey;
	private JLabel caption;
	private JTextField textField; 
	
	public SettingsTextField() {
		setLayout(new BorderLayout());
		caption = new JLabel();
		add(caption, BorderLayout.NORTH);
		textField = new JTextField();
		add(textField, BorderLayout.CENTER);
	}
	public void setOptionKey(String oK) {
		optionKey=oK;
	}	
	public String getOptionKey() {
		return optionKey;
	}
	public void setValue(String value) {
		textField.setText(value);
	}
	public String getValue() {
		return textField.getText();
	}
	public SettingsComponent newInstance() {
		return new SettingsTextField();
	}
	public void setCaption(String cap) {
		caption.setText(cap);
	}
	public void addActionListener(ActionListener l) {
		textField.addActionListener(l);
	}
}
