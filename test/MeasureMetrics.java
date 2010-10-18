/**
 * Klasse zum Messen von Softwaremetriken eines Textes
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package test;

import java.util.*;

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

	/**
	 * Gibt die Anzahl der echten Codezeilen zurück
	 * 
	 * @param text
	 *            Text dessen codezeilen gezählt werden sollen
	 * @return Anzahl der Codezeilen
	 */
	public int getLOCOnly(String text) {
		int lineAmount = 0;
		String code = removeComments(text);
		// Alle leeren Zeilen löschen (die nur aus leerzeichen und tabs
		// bestehen)
		String[] lines = code.split("\n");
		for (int i = 0; i < lines.length; i++) {
			lines[i] = lines[i].replaceAll(" ", "");
			lines[i] = lines[i].replaceAll("\t", "");
			lines[i] = lines[i].replaceAll("\f", "");
			lines[i] = lines[i].replaceAll("\r", "");
			if (lines[i].length() != 0) {
				lineAmount++;
			}
		}
		return lineAmount;
	}
	
	/**
	 * Liefert die McCabe Metrik zurück --> Anzahl der binären Verzweigungen + 1
	 * @param text Text dessen McCabe Metrik bestimmt werden soll
	 * @return Anzahl der binären Verzweigungen+1
	 */
	public int getCyclomaticComplexity(String text) {
		int forks = 0;
		String code = removeComments(text);
		code = removeStringText(code);
		int codeLength = code.length();
		//alle if's zählen
		code = code.replaceAll("if", "");
		forks += (codeLength - code.length())/2;
		//alle else zählen
		codeLength = code.length();
		code = code.replaceAll("else", "");
		forks += (codeLength - code.length())/4;
		//alle case zählen
		codeLength = code.length();
		code = code.replaceAll("case", "");
		forks += (codeLength - code.length())/4;
		//alle default zählen
		codeLength = code.length();
		code = code.replaceAll("default", "");
		forks += (codeLength - code.length())/7;		
		return forks+1;
	}
	
	/**
	 * Entfernt alle Texte die in Anführungszeichen stehen
	 * @param text
	 * @return
	 */
	private String removeStringText(String text) {
		String code = text;
		int index = code.indexOf("\"");
		String part1 = "";
		String part2 = "";		
		while(index != -1) {
			part1 = code.substring(0, index-1);
			part2 = code.substring(index+1);
			//nächstes Anführungszeichen suchen vor welchem kein \ steht
			index = part2.indexOf("\"");
			if(index==-1) {
				return part1;
			} else {
				while(index-1 == part2.indexOf("\\\"")) {
					part2 = part2.substring(index+1);
					index = part2.indexOf("\"");
				}
				part2 = part2.substring(index+1);
			}
			code = part1 + part2;
		}		
		return code;
	}
	
	/**
	 * Löscht alle Kommentare aus einem Text
	 * @param text Text der gesäubert werden soll
	 * @return Text ohne Kommentare
	 */
	private String removeComments(String text) {
		String code = text;
		// alle einzeiligen Kommentare löschen (damit man danach ohne diese zu
		// beachten die mehrzeiligen löschen kann)
		String part1 = "";
		String part2 = "";
		int linebreakIndex;
		int commentIndex = code.indexOf("//");
		while (commentIndex != -1) {
			part1 = code.substring(0, commentIndex - 1);
			part2 = code.substring(commentIndex + 2);
			linebreakIndex = part2.indexOf("\n");
			if (linebreakIndex == -1) {
				code = part1;
			} else {
				code = part1 + part2.substring(linebreakIndex + 1);
			}
		}
		// alle mehrzeiligen Kommentare löschen
		int commentEndIndex;
		commentIndex = code.indexOf("/*");
		while (commentIndex != -1) {
			part1 = code.substring(0, commentIndex - 1);
			part2 = code.substring(commentIndex + 2);
			commentEndIndex = part2.indexOf("*/");
			if (commentEndIndex == -1) {
				code = part1;
			} else {
				code = part1 + part2.substring(commentEndIndex + 2);
			}
		}
		return code;
	}

}
