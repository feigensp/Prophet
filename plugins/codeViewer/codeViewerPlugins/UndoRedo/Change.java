package plugins.codeViewer.codeViewerPlugins.UndoRedo;

public class Change {

	public static final int REMOVE = 0;
	public static final int INSERT = 1;
	
	int offset;
	String change;
	int type;
	int length;
	
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public String getChange() {
		return change;
	}
	public void setChange(String change) {
		this.change = change;
	}
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}	
	
	public String toString() {
		return offset + ":" + change + ":" + type;
	}
	
}
