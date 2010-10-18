/**
 * Klasse welche zum mitschneiden von Nutzerdaten dient
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package EditorTabbedPane;

import java.util.ArrayList;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import test.Watch;

public class UserDataRecorder {

	private ArrayList<FileTime> timeLine;
	private String selectedTitle;
	private Watch timeLineWatch;
	private JTabbedPane timeLineTabbedPane;
	private ArrayList<String> tabs;

	private ArrayList<FileTime> fileTimes;
	private ArrayList<Watch> clocks;
	private JTabbedPane fileTimesTabbedPane;
	private int lastClock;

	/**
	 * Standartkonstruktor
	 */
	public UserDataRecorder() {
		fileTimes = new ArrayList<FileTime>();
		timeLine = new ArrayList<FileTime>();
	}

	/**
	 * Methode um einen Zeitlinienaufbau bzgl. einer tabbedPane aufzubauen
	 * 
	 * @param tabbedPane
	 *            TabbedPane zu der ein Zeistrahl aufgebaut werden soll
	 */
	public void startTimeLine(JTabbedPane tabbedPane) {
		timeLineWatch = new Watch();
		timeLineTabbedPane = tabbedPane;
		int selected = timeLineTabbedPane.getSelectedIndex();
		tabs = new ArrayList<String>();

		// alle derzeitigen Tab-Titel hinzufügen
		for (int i = 0; i < timeLineTabbedPane.getComponentCount(); i++) {
			tabs.add(timeLineTabbedPane.getTitleAt(i));
		}

		timeLineWatch.start();
		timeLineWatch.pause();
		selectedTitle = "";
		if (selected >= 0) {
			selectedTitle = timeLineTabbedPane.getTitleAt(timeLineTabbedPane
					.getSelectedIndex());
			timeLineWatch.resume();
		}
		timeLineTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				// Tab wurde hinzugefügt
				if (!selectedTitle.equals("")) {
					timeLine.add(new FileTime(selectedTitle, timeLineWatch
							.getTime()));
				}
				timeLineWatch.stop();
				selectedTitle = timeLineTabbedPane
						.getTitleAt(timeLineTabbedPane.getSelectedIndex());
				timeLineWatch.resume();
			}
		});
	}

	/**
	 * Gibt den Zeistrahl zu einer TabbedPane zurück
	 * 
	 * @return ArrayList mit dem Zeistrahl
	 */
	public ArrayList<FileTime> getTimeLine() {
		timeLine.add(new FileTime(selectedTitle, timeLineWatch.getTime()));
		return timeLine;
	}

	/**
	 * Methode um den Zeitnehmer an einer TabbedPane zu setzen
	 * 
	 * @param tabbedPane
	 *            die TabbedPane, welche überwacht werden soll
	 */
	public void startFileTime(JTabbedPane tabbedPane) {
		clocks = new ArrayList<Watch>();
		this.fileTimesTabbedPane = tabbedPane;
		startTimeMeasure();
	}

	/**
	 * Startet das Zeitmessen und verwaltet es
	 */
	private void startTimeMeasure() {
		// derzeitige Tabs holen, falls verspätet gestartet
		for (int i = 0; i < fileTimesTabbedPane.getComponentCount(); i++) {
			addFileTime(i);
		}

		lastClock = fileTimesTabbedPane.getSelectedIndex();

		fileTimesTabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				//Prüfen ob noch ein Tab offen
				if(fileTimesTabbedPane.getComponentCount()>=1) {
					// letzte Clock pausieren und neue starten
					String title = fileTimesTabbedPane
							.getTitleAt(fileTimesTabbedPane.getSelectedIndex());
					int index = fileTimesTabbedPane.getSelectedIndex();
					for (int i = 0; i < fileTimes.size(); i++) {
						if (title.equals(fileTimes.get(i).getFile())) {
							index = i;
						}
					}
	
					// int i = tabbedPane.getSelectedIndex();
					if (lastClock >= 0) {
						clocks.get(lastClock).pause();
						fileTimes.get(lastClock).setTime(
								clocks.get(lastClock).getTime());
					}
	
					lastClock = index;
	
					if (index >= fileTimes.size()) {
						addFileTime(index);
					}
					clocks.get(index).resume();
				} else {
					//letzte Uhrzeit setzen
					fileTimes.get(lastClock).setTime(clocks.get(lastClock).getTime());					
				}
			}
		});

		// derzeitige Uhrzeit starten
		if (lastClock >= 0) {
			clocks.get(lastClock).resume();
		}
	}

	/**
	 * Gibt die aktuelle Zeitmessung zurück
	 * 
	 * @return ArrayList mit den Namen und Zeiten
	 */
	public ArrayList<FileTime> getFileTime() {
		// letzte Zeit noch aktualisieren
		if (lastClock >= 0) {
			fileTimes.get(lastClock).setTime(clocks.get(lastClock).getTime());
		}
		return fileTimes;
	}

	/**
	 * Hilfsfunktion, welche einen Tab zur Zeitmessung hinzufügt
	 * 
	 * @param index
	 *            Welcher Tab hinzugefügt werden soll
	 */
	private void addFileTime(int index) {
		fileTimes.add(new FileTime(fileTimesTabbedPane.getTitleAt(index), 0));
		clocks.add(new Watch());
		clocks.get(index).start();
		clocks.get(index).pause();
	}

	/**
	 * Klasse zur Kapselung eines Dateinamen mit einer Zeit
	 * 
	 * @author Markus Köppen, Andreas Hasselberg
	 * 
	 */
	public class FileTime {
		String file;
		long time;

		/**
		 * Leer-Konstruktor
		 */
		public FileTime() {
			file = "";
			time = 0;
		}

		/**
		 * Konstruktor mit Startwerten
		 * 
		 * @param file
		 *            Dateiname
		 * @param time
		 *            Bisherige Zeit
		 */
		public FileTime(String file, long time) {
			this.file = file;
			this.time = time;
		}

		/**
		 * Gibt den Inhalt als String zurück
		 * 
		 * @return String, welcher den Inhalt repräsentiert
		 */
		public String toString() {
			return file + ": " + time;
		}

		/**
		 * Liefert den Dateinamen zurück
		 * 
		 * @return Dateiname
		 */
		public String getFile() {
			return file;
		}

		/**
		 * liefert die aktuelle Zeit zurück
		 * 
		 * @return Zeit
		 */
		public long getTime() {
			return time;
		}

		/**
		 * Setzt die Zeit auf einen bestimmten wert
		 * 
		 * @param time
		 *            Wert auf den die Zeit gesetzt werden soll
		 */
		public void setTime(long time) {
			this.time = time;
		}
	}
}
