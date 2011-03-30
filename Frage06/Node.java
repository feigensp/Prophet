public class Node {

	private AbstractElement element;
	private Node next;

	public Node() {
		this.element = null;
		this.next = null;
	}

	public Node(AbstractElement e) {
		this.element = e;
		this.next = null;
	}

	public AbstractElement getElement() {
		return this.element;
	}

	public void setNext(Node n) {
		this.next = n;
	}

	public Node getNext() {
		return this.next;
	}

	public void setElement(AbstractElement e) {
		this.element = e;
	}
}
