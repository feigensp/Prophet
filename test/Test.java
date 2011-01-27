package test;

import javax.swing.JOptionPane;

public class Test {
	
	public static void main(String[] args) {
		String message = "<html>Ihre Lottozahlen:<br>";
		for (int i = 1; i <= 6; i++) {
			message += i + ".: " + ((int) (Math.random() * 49) + 1) + "<br>";
		}
		JOptionPane.showMessageDialog(null, message);
	}
}
