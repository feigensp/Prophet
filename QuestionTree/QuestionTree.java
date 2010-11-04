package QuestionTree;

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
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

@SuppressWarnings("serial")
public class QuestionTree extends JScrollPane {
	private JTree tree;
	
	QuestionTreeNode selected;
	
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
		tree = new JTree(new DefaultTreeModel(new QuestionTreeNode(), true));
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setViewportView(tree);
		tree.addMouseListener(new MouseAdapter() {			
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
					if (selPath==null) {
						treePopup.show(tree, e.getX(), e.getY());
					} else {
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
				if (e.getClickCount() == 2) {
					TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
					if (selPath!=null) {
						selected = (QuestionTreeNode) selPath.getLastPathComponent();
						if (selected.isCategory()) {
						/*	textPane.setEditable(false);
							textPane.setText("");
							viewPane.setText("");
							// keine Frage ausgewählt
							if (selectedNodePath.length == 2) {
								textPane.setText(EditorData.getSettingsDialogs(
									selectedNodePath[1]).getSettingsString());
							}*/
							fireCategory((CategoryNode)selected);
						} else {
							/*	textPane.setText(EditorData.getNode(selectedNodePath)
									.getContent().replaceAll("\r\n", "\n"));
								viewPane.setText(EditorData.HTMLSTART + textPane.getText()
									+ EditorData.HTMLEND);
								textPane.setEditable(true); */
							fireQuestion((QuestionNode)selected);
						}
					}
				}
			}
		});
		
		//this.viewPane = viewPane;
		//this.textPane = textPane;
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
					if ((name = JOptionPane.showInputDialog(this, "Name:")) != null) {
						addCategory(name);
					}
				} else
				// newquestion
				if (ae.getActionCommand().equals("newquestion")) {
					if ((name = JOptionPane.showInputDialog(this, "Name:")) != null) {
						addQuestion((CategoryNode)selected, name);
					}
				} else
				// rename
				if (ae.getActionCommand().equals("rename")) {
					if ((name = JOptionPane.showInputDialog(this, "Neuer Name:")) != null) {
						renameNode(selected, name);
					}
				} /*else
				// settings
				if (ae.getActionCommand().equals("settings")) {
					TreePath tp = this.getClosestPathForLocation(x, y);
					DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tp
							.getLastPathComponent();
					SettingsDialog dia = EditorData.getSettingsDialogs(currentNode
							.toString());
					if (dia != null) {
						dia.setVisible(true);
					}
				} else
				//import
				if(ae.getActionCommand().equals("import")) {
					importHTML();
				}*/
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
		/*myMenuItem = new JMenuItem("HTML-Datei importieren");
		myMenuItem.addActionListener(this);
		myMenuItem.setActionCommand("import");
		categoryPopup.add(myMenuItem);*/
		myMenuItem = new JMenuItem("-");
		categoryPopup.add(myMenuItem);
		myMenuItem = new JMenuItem("Löschen");
		myMenuItem.addActionListener(myActionlistener);
		myMenuItem.setActionCommand("remove");
		categoryPopup.add(myMenuItem);
		myMenuItem = new JMenuItem("Umbenennen");
		myMenuItem.addActionListener(myActionlistener);
		myMenuItem.setActionCommand("rename");		
		categoryPopup.add(myMenuItem);
		myMenuItem = new JMenuItem("-");	
		categoryPopup.add(myMenuItem);
		myMenuItem = new JMenuItem("Eigenschaften");
		myMenuItem.addActionListener(myActionlistener);
		myMenuItem.setActionCommand("settings");
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

		/*textPane.setEditable(false);
		textPane.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				// save the content to the right node
				EditorData.getNode(selectedNodePath).setContent(
						textPane.getText());
				viewPane.setText(EditorData.HTMLSTART + textPane.getText()
						+ EditorData.HTMLEND);
			}
		});*/
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
				//((DefaultTreeModel) tree.getModel()).nodeStructureChanged((javax.swing.tree.TreeNode) parent);
				tree.updateUI();
				/*
				// it shouldnt work... but it IS getting the source node (or looks like)
				DefaultMutableTreeNode source = (DefaultMutableTreeNode) getSelectionPath()
						.getLastPathComponent();
				Point p = dtde.getLocation();
				TreePath path = getClosestPathForLocation(p.x, p.y);
				DefaultMutableTreeNode target = (DefaultMutableTreeNode) path
						.getLastPathComponent();
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) source
						.getParent();
				DefaultTreeModel dtm = (DefaultTreeModel) getModel();
				// remove from JTree
				dtm.removeNodeFromParent(source);
				int targetIndex = 0;
				if (target != parent) {
					targetIndex = parent.getIndex(target) + 1;
				}
				// moving from level 3 to 2 to a differend parent
				if (sourcePath.length == 3 && target.getLevel() == 1
						&& target != parent) {
					// remove from data
					EditorData.getDataRoot().getChild(sourcePath[1]).removeChild(
							sourcePath[2]);
					// insert to new position in data
					EditorData.getDataRoot().getChild(target.toString()).addChild(
							new DataTreeNode(sourcePath[2]), 0);
					// inser to new position in JTree
					dtm.insertNodeInto(source, target, targetIndex);
				} else {
					// remove node from data
					if (sourcePath.length == 2) {
						EditorData.getDataRoot().removeChild(sourcePath[1]);
					} else {
						EditorData.getDataRoot().getChild(sourcePath[1]).removeChild(
								sourcePath[2]);
					}
					// new position in data
					if (sourcePath.length == 2) {
						EditorData.getDataRoot().addChild(
								new DataTreeNode(sourcePath[1]), targetIndex);
					} else {
						EditorData.getDataRoot().getChild(sourcePath[1]).addChild(
								new DataTreeNode(sourcePath[2]), targetIndex);
					}
					// insert to new position in JTree
					dtm.insertNodeInto(source, parent, targetIndex);
				}
				*/
			}

			public void dropActionChanged(DropTargetDragEvent arg0) {
			}

			public void dragExit(DropTargetEvent dte) {
			}

			public void dragEnter(DropTargetDragEvent arg0) {
			}
		});

		//tree.setEditable(false);
		tree.setEditable(true);
		tree.setRootVisible(false);
	}

	/**
	 * if you will show a new DataTreeNode (as Root) you should call this
	 * method. It rebuild the JTree so that the new data is shown.
	 */
	/*public void rootUpdated() {
		
		// get Root of the JTree
		TreePath tp = this.getClosestPathForLocation(0, 0);
		DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) tp
				.getLastPathComponent();
		for (DataTreeNode categorie : EditorData.getDataRoot().getChildren()) {
			// insert level 1 nodes (categories)
			DefaultMutableTreeNode cat = new DefaultMutableTreeNode(categorie
					.getName());
			SettingsDialog dia = new SettingsDialog(categorie.getName());

			Boolean[] newSettings = new Boolean[Settings.settings.size()];
			for (ElementAttribute categorieAttribute : categorie
					.getAttributes()) {
				int pos = Settings.settings.indexOf(categorieAttribute
						.getName());
				if (pos != -1 && pos < newSettings.length) {
					newSettings[pos] = categorieAttribute.getContent().equals(
							"true") ? true : false;
				}
			}
			String path = (String) categorie.getAttribute("path").getContent();
			dia.setSettings(path, newSettings);

			EditorData.addSettingsDialog(dia);
			rootNode.add(cat);

			// insert level 3 nodes (childs)
			for (DataTreeNode question : categorie.getChildren()) {
				DefaultMutableTreeNode quest = new DefaultMutableTreeNode(
						question.getName());
				cat.add(quest);
				String con = "";
				// tries to get the content out of the corresponding files of
				// the node
				try {
					Scanner scanner = new Scanner(new File(question.getName()
							+ ".html"));
					while (scanner.hasNextLine()) {
						con += scanner.nextLine() + "\n";
					}
					con = con.substring(EditorData.HTMLSTART.length(), con
							.length()
							- EditorData.HTMLEND.length() - 1);
				} catch (FileNotFoundException e) {
					System.out.println("Quelldatei nicht gefunden");
				}
				question.setContent(con);
			}
		}
	}*/

	/**
	 * this method will rename a level 2 or level 3 node the node is selected by
	 * the last mouse-button release
	 * 
	 * @param nameProposal
	 *            a proposal for the name of the node
	 */
	private boolean renameNode(QuestionTreeNode node, String name) {
		/*String name = getFreeName(nameProposal); // get a free name if it is not
		TreePath tp = this.getClosestPathForLocation(x, y);
		DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tp
				.getLastPathComponent();
		String path = tp.toString();
		path = path.substring(1, path.length() - 1);
		String[] pathElements = path.split(", ");
		// renaming in data
		switch (pathElements.length) {
		case 2: // level 2 (categorie)
			EditorData.getDataRoot().getChild(pathElements[1]).setName(name);
			EditorData.getSettingsDialogs(pathElements[1]).setId(name);
			break;
		case 3: // level 3 (child)
			EditorData.getDataRoot().getChild(pathElements[1]).getChild(
					pathElements[2]).setName(name);
			break;
		}
		// renaming in jtree
		currentNode.setUserObject(name);
		((DefaultTreeModel) this.getModel())
				.nodeStructureChanged((javax.swing.tree.TreeNode) currentNode);*/
		return node.setName(name);
	}

	/**
	 * this method will rename a child the renamed node will be selected after
	 * the last mouse-button release
	 */
	private void removeNode(QuestionTreeNode node) {
		/*TreePath tp = this.getClosestPathForLocation(x, y);
		DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tp
				.getLastPathComponent();
		String path = tp.toString();
		path = path.substring(1, path.length() - 1);
		String[] pathElements = path.split(", ");
		// remove child from data
		switch (pathElements.length) {
		case 2: // level 2...
			EditorData.getDataRoot().removeChild(pathElements[1]);
			EditorData.removeSettingsDialog(pathElements[1]);
			break;
		case 3: // level 3...
			EditorData.getDataRoot().getChild(pathElements[1]).removeChild(
					pathElements[2]);
			break;
		}
		// remove child from JTree
		if (currentNode.getParent() != null) {
			int nodeIndex = currentNode.getParent().getIndex(currentNode);
			currentNode.removeAllChildren();
			((DefaultMutableTreeNode) currentNode.getParent())
					.remove(nodeIndex);
			((DefaultTreeModel) this.getModel())
					.nodeStructureChanged((javax.swing.tree.TreeNode) currentNode);
		}*/
		node.removeFromParent();
	}

	/**
	 * this method will add a Child to the tree the position of the new child
	 * will be selected after the last mouse-button release
	 * 
	 * @param nameProposal
	 *            a name proposal for the new Child
	 */
	private void addCategory(String name) {
		/*String name = getFreeName(nameProposal); // free name if it is not
		TreePath tp = this.getClosestPathForLocation(x, y);
		DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tp
				.getLastPathComponent();
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(name);
		String path = tp.toString();
		path = path.substring(1, path.length() - 1);
		String[] pathElements = path.split(", ");
		// adding the child (Data and JTree)
		switch (pathElements.length) {
		case 1: // new level 2 (categorie)
			EditorData.getDataRoot().addChild(new DataTreeNode(name),
					EditorData.getDataRoot().getChildCount());
			currentNode.add(child);
			((DefaultTreeModel) this.getModel())
					.nodeStructureChanged((javax.swing.tree.TreeNode) currentNode);
			SettingsDialog dia = new SettingsDialog(name);
			EditorData.addSettingsDialog(dia);
			dia.setVisible(true);
			break;
		case 2: // new level 3 (question)
			EditorData.getDataRoot().getChild(pathElements[1]).addChild(
					new DataTreeNode(name),
					EditorData.getDataRoot().getChild(pathElements[1])
							.getChildCount());
			currentNode.add(child);
			((DefaultTreeModel) this.getModel())
					.nodeStructureChanged((TreeNode) currentNode);
			break;
		case 3: // as in case 2 (only different path, so slightly differend
			// code)
			EditorData.getDataRoot().getChild(pathElements[1]).addChild(
					new DataTreeNode(name),
					EditorData.getDataRoot().getChild(pathElements[1])
							.getChildCount());
			((DefaultMutableTreeNode) currentNode.getParent()).add(child);
			((DefaultTreeModel) this.getModel())
					.nodeStructureChanged((TreeNode) currentNode.getParent());
			break;
		}*/
		QuestionTreeNode root = (QuestionTreeNode)tree.getModel().getRoot();
		root.insert(new CategoryNode(name), root.getChildCount());
	}
	
	private void addQuestion(CategoryNode category, String name) {
		/*String name = getFreeName(nameProposal); // free name if it is not
		TreePath tp = this.getClosestPathForLocation(x, y);
		DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tp
				.getLastPathComponent();
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(name);
		String path = tp.toString();
		path = path.substring(1, path.length() - 1);
		String[] pathElements = path.split(", ");
		// adding the child (Data and JTree)
		switch (pathElements.length) {
		case 1: // new level 2 (categorie)
			EditorData.getDataRoot().addChild(new DataTreeNode(name),
					EditorData.getDataRoot().getChildCount());
			currentNode.add(child);
			((DefaultTreeModel) this.getModel())
					.nodeStructureChanged((javax.swing.tree.TreeNode) currentNode);
			SettingsDialog dia = new SettingsDialog(name);
			EditorData.addSettingsDialog(dia);
			dia.setVisible(true);
			break;
		case 2: // new level 3 (question)
			EditorData.getDataRoot().getChild(pathElements[1]).addChild(
					new DataTreeNode(name),
					EditorData.getDataRoot().getChild(pathElements[1])
							.getChildCount());
			currentNode.add(child);
			((DefaultTreeModel) this.getModel())
					.nodeStructureChanged((TreeNode) currentNode);
			break;
		case 3: // as in case 2 (only different path, so slightly differend
			// code)
			EditorData.getDataRoot().getChild(pathElements[1]).addChild(
					new DataTreeNode(name),
					EditorData.getDataRoot().getChild(pathElements[1])
							.getChildCount());
			((DefaultMutableTreeNode) currentNode.getParent()).add(child);
			((DefaultTreeModel) this.getModel())
					.nodeStructureChanged((TreeNode) currentNode.getParent());
			break;
		}*/
		category.insert(new QuestionNode(name),category.getChildCount());
	}

	/*/**
	 * This methode will get you a unique name from the tree it searches after
	 * the nameProposal, if it exists (or is blank) it will add an "_" this
	 * method eliminates all whitespace-characters too
	 * 
	 * @param name
	 *            a proposal for the name
	 * @return unique name
	 */
	/*public String getFreeName(String name) {
		String ret = name.replaceAll(" ", "");
		ret = ret.replaceAll("\t", "");
		if (ret.equals("")) {
			ret += "_";
		}
		while (EditorData.getDataRoot().nameExist(ret)) {
			ret += "_";
		}

		return ret;
	}*/

	/**
	 * this method imports an html file to the categorie most near to the mouse click
	 * if the file doesn't consist of a right body it is not importet
	 * if the file will be importet, it will be tests, if she ends with EditorData.HTMLEnd
	 * if yes the end ist cuted off
	 */
	/*public void importHTML(CategoryNode node) {
		TreePath tp = this.getClosestPathForLocation(x, y);
		DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tp
				.getLastPathComponent();
		String path = tp.toString();
		path = path.substring(1, path.length() - 1);
		String[] pathElements = path.split(", ");
		
		
		if(pathElements.length < 2 || pathElements.length > 3) {
			JOptionPane
			.showMessageDialog(null,
					"HTML-Dateien können nicht als Kategorie importiert werden.");			
		} else {
			JFileChooser fc = new JFileChooser();
			int returnVal = fc.showSaveDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				String htmlContent = "";
				try {
					Scanner sc = new Scanner(file);
					while(sc.hasNextLine()) {
						htmlContent += sc.nextLine();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				int begin = htmlContent.indexOf("<body>")+"<body>".length();
				int end = htmlContent.lastIndexOf("</body>");
				if(begin == -1 || end == -1) {
					JOptionPane.showMessageDialog(null, "Keine Datei mit gültigem Aufbau");
				} else {
					int start = file.toString().lastIndexOf("\\") == -1 ? 0 : file.toString().lastIndexOf("\\")+1;
					String name = getFreeName(file.toString().substring(start, file.toString().lastIndexOf(".")));
					DefaultMutableTreeNode child = new DefaultMutableTreeNode(name);					
					//test if the footer is the same as our html-file standart
					if(htmlContent.endsWith(EditorData.HTMLEND)) {
						htmlContent = htmlContent.substring(begin+6, htmlContent.length()-EditorData.HTMLEND.length());
					} else {
						htmlContent = htmlContent.substring(begin+6, end);
					}
					DataTreeNode dtn = new DataTreeNode(name);
					dtn.setContent(htmlContent);
					
					switch (pathElements.length) {
					case 2: // new level 3 (question)
						EditorData.getDataRoot().getChild(pathElements[1]).addChild(
								dtn,
								EditorData.getDataRoot().getChild(pathElements[1])
										.getChildCount());
						currentNode.add(child);
						((DefaultTreeModel) this.getModel())
								.nodeStructureChanged((TreeNode) currentNode);
						break;
					case 3: // as in case 2 (only different path, so slightly differend
						// code)
						EditorData.getDataRoot().getChild(pathElements[1]).addChild(
								dtn,
								EditorData.getDataRoot().getChild(pathElements[1])
										.getChildCount());
						((DefaultMutableTreeNode) currentNode.getParent()).add(child);
						((DefaultTreeModel) this.getModel())
								.nodeStructureChanged((TreeNode) currentNode.getParent());
						break;
					}
				}
			}
		}
	}*/
	
	public void setRoot(QuestionTreeNode n) {
		tree.setModel(new DefaultTreeModel(n));
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

	private void fireQuestion(QuestionNode question) {
		if (questionTreeListeners == null)
			return;
		QuestionTreeEvent event = new QuestionTreeEvent(this, question);
		for (Enumeration<QuestionTreeListener> e = questionTreeListeners.elements(); e
				.hasMoreElements();)
			((QuestionTreeListener) e.nextElement()).questionTreeEventOccured(event);			
	}
	
	private void fireCategory(CategoryNode category) {
		if (questionTreeListeners == null)
			return;
		QuestionTreeEvent event = new QuestionTreeEvent(this, category);
		for (Enumeration<QuestionTreeListener> e = questionTreeListeners.elements(); e
				.hasMoreElements();)
			((QuestionTreeListener) e.nextElement()).questionTreeEventOccured(event);			
	}
}
