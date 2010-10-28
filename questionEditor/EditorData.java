package questionEditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class EditorData {
	private static ArrayList<SettingsDialog> settingsDialogs = new ArrayList<SettingsDialog>();
	private static TreeNode dataRoot = new TreeNode("Übersicht");

	// Allgemeine reste-Methode
	public static void reset() {
		settingsDialogs.clear();
		dataRoot = null;
		dataRoot = new TreeNode("Übersicht");
	}

	// Für Tree Node
	public static TreeNode getDataRoot() {
		return dataRoot;
	}

	public static TreeNode getNode(String name) {
		for (int i = 0; i < dataRoot.getChildCount(); i++) {
			if (dataRoot.getChild(i).getName().equals(name)) {
				return dataRoot.getChild(i);
			}
			Vector<TreeNode> children = dataRoot.getChild(i).getChildren();
			for (TreeNode child : children) {
				if (child.getName().equals(name)) {
					return child;
				}
			}
		}
		return null;
	}

	public static void extractHTMLContent() {
		for (int i = 0; i < dataRoot.getChildCount(); i++) {
			Vector<TreeNode> children = dataRoot.getChild(i).getChildren();
			for (TreeNode child : children) {
				try {
					File newHTMLFile = new File(child.getName() + ".html");
					FileWriter fw = new FileWriter(newHTMLFile);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(child.getContent());
					bw.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}

	public static TreeNode getNode(String[] pathElements) {
		TreeNode tn = dataRoot;
		for (int i = 1; i < pathElements.length; i++) {
			tn = tn.getChild(pathElements[i]);
		}
		return tn;		
	}
	
	public static Vector<ElementAttribute> getNodeAttributes(
			String[] pathElements) {
		TreeNode tn = dataRoot;
		for (int i = 1; i < pathElements.length; i++) {
			tn = tn.getChild(pathElements[i]);
		}
		return tn.getAttributes();
	}

	// Methoden für SettingsDialogs
	public static void addSettingsDialog(SettingsDialog sd) {
		settingsDialogs.add(sd);
	}

	public static SettingsDialog getSettingsDialogs(String name) {
		for (int i = 0; i < settingsDialogs.size(); i++) {
			if (settingsDialogs.get(i).getId().equals(name)) {
				return settingsDialogs.get(i);
			}
		}
		return null;
	}

	public static boolean removeSettingsDialog(String name) {
		for (int i = 0; i < settingsDialogs.size(); i++) {
			if (settingsDialogs.get(i).getId().equals(name)) {
				settingsDialogs.remove(i);
				return true;
			}
		}
		return false;
	}
}
