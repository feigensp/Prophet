package experimentGUI.util.language;

import java.util.Locale;
import java.util.ResourceBundle;

public class UIElementNames {

	/**
	 * Menu items and elements
	 */
	public static String MENU_FILE = "File";
	public static String MENU_FILE_NEW = "New";
	public static String MENU_FILE_OPEN = "Open";
	public static String MENU_FILE_SAVE = "Save";
	public static String MENU_FILE_SAVE_AS = "Save as...";
	public static String MENU_FILE_QUIT = "Quit";

	public static String MENU_EDIT = "Edit";
	public static String MENU_EDIT_FIND = "Search";

	public static String MENU_EXPORT = "Export";
	public static String MENU_ITEM_HTML_OF_QUESTIONS = "Html file of questions";
	public static String MENU_ITEM_CSV_OF_ANSWERS = "CSV file of answers";

	public static String MENU_PLAUSIBILITY_FEATURES = "Check";

	/**
	 * String constants for (error) messages
	 */
	public static String MESSAGE_FILE_NOT_FOUND = "File not found";
	public static String MESSAGE_FILE_NOT_FOUND_TITLE = "Error";
	public static String MESSAGE_REPLACE_FILE = " already exists.\nReplace it?";
	public static String MESSAGE_REPLACE_FILE_TITLE = "Confirm save as";
	public static String MESSAGE_NO_VALID_EXPERIMENT_FILE = "No valid experiment file";

	public static String MESSAGE_PATH_NOT_FOUND = "Could not find path of file";
	public static String MENU_ITEM_CHECK_FORM_NAMES = "Check form names for duplicates";
	
	public static String MESSAGE_DUPLICATE_NO_VALUE = "no value";
	public static String MESSAGE_DUPLICATE_APPEARANCE = "Appearance";
	public static String MESSAGE_DUPLICATE_TITLE_DUPLICATES_EXIST = "Form components with same name- and value- Attributes in node:";
	public static String MESSAGE_DUPLICATE_TITLE_NO_DUPLICATES_EXIST = "No duplicates found";
	
	/**
	 * Constants for customization of experiment
	 */
	public static String MENU_TAB_EDITOR = "Editor";
	public static String MENU_TAB_PREVIEW = "Preview";
	public static String MENU_TAB_SETTINGS = "Settings";
	public static String MENU_TAB_NOTES = "Notes";
	
	public static String MENU_TAB_EDITOR_FONT_FACE = "Font face";
	
	
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

		MESSAGE_FILE_NOT_FOUND = labels.getString("MESSAGE_FILE_NOT_FOUND");
		MESSAGE_FILE_NOT_FOUND_TITLE = labels.getString("MESSAGE_FILE_NOT_FOUND_TITLE");
		MESSAGE_REPLACE_FILE = labels.getString("MESSAGE_REPLACE_FILE");
		MESSAGE_REPLACE_FILE_TITLE = labels.getString("MESSAGE_REPLACE_FILE_TITLE");
		MESSAGE_NO_VALID_EXPERIMENT_FILE = labels.getString("MESSAGE_NO_VALID_EXPERIMENT_FILE");
		MESSAGE_PATH_NOT_FOUND = labels.getString("MESSAGE_PATH_NOT_FOUND");
		
		MENU_EXPORT = labels.getString("MENU_EXPORT");
		MENU_ITEM_HTML_OF_QUESTIONS  = labels.getString("MENU_ITEM_HTML_OF_QUESTIONS");
		MENU_ITEM_CSV_OF_ANSWERS = labels.getString("MENU_ITEM_CSV_OF_ANSWERS");
		
		MENU_PLAUSIBILITY_FEATURES = labels.getString("MENU_PLAUSIBILITY_FEATURES");
		MENU_ITEM_CHECK_FORM_NAMES = labels.getString("MENU_ITEM_CHECK_FORM_NAMES");
		MESSAGE_DUPLICATE_NO_VALUE = labels.getString("MESSAGE_DUPLICATE_NO_VALUE");
		MESSAGE_DUPLICATE_APPEARANCE = labels.getString("MESSAGE_DUPLICATE_APPEARANCE");
		MESSAGE_DUPLICATE_TITLE_DUPLICATES_EXIST = labels.getString("MESSAGE_DUPLICATE_TITLE_DUPLICATES_EXIST");
		MESSAGE_DUPLICATE_TITLE_NO_DUPLICATES_EXIST = labels.getString("MESSAGE_DUPLICATE_TITLE_NO_DUPLICATES_EXIST");
		
		MENU_TAB_EDITOR = labels.getString("MENU_TAB_EDITOR");
		MENU_TAB_PREVIEW = labels.getString("MENU_TAB_PREVIEW");
		MENU_TAB_SETTINGS = labels.getString("MENU_TAB_SETTINGS");
		MENU_TAB_NOTES = labels.getString("MENU_TAB_NOTES");
		
		MENU_TAB_EDITOR_FONT_FACE = labels.getString("MENU_TAB_EDITOR_FONT_FACE");
	}
}
