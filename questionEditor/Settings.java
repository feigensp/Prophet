package questionEditor;

/**
 * A small class, it only stores an final arraylist with strings.
 * Every String represents a feature which could be selected oder deselected (in settingsDialog)
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import java.util.ArrayList;

public class Settings {

	public static final ArrayList<String> settings = new ArrayList<String>() {
		{
			add("Editierbar");
			add("Suchfunktion");
			add("Syntaxhighlighting");
			add("Zeilennummern");
		}
	};
}
