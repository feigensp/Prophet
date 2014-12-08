/**
 THIS PROGRAM IS PROVIDED "AS IS" WITHOUT ANY WARRANTIES (OR CONDITIONS),
 EXPRESS OR IMPLIED WITH RESPECT TO THE PROGRAM, INCLUDING THE IMPLIED WARRANTIES (OR CONDITIONS)
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK ARISING OUT OF USE OR
 PERFORMANCE OF THE PROGRAM AND DOCUMENTATION REMAINS WITH THE USER.
 */

package de.uni_passau.fim.infosun.prophet.util;

import java.awt.AWTError;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A vertical layout manager similar to <code>FlowLayout</code>.
 * Like <code>FlowLayout</code> components do not expand to fill available space except when the horizontal alignment
 * is <code>STRETCH</code> in which case components are stretched horizontally.
 * <p>
 * Unlike <code>FlowLayout</code>, components will not wrap to form another column if there isn't enough space
 * vertically. <code>VerticalLayout</code> can optionally anchor components to the top or bottom of the display area
 * or center them between the top and bottom.
 * <p>
 *
 * Revision date 12th July 2001
 *
 * @author Colin Mummery  e-mail: colin_mummery@yahoo.com Homepage:www.kagi.com/equitysoft -
 *         Based on 'FlexLayout' in Java class libraries Vol 2 Chan/Lee Addison-Wesley 1998
 */
public class VerticalLayout implements LayoutManager {

    /**
     * The horizontal alignment constant that designates centering. Also used to designate center anchoring.
     */
    public static final int CENTER = 0;

    /**
     * The horizontal alignment constant that designates right justification.
     */
    public static final int RIGHT = 1;

    /**
     * The horizontal alignment constant that designates left justification.
     */
    public static final int LEFT = 2;

    /**
     * The horizontal alignment constant that designates stretching the component horizontally.
     */
    public static final int STRETCH = 3;

    /**
     * The anchoring constant that designates anchoring to the top of the display area
     */
    public static final int TOP = 4;

    /**
     * The anchoring constant that designates anchoring to the bottom of the display area
     */
    public static final int BOTTOM = 5;

    /**
     * A read-only map containing <code>String</code> representations of the magic constants used by this class.
     */
    private static final Map<Integer, String> constantReps;

    static {
        Map<Integer, String> reps = new HashMap<>();
        reps.put(CENTER, "CENTER");
        reps.put(RIGHT, "RIGHT");
        reps.put(LEFT, "LEFT");
        reps.put(STRETCH, "STRETCH");
        reps.put(TOP, "TOP");
        reps.put(BOTTOM, "BOTTOM");

        constantReps = Collections.unmodifiableMap(reps);
    }

    private int vgap; // the vertical gap between components ... defaults to 5
    private int alignment; // LEFT, RIGHT, CENTER or STRETCH ... how the components are justified
    private int anchor; // TOP, BOTTOM or CENTER ... where are the components positioned in an overlarge space

    /**
     * Constructs a new <code>VerticalLayout</code> with a vertical gap of 5 pixels and horizontal centering. Components
     * will be anchored to the top of the display area.
     */
    public VerticalLayout() {
        this(5, CENTER, TOP);
    }

    /**
     * Constructs a new <code>VerticalLayout</code> with the specified vertical gap and horizontal centering. Components
     * will be anchored to the top of the display area.
     *
     * @param vgap
     *        the vertical spacing between components in pixels
     */
    public VerticalLayout(int vgap) {
        this(vgap, CENTER, TOP);
    }

    /**
     * Constructs a new <code>VerticalLayout</code> with the specified <code>vgap</code> and <code>alignment</code>
     * in which components will be anchored to the top of the display area.
     *
     * @param vgap
     *        the vertical spacing between components in pixels
     * @param alignment
     *        the horizontal alignment, must be one of {@link #LEFT}, {@link #RIGHT}, {@link #CENTER},
     *        {@link #STRETCH}
     */
    public VerticalLayout(int vgap, int alignment) {
        this(vgap, alignment, TOP);
    }

    /**
     * Constructs a new <code>VerticalLayout</code> with the specified <code>vgap</code>, <code>alignment</code> and
     * <code>anchor</code>.
     *
     * @param vgap
     *        the vertical spacing between components in pixels
     * @param alignment
     *        the horizontal alignment, must be one of {@link #LEFT}, {@link #RIGHT}, {@link #CENTER} or
     *        {@link #STRETCH}
     * @param anchor
     *        the anchor for the laid out components, must be one of {@link #TOP}, {@link #BOTTOM} or {@link #CENTER}
     */
    public VerticalLayout(int vgap, int alignment, int anchor) {

        if (!(alignment == LEFT || alignment == RIGHT || alignment == CENTER || alignment == STRETCH)) {
            throw new AWTError("Invalid alignment.");
        }

        if (!(anchor == TOP || anchor == BOTTOM || anchor == CENTER)) {
            throw new AWTError("Invalid anchor.");
        }

        this.vgap = vgap;
        this.alignment = alignment;
        this.anchor = anchor;
    }

    /**
     * Calculates the minimum or preferred size dimensions for the specified container, given the components it
     * contains.
     *
     * @param parent
     *         the component to be laid out
     * @param minimum
     *         true for minimum size, false for preferred size
     *
     * @see #preferredLayoutSize(java.awt.Container)
     * @see #minimumLayoutSize(java.awt.Container)
     */
    private Dimension layoutSize(Container parent, boolean minimum) {
        Dimension size = new Dimension();
        Dimension componentSize;
        Component[] components;
        Component component;

        synchronized (parent.getTreeLock()) {
            components = parent.getComponents();

            for (int i = 0; i < components.length; i++) {
                component = components[i];

                if (component.isVisible()) {
                    componentSize = minimum ? component.getMinimumSize() : component.getPreferredSize();
                    size.width = Math.max(size.width, componentSize.width);
                    size.height += componentSize.height;

                    if (i < components.length - 1) {
                        size.height += vgap;
                    }
                }
            }
        }

        Insets parentInsets = parent.getInsets();

        size.width += parentInsets.left + parentInsets.right;
        size.height += parentInsets.top + parentInsets.bottom + vgap + vgap;

        return size;
    }

    @Override
    public void layoutContainer(Container parent) {

        synchronized (parent.getTreeLock()) {
            Insets parentInsets = parent.getInsets();
            Dimension parentDimension = parent.getSize();
            Component[] components = parent.getComponents();
            Dimension componentDimension;
            int y = 0;

            for (Component component : components) {
                componentDimension = component.getPreferredSize();
                y += componentDimension.height + vgap;
            }
            y -= vgap; //otherwise there is a vgap too many

            // modify y to fit the chosen anchor
            if (anchor == TOP) {
                y = parentInsets.top;
            } else if (anchor == CENTER) {
                y = (parentDimension.height - y) / 2;
            } else {
                y = parentDimension.height - y - parentInsets.bottom;
            }

            int x;
            int width;

            // do the layout
            for (Component component : components) {
                componentDimension = component.getPreferredSize();
                x = parentInsets.left;
                width = componentDimension.width;

                if (alignment == CENTER) {
                    x = (parentDimension.width - componentDimension.width) / 2;
                } else if (alignment == RIGHT) {
                    x = parentDimension.width - componentDimension.width - parentInsets.right;
                } else if (alignment == STRETCH) {
                    width = parentDimension.width - parentInsets.left - parentInsets.right;
                }

                component.setBounds(x, y, width, componentDimension.height);
                y += componentDimension.height + vgap;
            }
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return layoutSize(parent, false);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return layoutSize(parent, true);
    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        // this LayoutManager does not store component specific information
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        // this LayoutManager does not store component specific information
    }

    @Override
    public String toString() {
        String unknown = "Unknown";
        String anchor = constantReps.containsKey(this.anchor) ? constantReps.get(this.anchor) : unknown;
        String alignment = constantReps.containsKey(this.alignment) ? constantReps.get(this.alignment) : unknown;

        return String.format("%s[vgap=%dpx align=%s anchor=%s]", getClass().getName(), vgap, alignment, anchor);
    }
}

