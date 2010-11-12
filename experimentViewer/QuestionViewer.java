package experimentViewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import util.QuestionTreeNode;
import util.XMLTreeHandler;
import experimentViewer.IconCellRenderer.ListIcons;

public class QuestionViewer extends JFrame {
	QuestionTreeNode experimentTree;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuestionViewer frame = new QuestionViewer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public QuestionViewer() {
		experimentTree = XMLTreeHandler.loadXMLTree("myTest.xml");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel timeshowing = new JPanel();
		contentPane.add(timeshowing, BorderLayout.SOUTH);

		QuestionListPanel overview = new QuestionListPanel(experimentTree);
		overview.setPreferredSize(new Dimension(150, 2));
		contentPane.add(overview, BorderLayout.WEST);		
		
		HTMLFileView textPane = new HTMLFileView("myTest.xml", overview);
		contentPane.add(textPane, BorderLayout.CENTER);		
	}
}
