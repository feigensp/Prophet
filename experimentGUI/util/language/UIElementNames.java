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
	public static String MESSAGE_ERROR = "Error";
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
	public static String MESSAGE_RELATIVE_PATH_NOTIFICATION = "Please note:\nYour action created a path relative to the current working directory.\nIf your XML file is not in the current working directory, you have to adjust the created path.";
	public static String MESSAGE_PATH_DOES_NOT_EXIST = "Path defined in the experiment does not exist";
	public static String TITLE_CODE_VIEWER = "Source code";
	
	//Mail-Plugin
	public static String MAIL_SEND_MAIL = "Send Mail";
	public static String MAIL_SMTP_SERVER = "SMTP server";
	public static String MAIL_SMTP_USER = "SMPT user";
	public static String MAIL_SMTP_PASSWORD = "SMPT password";
	public static String MAIL_SMTP_SENDER = "Sender address";
	public static String MAIL_SMTP_RECIPIENT = "Recipient address";
	public static String MAIL_MESSAGE_COULD_NOT_SEND_MAIL = "Could not send mail. Please notify experimenter.";
	
	//QuestionListPlugin
	public static String QUESTION_LIST_SHOW_LIST = "Show list of questions.";
	
	//ValidSubjectCodePlugin
	public static String SUBJECT_CODE_CHECK_SUBJECT_CODE = "Check subject code.";
	public static String SUBJECT_CODE_VALID_CODES = "valid codes (optional)";
	public static String SUBJECT_CODE_CODE_FILE = "File with subject codes (optional)";
	public static String SUBJECT_CODE_IGNORE_CASE = "Ignore upper and lower case";
	public static String SUBJECT_CODE_MESSAGE_FILE_NOT_FOUND = "Could not find file with subject codes";
	public static String SUBJECT_CODE_MESSAGE_CODE_NOT_FOUND = "Could not find subject code";
	
	//EditAndSavePlugin
	public static String EDIT_AND_SAVE_EDITABLE_CODE = "Allow edits to source code";
	public static String EDIT_AND_SAVE_SAVE = "Save";
	public static String EDIT_AND_SAVE_SAVE_ALL = "Save all";
	public static String EDIT_AND_SAVE_DIALOG_SAVE_CHANGES = "Save changes";
	
	//LineNumbersPlugin
	public static String LINE_NUMBER_SHOW_LINE_NUMBERS = "Show line numbers";
	
	//OpenedFromStartPlugin
	public static String OPENED_FROM_START_OPEN_FILE_ON_START = "Open file on start";
	public static String OPENED_FROM_START_FILE_TO_OPEN = "File to be opened (relative to path of source code)";
	
	//SearchBarPlugin
	public static String SEARCH_BAR_ENABLE_SEARCH = "Enable search";
	public static String SEARCH_BAR_DEACTIVATE_REGULAR_EXPRESSIONS = "Deactivate regular expressions";
	public static String SEARCH_BAR_ACTIVATE_GLOBAL_SEARCH = "Activate global search";
	public static String SEARCH_BAR_MENU_SEARCH = "Search";
	public static String SEARCH_BAR_MENU_GLOBAL_SEARCH = "Search globally";
	
	//ShowCIDECodePlugin
	public static String CIDE_HIGHLIGHT_SOURCE_CODE = "Highlight annotated source code with background colors (only avaible if source code is not editable)";
	public static String CIDE_COLOR_SELECTION = "Color selection";
	
	//SyntaxHighlightingPluign
	public static String SYNTAX_HIGHLIGHTING_ENABLE = "Enable syntax highlighting";
	
	//Recorder
	public static String RECORDER_TIME_INTERVAL_FOR_SUMMARY = "Time limit (in ms, e.g. 1000)";
	
	//Recorder.ChangePlugin
	public static String RECORDER_CHANGE_SOURCE_CODE_EDITS = "Edits to source code";
	public static String RECORDER_CHANGE_SUMMARIZE_CHANGES = "Summarize changes";

	//Recorder.ScrollingPlugin
	public static String RECORDER_SCROLL_SCROLLING_BEHAVIOR = "Scrolling behavior";
	public static String RECORDER_SCROLL_SUMMARIZE_SCROLLING = "Summarize scrolling";
	
	//EditorTabbedPane
	public static String EDITOR_TABBED_PANE_MESSAGE_TITLE_ERROR = "Error";
	public static String EDITOR_TABBED_PANE_MESSAGE_ERROR_COULD_NOT_OPEN_FILE = "Could not automatically open file";
	
	//PHPExportComponent
	public static String PHP_PHP_EXPORT = "PHP export";
	public static String PHP_HOST = "Host";
	public static String PHP_NAME_OF_DATABASE = "Name of database";
	public static String PHP_USER_NAME = "User name";
	public static String PHP_PASSWORD = "Password";
	public static String PHP_EXPORT_SCRIPT = "Export PHP script";
	public static String PHP_DIALOG_UNENCRYPTED_PASSWORD_CONTINUE = "Password will be stored unencrypted. Continue?";
	public static String PHP_DIALOG_TITLE_CONFIRM = "Confirmation";
	public static String PHP_MESSAGE_EXPORT_FINISHED = "Finished";
	
	//MacroEditor
	public static String MACRO_EDITOR_NEW_MACRO = "New macro";
	public static String MACRO_EDITOR_DELETE_MACRO = "Delete macro";
	public static String MACRO_EDITOR_ENTER_MACRO_NAME = "Enter macro name";
	
	//QuestionTreeHTMLHandler
	public static String QUESTION_TREE_HTML_HANDLER_MESSAGE_ERROR_WHILE_READING = "Error while reading form names from XML file";
	public static String QUESTION_TREE_XML_MESSAGE_ERROR_WHILE_WRITING_CSV = "Error while writing CSV file";
	
	//GlobalSearchBar
	public static String GLOBAL_SEARCH_BAR_CASE_SENSITIVE = "Case sensitive"; 
	public static String GLOBAL_SEARCH_BAR_SEARCH = "Find";
	public static String GLOBAL_SEARCH_BAR_REGULAR_EXPRESSION = "Regular expression";
	
	//Searchbar
	public static String SEARCH_BAR_CASE_SENSITIVE = "Case sensitive";
	public static String SEARCH_BAR_SEARCH_FORWARD = "Forward";
	public static String SEARCH_BAR_SEARCH_BACKWARD = "Backward";
	public static String SEARCH_BAR_REGULAR_EXPRESSION = "Regular expression";
	public static String SEARCH_BAR_MESSAGE_TEXT_NOT_FOUND = "Text not found";
	
	
	
	
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
		MESSAGE_ERROR = labels.getString("MESSAGE_FILE_NOT_FOUND_TITLE");
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
		MESSAGE_RELATIVE_PATH_NOTIFICATION = labels.getString("MESSAGE_RELATIVE_PATH_NOTIFICATION");
		MESSAGE_PATH_DOES_NOT_EXIST = labels.getString("MESSAGE_PATH_DOES_NOT_EXIST");
		TITLE_CODE_VIEWER = labels.getString("TITLE_CODE_VIEWER");
		
		//Mail-Plugin
		MAIL_SEND_MAIL  = labels.getString("MAIL_SEND_MAIL");
		MAIL_SMTP_SERVER = labels.getString("MAIL_SMTP_SERVER");
		MAIL_SMTP_USER = labels.getString("MAIL_SMTP_USER");
		MAIL_SMTP_PASSWORD = labels.getString("MAIL_SMTP_PASSWORD");
		MAIL_SMTP_SENDER = labels.getString("MAIL_SMTP_SENDER");
		MAIL_SMTP_RECIPIENT	= labels.getString("MAIL_SMTP_RECIPIENT");
		MAIL_MESSAGE_COULD_NOT_SEND_MAIL = labels.getString("MAIL_MESSAGE_COULD_NOT_SEND_MAIL");
		
		//QuestionListPlugin
		QUESTION_LIST_SHOW_LIST = labels.getString("QUESTION_LIST_SHOW_LIST");
		
		//ValidSubjectCodePlugin
		SUBJECT_CODE_CHECK_SUBJECT_CODE = labels.getString("SUBJECT_CODE_CHECK_SUBJECT_CODE");
		SUBJECT_CODE_VALID_CODES = labels.getString("SUBJECT_CODE_VALID_CODES");
		SUBJECT_CODE_CODE_FILE = labels.getString("SUBJECT_CODE_CODE_FILE");
		SUBJECT_CODE_IGNORE_CASE = labels.getString("SUBJECT_CODE_IGNORE_CASE");
		SUBJECT_CODE_MESSAGE_FILE_NOT_FOUND = labels.getString("SUBJECT_CODE_MESSAGE_FILE_NOT_FOUND");
		SUBJECT_CODE_MESSAGE_CODE_NOT_FOUND = labels.getString("SUBJECT_CODE_MESSAGE_CODE_NOT_FOUND");
		
		//EditAndSavePlugin
		EDIT_AND_SAVE_EDITABLE_CODE = labels.getString("EDIT_AND_SAVE_EDITABLE_CODE");
		EDIT_AND_SAVE_SAVE = labels.getString("EDIT_AND_SAVE_SAVE");
		EDIT_AND_SAVE_SAVE_ALL = labels.getString("EDIT_AND_SAVE_SAVE_ALL");
		EDIT_AND_SAVE_DIALOG_SAVE_CHANGES = labels.getString("EDIT_AND_SAVE_DIALOG_SAVE_CHANGES");

		//LineNumbersPlugin
		LINE_NUMBER_SHOW_LINE_NUMBERS = labels.getString("LINE_NUMBER_SHOW_LINE_NUMBERS");
		
		//OpenedFromStartPlugin
		OPENED_FROM_START_OPEN_FILE_ON_START = labels.getString("OPENED_FROM_START_OPEN_FILE_ON_START");
		OPENED_FROM_START_FILE_TO_OPEN = labels.getString("OPENED_FROM_START_FILE_TO_OPEN");
		
		//SearchBarPlugin
		SEARCH_BAR_ENABLE_SEARCH = labels.getString("SEARCH_BAR_ENABLE_SEARCH");
		SEARCH_BAR_DEACTIVATE_REGULAR_EXPRESSIONS = labels.getString("SEARCH_BAR_DEACTIVATE_REGULAR_EXPRESSIONS");
		SEARCH_BAR_ACTIVATE_GLOBAL_SEARCH = labels.getString("SEARCH_BAR_ACTIVATE_GLOBAL_SEARCH");
		SEARCH_BAR_MENU_SEARCH = labels.getString("SEARCH_BAR_MENU_SEARCH");
		SEARCH_BAR_MENU_GLOBAL_SEARCH = labels.getString("SEARCH_BAR_MENU_SEARCH");
		
		//ShowCIDECodePlugin
		CIDE_HIGHLIGHT_SOURCE_CODE = labels.getString("CIDE_HIGHLIGHT_SOURCE_CODE");
		CIDE_COLOR_SELECTION = labels.getString("CIDE_COLOR_SELECTION");
		
		//SyntaxHighlightingPlugin
		SYNTAX_HIGHLIGHTING_ENABLE = labels.getString("SYNTAX_HIGHLIGHTING_ENABLE");
		
		//Recorder.ChangePlugin
		RECORDER_CHANGE_SOURCE_CODE_EDITS = labels.getString("RECORDER_CHANGE_SOURCE_CODE_EDITS");
		RECORDER_CHANGE_SUMMARIZE_CHANGES = labels.getString("RECORDER_CHANGE_SUMMARIZE_CHANGES");
		RECORDER_TIME_INTERVAL_FOR_SUMMARY = labels.getString("RECORDER_TIME_INTERVAL_FOR_SUMMARY");
		
		//Recorder.ScrollingPlugin
		RECORDER_SCROLL_SCROLLING_BEHAVIOR = labels.getString("RECORDER_SCROLL_SCROLLING_BEHAVIOR");
		RECORDER_SCROLL_SUMMARIZE_SCROLLING = labels.getString("RECORDER_SCROLL_SUMMARIZE_SCROLLING");
		
		//EditorTabbedPane
		EDITOR_TABBED_PANE_MESSAGE_TITLE_ERROR = labels.getString("EDITOR_TABBED_PANE_MESSAGE_TITLE_ERROR");
		EDITOR_TABBED_PANE_MESSAGE_ERROR_COULD_NOT_OPEN_FILE = labels.getString("EDITOR_TABBED_PANE_MESSAGE_ERROR_COULD_NOT_OPEN_FILE");
		
		//PHPExportComponent
		PHP_PHP_EXPORT = labels.getString("PHP_PHP_EXPORT");
		PHP_HOST = labels.getString("PHP_HOST");
		PHP_NAME_OF_DATABASE = labels.getString("PHP_NAME_OF_DATABASE");
		PHP_USER_NAME = labels.getString("PHP_USER_NAME");
		PHP_PASSWORD = labels.getString("PHP_PASSWORD");
		PHP_EXPORT_SCRIPT = labels.getString("PHP_EXPORT_SCRIPT");
		PHP_DIALOG_UNENCRYPTED_PASSWORD_CONTINUE = labels.getString("PHP_DIALOG_UNENCRYPTED_PASSWORD_CONTINUE");
		PHP_DIALOG_TITLE_CONFIRM = labels.getString("PHP_DIALOG_TITLE_CONFIRM");
		PHP_MESSAGE_EXPORT_FINISHED = labels.getString("PHP_MESSAGE_EXPORT_FINISHED");
		
		//MacroEditor
		MACRO_EDITOR_NEW_MACRO = labels.getString("MACRO_EDITOR_NEW_MACRO");
		MACRO_EDITOR_DELETE_MACRO = labels.getString("MACRO_EDITOR_DELETE_MACRO");
		MACRO_EDITOR_ENTER_MACRO_NAME = labels.getString("MACRO_EDITOR_ENTER_MACRO_NAME");
		
		//QuestionTreeHTMLHandler
		QUESTION_TREE_HTML_HANDLER_MESSAGE_ERROR_WHILE_READING = labels.getString("QUESTION_TREE_HTML_HANDLER_MESSAGE_ERROR_WHILE_READING");
		QUESTION_TREE_XML_MESSAGE_ERROR_WHILE_WRITING_CSV = labels.getString("QUESTION_TREE_XML_MESSAGE_ERROR_WHILE_WRITING_CSV");
		
		//GlobalSearchBar
		GLOBAL_SEARCH_BAR_CASE_SENSITIVE = labels.getString("GLOBAL_SEARCH_BAR_CASE_SENSITIVE");
		GLOBAL_SEARCH_BAR_SEARCH = labels.getString("GLOBAL_SEARCH_BAR_SEARCH");
		GLOBAL_SEARCH_BAR_REGULAR_EXPRESSION = labels.getString("GLOBAL_SEARCH_BAR_REGULAR_EXPRESSION");
		
		//SearchBar
		SEARCH_BAR_CASE_SENSITIVE = labels.getString("SEARCH_BAR_CASE_SENSITIVE");
		SEARCH_BAR_SEARCH_FORWARD = labels.getString("SEARCH_BAR_SEARCH_FORWARD");
		SEARCH_BAR_SEARCH_BACKWARD = labels.getString("SEARCH_BAR_SEARCH_BACKWARD");
		SEARCH_BAR_REGULAR_EXPRESSION = labels.getString("SEARCH_BAR_REGULAR_EXPRESSION");
		SEARCH_BAR_MESSAGE_TEXT_NOT_FOUND = labels.getString("SEARCH_BAR_MESSAGE_TEXT_NOT_FOUND");
	}
}
