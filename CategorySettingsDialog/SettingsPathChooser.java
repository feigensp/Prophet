package CategorySettingsDialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class SettingsPathChooser extends JPanel implements SettingsComponent {
	private String optionKey;
	private JLabel caption;
	private JTextField textField;
	private JButton pathButton;
	
	public SettingsPathChooser() {
		setLayout(new BorderLayout());
		caption = new JLabel();
		add(caption,BorderLayout.NORTH);
		JPanel pathPanel = new JPanel();
		pathPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		add(pathPanel, BorderLayout.CENTER);
		
		textField = new JTextField();
		textField.setEditable(false);
		pathPanel.add(textField);
		textField.setColumns(20);
		pathButton = new JButton("Durchsuchen");
		pathPanel.add(pathButton);
		pathButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fc = new JFileChooser(new File("."));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					String home = new File ("").getAbsolutePath();
					String selectedPath = fc.getSelectedFile().getAbsolutePath();
					System.out.println(home);
					System.out.println(selectedPath);
					if (selectedPath.startsWith(home)) {
						String substring = selectedPath.substring(home.length());
						if (substring.isEmpty()) {
							textField.setText(".");
						} else {
							textField.setText(substring);
						}
					} else {
						textField.setText(fc.getSelectedFile().toString());
					}
				}
			}
		});
	}
	public void setOptionKey(String oK) {
		optionKey=oK;
	}	
	public String getOptionKey() {
		return optionKey;
	}
	public String getValue() {
		return textField.getText();
	}
	public void addActionListener(ActionListener l) {
		textField.addActionListener(l);
	}
	public SettingsComponent newInstance() {
		return new SettingsPathChooser();
	}
	@Override
	public void setCaption(String cap) {
		caption.setText(cap);
	}
	@Override
	public void setValue(String value) {
		textField.setText(value);
	}
}
