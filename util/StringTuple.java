package util;

/**
 * This class represents a tupel of strings
 * @author Markus Köppen, Andreas Hasselberh
 *
 */
public class StringTuple {
	String key;
	String value;
	
	/**
	 * Konstruktor which initialises the key and the value of the tupel with an empty string
	 */
	public StringTuple() {
		key = "";
		value = "";
	}
	
	/**
	 * Konstruktor which initialises the key and the value with specific strings
	 * @param key the key value
	 * @param value the values value
	 */
	public StringTuple(String key, String value) {
		this.key = key;
		this.value = value;		
	}
	
	/**
	 * returns the key of this tupel
	 * @return key of the tupel
	 */
	public String getKey() {
		return key;
	}
	/**
	 * set a new key
	 * @param key new key of the tupel
	 */
	public void setKey(String key) {
		this.key = key;
	}
	/**
	 * returns the value of the tupel
	 * @return value of the tupel
	 */
	public String getValue() {
		return value;
	}
	/**
	 * set a new value
	 * @param value new value of the tupel
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
