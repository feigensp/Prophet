public class LinkedList {

	Node head;

	public LinkedList() {
		this.head = new Node();
	}

	public Node getElementAt(int pos) {
		Node current = this.head;
		for (int i = 0; i <= pos; i++) {
			if (current.getNext() != null) {
				current = current.getNext();
			} else {
				return null;
			}
		}
		return current;
	}

	public void insert(AbstractElement e) {
		Node newNode = new Node(e);
		newNode.setNext(this.head.getNext());
		head.setNext(newNode);
	}
	
	public void insertAlgorithm(AbstractElement ae){
		Iterator iter = this.iterator();
		Node previous = iter.next();
		if (ae.compareTo(previous.getElement()) < 0) {
			insert(ae);
			return;
		}
		while (iter.hasNext()) {
			Node tmp = iter.next();
			if (ae.compareTo(tmp.getElement()) < 0) {
				Node newNode = new Node(ae);
				newNode.setNext(tmp);
				previous.setNext(newNode);
				break;
			} 
			previous = tmp;
		}
		if (ae.compareTo(previous.getElement()) > 0) {
			Node newNode = new Node(ae);
			previous.setNext(newNode);
			return;
		}
	}

	public String removePos(int pos) {
		Node previous = null;
		Node current = this.head;
		Node delete = null;
		for (int i = 1; i <= pos; i++) {
			previous = current;
			if (current.getNext() != null) {
				current = current.getNext();
			} else {
				return null;
			}
			delete = current;
		}
		previous.setNext(current.getNext());
		return delete.getElement().getName();
	}

	public int size() {
		Node iterator = head;
		int size = 0;
		while (iterator.getNext() != null) {
			iterator = iterator.getNext();
			size++;
		}
		return size;
	}

	public final void sortBubbleSort() {
		AbstractElement[] array = new AbstractElement[this.size()];
		Node current = this.head;
		int i = 0;
		array[i] = current.getElement();
		while ((current != null) && ((current = current.getNext()) != null)) {
			array[i++] = current.getElement();
		}
		sort(array, this.size());
		i = 0;
		current = head;
		while ((current != null) && ((current = current.getNext()) != null)) {
			current.setElement(array[i++]);
		}
	}

	public final void sortPerformance() {
		long t1 = System.currentTimeMillis();
		sortBubbleSort();
		long t2 = System.currentTimeMillis();
		t2 = t2 - t1;
		System.out.println("Sortierung benötigte: " + t2 + " Milisekunden");
	}

	public void sort() {
		Runtime r = Runtime.getRuntime();
		long mem1, mem2;
		mem1 = r.totalMemory();
		mem2 = r.freeMemory();
		sortPerformance();
		mem1 = r.totalMemory() - mem1;
		mem2 = r.freeMemory() - mem2;

		System.out.println("Verwendeter Speicher: " + mem1
				+ " ; Änderung der Speicherauslastung: " + mem2);
	}

	private void sort(AbstractElement[] array, int size) {
		boolean swapped;
		do {
			swapped = false;
			for (int i = 0; i < size - 1; i++) {
				if (array[i + 1].compareTo(array[i]) < 0) {
					swap(array, i, i + 1);
					swapped = true;
				}
			}
		} while (swapped);
	}

	private void swap(AbstractElement[] array, int index1, int index2) {
		AbstractElement swapTmp = array[index1];
		array[index1] = array[index2];
		array[index2] = swapTmp;
	}

	public String toString() {
		String result = "";
		Iterator it = this.iterator();
		while (it.hasNext()) {
			result += it.next().toString() + "\r\n";
		}
		return result;
	}
	
	public Iterator iterator(){
		return new Iterator(this, 0);
	}
}
