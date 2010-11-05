package questionViewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class QuestionViewTest extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuestionViewTest frame = new QuestionViewTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public QuestionViewTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel timeshowing = new JPanel();
		panel.add(timeshowing, BorderLayout.SOUTH);

		CategorieQuestionListsPanel overview = new CategorieQuestionListsPanel();
		overview.setPreferredSize(new Dimension(150, 2));
		panel.add(overview, BorderLayout.WEST);
		
		HTMLFileView textPane = new HTMLFileView("myTest.xml", overview);
		panel.add(textPane, BorderLayout.CENTER);
		
	}

}
