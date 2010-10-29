package questionEditor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class EditorData {
	private static ArrayList<SettingsDialog> settingsDialogs = new ArrayList<SettingsDialog>();
	private static DataTreeNode dataRoot = new DataTreeNode("Übersicht");
	
	private static String htmlContent = "";
	
	public static final String HTMLSTART = "<html><head></head><body>";
	public static final String HTMLEND = "<br><br><br><input type='submit' value='Weiter'></body></html>";
	public static final String FAKEHTMLEND = "<br><br><br><input type='submit' value='Weiter' disabled></body></html>";
		
	public static String getHtmlContent() {
		return htmlContent;
	}
	
	public static void setHtmlContent(String content) {
		htmlContent = content;
	}

	// Allgemeine reste-Methode
	public static void reset() {
		settingsDialogs.clear();
		dataRoot = null;
		dataRoot = new DataTreeNode("Übersicht");
	}

	// Für Tree Node
	public static DataTreeNode getDataRoot() {
		return dataRoot;
	}
	
	public static void setDataRoot(DataTreeNode newRoot) {
		dataRoot = newRoot;
		System.out.println("Neue Wurzel: " + dataRoot);
	}

	public static DataTreeNode getNode(String name) {
		for (int i = 0; i < dataRoot.getChildCount(); i++) {
			if (dataRoot.getChild(i).getName().equals(name)) {
				return dataRoot.getChild(i);
			}
			Vector<DataTreeNode> children = dataRoot.getChild(i).getChildren();
			for (DataTreeNode child : children) {
				if (child.getName().equals(name)) {
					return child;
				}
			}
		}
		return null;
	}

	public static void extractHTMLContent() {
		for (int i = 0; i < dataRoot.getChildCount(); i++) {
			Vector<DataTreeNode> children = dataRoot.getChild(i).getChildren();
			for (DataTreeNode child : children) {
				String content = HTMLSTART + child.getContent() + HTMLEND;
				try {
					File newHTMLFile = new File(child.getName() + ".html");
					FileWriter fw = new FileWriter(newHTMLFile);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(content);
					bw.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}

	public static DataTreeNode getNode(String[] pathElements) {
		DataTreeNode tn = dataRoot;
		for (int i = 1; i < pathElements.length; i++) {
			tn = tn.getChild(pathElements[i]);
		}
		return tn;		
	}
	
	public static Vector<ElementAttribute> getNodeAttributes(
			String[] pathElements) {
		DataTreeNode tn = dataRoot;
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
