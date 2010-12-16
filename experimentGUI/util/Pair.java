package experimentGUI.util;

public class Pair<K,V> {

	K key;
	V value;
	
	public Pair() {
		key = null;
		value = null;
	}
	
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	public String toString() {
		return key.toString() + value.toString();
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}
}
