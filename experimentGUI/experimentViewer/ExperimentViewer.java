package experimentGUI.experimentViewer;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import experimentGUI.PluginInterface;
import experimentGUI.PluginList;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.questionTreeNode.QuestionTreeXMLHandler;


public class ExperimentViewer extends JFrame {
	QuestionTreeNode tree;
	JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String laf = UIManager.getSystemLookAndFeelClassName();
					UIManager.setLookAndFeel(laf);
					ExperimentViewer frame = new ExperimentViewer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ExperimentViewer() {
		tree = QuestionTreeXMLHandler.loadXMLTree("test.xml");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());	
		
		HTMLFileView textPane = new HTMLFileView(tree);
		contentPane.add(textPane, BorderLayout.CENTER);
		
		for (PluginInterface plugin : PluginList.getPlugins()) {
			plugin.experimentViewerRun(this);
		}
		textPane.start();
		pack();
	}
	public QuestionTreeNode getTree() {
		return tree;
	}
}
