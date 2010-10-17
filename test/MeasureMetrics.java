/**
 * Klasse zum Messen von Softwaremetriken eines Textes
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package test;

public class MeasureMetrics {

	/**
	 * Gibt die Anzahl der Codezeilen zurück
	 * 
	 * @param text
	 *            Text dessen Zeilen gezählt werden sollen
	 * @return Anzahl der Zeilen
	 */
	public int getLOC(String text) {
		return text.length() - text.replaceAll("\n", "").length();
	}

}
