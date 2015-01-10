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

    protected K key;
    protected V value;

    /**
     * Constructs a <code>Pair</code> containing the given values.
     *
     * @param key
     *         the key of the pair
     * @param value
     *         the value of the pair
     * @param <K>
     *         the type of the key
     * @param <V>
     *         the type of the value
     *
     * @return a <code>Pair</code> containing <code>key</code> and <code>value</code>
     */
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }
    
    /**
     * Constructs a new <code>Pair</code> containing the given values.
     *
     * @param key
     *        the key of the pair
     * @param value
     *        the value of the pair
     */
    protected Pair(K key, V value) {
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
        return String.format("(%s, %s)", key, value);
    }
}
