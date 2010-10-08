/**
 * Diese Klasse repräsentiert eine Stoppuhr.
 * @author Markus Köppen, Andreas Hasselberg
 */

package test;

import javax.swing.JLabel;

public class Watch implements Runnable {

	private long time;		//Startzeit der Messung (oder äquivalent)
	private JLabel display;
	boolean isRunning;		//Bestimmt ob der Thread läuft oder wartet
	private long clock;		//Aktuelle Laufzeit
	private Thread t;
	
	/**
	 * Konstruktor durch welchem die Zeit auch optisch dargestellt wird
	 * @param display JLabel auf welchen die Zeit geschriebenw erden wird
	 */
	public Watch(JLabel display) {
		this.display = display;
		t = new Thread(this);
	}
	
	/**
	 * Standartkonstruktor
	 */
	public Watch() {
		t = new Thread(this);
	}
	
	/**
	 * Startet die Stoppuhr
	 */
	public void start() {
		time = System.currentTimeMillis();
		isRunning = true;
		clock = 0;
		t.start();
	}
	
	/**
	 * Pausiert die Stoppuhr
	 */
	public void pause() {
		isRunning = false;
	}
	
	/**
	 * Lässt die Stoppuhr ihre Arbeit wieder aufnehmen
	 */
	public void resume() {
		synchronized (this) {
			isRunning = true;
			notify();
		}
		time = System.currentTimeMillis() - (clock * 1000);
	}
	
	/**
	 * Stoppt die Stoppuhr
	 */
	public void stop() {
		isRunning = false;
		time = 0;
		clock = 0;
	}
	
	/**
	 * Methode um Abzufragen ob die Stoppuhr gerade läuft
	 * @return true wenn die Stoppuhr läuft, sonst false
	 */
	public boolean isActive() {
		return isRunning;
	}

	/**
	 * Die Run Methode des Stoppuhr Threads - alle 1 sek ausgeführt
	 * Aktualisiert die derzeitige Zeit und schreibt diese bei Benutzung des 
	 * ensprechenden Konstruktors in das JLabel
	 */
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				synchronized (this) {
					if (!isRunning) {
						wait();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			clock = (System.currentTimeMillis() - time) / 1000;
			if(display != null) {
				//Zeiten ins display schreiben
				if (clock / 60 < 10) {
					if (clock < 10) {
						display.setText("Zeit: 0" + (clock / 60) + ":0" + clock
								+ " (5 min)");
					} else {
						display.setText("Zeit: 0" + (clock / 60) + ":" + clock
								+ " (5 min)");
					}
				} else {
					if (clock < 10) {
						display.setText("Zeit: " + (clock / 60) + ":0" + clock
								+ " (5 min)");
					} else {
						display.setText("Zeit: " + (clock / 60) + ":" + clock
								+ " (5 min)");
					}
				}
			}
		}
	}

}
