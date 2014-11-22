package de.uni_passau.fim.infosun.prophet.util;

/**
 * A type generic pair of two values.
 *
 * @param <K>
 *         the type of the key
 * @param <V>
 *         the type of the value
 *
 * @author Markus Köppen
 * @author Andreas Hasselberg
 * @author Georg Seibt
 */
public class Pair<K, V> {

    private K key;
    private V value;

    /**
     * Constructs a new <code>Pair</code> with both <code>key</code> and <code>value</code> set to <code>null</code>.
     */
    public Pair() {
        key = null;
        value = null;
    }

    /**
     * Constructs a new <code>Pair</code> containing the given values.
     *
     * @param key
     *        the key of the pair
     * @param value
     *        the value of the pair
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Returns the key of the <code>Pair</code>.
     *
     * @return the key
     */
    public K getKey() {
        return key;
    }

    /**
     * Sets the key to <code>key</code>.
     *
     * @param key
     *         the new key
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * Returns the value of the <code>Pair</code>.
     *
     * @return the value
     */
    public V getValue() {
        return value;
    }

    /**
     * Sets the value to <code>value</code>.
     *
     * @param value
     *         new value
     */
    public void setValue(V value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("Pair{key=%s, value=%s}", key, value);
    }
}
