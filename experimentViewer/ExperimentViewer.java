package experimentViewer;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import plugins.ExperimentPlugin;
import plugins.ExperimentPluginList;
import util.QuestionTreeNode;
import util.XMLTreeHandler;

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
					ExperimentViewer frame = new ExperimentViewer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ExperimentViewer() {
		tree = XMLTreeHandler.loadXMLTree("test.xml");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel timeshowing = new JPanel();
		contentPane.add(timeshowing, BorderLayout.SOUTH);		
		
		HTMLFileView textPane = new HTMLFileView(tree);
		contentPane.add(textPane, BorderLayout.CENTER);
		
		for (ExperimentPlugin plugin : ExperimentPluginList.getPlugins()) {
			plugin.experimentViewerRun(this);
		}
		textPane.start();
	}
	public QuestionTreeNode getTree() {
		return tree;
	}
}
