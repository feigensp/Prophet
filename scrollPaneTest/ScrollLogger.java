package scrollPaneTest;

import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JViewport;
import javax.swing.text.BadLocationException;

public class ScrollLogger {

	private static JScrollPane scrollPane;
	private static JTextPane textPane;

	public static void setScrollLogger(JScrollPane sp, JTextPane tp) {
		scrollPane = sp;
		textPane = tp;

		scrollPane.getVerticalScrollBar().addAdjustmentListener(
				new AdjustmentListener() {
					public void adjustmentValueChanged(AdjustmentEvent arg0) {
						viewVisibleText();
						System.out.println("_____________________________");
					}
				});
	}

	private static void viewVisibleText() {
		int offSet = 0;
		int end = 0;
		try {
			JViewport viewPort = scrollPane.getViewport();
			offSet = textPane.viewToModel(viewPort.getViewRect().getLocation());
			int x = (int) viewPort.getViewRect().getWidth();
			int y = (int) viewPort.getViewRect().getLocation().getY()
					+ (int) viewPort.getVisibleRect().getHeight();
			if (y > textPane.getHeight()) {
				y = textPane.getHeight();
			}
			Point endPoint = new Point(x, y);
			end = textPane.viewToModel(endPoint);
			System.out.println("offSet: " + offSet + "- end: " + end
					+ "- end-offSet: " + (end - offSet));
			try {
				System.out.println(textPane.getText(offSet, end - offSet));
			} catch (BadLocationException ble) {
				System.out.println("feheler...");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
