package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.Plugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.Recorder;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.recorder.loggingTree.LoggingTreeNode;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.searchBar.GlobalSearchBar;
import de.uni_passau.fim.infosun.prophet.util.searchBar.SearchBar;
import de.uni_passau.fim.infosun.prophet.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.components.SettingsCheckBox;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class SearchBarPlugin implements Plugin {

    public final static String KEY = "searchable";
    public final static String KEY_DISABLE_REGEX = "disableregex";
    public final static String KEY_ENABLE_GLOBAL = "enableglobal";

    public final static String TYPE_SEARCH = "search";
    public final static String ATTRIBUTE_ACTION = "action";
    public final static String ATTRIBUTE_QUERY = "query";
    public final static String ATTRIBUTE_SUCCESS = "success";

	private Recorder recorder;
    private boolean enabled;
	private boolean regexDisabled;

    private Map<EditorPanel, SearchBar> searchBars;
    private GlobalSearchBar globalSearchBar;

	/**
	 * Constructs a new <code>SearchBarPlugin</code>.
	 */
	public SearchBarPlugin() {
		this.searchBars = new HashMap<>();
	}

    @Override
    public Setting getSetting(Attribute mainAttribute) {

        Attribute attribute = mainAttribute.getSubAttribute(KEY);
        PluginSettings pluginSettings = new PluginSettings(attribute, getClass().getSimpleName(), true);
        pluginSettings.setCaption(UIElementNames.getLocalized("SEARCH_BAR_ENABLE_SEARCH"));

        Attribute subAttribute = attribute.getSubAttribute(KEY_DISABLE_REGEX);
        Setting subSetting = new SettingsCheckBox(subAttribute, null);
        subSetting.setCaption(UIElementNames.getLocalized("SEARCH_BAR_DEACTIVATE_REGULAR_EXPRESSIONS"));
        pluginSettings.addSetting(subSetting);

        subAttribute = attribute.getSubAttribute(KEY_ENABLE_GLOBAL);
        subSetting = new SettingsCheckBox(subAttribute, null);
        subSetting.setCaption(UIElementNames.getLocalized("SEARCH_BAR_ACTIVATE_GLOBAL_SEARCH"));
        pluginSettings.addSetting(subSetting);

        return pluginSettings;
    }

	@Override
	public void onCreate(CodeViewer viewer) {
		Attribute attr = viewer.getAttribute();

		this.enabled = attr.containsSubAttribute(KEY) && Boolean.parseBoolean(attr.getSubAttribute(KEY).getValue());

		if (!enabled) {
			return;
		}

		this.recorder = viewer.getRecorder();
		this.regexDisabled = Boolean.parseBoolean(
				attr.getSubAttribute(KEY).getSubAttribute(KEY_DISABLE_REGEX).getValue());

		JMenuItem findMenuItem = new JMenuItem(UIElementNames.getLocalized("SEARCH_BAR_MENU_SEARCH"));
		findMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.Event.CTRL_MASK));
		findMenuItem.addActionListener(e -> {
			SearchBar curr = searchBars.get(viewer.getTabbedPane().getSelectedComponent());
			if (curr != null) {
				curr.setVisible(true);
				curr.grabFocus();
			}
		});
		viewer.addMenuItemToEditMenu(findMenuItem);

		boolean activateGlobal =
				Boolean.parseBoolean(attr.getSubAttribute(KEY).getSubAttribute(KEY_ENABLE_GLOBAL).getValue());
		if (activateGlobal) {
			globalSearchBar = new GlobalSearchBar(viewer);
			globalSearchBar.setVisible(false);

			globalSearchBar.addSearchBarListener((action, query, success) -> {
				LoggingTreeNode node = new LoggingTreeNode(TYPE_SEARCH);
				node.setAttribute(ATTRIBUTE_ACTION, action);
				node.setAttribute(ATTRIBUTE_QUERY, query);
				node.setAttribute(ATTRIBUTE_SUCCESS, "" + success);
				viewer.getRecorder().addLoggingTreeNode(node);
			});

			if (Boolean.parseBoolean(attr.getSubAttribute(KEY).getSubAttribute(KEY_DISABLE_REGEX).getValue())) {
				globalSearchBar.getRegexCB().setVisible(false);
			}

			viewer.add(globalSearchBar, BorderLayout.SOUTH);

			JMenuItem findGlobalMenuItem = new JMenuItem(UIElementNames.getLocalized("SEARCH_BAR_MENU_GLOBAL_SEARCH"));
			findGlobalMenuItem
					.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.Event.CTRL_MASK));
			findGlobalMenuItem.addActionListener(event -> {
				globalSearchBar.setVisible(true);
				globalSearchBar.grabFocus();
			});
			viewer.addMenuItemToEditMenu(findGlobalMenuItem);
		}
	}

	@Override
	public void onEditorPanelCreate(EditorPanel editorPanel) {

		if (!enabled) {
			return;
		}

		RSyntaxTextArea textPane = editorPanel.getTextArea();
		SearchBar searchBar = new SearchBar(textPane);
		searchBar.setVisible(false);
		searchBar.addSearchBarListener((action, query, success) -> {
			LoggingTreeNode node = new LoggingTreeNode(TYPE_SEARCH);
			node.setAttribute(ATTRIBUTE_ACTION, action);
			node.setAttribute(ATTRIBUTE_QUERY, query);
			node.setAttribute(ATTRIBUTE_SUCCESS, "" + success);
			recorder.addLoggingTreeNode(node);
		});

		if (regexDisabled) {
			searchBar.getRegexCB().setVisible(false);
		}

		editorPanel.add(searchBar, BorderLayout.SOUTH);
		searchBars.put(editorPanel, searchBar);
	}

    @Override
    public void onClose() {
		enabled = false;
		regexDisabled = false;

		searchBars.clear();

		recorder = null;
		globalSearchBar = null;
    }

    @Override
    public void onEditorPanelClose(EditorPanel editorPanel) {

		if (enabled) {
            searchBars.remove(editorPanel);
        }
    }
}
