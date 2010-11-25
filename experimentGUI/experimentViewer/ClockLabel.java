/**


 * Diese Klasse repräsentiert eine Stoppuhr.
 * @author Markus Köppen, Andreas Hasselberg
 */

package experimentGUI.experimentViewer;

import java.text.DecimalFormat;

import javax.swing.JLabel;

import experimentGUI.util.questionTreeNode.QuestionTreeNode;


@SuppressWarnings("serial")
public class ClockLabel extends JLabel implements Runnable {
	private final static String KEY_TIME = "time";

	private long startTime=0;
	boolean isStarted = false;
	boolean isRunning=false;
	boolean isStopped=false;
	private long currentTime=0;
	private String caption;
	private Thread myThread;
	QuestionTreeNode questionNode = null;

	public ClockLabel(QuestionTreeNode questionNode, String caption) {
		this.questionNode = questionNode;
		this.caption = caption;
	}

	/**
	 * Konstruktor durch welchen aktuelle Uhrzeit im Label angezeigt wird
	 * 
	 * @param caption Beschriftung die vor der Uhrzeit stehen soll
	 */
	public ClockLabel(String caption) {
		this(null, caption);
	}
	
	/**
	 * Konstruktor für eine "unsichtbare" Uhr
	 */
	public ClockLabel() {
		this(null);
	}

	/**
	 * Startet die Stoppuhr
	 */
	public void start() {
		if (!isStarted) {
			myThread = new Thread(this);
			myThread.start();
		}
	}

	/**
	 * Pausiert die Stoppuhr
	 */
	public void pause() {
		if (isRunning) {
			isRunning = false;
			saveTime();
		}
	}

	public void saveTime() {
		if (isStarted && questionNode!=null) {
			questionNode.setAnswerTime(currentTime/1000);
		}
	}
	
	/**
	 * Lässt die Stoppuhr ihre Arbeit wieder aufnehmen
	 */
	public void resume() {
		if(isStarted && !isRunning && !isStopped) {
			synchronized (this) {
				isRunning = true;
				notify();
			}
			startTime = System.currentTimeMillis() - currentTime;
		}
	}

	/**
	 * Stoppt die Stoppuhr
	 */
	public void stop() {
		if (isStarted && !isStopped) {
			synchronized (this) {
				isRunning = false;
				isStopped = true;
				notify();
			}
			saveTime();
			myThread = null;
		}
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
		startTime = System.currentTimeMillis();
		isStarted=true;
		isRunning = true;
		isStopped = false;
		currentTime = 0;
		while (!isStopped) {
			currentTime = (System.currentTimeMillis() - startTime);
			if (caption != null) {
				DecimalFormat df = new DecimalFormat("#00");
				long seconds = (currentTime/1000)%60;
				long minutes = (currentTime/1000)/60;
				setText(caption + ": " + df.format(minutes) + ":" + df.format(seconds));
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
		return currentTime/1000;
	}
}
