package experimentEditor.QuestionTree;

/**
 * This Class inherits from JTree.
 * It interacts with EditorData and build there a DataTree (from DataTreeNode) like the data in JTree.
 * It permit limited Drag and Drop, such as Renaming and a 3 Level System.
 * The first Level is the root, the child of the root could have childs again, no more Levels are allowed.
 * All Childs of the second Level get a SettingDialog and EditorData will save the informations.
 * All nodes will have a unique name.
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import util.QuestionTreeNode;

@SuppressWarnings("serial")
public class QuestionTree extends JScrollPane {
	private JTree tree;
	private QuestionTreeNode selected;
	
	private JPopupMenu treePopup; // the popup-menu of the tree itself
	private JPopupMenu categoryPopup; // the popup-menu of categories
	private JPopupMenu questionPopup; // the popup-menu of questions
	
	Vector<QuestionTreeListener> questionTreeListeners;

	/**
	 * The Constructor of the class it creates the popup menu and some settings,
	 * initialising
	 * 
	 * @param root
	 *            root of the jtree
	 * @param textPane
	 *            pane for showing level 3 content
	 * @param viewPane
	 *            pane for showing level 3 content in html
	 */
	public QuestionTree() {
		tree = new JTree((TreeModel)null);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setViewportView(tree);
		tree.addMouseListener(new MouseAdapter() {			
			public void mouseReleased(MouseEvent e) {
				if (tree.getModel()!=null && e.isPopupTrigger()) {
					TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
					if (selPath==null) {
						treePopup.show(tree, e.getX(), e.getY());
					} else {
						tree.setSelectionPath(selPath);
						selected = (QuestionTreeNode) selPath.getLastPathComponent();
						if (selected.isCategory()) {
							categoryPopup.show(tree, e.getX(), e.getY());
						} else {
							questionPopup.show(tree, e.getX(), e.getY());
						}
					}
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
				if (selPath!=null) {
					selected = (QuestionTreeNode) selPath.getLastPathComponent();
					fireEvent(selected);
				}
			}
		});
		
		// create popup menu
		JMenuItem myMenuItem;
		
		ActionListener myActionlistener = new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String name;
				// remove
				if (ae.getActionCommand().equals("remove")) {
					removeNode(selected);
				} else
				// newcategory
				if (ae.getActionCommand().equals("newcategory")) {
					if ((name = JOptionPane.showInputDialog("Name:", "")) != null) {
						addCategory(name);
					}
				} else
				// newquestion
				if (ae.getActionCommand().equals("newquestion")) {
					if ((name = JOptionPane.showInputDialog("Name:", "")) != null) {
						addQuestion(selected, name);
					}
				} else
				// rename
				if (ae.getActionCommand().equals("rename")) {
					if ((name = JOptionPane.showInputDialog("Neuer Name:", selected.getName())) != null) {
						renameNode(selected, name);
					}
				}
			}
		};
		
		treePopup = new JPopupMenu();
		myMenuItem = new JMenuItem("Neue Kategorie");
		myMenuItem.addActionListener(myActionlistener);
		myMenuItem.setActionCommand("newcategory");
		treePopup.add(myMenuItem);
		
		categoryPopup = new JPopupMenu();
		myMenuItem = new JMenuItem("Neue Frage");
		myMenuItem.addActionListener(myActionlistener);
		myMenuItem.setActionCommand("newquestion");		
		categoryPopup.add(myMenuItem);
		categoryPopup.addSeparator();
		myMenuItem = new JMenuItem("Löschen");
		myMenuItem.addActionListener(myActionlistener);
		myMenuItem.setActionCommand("remove");
		categoryPopup.add(myMenuItem);
		myMenuItem = new JMenuItem("Umbenennen");
		myMenuItem.addActionListener(myActionlistener);
		myMenuItem.setActionCommand("rename");		
		categoryPopup.add(myMenuItem);
		
		questionPopup = new JPopupMenu();
		myMenuItem = new JMenuItem("Löschen");
		myMenuItem.addActionListener(myActionlistener);
		myMenuItem.setActionCommand("remove");
		questionPopup.add(myMenuItem);
		myMenuItem = new JMenuItem("Umbenennen");
		myMenuItem.addActionListener(myActionlistener);
		myMenuItem.setActionCommand("rename");		
		questionPopup.add(myMenuItem);

		tree.setDragEnabled(true);
		tree.setDropTarget(new DropTarget() {
			public void dragOver(DropTargetDragEvent dtde) {
				QuestionTreeNode source = (QuestionTreeNode) tree.getSelectionPath().getLastPathComponent();
				Point p = dtde.getLocation();
				TreePath selPath = tree.getPathForLocation(p.x, p.y);
				if (selPath==null) {
					dtde.rejectDrag();
					return;
				}
				QuestionTreeNode target = (QuestionTreeNode) selPath.getLastPathComponent();
				// only accept some actions
				// move from level to same level (not important which parent)
				if (!(source.isCategory() && target.isQuestion())) {
					dtde.acceptDrag(dtde.getDropAction());
				} else {
					dtde.rejectDrag();
				}
			}
			public void drop(DropTargetDropEvent dtde) {
				QuestionTreeNode source = (QuestionTreeNode) tree.getSelectionPath().getLastPathComponent();
				Point p = dtde.getLocation();
				TreePath selPath = tree.getPathForLocation(p.x, p.y);
				if (selPath==null) {
					return;
				}
				QuestionTreeNode target = (QuestionTreeNode) selPath.getLastPathComponent();
				QuestionTreeNode parent = (QuestionTreeNode) target.getParent();
				System.out.println("source "+source);
				System.out.println("target "+target);
				System.out.println("parent "+parent);
				
				if(source.isCategory()==target.isCategory()) {
					source.removeFromParent();
					parent.insert(source, parent.getIndex(target)+1);
				} else {
					source.removeFromParent();
					target.insert(source, target.getChildCount());
				}
				tree.updateUI();
			}

			public void dropActionChanged(DropTargetDragEvent arg0) {
			}

			public void dragExit(DropTargetEvent dte) {
			}

			public void dragEnter(DropTargetDragEvent arg0) {
			}
		});

		tree.setEditable(true);
		tree.setRootVisible(false);
	}

	/**
	 * this method will rename a level 2 or level 3 node the node is selected by
	 * the last mouse-button release
	 * 
	 * @param nameProposal
	 *            a proposal for the name of the node
	 */
	private boolean renameNode(QuestionTreeNode node, String name) {
		boolean result = node.setName(name);
		tree.updateUI();
		return result;
	}

	/**
	 * this method will rename a child the renamed node will be selected after
	 * the last mouse-button release
	 */
	private void removeNode(QuestionTreeNode node) {
		node.removeFromParent();
		tree.updateUI();
	}

	/**
	 * this method will add a Child to the tree the position of the new child
	 * will be selected after the last mouse-button release
	 * 
	 * @param nameProposal
	 *            a name proposal for the new Child
	 */
	private void addCategory(String name) {
		QuestionTreeNode root = (QuestionTreeNode)tree.getModel().getRoot();
		QuestionTreeNode newCategory = new QuestionTreeNode("category",name);
		root.insert(newCategory, root.getChildCount());
		tree.updateUI();
	}
	
	private void addQuestion(QuestionTreeNode category, String name) {
		QuestionTreeNode newQuestion = new QuestionTreeNode("question",name);
		category.insert(newQuestion,category.getChildCount());
		tree.updateUI();
	}
	public void newRoot() {
		setRoot(new QuestionTreeNode("root"));
	}
	public void setRoot(QuestionTreeNode n) {
		selected=null;
		if (n!=null) {
			tree.setModel(new DefaultTreeModel(n));
			tree.setEnabled(true);
		} else {
			tree.setModel(new DefaultTreeModel(/*new QuestionTreeNode("")*/null));
			tree.setEnabled(false);			
		}
		tree.updateUI();
		fireEvent(null);
	}
	public QuestionTreeNode getRoot() {
		return (QuestionTreeNode)tree.getModel().getRoot();
	}
	
	/*
	 * Vorbereitungen zum Casten eines ActionEvents
	 */
	public void addQuestionTreeListener(QuestionTreeListener l) {
		if (questionTreeListeners == null)
			questionTreeListeners = new Vector<QuestionTreeListener>();
		questionTreeListeners.addElement(l);
	}

	public void removeQuestionTreeListener(QuestionTreeListener l) {
		if (questionTreeListeners != null)
			questionTreeListeners.removeElement(l);
	}

	private void fireEvent(QuestionTreeNode questionTreeNode) {
		if (questionTreeListeners == null)
			return;
		QuestionTreeEvent event = new QuestionTreeEvent(this, questionTreeNode);
		for (Enumeration<QuestionTreeListener> e = questionTreeListeners.elements(); e
				.hasMoreElements();)
			((QuestionTreeListener) e.nextElement()).questionTreeEventOccured(event);			
	}
}
