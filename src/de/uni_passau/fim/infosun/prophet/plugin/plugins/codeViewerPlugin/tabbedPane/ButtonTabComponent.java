/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * Modified by Markus Köppen, Andreas Hasselberg and Georg Seibt
 */
package de.uni_passau.fim.infosun.prophet.plugin.plugins.codeViewerPlugin.tabbedPane;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

/**
 * A <code>JPanel</code> used for rendering the title of an <code>EditorPanel</code> tab in the
 * <code>EditorTabbedPane</code>.
 */
public class ButtonTabComponent extends JPanel {

    private EditorTabbedPane tabbedPane;
    private EditorPanel editorPanel;

    /**
     * Constructs a new <code>ButtonTabComponent</code> for the given <code>editorPanel</code> that must be contained
     * in the given <code>tabbedPane</code>.
     *
     * @param tabbedPane
     *         the <code>EditorTabbedPane</code> containing the <code>editorPanel</code>
     * @param editorPanel
     *         the <code>EditorPanel</code> whose title this <code>ButtonTabComponent</code> should render
     * @throws NullPointerException
     *         if <code>tabbedPane</code> or <code>editorPanel</code> is <code>null</code>
     */
    public ButtonTabComponent(EditorTabbedPane tabbedPane, EditorPanel editorPanel) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));

        Objects.requireNonNull(tabbedPane, "tabbedPane must not be null!");
        Objects.requireNonNull(editorPanel, "editorPanel must not be null!");

        this.tabbedPane = tabbedPane;
        this.editorPanel = editorPanel;

        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

        JLabel label = new TabLabel();
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        add(label);

        JButton button = new TabButton();
        add(button);
    }

    /**
     * A <code>JLabel</code> used for displaying the title of the tab.
     */
    private class TabLabel extends JLabel {

        @Override
        public String getText() {
            int i = tabbedPane.indexOfTabComponent(ButtonTabComponent.this);

            if (i != -1) {
                return tabbedPane.getTitleAt(i);
            }

            return null;
        }
    }

    /**
     * The <code>JButton</code> used for closing the <code>EditorPanel</code>s tab.
     */
    private class TabButton extends JButton implements ActionListener {

        public TabButton() {
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close this tab");
            // Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            // Make it transparent
            setContentAreaFilled(false);
            // No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            // Making nice rollover effect
            // we use the same listener for all buttons
            addMouseListener(buttonMouseListener);
            setRolloverEnabled(true);
            // Close the proper tab by clicking the button
            addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            tabbedPane.closeEditorPanel(editorPanel);
        }

        // we don't want to update UI for this button
        @Override
        public void updateUI() {
        }

        // paint the cross
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            // shift the image for pressed buttons
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setStroke(new BasicStroke(2));
            g2.setColor(this.getBackground());
            if (getModel().isRollover()) {
                g2.setColor(Color.BLACK);
            }
            int delta = 6;
            g2.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
            g2.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
            g2.dispose();
        }
    }

    /**
     * A <code>MouseListener</code> that hides the border of an <code>AbstractButton</code> when the mouse exits it.
     */
    private static final MouseListener buttonMouseListener = new MouseAdapter() {

        @Override
        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(true);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                AbstractButton button = (AbstractButton) component;
                button.setBorderPainted(false);
            }
        }
    };
}
