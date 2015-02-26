package de.uni_passau.fim.infosun.prophet.util;

/**
 * A type generic triple of three values.
 *
 * @param <V1>
 *         the type of the first value
 * @param <V2>
 *         the type of the second value
 * @param <V3>
 *         the type of the third value
 *
 * @author Markus Köppen
 * @author Andreas Hasselberg
 * @author Georg Seibt
 */
public class Triple<V1, V2, V3> extends Pair<V1, V2> {

    private V3 third;

    /**
     * Constructs a <code>Triple</code> containing the given values.
     *
     * @param first
     *         the first value
     * @param second
     *         the second value
     * @param third
     *         the third value
     * @param <V1>
     *         the type of the first value
     * @param <V2>
     *         the type of the second value
     * @param <V3>
     *         the type of the third value
     *
     * @return a <code>Triple</code> containing the given values
     */
    public static <V1, V2, V3> Triple<V1, V2, V3> of(V1 first, V2 second, V3 third) {
        return new Triple<>(first, second, third);
    }

    /**
     * Constructs a new <code>Triple</code> containing the given values.
     *
     * @param first
     *         the first value
     * @param second
     *         the second value
     * @param third
     *         the third value
     */
    private Triple(V1 first, V2 second, V3 third) {
        super(first, second);
        this.third = third;
    }

    /**
     * Gets the third value.
     *
     * @return the third value
     */
    public V3 getThird() {
        return third;
    }

    /**
     * Sets the third value.
     *
     * @param third
     *         the new third value
     */
    public void setThird(V3 third) {
        this.third = third;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s, %s)", first, second, third);
    }
}
