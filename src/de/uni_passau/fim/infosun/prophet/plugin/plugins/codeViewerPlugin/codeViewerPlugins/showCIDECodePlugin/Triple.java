package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.showCIDECodePlugin;

/**
 * A triple composed of a key and two values.
 *
 * @param <K> the type of the key
 * @param <V1> the type of the first value
 * @param <V2> the type of the second value
 *
 * @author Markus Köppen, Andreas Hasselberg
 */
public class Triple<K, V1, V2> {

    private K key;
    private V1 value1;
    private V2 value2;

    /**
     * Constructs a new <code>Triple</code> initialising the key and both values with <code>null</code>.
     */
    public Triple() {
        this(null, null, null);
    }

    /**
     * Constructs a new <code>Triple</code> initialising the key and both values with the given parameters.
     *
     * @param key
     *         the value for the key
     * @param value1
     *         the first value
     * @param value2
     *         the second value
     */
    public Triple(K key, V1 value1, V2 value2) {
        this.key = key;
        this.value1 = value1;
        this.value2 = value2;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public K getKey() {
        return key;
    }

    /**
     * Sets the key.
     *
     * @param key the new key
     */
    public void setKey(K key) {
        this.key = key;
    }

    /**
     * Gets the first value.
     *
     * @return the first value
     */
    public V1 getValue1() {
        return value1;
    }

    /**
     * Sets the first value.
     *
     * @param value1 the new first value
     */
    public void setValue1(V1 value1) {
        this.value1 = value1;
    }

    /**
     * Gets the second value.
     *
     * @return the second value
     */
    public V2 getValue2() {
        return value2;
    }

    /**
     * Sets the second value.
     *
     * @param value2 the new second value
     */
    public void setValue2(V2 value2) {
        this.value2 = value2;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", key.toString(), value1.toString(), value2.toString());
    }
}
