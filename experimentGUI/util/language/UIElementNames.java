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
	public static String FONT_FACE_BOLD = "Bold";
	public static String FONT_FACE_ITALIC = "Italic";
	public static String FONT_FACE_UNDERLINE = "Underline";
	public static String MENU_TAB_EDITOR_FONT_SIZE = "Font size";

	public static String HTML_TEXT_FIELD = "Text field";
	public static String HTML_TEXT_AREA = "Text area";
	public static String HTML_LIST = "List";
	public static String HTML_COMBO_BOX = "Combo box";
	public static String HTML_RADIO_BUTTON = "Radio button";
	public static String HTML_CHECK_BOX = "Check box";

	public static String MENU_TAB_EDITOR_FORMS = "Forms";
	public static String DIALOG_DEFINE_TEXT_FIELD = "Name of text field";
	public static String DIALOG_DEFINE_TEXT_AREA = "Name of text area";
	public static String DIALOG_DEFINE_LIST_INFORMATION = "List information";
	public static String MENU_TAB_EDITOR_MACROS = "Select macro";
	public static String KEYBOARD_CTRL = "Ctrl";

	public static String BUTTON_LABEL_OK = "Ok";
	public static String BUTTON_LABEL_CANCEL = "Cancel";
	public static String BUTTON_LABEL_FIND = "Find...";
	public static String EXPERIMENT_CODE = "Experiment code";

	public static String FOOTER_FORWARD_CAPTION = "Next";
	public static String FOOTER_BACKWARD_CAPTION = "Back";
	public static String FOOTER_END_CATEGORY_CAPTION = "Finish category";
	public static String FOOTER_START_EXPERIMENT_CAPTION = "Start experiment";
	public static String FOOTER_SUBJECT_CODE_CAPTION = "Subject code:";
	
	public static String MESSAGE_COULD_NOT_START_BROWSER = "Could not start browser";
	public static String MESSAGE_INVALID_URL = "Invalid URL";
	public static String MESSAGE_COULD_NOT_OPEN_STANDARD_BROWSER = "Could not open standard browser";

	public static String MENU_TAB_SETTINGS_DONT_SHOW_CONTENT = "Don't show content";
	public static String MENU_TAB_SETTINGS_ALLOW_BACK_AND_FORTH = "Allow back and forth";
	public static String MENU_TAB_SETTINGS_DEACTIVATE_NODES = "Deactivate this node and all sub nodes";
	public static String MENU_TAB_SETTINGS_DEACTIVATE_THIS_NODE = "Deactivate this node";
	public static String MENU_TAB_SETTINGS_REQUIRED_ANSWERS = "Required answers";
	public static String MENU_TAB_SETTINGS_REQUIRED_ANSWER_COMPONENTS = "Components that must be contained in answers (one per line)"; 
	public static String MENU_TAB_SETTINGS_MESSAGE_FILL_ALL_FIELDS = "Please complete all required fields";
	public static String MENU_TAB_SETTINGS_TIME_OUT = "Time out";
	public static String MENU_TAB_SETTINGS_MAX_TIME_EXPERIMENT = "Max time for complete experiment (in minutes)";
	public static String MENU_TAB_SETTINGS_MAX_TIME_CATEGORY = "Max time for category (in seconds)";
	public static String MENU_TAB_SETTINGS_MAX_TIME_QUESTION = "Max time for question (in seconds)";
	public static String MENU_TAB_SETTINGS_HARD_TIME_OUT = "Abort question when time is up (hard time out)";
	public static String MENU_TAB_SETTINGS_TIME_OUT_WARN_SUBJECTS = "Notify participants that time is about to run out";
	public static String MENU_TAB_SETTINGS_TIME_OUT_WARNING_TIME = "Seconds between notification and time out";
	public static String MENU_TAB_SETTINGS_TIME_OUT_WARNING_MESSAGE = "Notification message";
	public static String MENU_TAB_SETTINGS_TIME_OUT_MESSAGE = "Message after time out (optional)";
	public static String MENU_TAB_SETTINGS_IGNORE_TIME_OUT = "Ignore experiment time out for this and all sub nodes";
	public static String MENU_TAB_SETTINGS_ACTIVATE_CODE_VIEWER = "Activate code viewer";
	public static String MENU_TAB_SETTINGS_SOURCE_CODE_PATH = "Path of source code";
	public static String MENU_TAB_SETTINGS_EXTERNAL_PROGRAMS = "Start external programs";
	public static String MENU_TAB_SETTINGS_PATH_OF_EXTERNAL_PROGRAMS = "Path of external programs (one per line)";
	public static String MENU_TAB_SETTINGS_EXTERNAL_PROGRAMS_TITLE = "Programs";
	public static String MESSAGE_ONLY_ONE_INSTANCE = "This feature can only be used after the existing instance is closed";
	public static String MESSAGE_COULD_NOT_START_PROGRAM = "Could not start program";
	
	
	
	
	
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
		FONT_FACE_BOLD = labels.getString("FONT_FACE_BOLD");
		FONT_FACE_ITALIC = labels.getString("FONT_FACE_ITALIC");
		FONT_FACE_UNDERLINE = labels.getString("FONT_FACE_UNDERLINE");
		MENU_TAB_EDITOR_FONT_SIZE = labels.getString("MENU_TAB_EDITOR_FONT_SIZE");

		HTML_TEXT_FIELD = labels.getString("HTML_TEXT_FIELD");
		HTML_TEXT_AREA = labels.getString("HTML_TEXT_AREA");
		HTML_LIST = labels.getString("HTML_LIST");
		HTML_COMBO_BOX = labels.getString("HTML_COMBO_BOX");
		HTML_RADIO_BUTTON = labels.getString("HTML_RADIO_BUTTON");
		HTML_CHECK_BOX = labels.getString("HTML_CHECK_BOX");
		
		MENU_TAB_EDITOR_FORMS = labels.getString("MENU_TAB_EDITOR_FORMS");
		DIALOG_DEFINE_TEXT_FIELD = labels.getString("DIALOG_DEFINE_TEXT_FIELD");
		DIALOG_DEFINE_TEXT_AREA = labels.getString("DIALOG_DEFINE_TEXT_AREA");
		DIALOG_DEFINE_LIST_INFORMATION = labels.getString("DIALOG_DEFINE_LIST_INFORMATION");
		
		MENU_TAB_EDITOR_MACROS = labels.getString("MENU_TAB_EDITOR_MACROS");
		KEYBOARD_CTRL = labels.getString("KEYBOARD_CTRL");
		BUTTON_LABEL_OK = labels.getString("BUTTON_LABEL_OK");
		BUTTON_LABEL_CANCEL = labels.getString("BUTTON_LABEL_CANCEL");
		BUTTON_LABEL_FIND = labels.getString("BUTTON_LABEL_FIND");
		EXPERIMENT_CODE = labels.getString("EXPERIMENT_CODE");

		FOOTER_FORWARD_CAPTION = labels.getString("FOOTER_FORWARD_CAPTION");
		FOOTER_BACKWARD_CAPTION = labels.getString("FOOTER_BACKWARD_CAPTION");
		FOOTER_END_CATEGORY_CAPTION = labels.getString("FOOTER_END_CATEGORY_CAPTION");
		FOOTER_START_EXPERIMENT_CAPTION = labels.getString("FOOTER_START_EXPERIMENT_CAPTION");
		FOOTER_SUBJECT_CODE_CAPTION = labels.getString("FOOTER_SUBJECT_CODE_CAPTION");
		
		MESSAGE_COULD_NOT_START_BROWSER = labels.getString("MESSAGE_COULD_NOT_START_BROWSER");
		MESSAGE_INVALID_URL = labels.getString("MESSAGE_INVALID_URL");
		MESSAGE_COULD_NOT_OPEN_STANDARD_BROWSER = labels.getString("MESSAGE_COULD_NOT_OPEN_STANDARD_BROWSER");

		MENU_TAB_SETTINGS_DONT_SHOW_CONTENT = labels.getString("MENU_TAB_SETTINGS_DONT_SHOW_CONTENT");
		MENU_TAB_SETTINGS_ALLOW_BACK_AND_FORTH = labels.getString("MENU_TAB_SETTINGS_ALLOW_BACK_AND_FORTH");
		MENU_TAB_SETTINGS_DEACTIVATE_NODES = labels.getString("MENU_TAB_SETTINGS_DEACTIVATE_NODES");
		MENU_TAB_SETTINGS_DEACTIVATE_THIS_NODE = labels.getString("MENU_TAB_SETTINGS_DEACTIVATE_THIS_NODE");
		MENU_TAB_SETTINGS_REQUIRED_ANSWERS = labels.getString("MENU_TAB_SETTINGS_REQUIRED_ANSWERS");
		MENU_TAB_SETTINGS_REQUIRED_ANSWER_COMPONENTS = labels.getString("MENU_TAB_SETTINGS_REQUIRED_ANSWER_COMPONENTS");
		MENU_TAB_SETTINGS_MESSAGE_FILL_ALL_FIELDS = labels.getString("MENU_TAB_SETTINGS_MESSAGE_FILL_ALL_FIELDS");
		MENU_TAB_SETTINGS_TIME_OUT = labels.getString("MENU_TAB_SETTINGS_TIME_OUT");
		MENU_TAB_SETTINGS_MAX_TIME_EXPERIMENT = labels.getString("MENU_TAB_SETTINGS_MAX_TIME_EXPERIMENT");
		MENU_TAB_SETTINGS_MAX_TIME_CATEGORY = labels.getString("MENU_TAB_SETTINGS_MAX_TIME_CATEGORY");
		MENU_TAB_SETTINGS_MAX_TIME_QUESTION = labels.getString("MENU_TAB_SETTINGS_MAX_TIME_QUESTION");
		MENU_TAB_SETTINGS_HARD_TIME_OUT = labels.getString("MENU_TAB_SETTINGS_HARD_TIME_OUT");
		MENU_TAB_SETTINGS_TIME_OUT_WARN_SUBJECTS = labels.getString("MENU_TAB_SETTINGS_TIME_OUT_WARN_SUBJECTS");
		MENU_TAB_SETTINGS_TIME_OUT_WARNING_TIME = labels.getString("MENU_TAB_SETTINGS_TIME_OUT_WARNING_TIME");
		MENU_TAB_SETTINGS_TIME_OUT_WARNING_MESSAGE = labels.getString("MENU_TAB_SETTINGS_TIME_OUT_WARNING_MESSAGE");
		MENU_TAB_SETTINGS_TIME_OUT_MESSAGE = labels.getString("MENU_TAB_SETTINGS_TIME_OUT_MESSAGE");
		MENU_TAB_SETTINGS_IGNORE_TIME_OUT = labels.getString("MENU_TAB_SETTINGS_IGNORE_TIME_OUT");
		MENU_TAB_SETTINGS_ACTIVATE_CODE_VIEWER = labels.getString("MENU_TAB_SETTINGS_ACTIVATE_CODE_VIEWER");
		MENU_TAB_SETTINGS_SOURCE_CODE_PATH = labels.getString("MENU_TAB_SETTINGS_SOURCE_CODE_PATH");
		MENU_TAB_SETTINGS_EXTERNAL_PROGRAMS = labels.getString("MENU_TAB_SETTINGS_EXTERNAL_PROGRAMS");
		MENU_TAB_SETTINGS_PATH_OF_EXTERNAL_PROGRAMS = labels.getString("MENU_TAB_SETTINGS_PATH_OF_EXTERNAL_PROGRAMS");
		MENU_TAB_SETTINGS_EXTERNAL_PROGRAMS_TITLE = labels.getString("MENU_TAB_SETTINGS_EXTERNAL_PROGRAMS_TITLE");
		MESSAGE_ONLY_ONE_INSTANCE = labels.getString("MESSAGE_ONLY_ONE_INSTANCE");
		MESSAGE_COULD_NOT_START_PROGRAM = labels.getString("MESSAGE_COULD_NOT_START_PROGRAM");
		
		
	}
}
