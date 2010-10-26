package questionView;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;

public class ExtendedFlowLayout extends FlowLayout {

	private static final long serialVersionUID = 1L;
	
	public ExtendedFlowLayout() {
		super();
	}

	public ExtendedFlowLayout(int arg0, int arg1, int arg2) {
		super(arg0, arg1, arg2);
	}

	public ExtendedFlowLayout(int arg0) {
		super(arg0);
	}

	public Dimension preferredLayoutSize(Container target) {
		
		synchronized (target.getTreeLock()) {
			int width = 0;
			int height = 0;
			Dimension dim = new Dimension(0, 0);
			Insets insets = target.getInsets();
			int nmembers = target.getComponentCount();
			boolean firstVisibleComponent = true;
			for (int i = 0 ; i < nmembers ; i++) {
			    Component m = target.getComponent(i);
			    if (m.isVisible()) {
					Dimension d = m.getPreferredSize();
					if (firstVisibleComponent) {
						firstVisibleComponent = false;
						width = d.width;
						height = d.height;
					}
					else {
						if (width + d.width > target.getWidth() - insets.left - insets.right - getHgap() * 2) {
							dim.height += height + getVgap();
							dim.width = Math.max(dim.width, width);
							width = d.width;
							height = d.height;
						}
						else {
							width += d.width + getHgap();
							height = Math.max(d.height, height);
						}
					}
			    }
			}
			dim.height += height + getVgap() * 2 + insets.top + insets.bottom;
			dim.width = Math.max(dim.width, width) + getHgap() * 2 + insets.left + insets.right;
			return dim;
		}
	}
	
	public Dimension minimumLayoutSize(Container target) {
		
		synchronized (target.getTreeLock()) {
			int width = 0;
			int height = 0;
			Dimension dim = new Dimension(0, 0);
			Insets insets = target.getInsets();
			int nmembers = target.getComponentCount();
			boolean firstVisibleComponent = true;
			for (int i = 0 ; i < nmembers ; i++) {
			    Component m = target.getComponent(i);
			    if (m.isVisible()) {
					Dimension d = m.getMinimumSize();
					if (firstVisibleComponent) {
						firstVisibleComponent = false;
						width = d.width;
						height = d.height;
					}
					else {
						if (width + d.width > target.getWidth() - insets.left - insets.right - getHgap() * 2) {
							dim.height += height + getVgap();
							dim.width = Math.max(dim.width, width);
							width = d.width;
							height = d.height;
						}
						else {
							width += d.width + getHgap();
							height = Math.max(d.height, height);
						}
					}
			    }
			}
			dim.height += height + getVgap() * 2 + insets.top + insets.bottom;
			dim.width = Math.max(dim.width, width) + getHgap() * 2 + insets.left + insets.right;
			return dim;
		}
	}
}