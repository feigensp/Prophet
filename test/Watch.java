/**


 * Diese Klasse repräsentiert eine Stoppuhr.
 * @author Markus Köppen, Andreas Hasselberg
 */

package test;

import javax.swing.JLabel;

public class Watch extends JLabel implements Runnable {

	private long time; // Startzeit der Messung (oder äquivalent)
	boolean isRunning; // Bestimmt ob der Thread läuft oder wartet
	private long clock; // Aktuelle Laufzeit
	private String label;
	private Thread t;

	/**
	 * Konstruktor durch welchem die Zeit auch optisch dargestellt wird
	 * 
	 * @param display
	 *            JLabel auf welchen die Zeit geschriebenw erden wird
	 */

	/**
	 * Konstruktor durch welchen aktuelle Uhrzeit im Label angezeigt wird
	 * 
	 * @param label Beschriftung die vor der Uhrzeit stehen soll
	 */
	public Watch(String label) {
		this.label = label;
		t = new Thread(this);
	}
	
	/**
	 * Konstruktor für eine "unsichtbare" Uhr
	 */
	public Watch() {
		this.label = null;
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
		if(!isRunning) {
			synchronized (this) {
				isRunning = true;
				notify();
			}
			time = System.currentTimeMillis() - (clock * 1000);
		}
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
	 * 
	 * @return true wenn die Stoppuhr läuft, sonst false
	 */
	public boolean isActive() {
		return isRunning;
	}

	/**
	 * Die Run Methode des Stoppuhr Threads - alle 1 sek ausgeführt Aktualisiert
	 * die derzeitige Zeit und schreibt diese bei Benutzung des ensprechenden
	 * Konstruktors in das JLabel
	 */
	public void run() {
		while (true) {
			clock = (System.currentTimeMillis() - time) / 1000;
			if (label != null) {
				// Zeiten ins display schreiben
				if (clock / 60 < 10) {
					if (clock < 10) {
						setText(label + ": 0" + (clock / 60) + ":0" + clock);
					} else {
						setText(label + ": 0" + (clock / 60) + ":" + clock);
					}
				} else {
					if (clock < 10) {
						setText(label + ": " + (clock / 60) + ":0" + clock);
					} else {
						setText(label + ": " + (clock / 60) + ":" + clock);
					}
				}
			}
			try {
				Thread.sleep(200);
				synchronized (this) {
					if (!isRunning) {
						wait();
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
	/**
	 * Gibt die aktuell gestoppte Zeit zurück
	 * @return Zeit Sekunden
	 */
	public long getTime() {
		return clock;
	}
}
