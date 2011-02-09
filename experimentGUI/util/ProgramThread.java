package experimentGUI.util;

import java.io.IOException;

import javax.swing.JOptionPane;

public class ProgramThread extends Thread {
	private String command;
	private boolean isRunning;
	private Process p;

	public ProgramThread(String command) {
		this.command = command;
		this.isRunning = true;
		this.start();
	}

	public void run() {
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
					"Programm konnte nicht korrekt gestartet werden: " + e.getMessage());
		} catch (InterruptedException e) {
			JOptionPane.showMessageDialog(null,
					"Programm wurde unerwartet beendet: " + e.getMessage());
		}
		isRunning = false;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
	
	public void endProcess() {
		p.destroy();
	}

}
