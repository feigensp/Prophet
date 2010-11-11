package experimentEditor.ContentEdit.ToolBar;

import javax.swing.JToolBar;

import experimentEditor.EditArea;


@SuppressWarnings("serial")
public class ToolBar extends JToolBar {
	EditArea editArea;
	
	public ToolBar(EditArea ea) {
		editArea = ea;
		setFloatable(false);

		add(new FontStyleBox(editArea));
		add(new FontSizeBox(editArea));
		add(new FormularBox(editArea));
		add(new MacroBox(editArea));
	}
}
