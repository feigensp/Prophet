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

	public static String MESSAGE_FILE_NOT_FOUND = "File not found";
	public static String MESSAGE_FILE_NOT_FOUND_TITLE = "Error";
	public static String MESSAGE_REPLACE_FILE = " already exists.\nReplace it?";
	public static String MESSAGE_REPLACE_FILE_TITLE = "Confirm save as";
	public static String MESSAGE_NO_VALID_EXPERIMENT_FILE = "No valid experiment file";

	public static String MENU_EXPORT = "Export";
	public static String MENU_ITEM_HTML_OF_QUESTIONS = "Html file of questions";
	public static String MENU_ITEM_CSV_OF_ANSWERS;

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

		/**
		 * String constants for (error) messages
		 */
		MESSAGE_FILE_NOT_FOUND = labels.getString("MESSAGE_FILE_NOT_FOUND");
		MESSAGE_FILE_NOT_FOUND_TITLE = labels.getString("MESSAGE_FILE_NOT_FOUND_TITLE");
		MESSAGE_REPLACE_FILE = labels.getString(" MESSAGE_REPLACE_FILE");
		MESSAGE_REPLACE_FILE_TITLE = labels.getString("MESSAGE_REPLACE_FILE_TITLE");
		MESSAGE_NO_VALID_EXPERIMENT_FILE = labels.getString("MESSAGE_NO_VALID_EXPERIMENT_FILE");
		MENU_EXPORT = labels.getString("MENU_EXPORT");
		MENU_ITEM_HTML_OF_QUESTIONS  = labels.getString("MENU_ITEM_HTML_OF_QUESTIONS");
		MENU_ITEM_CSV_OF_ANSWERS = labels.getString("MENU_ITEM_CSV_OF_ANSWERS");
	}
}
