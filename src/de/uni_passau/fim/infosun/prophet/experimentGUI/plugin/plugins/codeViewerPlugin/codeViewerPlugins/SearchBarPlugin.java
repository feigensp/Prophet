package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.codeViewerPlugins;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.CodeViewerPlugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.recorder.loggingTreeNode
        .LoggingTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.codeViewerPlugin.tabbedPane.EditorPanel;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree.Attribute;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.searchBar.GlobalSearchBar;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.searchBar.SearchBar;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.PluginSettings;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.Setting;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settings.components.SettingsCheckBox;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

public class SearchBarPlugin implements CodeViewerPlugin {

    public final static String KEY = "searchable";
    public final static String KEY_DISABLE_REGEX = "disableregex";
    public final static String KEY_ENABLE_GLOBAL = "enableglobal";

    public final static String TYPE_SEARCH = "search";
    public final static String ATTRIBUTE_ACTION = "action";
    public final static String ATTRIBUTE_QUERY = "query";
    public final static String ATTRIBUTE_SUCCESS = "success";
    private CodeViewer viewer;

    private Attribute selected;
    private boolean enabled;

    private Map<EditorPanel, SearchBar> map;
    private GlobalSearchBar globalSearchBar;

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
    public void init(Attribute selected) {
        this.selected = selected;
        this.enabled = Boolean.parseBoolean(selected.getSubAttribute(KEY).getValue());
    }

    @Override
    public void onFrameCreate(CodeViewer v) {
        viewer = v;
        if (enabled) {
            map = new HashMap<>();
            JMenuItem findMenuItem = new JMenuItem(UIElementNames.getLocalized("SEARCH_BAR_MENU_SEARCH"));
            findMenuItem.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.Event.CTRL_MASK));
            findMenuItem.addActionListener(e -> {
                SearchBar curr = map.get(viewer.getTabbedPane().getSelectedComponent());
                if (curr != null) {
                    curr.setVisible(true);
                    curr.grabFocus();
                }
            });
            viewer.addMenuItemToEditMenu(findMenuItem);

            boolean activateGlobal =
                    Boolean.parseBoolean(selected.getSubAttribute(KEY).getSubAttribute(KEY_ENABLE_GLOBAL).getValue());
            if (activateGlobal) {
                globalSearchBar = new GlobalSearchBar(viewer.getShowDir(), v);
                globalSearchBar.setVisible(false);

                globalSearchBar.addSearchBarListener((action, query, success) -> {
                    LoggingTreeNode node = new LoggingTreeNode(TYPE_SEARCH);
                    node.setAttribute(ATTRIBUTE_ACTION, action);
                    node.setAttribute(ATTRIBUTE_QUERY, query);
                    node.setAttribute(ATTRIBUTE_SUCCESS, "" + success);
                    viewer.getRecorder().addLoggingTreeNode(node);
                });

                if (Boolean.parseBoolean(selected.getSubAttribute(KEY).getSubAttribute(KEY_DISABLE_REGEX).getValue())) {
                    globalSearchBar.getRegexCB().setVisible(false);
                }

                viewer.add(globalSearchBar, BorderLayout.SOUTH);

                JMenuItem findGlobalMenuItem = new JMenuItem(UIElementNames.getLocalized(
                        "SEARCH_BAR_MENU_GLOBAL_SEARCH"));
                findGlobalMenuItem
                        .setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.Event.CTRL_MASK));
                findGlobalMenuItem.addActionListener(event -> {
                    globalSearchBar.setVisible(true);
                    globalSearchBar.grabFocus();
                });
                viewer.addMenuItemToEditMenu(findGlobalMenuItem);
            }
        }
    }

    @Override
    public void onEditorPanelCreate(EditorPanel editorPanel) {
        if (enabled) {
            RSyntaxTextArea textPane = editorPanel.getTextArea();
            SearchBar searchBar = new SearchBar(textPane);
            searchBar.setVisible(false);
            searchBar.addSearchBarListener((action, query, success) -> {
                LoggingTreeNode node = new LoggingTreeNode(TYPE_SEARCH);
                node.setAttribute(ATTRIBUTE_ACTION, action);
                node.setAttribute(ATTRIBUTE_QUERY, query);
                node.setAttribute(ATTRIBUTE_SUCCESS, "" + success);
                viewer.getRecorder().addLoggingTreeNode(node);
            });

            if (Boolean.parseBoolean(selected.getSubAttribute(KEY).getSubAttribute(KEY_DISABLE_REGEX).getValue())) {
                searchBar.getRegexCB().setVisible(false);
            }

            editorPanel.add(searchBar, BorderLayout.SOUTH);
            map.put(editorPanel, searchBar);
        }
    }

    @Override
    public void onClose() {
    }

    @Override
    public void onEditorPanelClose(EditorPanel editorPanel) {
        if (enabled) {
            map.remove(editorPanel);
        }
    }
}
