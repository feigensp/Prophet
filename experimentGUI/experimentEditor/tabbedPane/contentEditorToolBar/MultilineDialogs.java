package experimentGUI.experimentEditor.tabbedPane.contentEditorToolBar;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import experimentGUI.util.macroEditor.StringTuple;


@SuppressWarnings("serial")
public class MultilineDialogs extends JDialog implements ActionListener {
	
	private static JTextPane contentTextPane;
	private static JTextField nameTextField;
	private static StringTuple dialogInfos;
	
	public static StringTuple showMultilineInputDialog(String headline) {
		MultilineDialogs dialog = new MultilineDialogs(headline);
		dialog.setVisible(true);
		dialog.dispose();
		return dialogInfos;
	}

	public MultilineDialogs(String headline) {
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		JPanel contentPanel = new JPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		JLabel headlineLabel = new JLabel(headline);
		headlineLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		headlineLabel.setHorizontalAlignment(SwingConstants.CENTER);
		contentPanel.add(headlineLabel, BorderLayout.NORTH);
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		contentPanel.add(inputPanel, BorderLayout.CENTER);
		JPanel namePanel = new JPanel();
		namePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		inputPanel.add(namePanel, BorderLayout.NORTH);
		namePanel.add(new JLabel("Name:"));
		nameTextField = new JTextField("", 8);
		namePanel.add(nameTextField);
		contentTextPane = new JTextPane();
		inputPanel.add(contentTextPane, BorderLayout.CENTER);
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		JButton okButton = new JButton("OK");
		okButton.setActionCommand("ok");
		okButton.addActionListener(this);
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);
		buttonPane.add(cancelButton);	
	}
	
	public void actionPerformed(ActionEvent ae) {
		if(ae.getActionCommand().equals("ok")) {
			dialogInfos = new StringTuple();
			dialogInfos.setKey(nameTextField.getText());
			dialogInfos.setValue(contentTextPane.getText());
			this.setVisible(false);
		} else {
			dialogInfos = null;
			this.setVisible(false);
		}
	}

}
