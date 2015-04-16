package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins;

import java.awt.BorderLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.Plugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin
        .RecorderPlugin;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.recorderPlugin.recordEntries.SearchEntry;
import de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.util.searchBar.GlobalSearchBar;
import de.uni_passau.fim.infosun.prophet.util.searchBar.SearchBar;
import de.uni_passau.fim.infosun.prophet.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.util.settings.SettingsList;
import de.uni_passau.fim.infosun.prophet.util.settings.components.CheckBoxSetting;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

/**
 * A <code>Plugin</code> that will enable showing <code>SearchBar</code>s for <code>EditorPanel</code>s opened in the
 * <code>CodeViewer</code>. <br> <br>
 * <center>
 *  <table border="1">
 *     <caption>Key Bindings</caption>
 *     <tr>
 *         <th>Key Combination</th>
 *         <th>Function</th>
 *     </tr>
 *     <tr>
 *         <td>CTRL + F</td>
 *         <td>Show SearchBar</td>
 *     </tr>
 *	   <tr>
 *		   <td>CTRL + H</td>
 *		   <td>Show global SearchBar</td>
 *	   </tr>
 * 	</table>
 * </center>
 */
public class SearchBarPlugin implements Plugin {

    public static final String KEY = "searchable";
    public static final String KEY_DISABLE_REGEX = "disableregex";
    public static final String KEY_ENABLE_GLOBAL = "enableglobal";

    public static final String TYPE_SEARCH = "search";
    public static final String ATTRIBUTE_ACTION = "action";
    public static final String ATTRIBUTE_QUERY = "query";
    public static final String ATTRIBUTE_SUCCESS = "success";

	private RecorderPlugin recorder;
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
        SettingsList settingsList = new SettingsList(attribute, getClass().getSimpleName(), true);
        settingsList.setCaption(UIElementNames.getLocalized("SEARCH_BAR_ENABLE_SEARCH"));

        Attribute subAttribute = attribute.getSubAttribute(KEY_DISABLE_REGEX);
        Setting subSetting = new CheckBoxSetting(subAttribute, null);
        subSetting.setCaption(UIElementNames.getLocalized("SEARCH_BAR_DEACTIVATE_REGULAR_EXPRESSIONS"));
        settingsList.addSetting(subSetting);

        subAttribute = attribute.getSubAttribute(KEY_ENABLE_GLOBAL);
        subSetting = new CheckBoxSetting(subAttribute, null);
        subSetting.setCaption(UIElementNames.getLocalized("SEARCH_BAR_ACTIVATE_GLOBAL_SEARCH"));
        settingsList.addSetting(subSetting);

        return settingsList;
    }

	@Override
	public void onCreate(CodeViewer codeViewer) {
		Attribute vAttr = codeViewer.getAttribute();
		this.enabled = vAttr.containsSubAttribute(KEY) && Boolean.parseBoolean(vAttr.getSubAttribute(KEY).getValue());

		if (!enabled) {
			return;
		}

		Attribute pluginAttr = vAttr.getSubAttribute(KEY);

		this.recorder = codeViewer.getRecorder();
		this.regexDisabled = Boolean.parseBoolean(pluginAttr.getSubAttribute(KEY_DISABLE_REGEX).getValue());

		JMenuItem findMenuItem = new JMenuItem(UIElementNames.getLocalized("SEARCH_BAR_MENU_SEARCH"));
		findMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		findMenuItem.addActionListener(e -> {
			SearchBar sBar = searchBars.get(codeViewer.getTabbedPane().getSelectedComponent());

			if (sBar != null) {
				sBar.setVisible(true);
				sBar.grabFocus();
			}
		});

		codeViewer.addMenuItemToEditMenu(findMenuItem);

		if (Boolean.parseBoolean(pluginAttr.getSubAttribute(KEY_ENABLE_GLOBAL).getValue())) {
			globalSearchBar = new GlobalSearchBar(codeViewer);
			globalSearchBar.setVisible(false);

			globalSearchBar.addSearchBarListener((action, query, success) ->
					recorder.record(codeViewer, new SearchEntry(action, query, success)));

			if (regexDisabled) {
				globalSearchBar.getRegexCB().setVisible(false);
			}

			codeViewer.add(globalSearchBar, BorderLayout.SOUTH);

			JMenuItem findGlobalMenuItem = new JMenuItem(UIElementNames.getLocalized("SEARCH_BAR_MENU_GLOBAL_SEARCH"));
			findGlobalMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK));
			findGlobalMenuItem.addActionListener(event -> {
				globalSearchBar.setVisible(true);
				globalSearchBar.grabFocus();
			});

			codeViewer.addMenuItemToEditMenu(findGlobalMenuItem);
		}
	}

	@Override
	public void onEditorPanelCreate(CodeViewer codeViewer, EditorPanel editorPanel) {

		if (!enabled) {
			return;
		}

		RSyntaxTextArea textPane = editorPanel.getTextArea();
		SearchBar searchBar = new SearchBar(textPane);
		searchBar.setVisible(false);

		searchBar.addSearchBarListener((action, query, success) ->
				recorder.record(codeViewer, new SearchEntry(action, query, success)));

		if (regexDisabled) {
			searchBar.getRegexCB().setVisible(false);
		}

		editorPanel.add(searchBar, BorderLayout.SOUTH);
		searchBars.put(editorPanel, searchBar);
	}

    @Override
    public void onClose(CodeViewer codeViewer) {
		enabled = false;
		regexDisabled = false;

		searchBars.clear();

		recorder = null;
		globalSearchBar = null;
    }

    @Override
    public void onEditorPanelClose(CodeViewer codeViewer, EditorPanel editorPanel) {

		if (enabled) {
            searchBars.remove(editorPanel);
        }
    }
}
