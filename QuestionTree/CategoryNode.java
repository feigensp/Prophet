package QuestionTree;

import java.io.File;


public class CategoryNode extends QuestionTreeNode {
	private File path = null;
	
	public CategoryNode(String n) {
		super(n);
	}

	public boolean isCategory() {
		return true;
	}
	public File getPath() {
		return path;
	}
	public boolean setPath(File p) {
		if (!p.isDirectory()) {
			return false;
		}
		path=p;
		return true;
	}
}
