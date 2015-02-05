package de.uni_passau.fim.infosun.prophet.plugin.plugins.questionListPlugin;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;
import javax.swing.Icon;

/**
 * A small <code>Icon</code> used by the <code>QuestionList</code>. 
 */
public class ListIcon implements Icon {

    public static final int NOICON = -1;
    public static final int DOWNARROW = 0;
    public static final int UPDOWNARROW = 1;
    public static final int CIRCLE = 2;

    private int type;

    /**
     * Constructs a new <code>ListIcon</code> of the specified type.
     * <code>type</code> must be one of {@link #NOICON}, {@link #DOWNARROW}, {@link #UPDOWNARROW}, {@link #CIRCLE}.  
     * 
     * @param type the type of the <code>ListIcon</code>
     */
    public ListIcon(int type) {
        this.type = type;
    }

    @Override
    public int getIconWidth() {
        return 16;
    }

    @Override
    public int getIconHeight() {
        return 16;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        
        if (type != NOICON) {
            g.setColor(Color.BLACK);
            
            if (type == DOWNARROW || type == UPDOWNARROW) {
                // draw arrow down
                Polygon p = new Polygon();
                p.addPoint(3, 1);
                p.addPoint(3, 12);
                p.addPoint(1, 12);
                p.addPoint(4, 15);
                p.addPoint(7, 12);
                p.addPoint(5, 12);
                p.addPoint(5, 1);
                g.drawPolygon(p);
                g.fillPolygon(p);
            }
            
            if (type == UPDOWNARROW) {
                // draw arrow up
                Polygon p = new Polygon();
                p.addPoint(12, 1);
                p.addPoint(9, 4);
                p.addPoint(11, 4);
                p.addPoint(11, 15);
                p.addPoint(13, 15);
                p.addPoint(13, 4);
                p.addPoint(15, 4);
                g.drawPolygon(p);
                g.fillPolygon(p);
            }
            
            if (type == CIRCLE) {
                g.drawRoundRect(6, 6, 5, 5, 4, 4);
            }
        }
    }
}
