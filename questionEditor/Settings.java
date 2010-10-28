package questionEditor;

import java.util.ArrayList;

public class Settings {
	
	private static ArrayList<String> settings = new ArrayList<String>() {
		{
			add("Editierbar");
			add("Suchfunktion");
			add("Syntaxhighlighting");
			add("Zeilennummern");
		}
	};
	
	public static ArrayList<String> getSettings() {
		return settings;
	}
}
