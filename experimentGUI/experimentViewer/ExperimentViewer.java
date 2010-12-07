package experimentGUI.experimentViewer;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import experimentGUI.PluginInterface;
import experimentGUI.PluginList;
import experimentGUI.util.questionTreeNode.QuestionTreeNode;
import experimentGUI.util.questionTreeNode.QuestionTreeXMLHandler;


public class ExperimentViewer extends JFrame {
	private QuestionTreeNode tree;
	private JPanel contentPane;
	
	public static final String CONF_FILE_PATH = "questionnaire.conf";

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
		FileReader fr;
		String questionnairePath = null;
		try {
			fr = new FileReader(CONF_FILE_PATH);
		    BufferedReader br = new BufferedReader(fr);
		    questionnairePath  = br.readLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(CONF_FILE_PATH+" nicht gefunden");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Fehler beim lesen von "+CONF_FILE_PATH);
		}

		if(questionnairePath != null) {	
			tree = QuestionTreeXMLHandler.loadXMLTree(questionnairePath);	

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
			this.setSize(800, 600);	
		} else {
			System.out.println("Fehler beim laden des Fragebogens.");
		}
	}
	public QuestionTreeNode getTree() {
		return tree;
	}
}
