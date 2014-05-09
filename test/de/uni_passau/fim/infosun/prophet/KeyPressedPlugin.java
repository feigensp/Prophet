package de.uni_passau.fim.infosun.prophet;

import java.awt.event.ActionEvent;
import javax.swing.*;

import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.Plugin;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTree.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsComponentDescription;

public class KeyPressedPlugin implements Plugin {

    @Override
    public SettingsComponentDescription getSettings(QuestionTreeNode node) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void experimentViewerRun(ExperimentViewer experimentViewer) {
        JPanel contentPanel = experimentViewer.getContentPanel();
        Action xAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "X pressed!");
            }
        };
        contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("X"), "xAction");
        contentPanel.getActionMap().put("xAction", xAction);
    }

    @Override
    public boolean denyEnterNode(QuestionTreeNode node) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void enterNode(QuestionTreeNode node) {
        // TODO Auto-generated method stub

    }

    @Override
    public String denyNextNode(QuestionTreeNode currentNode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void exitNode(QuestionTreeNode node) {
        // TODO Auto-generated method stub

    }

    @Override
    public String finishExperiment() {
        // TODO Auto-generated method stub
        return null;
    }
}
