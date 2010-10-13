/**
 * Klasse welche zu jeden Inhalt (beliebiger Datentyp) einen Identifizierungsnamen bereithält.
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

package experimentQuestionCreator;

public class ElementAttribute<Type> {

	String name;	//Name für das Datum
	Type content;	//Datum
	
	/**
	 * Konstruktor welcher ein ElementAttribute erschafft
	 * @param name Name für das Datum
	 * @param content Datum
	 */
	public ElementAttribute(String name, Type content) {
		this.name = name;
		this.content = content;
	}
	
	/**
	 * Liefert den Namen zurück
	 * @return Name als Datum
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Liefert das Datum zurück
	 * @return Datum als sein Datentyp
	 */
	public Type getContent() {
		return content;
	}
}
