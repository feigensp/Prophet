package de.uni_passau.fim.infosun.prophet.util;

/**
 * A type generic pair of two values.
 *
 * @param <V1>
 *         the type of the first value
 * @param <V2>
 *         the type of the second value
 *
 * @author Markus Köppen
 * @author Andreas Hasselberg
 * @author Georg Seibt
 */
public class Pair<V1, V2> {

    protected V1 first;
    protected V2 second;

    /**
     * Constructs a <code>Pair</code> containing the given values.
     *
     * @param first
     *         the first value
     * @param second
     *         the second value
     * @param <V1>
     *         the type of the first value
     * @param <V2>
     *         the type of the second value
     *
     * @return a <code>Pair</code> containing the given values
     */
    public static <V1, V2> Pair<V1, V2> of(V1 first, V2 second) {
        return new Pair<>(first, second);
    }

    /**
     * Constructs a new <code>Pair</code> containing the given values.
     *
     * @param first
     *         the first value
     * @param second
     *         the second value
     */
    protected Pair(V1 first, V2 second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Returns the first value of the <code>Pair</code>.
     *
     * @return the first value
     */
    public V1 getFirst() {
        return first;
    }

    /**
     * Sets the first value to <code>first</code>.
     *
     * @param first
     *         the new first value
     */
    public void setFirst(V1 first) {
        this.first = first;
    }

    /**
     * Returns the second value of the <code>Pair</code>.
     *
     * @return the second value
     */
    public V2 getSecond() {
        return second;
    }

    /**
     * Sets the second value to <code>second</code>.
     *
     * @param second
     *         the new second value
     */
    public void setSecond(V2 second) {
        this.second = second;
    }

    @Override
    public String toString() {
        return String.format("(%s, %s)", first, second);
    }
}
