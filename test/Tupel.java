package test;

public class Tupel<K, V> {

	K key;
	V value;
	
	public Tupel(K key, V value) {
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
