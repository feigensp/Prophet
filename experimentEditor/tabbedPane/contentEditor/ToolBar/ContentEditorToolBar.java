package experimentEditor.tabbedPane.contentEditor.ToolBar;

import java.awt.Component;

import javax.swing.JToolBar;

import experimentEditor.tabbedPane.contentEditor.EditArea;


@SuppressWarnings("serial")
public class ContentEditorToolBar extends JToolBar {
	EditArea editArea;
	
	public ContentEditorToolBar(EditArea ea) {
		editArea = ea;
		setFloatable(false);

		add(new FontStyleBox(editArea));
		add(new FontSizeBox(editArea));
		add(new FormularBox(editArea));
		add(new MacroBox(editArea));
	}
	public void setEnabled(boolean enabled) {
		for (Component c : this.getComponents()) {
			c.setEnabled(enabled);
		}
	}
}
