package questionEditor;

/**
 * This Class a a pure Data store
 * Other classes like PopupTree and SettingsDialog will interact through this Data
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class EditorData {
	
	private static ArrayList<SettingsDialog> settingsDialogs = new ArrayList<SettingsDialog>();
	private static DataTreeNode dataRoot = new DataTreeNode("Übersicht");
	
	private static String htmlContent = "";	//a little String storage
	
	//Constants for creating Html header and footer
	public static final String HTMLSTART = "<html><head></head><body>";
	public static final String HTMLEND = "<br><br><br><input type='submit' value='Weiter'></body></html>";
	public static final String FAKEHTMLEND = "<br><br><br><input type='submit' value='Weiter' disabled></body></html>";

	/**
	 * this method will reset all Data
	 */
	public static void reset() {
		settingsDialogs.clear();
		htmlContent = "";
		dataRoot = null;
		dataRoot = new DataTreeNode("Übersicht");
	}
	
	/**
	 * will return the string storage
	 * @return stringStorage
	 */
	public static String getHtmlContent() {
		return htmlContent;
	}
	
	/**
	 * will set the string storage a new value
	 * @param content new value
	 */
	public static void setHtmlContent(String content) {
		htmlContent = content;
	}

	/**
	 * this Method will get you the root from the DataTree
	 * @return DataTreeNode-root
	 */
	public static DataTreeNode getDataRoot() {
		return dataRoot;
	}
	
	/**
	 * this method will set a new DataTree
	 * @param newRoot the new root as so the new DataTreeNode-Tree
	 */
	public static void setDataRoot(DataTreeNode newRoot) {
		dataRoot = newRoot;
	}

	/**
	 * this methode will return you a Node out of the Data tree by the string identifier
	 * @param name string identifier from the node
	 * @return first DataTreeNode with the string as identifier 
	 */
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

	/**
	 * will get you the node by path
	 * @param pathElements the path as string array - the path determinated by the name of the nodes
	 * @return DataTreeNode which matched the path
	 */
	public static DataTreeNode getNode(String[] pathElements) {
		DataTreeNode tn = dataRoot;
		for (int i = 1; i < pathElements.length; i++) {
			tn = tn.getChild(pathElements[i]);
		}
		return tn;		
	}

	/**
	 * will write the content of every lvl 3 node to a own html-file
	 */
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

	/**
	 * adds an settingDialog
	 * @param sd settingDialog which should be added
	 */
	public static void addSettingsDialog(SettingsDialog sd) {
		settingsDialogs.add(sd);
	}

	/**
	 * Gets a settingDialog by the String identifier
	 * @param name stringIdentifier of the settingDialog
	 * @return settingDialog with name as string identifier (null if not found)
	 */
	public static SettingsDialog getSettingsDialogs(String name) {
		for (int i = 0; i < settingsDialogs.size(); i++) {
			if (settingsDialogs.get(i).getId().equals(name)) {
				return settingsDialogs.get(i);
			}
		}
		return null;
	}

	/**
	 * deletes the first settingsDialog with the stringIdentifier name
	 * @param name string identifier of the settingsDialog which should be deleted
	 * @return true if found, false if not
	 */
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
