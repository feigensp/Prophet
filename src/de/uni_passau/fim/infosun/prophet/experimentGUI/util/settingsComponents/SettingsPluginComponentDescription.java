package de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents;

import java.util.Vector;

import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTreeNode.QuestionTreeNode;

public class SettingsPluginComponentDescription extends SettingsComponentDescription {

    private Vector<SettingsComponentDescription> subComponents = new Vector<SettingsComponentDescription>();

    private boolean enableable;

    public SettingsPluginComponentDescription(String key, String caption, boolean enableable) {
        super(SettingsPluginComponent.class, key, caption);
        this.enableable = enableable;
    }

    public void addSubComponent(SettingsComponentDescription descr) {
        subComponents.add(descr);
    }

    public SettingsComponent build(QuestionTreeNode treeNode) {
        SettingsPluginComponent result = (SettingsPluginComponent) super.build(treeNode);
        if (!enableable) {
            result.notEnableable();
        }
        QuestionTreeNode myNode = treeNode.getAddAttribute(getKey());
        for (SettingsComponentDescription desc : subComponents) {
            result.addComponent(desc.build(myNode));
        }
        return result;
    }
}
