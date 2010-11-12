package experimentEditor.tabbedPane.settingsEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import util.QuestionTreeNode;

@SuppressWarnings("serial")
public abstract class SettingsComponent extends JPanel {
	String key;
	QuestionTreeNode selected;
	
	private ActionListener defaultActionListener = new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if (getSelected()!=null) {
				getSelected().setAttribute(getKey(), getValue());
			}
		}		
	};
	
	public ActionListener getDefaultActionListener() {
		return defaultActionListener;
	}	
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public QuestionTreeNode getSelected() {
		return selected;
	}
	public void setSelected(QuestionTreeNode selected) {
		this.selected = selected;
	}
	
	public abstract void setCaption(String caption);
	public abstract String getCaption();
	
	public abstract void setValue(String value);
	public abstract String getValue();
}
