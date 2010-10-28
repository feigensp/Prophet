package questionEditor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Vector;

public class Settings {
	
	private static ElementAttribute[] ea = {
		new ElementAttribute<String>("edit", "Editierbar"), 
		new ElementAttribute<String>("search", "Suchfunktion"),
		new ElementAttribute<String>("linecount", "Zeilennummern"),
		new ElementAttribute<String>("synhigh", "Syntaxhighlighting")};
	private static Vector<ElementAttribute<String>> settings = new Vector<ElementAttribute<String>>((Collection<? extends ElementAttribute<String>>) Arrays.asList(ea));
	
	public static Vector<ElementAttribute<String>> getSettings() {
		return settings;
	}
}
