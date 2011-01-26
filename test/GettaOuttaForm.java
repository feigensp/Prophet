package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

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

import experimentGUI.Constants;

public class GettaOuttaForm extends JFrame {
	
	

	private static final String HTML_START = "<html><body><form>";
	private static final String HTML_DIVIDER = "<br /><br /><hr /><br /><br />";
	private static final String FOOTER_FORWARD_CAPTION = "Weiter";
	private static final String FOOTER_BACKWARD_CAPTION = "Zurück";
	private static final String FOOTER_END_CATEGORY_CAPTION = "Kategorie Abschließen";
	private static final String FOOTER_START_EXPERIMENT_CAPTION = "Experiment starten";
	private static final String FOOTER_FORWARD = "<input name =\""+Constants.KEY_FORWARD+"\" type=\"submit\" value=\""+FOOTER_FORWARD_CAPTION+"\" />";
	private static final String FOOTER_BACKWARD = "<input name =\""+Constants.KEY_BACKWARD+"\" type=\"submit\" value=\""+FOOTER_BACKWARD_CAPTION+"\" />";
	private static final String FOOTER_END_CATEGORY = "<input name =\""+Constants.KEY_FORWARD+"\" type=\"submit\" value=\""+FOOTER_END_CATEGORY_CAPTION+"\" />";
	private static final String FOOTER_START_EXPERIMENT = "<table><tr><td>Probandencode:</td><td><input name=\""+Constants.KEY_SUBJECT+"\" /></td></tr></table>"+HTML_DIVIDER+"<input name =\""+Constants.KEY_FORWARD+"\" type=\"submit\" value=\""+FOOTER_START_EXPERIMENT_CAPTION+"\" />";

	private JPanel contentPane;
	private JTextPane textPane;
	private HTMLEditorKit htmlEditorKit;
	private static final String CONTENT = "Was wird ausgegeben, wenn folgende main-Methode ausgeführt wird? <br> <br> <textarea name=\"answerAufgabe1\" cols=\"50\" rows=\"10\"></textarea>";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GettaOuttaForm frame = new GettaOuttaForm();
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
	public GettaOuttaForm() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		htmlEditorKit = new HTMLEditorKit() {
			public ViewFactory getViewFactory() {
				return new HTMLEditorKit.HTMLFactory() {
					public View create(Element elem) {
						Object o = elem.getAttributes().getAttribute(
								StyleConstants.NameAttribute);
						if (o instanceof HTML.Tag) {
							HTML.Tag kind = (HTML.Tag) o;
							if (kind == HTML.Tag.INPUT)
								return new FormView(elem) {
									// What should happen when the buttons are
									// pressed?
									protected void submitData(String data) {
										System.out.println("hey");
									}
								};
						}
						return super.create(elem);
					}
				};
			}
		};

		textPane = new JTextPane();
		textPane.setEditable(false);
		textPane.setEditorKit(htmlEditorKit);
		textPane.setText(CONTENT + FOOTER_FORWARD);
		
		contentPane.add(textPane, BorderLayout.CENTER);
		JButton button = new JButton("GETTA!!!!");
		contentPane.add(button, BorderLayout.SOUTH);
		htmlEditorKit.getAccessibleContext().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				System.out.println("heyho");
			}
		});

		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(Arrays.toString(htmlEditorKit.getActions()));
//				System.out.println(htmlEditorKit.getAccessibleContext().getAccessibleValue());
//				System.out.println(htmlEditorKit.getAccessibleContext());
			}
		});
	}

}
