public class ListMain {

	public static void main(String args[]) {
		LinkedList list = new LinkedList();
		AbstractElement a1 = new AbstractElement("A1");
		AbstractElement a2 = new AbstractElement("A2");
		AbstractElement a3 = new AbstractElement("A3");
		AbstractElement a4 = new AbstractElement("A4");
		AbstractElement a5 = new AbstractElement("A5");

		list.insert(a1);
		list.insert(a2);
		list.insert(a3);
		list.insert(a4);
		list.insert(a5);

		list.insertAlgorithm(new AbstractElement("A0"));
		
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next().getElement());
		}
	}
}
