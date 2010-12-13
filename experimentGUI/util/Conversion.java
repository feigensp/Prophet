package experimentGUI.util;

public class Conversion {

	
	public static String toWindowsPath(String path) {
		if(path != null && System.getProperty("file.separator").equals("\\")) {
			return path.replaceAll("/", "\\");
		} 
		return path;
	}
}
