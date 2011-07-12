package experimentGUI.plugins.codeViewerPlugin;

import java.util.Vector;

import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.EditAndSavePlugin;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.LineNumbersPlugin;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.OpenedFromStartPlugin;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.SearchBarPlugin;
import experimentGUI.plugins.codeViewerPlugin.codeViewerPlugins.SyntaxHighlightingPlugin;
import experimentGUI.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.settingsComponents.SettingsComponentDescription;


public class CodeViewerPluginList {
	private static Vector<CodeViewerPluginInterface> plugins = new Vector<CodeViewerPluginInterface>() {
		private static final long serialVersionUID = 1L;
		{
			add(new EditAndSavePlugin());
			add(new LineNumbersPlugin());
			add(new SearchBarPlugin());
			add(new SyntaxHighlightingPlugin());
//			add(new ShowCIDECodePlugin());
			add(new OpenedFromStartPlugin());
		}
	};
	
//	public static Vector<CodeViewerPluginInterface> getPlugins() {
//		return plugins;
//	}
	public static void add(CodeViewerPluginInterface plugin) {
		plugins.add(plugin);
	}
	public static boolean remove(CodeViewerPluginInterface plugin) {
		return plugins.remove(plugin);
	}

	public static SettingsComponentDescription getSettingsComponentDescription() {
		SettingsComponentDescription result = null;
		for (CodeViewerPluginInterface plugin : plugins) {
			try {
				SettingsComponentDescription desc = plugin.getSettingsComponentDescription();
				if (desc!=null) {
					if (result==null) {
						result=desc;
					} else {
						result.addNextComponent(desc);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		return result;
	}

	public static void init(QuestionTreeNode selected) {
		for (CodeViewerPluginInterface plugin : plugins) {
			try {
				plugin.init(selected);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void onFrameCreate(CodeViewer viewer) {
		for (CodeViewerPluginInterface plugin : plugins) {
			try {
				plugin.onFrameCreate(viewer);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void onEditorPanelCreate(EditorPanel editorPanel) {
		for (CodeViewerPluginInterface plugin : plugins) {
			try {
				plugin.onEditorPanelCreate(editorPanel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void onEditorPanelClose(EditorPanel editorPanel) {
		for (CodeViewerPluginInterface plugin : plugins) {
			try {
				plugin.onEditorPanelClose(editorPanel);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void onClose() {
		for (CodeViewerPluginInterface plugin : plugins) {
			try {
				plugin.onClose();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
