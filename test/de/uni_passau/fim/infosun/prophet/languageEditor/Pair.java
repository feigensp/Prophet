package test.languageEditor;

/**
 * This class represents a generic data structure version of a pair
 *
 * @param <K>
 *         type of the key
 * @param <V>
 *         type of the value
 *
 * @author Markus K�ppen, Andreas Hasselberg
 */
public class Pair<K, V> {

    K key;
    V value;

    /**
     * Constructor which initialize key and value with null
     */
    public Pair() {
        key = null;
        value = null;
    }

    /**
     * Constructor which initialize key and value with specific content
     *
     * @param key
     *         value of the key
     * @param value
     *         value of the value
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * returns a String representation of the pair
     */
    public String toString() {
        return key.toString() + ":" + value.toString();
    }

    /**
     * returns the key
     *
     * @return key
     */
    public K getKey() {
        return key;
    }

    /**
     * sets the key
     *
     * @param key
     *         new key
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * returns the value
     *
     * @return value
     */
    public V getValue() {
        return value;
    }

    /**
     * sets the value
     *
     * @param value
     *         new value
     */
    public void setValue(V value) {
        this.value = value;
    }
}
