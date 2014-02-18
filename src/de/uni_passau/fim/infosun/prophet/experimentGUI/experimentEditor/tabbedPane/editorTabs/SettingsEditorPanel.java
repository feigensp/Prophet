package de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.editorTabs;

/**
 * A Dialog to adjust some settings.
 * One is a path an the others are predefined (Settings.java) and could be true oder false
 *
 * @author Markus K�ppen, Andreas Hasselberg
 */

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import de.uni_passau.fim.infosun.prophet.experimentGUI.Constants;
import de.uni_passau.fim.infosun.prophet.experimentGUI.PluginList;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTab;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentEditor.tabbedPane.ExperimentEditorTabbedPane;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.VerticalLayout;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.UIElementNames;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTreeNode.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsComponent;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsComponentDescription;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.components.SettingsCheckBox;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.components.SettingsTextField;

/**
 * A tab for changing settings for nodes. Settings are defined by plugins, some are hard-coded.
 *
 * @author Administrator
 */
@SuppressWarnings("serial")
public class SettingsEditorPanel extends ExperimentEditorTab {

    private HashMap<QuestionTreeNode, JScrollPane> scrollPanes = new HashMap<>();
    private HashMap<QuestionTreeNode, ArrayList<SettingsComponent>> settingsComponents = new HashMap<>();
    private QuestionTreeNode selected;

    /**
     * Constructor
     */
    public SettingsEditorPanel() {
        setLayout(new BorderLayout());
        this.setOpaque(false);
    }

    /**
     * Loads the settings and saved options for the specified node into the tab, called by EditorTabbedPane
     */
    public void activate(QuestionTreeNode selectedNode) {

        if (selectedNode == null) {
            return;
        }

        selected = selectedNode;
        this.removeAll();
        this.updateUI();

        JScrollPane scrollPane = scrollPanes.get(selected);

        if (scrollPane == null) {
            scrollPane = buildOptionScrollPane();
            scrollPanes.put(selected, scrollPane);
        }

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Builds a <code>JScrollPane</code> containing all the <code>SettingsComponentDescription</code> objects
     * appropriate for the currently selected node.
     *
     * @return the JScrollPane containing all options
     */
    private JScrollPane buildOptionScrollPane() {
        JScrollPane scrollPane;
        JPanel settingsPanel;

        settingsPanel = new JPanel();
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        settingsPanel.setLayout(new VerticalLayout(5, VerticalLayout.LEFT, VerticalLayout.TOP));

        scrollPane = new JScrollPane(settingsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // add the standard options for experiments and categories
        if (selected.isExperiment()) {
            settingsPanel.add(new SettingsComponentDescription(SettingsTextField.class, Constants.KEY_EXPERIMENT_CODE,
                    UIElementNames.EXPERIMENT_CODE + ": ").build(selected));
        }

        if (selected.isCategory()) {
            settingsPanel.add(new SettingsComponentDescription(SettingsCheckBox.class, Constants.KEY_DONOTSHOWCONTENT,
                    UIElementNames.MENU_TAB_SETTINGS_DONT_SHOW_CONTENT).build(selected));
            settingsPanel.add(new SettingsComponentDescription(SettingsCheckBox.class, Constants.KEY_QUESTIONSWITCHING,
                    UIElementNames.MENU_TAB_SETTINGS_ALLOW_BACK_AND_FORTH).build(selected));
        }

        // add the options contributed by plugins
        SettingsComponentDescription desc = PluginList.getSettingsComponentDescription(selected);

        if (desc != null) {
            settingsComponents.put(selected, new ArrayList<SettingsComponent>());

            do {
                SettingsComponent c = desc.build(selected);
                settingsComponents.get(selected).add(c);
                settingsPanel.add(c);
            } while ((desc = desc.getNextComponentDescription()) != null);
        }

        ExperimentEditorTabbedPane.recursiveSetOpaque(scrollPane);

        return scrollPane;
    }

    /**
     * saves all changes made, called by EditorTabbedPane
     */
    @Override
    public void save() {
        if (selected != null) {
            ArrayList<SettingsComponent> comps = settingsComponents.get(selected);
            if (comps != null) {
                for (SettingsComponent c : comps) {
                    c.saveValue();
                }
            }
        }
    }
}
