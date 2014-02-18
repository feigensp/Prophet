package de.uni_passau.fim.infosun.prophet;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import de.uni_passau.fim.infosun.prophet.experimentGUI.PluginInterface;
import de.uni_passau.fim.infosun.prophet.experimentGUI.experimentViewer.ExperimentViewer;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.questionTreeNode.QuestionTreeNode;
import de.uni_passau.fim.infosun.prophet.experimentGUI.util.settingsComponents.SettingsComponentDescription;

public class KeyPressedPlugin implements PluginInterface {

    @Override
    public SettingsComponentDescription getSettingsComponentDescription(QuestionTreeNode node) {
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
