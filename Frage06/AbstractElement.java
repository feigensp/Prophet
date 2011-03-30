public class AbstractElement implements Comparable<Object> {

	private String name;
	private int attribute;

	public AbstractElement(String n) {
		this.name = n;
	}

	public AbstractElement(String name, int attr) {
		this.attribute = attr;
		this.name = name;
	}

	public int compareTo(Object o1) {
		return this.name.compareTo(((AbstractElement) o1).name);
	}

	public String toString() {
		return this.name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAttribute() {
		return attribute;
	}

	public void setAttribute(int attribute) {
		this.attribute = attribute;
	}
}
