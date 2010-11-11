package experimentEditor.SettingsEdit;

/**
 * A Dialog to adjust some settings.
 * One is a path an the others are predefined (Settings.java) and could be true oder false
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import util.QuestionTreeNode;


@SuppressWarnings("serial")
public class CategoryEditorPanel extends JPanel {
	private QuestionTreeNode selected;

	/**
	 * Constructor which defines the appearance and do the initialisation of the
	 * variables etc.
	 * 
	 * @param id
	 *            String identifier of this Dialog
	 */
	public CategoryEditorPanel(QuestionTreeNode sel) {
		selected=sel;
		setLayout(new VerticalFlowLayout());
		
		ActionListener myActionListener = new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				SettingsComponent source = (SettingsComponent) event.getSource();
				selected.setAttribute(source.getOptionKey(), source.getValue());
			}
		};

		// adding checkboxes for the given possible settings in Settings.java
		for (SettingsOption setting : Settings.settings) {
			SettingsComponent opt = setting.newInstance(selected.getAttribute(setting.getKey()));
			opt.addActionListener(myActionListener);
			add((Component)opt);			
		}
	}
}