package experimentGUI.experimentEditor.tabbedPane.editorTabs.contentEditorToolBar;

import java.awt.Component;

import javax.swing.JToolBar;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;


@SuppressWarnings("serial")
public class ContentEditorToolBar extends JToolBar {
	private RSyntaxTextArea editArea;
	
	public ContentEditorToolBar(RSyntaxTextArea ea) {
		editArea = ea;
		setFloatable(false);

		add(new FontStyleBox(editArea));
		add(new FontSizeBox(editArea));
		add(new FormularBox(editArea));
		add(new MacroBox(editArea));
//		add(new MultipleNameTestButton(editArea, "Namenstest"));
	}
	public void setEnabled(boolean enabled) {
		for (Component c : this.getComponents()) {
			c.setEnabled(enabled);
		}
	}
}
