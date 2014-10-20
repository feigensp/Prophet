/**
 THIS PROGRAM IS PROVIDED "AS IS" WITHOUT ANY WARRANTIES (OR CONDITIONS),
 EXPRESS OR IMPLIED WITH RESPECT TO THE PROGRAM, INCLUDING THE IMPLIED WARRANTIES (OR CONDITIONS)
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE ENTIRE RISK ARISING OUT OF USE OR
 PERFORMANCE OF THE PROGRAM AND DOCUMENTATION REMAINS WITH THE USER.
 */

package de.uni_passau.fim.infosun.prophet.experimentGUI.util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;

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
     *        the horizontal alignment, should be one of {@link #LEFT}, {@link #RIGHT}, {@link #CENTER},
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
     *        the horizontal alignment, should be one of {@link #LEFT}, {@link #RIGHT}, {@link #CENTER} or
     *        {@link #STRETCH}
     * @param anchor
     *        the anchor for the layed out components, should be one of {@link #TOP}, {@link #BOTTOM} or {@link #CENTER}
     */
    public VerticalLayout(int vgap, int alignment, int anchor) {
        this.vgap = vgap;
        this.alignment = alignment;
        this.anchor = anchor;
    }

    private Dimension layoutSize(Container parent, boolean minimum) {
        Dimension dim = new Dimension(0, 0);
        Dimension d;
        synchronized (parent.getTreeLock()) {
            int n = parent.getComponentCount();
            for (int i = 0; i < n; i++) {
                Component c = parent.getComponent(i);
                if (c.isVisible()) {
                    d = minimum ? c.getMinimumSize() : c.getPreferredSize();
                    dim.width = Math.max(dim.width, d.width);
                    dim.height += d.height;
                    if (i > 0) {
                        dim.height += vgap;
                    }
                }
            }
        }
        Insets insets = parent.getInsets();
        dim.width += insets.left + insets.right;
        dim.height += insets.top + insets.bottom + vgap + vgap;
        return dim;
    }

    @Override
    public void layoutContainer(Container parent) {

        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            Dimension pd = parent.getSize();
            int n = parent.getComponentCount();
            int y = 0;

            //work out the total size
            for (int i = 0; i < n; i++) {
                Component c = parent.getComponent(i);
                Dimension d = c.getPreferredSize();
                y += d.height + vgap;
            }
            y -= vgap; //otherwise there's a vgap too many

            //Work out the anchor paint
            if (anchor == TOP) {
                y = insets.top;
            } else if (anchor == CENTER) {
                y = (pd.height - y) / 2;
            } else {
                y = pd.height - y - insets.bottom;
            }

            //do layout
            for (int i = 0; i < n; i++) {
                Component c = parent.getComponent(i);
                Dimension d = c.getPreferredSize();
                int x = insets.left;
                int wid = d.width;

                if (alignment == CENTER) {
                    x = (pd.width - d.width) / 2;
                } else if (alignment == RIGHT) {
                    x = pd.width - d.width - insets.right;
                } else if (alignment == STRETCH) {
                    wid = pd.width - insets.left - insets.right;
                }

                c.setBounds(x, y, wid, d.height);
                y += d.height + vgap;
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
        String anchor = toString(this.anchor);
        String alignment = toString(this.alignment);

        return String.format("%s[vgap=%dpx align=%s anchor=%s]", getClass().getName(), vgap, alignment, anchor);
    }

    /**
     * Returns a description (the variable name) for one of the static constants used by this class.
     *
     * @param i
     *         the constant
     *
     * @return the description
     */
    private String toString(int i) {
        String result;

        switch (i) {
            case CENTER:
                result = "CENTER";
                break;
            case RIGHT:
                result = "RIGHT";
                break;
            case LEFT:
                result = "LEFT";
                break;
            case STRETCH:
                result = "STRETCH";
                break;
            case TOP:
                result = "TOP";
                break;
            case BOTTOM:
                result = "BOTTOM";
                break;
            default:
                result = "Unknown constant.";
        }

        return result;
    }
}

