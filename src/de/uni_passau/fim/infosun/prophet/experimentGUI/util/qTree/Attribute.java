package de.uni_passau.fim.infosun.prophet.experimentGUI.util.qTree;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A simple key/value pair that can have sub-attributes.
 */
public class Attribute implements Cloneable {

    private String key;
    private String value;
    private Map<String, Attribute> subAttributes;

    /**
     * Constructs a new <code>Attribute</code> representing the given key/value pair.
     *
     * @param key the key for the <code>Attribute</code>
     * @param value the value for the <code>Attribute</code>
     *
     * @throws NullPointerException if key or value is <cod>null</cod>
     */
    public Attribute(String key, String value) {
        Objects.requireNonNull(key, "key must not be null!");
        Objects.requireNonNull(value, "value must not be null!");

        this.key = key;
        this.value = value;
        this.subAttributes = new HashMap<>();
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value.
     *
     * @param value the new value
     *
     * @throws NullPointerException if <code>value</code> is <code>null</code>
     */
    public void setValue(String value) {
        Objects.requireNonNull(value, "value must not be null!");

        this.value = value;
    }

    /**
     * Gets the sub-attribute with the given key.
     *
     * @param key the key of the sub-attribute
     * @return the <code>Attribute</code> or <code>null</code> if no <code>Attribute</code> with the given key is found
     */
    public Attribute getSubAttribute(String key) {
        Objects.requireNonNull(key, "key must not be null!");

        return subAttributes.get(key);
    }

    /**
     * Gets the sub-attributes of this <code>Attribute</code>.
     *
     * @return the sub-attributes
     */
    public Collection<Attribute> getSubAttributes() {
        return subAttributes.values();
    }

    /**
     * Adds the given attribute to this attributes sub-attributes. If an attribute with the key of the given
     * attribute has previously been added to this attribute it will be overwritten.
     *
     * @param attribute the <code>Attribute</code> to be added
     */
    public void addSubAttribute(Attribute attribute) {
        Objects.requireNonNull(attribute, "attribute must not be null!");

        subAttributes.put(attribute.getKey(), attribute);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Attribute clone = (Attribute) super.clone();
        Map<String, Attribute> cSubAttributes = new HashMap<>();

        // clone the sub attributes
        for (Map.Entry<String, Attribute> entry :  subAttributes.entrySet()) {
            cSubAttributes.put(entry.getKey(), (Attribute) entry.getValue().clone());
        }
        clone.subAttributes = cSubAttributes;

        return clone;
    }

    /**
     * This method will be called (by the JVM) after this object was deserialised. It is implemented because
     * the XML representation for this object uses implicit collections/maps whose fields will be <code>null</code>
     * if they were empty at the time of serialisation. These fields will be initialised with empty collections.
     *
     * @return always returns <code>this</code>
     */
    private Object readResolve() {

        if (subAttributes == null) {
            subAttributes = new HashMap<>();
        }

        return this;
    }
}
