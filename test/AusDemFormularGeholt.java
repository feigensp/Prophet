package test;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

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

public class AusDemFormularGeholt {	
		
	private static final String CONTENT = "<form>Bla?<br>" +
		"<input name=\"hallo\" type=\"text\" value=\"\"><br>" + 
		"<textarea name=\"answerAufgabe1\" cols=\"50\" rows=\"10\"></textarea><br>" + 
		"<input name =\"meinButton\" type=\"submit\" value=\"weiter\" /></form>";

	private static JPanel contentPane;
	private static JTextPane textPane;
	private static JFrame frame;
	private static JScrollPane scrollPane;
	
	public static void main(String[] args) {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		frame.setContentPane(contentPane);		

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
							HTML.Tag kind = (HTML.Tag) o;
							if (kind == HTML.Tag.INPUT)
								return new FormView(elem) {
									// What should happen when the buttons are
									// pressed?
									protected void submitData(String data) {
										System.out.println("Daten: ");
										System.out.println(data);
									}
								};
						}
						return super.create(elem);
					}
				};
			}
		});	
		textPane.setText(CONTENT);
		
		scrollPane = new JScrollPane(textPane);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		JButton button = new JButton("Hols dir");	
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				try {
					Robot r = new Robot();
					int scrollMax = scrollPane.getVerticalScrollBar().getMaximum();
					scrollPane.getVerticalScrollBar().setValue(scrollMax);
					Point buttonLoc = textPane.getComponent(textPane.getComponentCount()-1).getLocationOnScreen();
					r.mouseMove((int)buttonLoc.getX(), (int)buttonLoc.getY());
					r.mousePress(InputEvent.BUTTON1_MASK);
					r.mouseRelease(InputEvent.BUTTON1_MASK);
				} catch (AWTException e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(button, BorderLayout.SOUTH);
		frame.setVisible(true);
	}

}
