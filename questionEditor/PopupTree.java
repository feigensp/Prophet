package questionEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class PopupTree extends JTree implements ActionListener, MouseListener {
	private JPopupMenu treePopup;

	private int x, y;

	//private TreeNode dataRoot;

	private JTextPane textPane;
	private String editableNodePath;

	public PopupTree(DefaultMutableTreeNode root, final JTextPane textPane) {
		super(root);
		
		this.textPane = textPane;
		EditorData.getDataRoot().setName(root.toString());
		// PopupFenster erstellen
		JMenuItem mi;
		treePopup = new JPopupMenu();
		mi = new JMenuItem("Neu");
		mi.addActionListener(this);
		mi.setActionCommand("new");
		treePopup.add(mi);
		mi = new JMenuItem("löschen");
		mi.addActionListener(this);
		mi.setActionCommand("remove");
		treePopup.add(mi);
		mi = new JMenuItem("umbenennen");
		mi.addActionListener(this);
		mi.setActionCommand("rename");
		treePopup.add(mi);
		mi = new JMenuItem("Einstellungen");
		mi.addActionListener(this);
		mi.setActionCommand("settings");
		treePopup.add(mi);

		textPane.setEditable(false);
		textPane.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent ke) {
				if (textPane.isEditable()) {
					String path = editableNodePath;
					String[] pathElements = path.split(", ");
					EditorData.getNode(pathElements).setContent(textPane.getText().replaceAll("\n", "\r\n"));
				}
			}
		});

		this.addMouseListener(this);

		this.setEditable(false);

		x = 0;
		y = 0;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			x = e.getX();
			y = e.getY();
			treePopup.show((JComponent) e.getSource(), e.getX(), e.getY());
		} else {
			TreePath tp = this.getSelectionPath();
			if (tp != null) {
				String path = tp.toString();
				path = path.substring(1, path.length() - 1);
				editableNodePath = path;
				String[] pathElements = path.split(", ");
				if (pathElements.length != 3) {
					// keine Frage ausgewählt
					textPane.setEditable(false);
					textPane.setText("");
				} else {
					textPane.setText(EditorData.getNode(pathElements).getContent().replaceAll("\r\n", "\n"));
					textPane.setEditable(true);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {

		DefaultMutableTreeNode dmtn, node;

		// Knoten des Baums, der in der nähe war
		dmtn = (DefaultMutableTreeNode) this.getClosestPathForLocation(x, y)
				.getLastPathComponent();
		String name;

		// Löschen
		if (ae.getActionCommand().equals("remove")) {
			removeChild();
		} else
		// Neu
		if (ae.getActionCommand().equals("new")) {
			if ((name = JOptionPane.showInputDialog(this, "Name:")) != null) {
				addChild(name);
			}
		} else
		// Umbenennen
		if (ae.getActionCommand().equals("rename")) {
			if ((name = JOptionPane.showInputDialog(this, "Neuer Name:")) != null) {
				renameChild(name);
			}
		} else
		// Einstellungen
		if (ae.getActionCommand().equals("settings")) {
			TreePath tp = this.getClosestPathForLocation(x, y);
			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tp
					.getLastPathComponent();
			SettingsDialog dia = EditorData.getSettingsDialogs(currentNode.toString());
			if(dia!= null) {
				dia.setVisible(true);
			}
		}
		System.out.println(EditorData.getDataRoot().toString());
	}

	private void renameChild(String nameProposal) {
		String name = getFreeName(nameProposal);
		TreePath tp = this.getClosestPathForLocation(x, y);
		DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tp
				.getLastPathComponent();
		String path = tp.toString();
		path = path.substring(1, path.length() - 1);
		String[] pathElements = path.split(", ");
		// TreeNode umbenennen
		switch (pathElements.length) {
		case 2: // Kategorie
			EditorData.getDataRoot().getChild(pathElements[1]).setName(name);
			EditorData.getSettingsDialogs(pathElements[1]).setId(name);
			break;
		case 3: // Frage
			EditorData.getDataRoot().getChild(pathElements[1]).getChild(pathElements[2])
					.setName(name);
			break;
		}
		currentNode.setUserObject(name);
		((DefaultTreeModel) this.getModel())
				.nodeStructureChanged((javax.swing.tree.TreeNode) currentNode);
	}

	private void removeChild() {
		// Benötigte Infos holen
		TreePath tp = this.getClosestPathForLocation(x, y);
		DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tp
				.getLastPathComponent();
		String path = tp.toString();
		path = path.substring(1, path.length() - 1);
		String[] pathElements = path.split(", ");
		// Child aus dem TreeNode löschen
		switch (pathElements.length) {
		case 2: // Es wird eine Fragenkategorie gelöscht
			EditorData.getDataRoot().removeChild(pathElements[1]);
			EditorData.removeSettingsDialog(pathElements[1]);
			break;
		case 3: // Es wird eine Frage gelöscht
			EditorData.getDataRoot().getChild(pathElements[1]).removeChild(pathElements[2]);
			break;
		}
		// Child aus dem JTree löschen
		if (currentNode.getParent() != null) {
			int nodeIndex = currentNode.getParent().getIndex(currentNode);
			currentNode.removeAllChildren();
			((DefaultMutableTreeNode) currentNode.getParent())
					.remove(nodeIndex);
			((DefaultTreeModel) this.getModel())
					.nodeStructureChanged((javax.swing.tree.TreeNode) currentNode);
		}
	}

	private void addChild(String nameProposal) {
		String name = getFreeName(nameProposal);
		TreePath tp = this.getClosestPathForLocation(x, y);
		DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) tp
				.getLastPathComponent();
		DefaultMutableTreeNode child = new DefaultMutableTreeNode(name);
		String path = tp.toString();
		path = path.substring(1, path.length() - 1);
		String[] pathElements = path.split(", ");
		// Je nachdem wo es eingefügt wird angepasstes vorgehen:
		TreeNode tn = new TreeNode(name);
		switch (pathElements.length) {
		case 1: // Es wird eine neue Fragenkategorie hinzugefügt
			EditorData.getDataRoot().addChild(tn);
			currentNode.add(child);
			((DefaultTreeModel) this.getModel())
					.nodeStructureChanged((javax.swing.tree.TreeNode) currentNode);
			EditorData.addSettingsDialog(new SettingsDialog(name));
			break;
		case 2: // Neue Frage
			EditorData.getDataRoot().getChild(pathElements[1]).addChild(tn);
			currentNode.add(child);
			((DefaultTreeModel) this.getModel())
					.nodeStructureChanged((javax.swing.tree.TreeNode) currentNode);
			break;
		case 3: // Auch neue Frage
			EditorData.getDataRoot().getChild(pathElements[1]).addChild(tn);
			((DefaultMutableTreeNode) currentNode.getParent()).add(child);
			((DefaultTreeModel) this.getModel())
					.nodeStructureChanged((javax.swing.tree.TreeNode) currentNode
							.getParent());
			break;
		}
	}

	public String getFreeName(String name) {
		String ret = name.replaceAll(" ", "");
		if(ret.equals("")) {
			ret += "_";
		}
		while (EditorData.getDataRoot().nameExist(ret)) {
			ret += "_";
		}
		
		return ret;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}
}
