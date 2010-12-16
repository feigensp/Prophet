package experimentGUI.util;

import java.util.HashMap;

public class Language {
	
	private static HashMap<String, HashMap<String, String>> text;
	public static final String XML_PATH = "languages.xml";
	public static final String LAN_ENGLISH = "english";
	public static final String LAN_DEUTSCH = "deutsch";
	
	public Language() {
		text = new HashMap<String, HashMap<String, String>>();
	}
	
	public static void loadSpecifications() {		
	}
	
	public static void setLanguage(String language) {
	}
	
	public static String getValue(String key) {
		return null;
	}
}
