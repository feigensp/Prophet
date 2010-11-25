package experimentGUI.plugins.questionLists;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLEditorKit;

import experimentGUI.util.questionTreeNode.QuestionTreeNode;


/**
 * Creates a panel which consits JList's.
 * The Lists represents categories and every item in the list represents a question
 * @author Markus Köppen, Andreas Hasselberg
 *
 */
public class QuestionList extends JScrollPane{
	private QuestionTreeNode root;
	private JTextPane textPane;
	
	/**
	 * Constructor which creates an Empty Panel
	 */
	public QuestionList(QuestionTreeNode root) {
		this.root=root;
		textPane = new JTextPane();
		textPane.setEditorKit(new HTMLEditorKit());
		textPane.setEditable(false);
		this.setViewportView(textPane);
	}
	
	public void visit(QuestionTreeNode node) {
		String content = "<html><body>";
		boolean inactive = Boolean.parseBoolean(root.getAttributeValue("inactive"));
		if (!inactive) {
			content+=print(root,node);
		}
		content+="</body></html>";
		textPane.setText(content);
	}

	private String print(QuestionTreeNode node, QuestionTreeNode selected) {
		String name = node == selected ? "<b><u>"+node.getName().toUpperCase()+"</u></b>" : node.getName();
		String subNodes="";
		if (node.getChildCount()>0) {
			String nodeList = "";
			for (int i=0;i<node.getChildCount();i++) {
				QuestionTreeNode subNode = (QuestionTreeNode)node.getChildAt(i);
				boolean inactive = Boolean.parseBoolean(subNode.getAttributeValue("inactive"));
				if (!inactive) {
					String nodeText = print(subNode,selected);
					nodeList+=nodeText.length()>0 ? "<li>"+nodeText+"</li>" : "";
				}
			}
			if (nodeList.length()>0) {
				subNodes="<ul>"+nodeList+"</ul>";
			}
		}
		boolean donotshowcontent = Boolean.parseBoolean(node.getAttributeValue("donotshowcontent"));
		return donotshowcontent && subNodes.length()==0 ? "" : name+subNodes;
	}
}
