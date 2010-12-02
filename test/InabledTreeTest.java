package test;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultTreeCellRenderer;

public class InabledTreeTest extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTree tree;
	private DefaultTreeCellRenderer renderer;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String laf = UIManager.getSystemLookAndFeelClassName();
					UIManager.setLookAndFeel(laf);
					InabledTreeTest frame = new InabledTreeTest();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InabledTreeTest() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));

		tree = new JTree();
//		renderer = new DefaultTreeCellRenderer();
//		renderer.setBackground(Color.WHITE);
//		renderer.setTextSelectionColor(Color.RED);
		tree.setCellRenderer(new MyTreeCellRenderer());
		contentPane.add(tree, BorderLayout.WEST);

		JButton button = new JButton("New button");
		button.addActionListener(this);
		contentPane.add(button, BorderLayout.NORTH);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		tree.setSelectionPath(tree.getClosestPathForLocation(100, 100));
		tree.setEnabled(false);
	}

}
