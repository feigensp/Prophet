package questionEditor.QuestEdit;

import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.FormView;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

import questionEditor.EditArea;
import util.QuestionTreeNode;


@SuppressWarnings("serial")
public class QuestionEditorPanel extends JPanel {
	public static final String HTMLSTART = "<html><head></head><body>";
	public static final String HTMLEND = "<br><br><br><input type='submit' value='Weiter'></body></html>";
	
	private EditArea editArea;
	private JTextPane viewPane;
	private QuestionTreeNode selected;	
	
	private JTabbedPane editViewTabbedPane;
	private QuestionEditorToolBar toolBar;
	
	public QuestionEditorPanel(QuestionTreeNode sel) {
		selected=sel;
		
		viewPane = new JTextPane();
		viewPane.setEditorKit(new HTMLEditorKit() {
			public ViewFactory getViewFactory() {
				return new HTMLEditorKit.HTMLFactory() {
					public View create(Element elem) {
						Object o = elem.getAttributes().getAttribute(
								StyleConstants.NameAttribute);
						if (o instanceof HTML.Tag) {
							HTML.Tag kind = (HTML.Tag) o;
							if (kind == HTML.Tag.INPUT)
								return new FormView(elem) {
									protected void submitData(String data) {
										System.out.println("Test");
									}
								};
						}
						return super.create(elem);
					}
				};
			}
		});
		
		editArea = new EditArea();
		editArea.getDocument().addDocumentListener(new DocumentListener() {
			public void myChange() {
				if (selected!=null) {
					if (selected.isQuestion()) {
						selected.setContent(editArea.getText());
						viewPane.setText(HTMLSTART + editArea.getText()
								+ HTMLEND);
					}
				}
			}
			@Override
			public void changedUpdate(DocumentEvent arg0) {
				myChange();		
			}

			@Override
			public void insertUpdate(DocumentEvent arg0) {
				myChange();		
			}

			@Override
			public void removeUpdate(DocumentEvent arg0) {
				myChange();
			}
		});
		
		editArea.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ke) {
				if (ke.isShiftDown() && ke.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						editArea.getDocument().insertString(editArea.getCaretPosition(), "<br>\n", null);
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
			}
		});
		
		if (selected!=null) {
			if (selected.isQuestion()) {
				editArea.setText(selected.getContent());
				viewPane.setText(HTMLSTART + editArea.getText()
						+ HTMLEND);
			}
		}
		
		setLayout(new BorderLayout(0, 0));
		
		editViewTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(editViewTabbedPane);

		JPanel editPanel = new JPanel();
		editViewTabbedPane.addTab("Editor", null, editPanel, null);
		editPanel.setLayout(new BorderLayout(0, 0));		
		toolBar = new QuestionEditorToolBar(editArea);
		editPanel.add(toolBar, BorderLayout.NORTH);
		editPanel.add(new JScrollPane(editArea), BorderLayout.CENTER);

		JPanel viewPanel = new JPanel();
		editViewTabbedPane.addTab("Betrachter", null, viewPanel, null);
		viewPanel.setLayout(new BorderLayout(0, 0));

		viewPane.setEditable(false);
		viewPanel.add(new JScrollPane(viewPane), BorderLayout.CENTER);
	}
}
