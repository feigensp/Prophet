package questionView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TestA {

	public static void main(String[] args) {
		
		JFrame frame = new JFrame("ExtendedFlowLayout vs. FlowLayout");
		JPanel normalFlow = new JPanel();
		JPanel extendedFlow = new JPanel();
		JLabel n1 = new JLabel("Das ist ein Label mit viel Text (FL)");
		JLabel n2 = new JLabel("Das ist noch ein Label mit viel Text (FL)");
		JLabel n3 = new JLabel("Und das ist ein drittes Label mit viel Text (FL)");
		JLabel e1 = new JLabel("Das ist ein Label mit viel Text (EFL)");
		JLabel e2 = new JLabel("Das ist noch ein Label mit viel Text (EFL)");
		JLabel e3 = new JLabel("Und das ist ein drittes Label mit viel Text (EFL)");
		
		n1.setBackground(Color.YELLOW);
		e1.setBackground(Color.YELLOW);
		n2.setBackground(Color.RED);
		e2.setBackground(Color.RED);
		n3.setBackground(Color.GREEN);
		e3.setBackground(Color.GREEN);

		n1.setOpaque(true);
		n2.setOpaque(true);
		n3.setOpaque(true);
		e1.setOpaque(true);
		e2.setOpaque(true);
		e3.setOpaque(true);
		
		frame.setLayout(new BorderLayout());
		normalFlow.setLayout(new FlowLayout(FlowLayout.LEFT));
		extendedFlow.setLayout(new ExtendedFlowLayout(ExtendedFlowLayout.LEFT));
		
		frame.add(normalFlow, BorderLayout.NORTH);
		frame.add(extendedFlow, BorderLayout.SOUTH);
		frame.add(new JLabel("Platzhalter"), BorderLayout.CENTER);
		
		normalFlow.add(n1);
		normalFlow.add(n2);
		normalFlow.add(n3);
		
		extendedFlow.add(e1);
		extendedFlow.add(e2);
		extendedFlow.add(e3);
		
		frame.setSize(450, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}