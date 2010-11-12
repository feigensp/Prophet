package experimentEditor.tabbedPane.contentViewer;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Element;
import javax.swing.text.StyleConstants;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import javax.swing.text.html.FormView;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

import util.QuestionTreeNode;
import experimentEditor.tabbedPane.ExperimentEditorTab;

@SuppressWarnings("serial")
public class ContentViewerPanel extends ExperimentEditorTab {
	public static final String HTMLSTART = "<html><body>";
	public static final String HTMLEND = "</body></html>";
	
	QuestionTreeNode selected;
	JTextPane viewerPane;

	public ContentViewerPanel() {
		this.setLayout(new BorderLayout());
		viewerPane = new JTextPane();
		viewerPane.setEditable(false);
		viewerPane.setEditorKit(new HTMLEditorKit() {
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
		add(new JScrollPane(viewerPane), BorderLayout.CENTER);
	}
	public void activate(QuestionTreeNode selected) {
		this.selected=selected;
		if (selected!=null) {
			viewerPane.setText(HTMLSTART + selected.getContent()+ HTMLEND);
		} else {
			viewerPane.setText("");
		}
	}
}
