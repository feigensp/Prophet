package de.uni_passau.fim.infosun.prophet;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

public class FindAndReplaceDemo extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	private RSyntaxTextArea textArea;
	private JTextField searchField;
	private JCheckBox regexCB;
	private JCheckBox matchCaseCB;

	public FindAndReplaceDemo() {

		JPanel cp = new JPanel(new BorderLayout());

		textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		RTextScrollPane sp = new RTextScrollPane(textArea);
		cp.add(sp);

		// Create a toolbar with searching options.
		JToolBar toolBar = new JToolBar();
		searchField = new JTextField(30);
		toolBar.add(searchField);
		JButton b = new JButton("Find Next");
		b.setActionCommand("FindNext");
		b.addActionListener(this);
		toolBar.add(b);
		b = new JButton("Find Previous");
		b.setActionCommand("FindPrev");
		b.addActionListener(this);
		toolBar.add(b);
		regexCB = new JCheckBox("Regex");
		toolBar.add(regexCB);
		matchCaseCB = new JCheckBox("Match Case");
		toolBar.add(matchCaseCB);
		cp.add(toolBar, BorderLayout.NORTH);

		setContentPane(cp);
		setTitle("RSyntaxTextArea 1.4 - Example 4 - Find and Replace Demo");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);

	}

	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();

		if ("FindNext".equals(command)) {
			String text = searchField.getText();
			if (text.length() == 0) {
				return;
			}

            SearchContext searchContext = new SearchContext();
            searchContext.setSearchFor(text);
            searchContext.setSearchForward(true);
            searchContext.setMatchCase(matchCaseCB.isSelected());
            searchContext.setWholeWord(false);
            searchContext.setRegularExpression(regexCB.isSelected());

            boolean found = SearchEngine.find(textArea, searchContext);

			if (!found) {
				JOptionPane.showMessageDialog(this, "Text not found");
			}
		}

		else if ("FindPrev".equals(command)) {
			String text = searchField.getText();
			if (text.length() == 0) {
				return;
			}

            SearchContext searchContext = new SearchContext();
            searchContext.setSearchFor(text);
            searchContext.setSearchForward(false);
            searchContext.setMatchCase(matchCaseCB.isSelected());
            searchContext.setWholeWord(false);
            searchContext.setRegularExpression(regexCB.isSelected());

            boolean found = SearchEngine.find(textArea, searchContext);

			if (!found) {
				JOptionPane.showMessageDialog(this, "Text not found");
			}
		}

	}

	public static void main(String[] args) {
		// Start all Swing applications on the EDT.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					String laf = UIManager.getSystemLookAndFeelClassName();
					UIManager.setLookAndFeel(laf);
				} catch (Exception e) {
				}
				FindAndReplaceDemo demo = new FindAndReplaceDemo();
				demo.setVisible(true);
				demo.textArea.requestFocusInWindow();
			}
		});
	}

}
