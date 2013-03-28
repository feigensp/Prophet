package experimentGUI.util.language;

import java.util.Locale;
import java.util.ResourceBundle;

public class UIElementNames {

	public static String MENU_FILE = "File";
	public static String MENU_FILE_NEW = "New";
	public static String MENU_FILE_OPEN = "Open";
	public static String MENU_FILE_SAVE = "Save";
	public static String MENU_FILE_SAVE_AS = "Save as...";
	public static String MENU_FILE_QUIT = "Quit";

	public static String MENU_EDIT = "Edit";
	public static String MENU_EDIT_FIND = "Search";

	public static void setUIElements(Locale locale) {
		ResourceBundle labels = ResourceBundle.getBundle(
				"experimentGUI.util.language.LabelsBundle", locale);
		MENU_FILE = labels.getString("MENU_FILE");
		MENU_FILE_NEW = labels.getString("MENU_FILE_NEW");
		MENU_FILE_OPEN = labels.getString("MENU_FILE_OPEN");
		MENU_FILE_SAVE = labels.getString("MENU_FILE_SAVE");
		MENU_FILE_SAVE_AS = labels.getString("MENU_FILE_SAVE_AS");
		MENU_FILE_QUIT = labels.getString("MENU_FILE_QUIT");

		MENU_EDIT = labels.getString("MENU_EDIT");
		MENU_EDIT_FIND = labels.getString("MENU_EDIT_FIND");

	}
}
