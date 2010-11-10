package questionViewer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

class IconCellRenderer extends JLabel implements ListCellRenderer {
	public static final Color highlightColor = new Color(184, 207, 229);
	
	private JList listBox;
	private int iconType;

	IconCellRenderer(JList listBox, int iconType) {
		setOpaque(true);
		this.listBox = listBox;
		this.iconType = iconType;
	}

	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		if (listBox.isSelectedIndex(index)) {
			setBackground(highlightColor);
			setForeground(Color.BLACK);
		} else {
			setBackground(Color.white);
			setForeground(Color.black);
		}
		setText(value.toString());
		setIcon(new ListIcons(iconType));
		return this;
	}

	class ListIcons implements Icon {
		public static final int DOWNARROW = 0;
		public static final int UPDOWNARROW = 1;
		private int type;

		ListIcons(int type) {
			this.type = type;
		}

		public int getIconWidth() {
			return 16;
		}

		public int getIconHeight() {
			return 16;
		}

		public void paintIcon(Component c, Graphics g, int x, int y) {
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
			}
		}
	}
}
