/**


 * Diese Klasse repräsentiert eine Stoppuhr.
 * @author Markus Köppen, Andreas Hasselberg
 */

package experimentViewer;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class ClockLabel extends JLabel implements Runnable {

	private long startTime; // Startzeit der Messung (oder äquivalent)
	boolean isRunning; // Bestimmt ob der Thread läuft oder wartet
	private long currentTime; // Aktuelle Laufzeit
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
	public ClockLabel(String label) {
		this.label = label;
		t = new Thread(this);
	}
	
	/**
	 * Konstruktor für eine "unsichtbare" Uhr
	 */
	public ClockLabel() {
		this.label = null;
		t = new Thread(this);
	}

	/**
	 * Startet die Stoppuhr
	 */
	public void start() {
		startTime = System.currentTimeMillis();
		isRunning = true;
		currentTime = 0;
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
			startTime = System.currentTimeMillis() - (currentTime * 1000);
		}
	}

	/**
	 * Stoppt die Stoppuhr
	 */
	public void stop() {
		isRunning = false;
		startTime = 0;
		currentTime = 0;
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
			currentTime = (System.currentTimeMillis() - startTime) / 1000;
			if (label != null) {
				// Zeiten ins display schreiben
				if (currentTime / 60 < 10) {
					if (currentTime < 10) {
						setText(label + ": 0" + (currentTime / 60) + ":0" + currentTime);
					} else {
						setText(label + ": 0" + (currentTime / 60) + ":" + currentTime);
					}
				} else {
					if (currentTime < 10) {
						setText(label + ": " + (currentTime / 60) + ":0" + currentTime);
					} else {
						setText(label + ": " + (currentTime / 60) + ":" + currentTime);
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
	public long getCurrentTime() {
		return currentTime;
	}
}
