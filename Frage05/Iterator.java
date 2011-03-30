public class Iterator {
	public int currentPos;
	private LinkedList list;

	public Iterator(LinkedList toIter, int start) {
		this.currentPos = start;
		this.list = toIter;
	}

	public Iterator() {}
	
	public boolean hasNext(){
		if (currentPos < list.size())
			return true;
		return false;
	}

	public Iterator(LinkedList toIter) {
		this.list = toIter;
		this.currentPos = toIter.size() - 1;
	}

	public Node next() {
		Node node = list.getElementAt(this.currentPos);
		currentPos++;
		return node;
	}
}
