package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.codeViewerPlugins.showCIDECodePlugin;

import de.uni_passau.fim.infosun.prophet.util.Pair;

/**
 * A triple composed of a key and two values.
 *
 * @param <K> the type of the key
 * @param <V1> the type of the first value
 * @param <V2> the type of the second value
 *
 * @author Markus Köppen, Andreas Hasselberg
 */
public class Triple<K, V1, V2> extends Pair<K, V1> {
    
    private V2 value2;

    /**
     * Constructs a <code>Triple</code> containing the given values.
     * 
     * @param key the value for the key
     * @param v1 the first value
     * @param v2 the second value
     * @param <K> the type of the key
     * @param <V1> the type of the first value
     * @param <V2> the type of the second value
     * @return a <code>Triple</code> containing the given values
     */
    public static <K, V1, V2> Triple<K, V1, V2> of(K key, V1 v1, V2 v2) {
        return new Triple<>(key, v1, v2);
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
    private Triple(K key, V1 value1, V2 value2) {
        super(key, value1);
        this.value2 = value2;
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
        return String.format("(%s, %s, %s)", first, second, value2);
    }
}
