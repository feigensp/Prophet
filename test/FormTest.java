package test;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.FormView;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class FormTest {

	
	private static final String CONTENT = "<form>Bla?<br>" +
		"<input type=\"hidden\" name=\"bla\" value=\"krach\"><br>" + 
		"<input type=\"checkbox\" name=\"bla1\" value=\"1\">" +
		"<input type=\"checkbox\" name=\"bla2\" value=\"2\">" +
		"<input type=\"checkbox\" name=\"bla3\" value=\"3\"><br>" + 
		"<input type=\"radio\" name=\"bla4\" value=\"1\">" +
		"<input type=\"radio\" name=\"bla4\" value=\"2\">" +
		"<input type=\"radio\" name=\"bla4\" value=\"3\"><br>" + 
		"<textarea name=\"answerAufgabe1\" cols=\"50\" rows=\"10\"></textarea><br>" + 
		"<input name=\"hallo\" type=\"text\"><br>" + 
		"<input name =\"nextQuestion\" type=\"submit\" value=\"weiter\" /></form>";

	private static JPanel contentPane;
	private static JTextPane textPane;
	private static JFrame frame;
	private static JScrollPane scrollPane;
	private static FormView submitButton;
	private static NotNullButton tmpButton = new NotNullButton();
	
	
	
	public static void main(String[] args) {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 400, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		frame.setContentPane(contentPane);		
		
		tmpButton.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				System.out.println(tmpButton.getButton());
			}
			
		});
		
		textPane = new JTextPane();		
		textPane.setEditable(false);
		textPane.setEditorKit(new HTMLEditorKit() {
			private static final long serialVersionUID = 1L;

			public ViewFactory getViewFactory() {
				return new HTMLEditorKit.HTMLFactory() {
					public View create(Element elem) {
						Object o = elem.getAttributes().getAttribute(
								StyleConstants.NameAttribute);
						if (o instanceof HTML.Tag) {
							if (o == HTML.Tag.INPUT || o == HTML.Tag.TEXTAREA || o == HTML.Tag.SELECT) {
								FormView formView = new FormView(elem) {
									// What should happen when the buttons are
									// pressed?
									protected void submitData(String data) {
										System.out.println("Daten: ");
										System.out.println(data);
									}
								};
								if (o == HTML.Tag.INPUT &&
										elem.getAttributes().getAttribute(HTML.Attribute.NAME)!=null &&
										elem.getAttributes().getAttribute(HTML.Attribute.NAME).equals(
												experimentGUI.Constants.KEY_FORWARD) &&
										elem.getAttributes().getAttribute(HTML.Attribute.TYPE)!=null &&
										elem.getAttributes().getAttribute(HTML.Attribute.TYPE).equals("submit")) {
									submitButton=formView;
									System.out.println((JButton)submitButton.getComponent());
									tmpButton.setButton((JButton)submitButton.getComponent());
								}
								
								return formView;
							}
						}
						return super.create(elem);
					}
				};
			}
		});	
		System.out.println(submitButton);
		textPane.setText(CONTENT);
		
		scrollPane = new JScrollPane(textPane);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JButton button = new JButton("Hols dir");	
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (submitButton!=null && submitButton.getComponent()!=null && submitButton.getComponent() instanceof JButton) {
					((JButton)submitButton.getComponent()).doClick();
				}
			}
		});
		contentPane.add(button, BorderLayout.SOUTH);
		frame.setVisible(true);
	}

}
