package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.FormView;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLEditorKit.InsertHTMLTextAction;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class EditorPaneHTML extends JFrame {

	private JPanel contentPane;
	private HTMLEditorKit htmlKit;
	private JTextPane textPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EditorPaneHTML frame = new EditorPaneHTML();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public EditorPaneHTML() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		JButton button = new JButton("New button");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		contentPane.add(button, BorderLayout.SOUTH);

		textPane = new JTextPane();
		textPane.setEditable(false);
		htmlKit = new HTMLEditorKit();
		textPane.setEditorKit(htmlKit);
		textPane.setText("<form>Vorname: <input name=\"Vorname\" type=\"text\" value=\"James T.\"><br>Nachname: <input name=\"Nachname\" type=\"text\" value=\"Kirk\"><br>Dienstrgrad: <select name=\"Dienstgrad\"><option>Captain</select><br><input type=\"submit\"></form>");

		// InsertHTMLTextAction htmlKid = new
		// HTMLEditorKit.InsertHTMLTextAction("LI",
		// "<ul><li></li></ul>", HTML.Tag.UL,
		// HTML.Tag.LI, HTML.Tag.BODY, HTML.Tag.UL);
		//
		// textPane.setEditorKit(htmlKit);
		contentPane.add(textPane, BorderLayout.CENTER);
	}
}
