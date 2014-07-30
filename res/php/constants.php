<?php
	define("KEY_NAME", "name");
	define("KEY_VALUE", "value");
	define("NAME_DONOTSHOWCONTENT", "donotshowcontent");
	define("NAME_INACTIVE", "inactive");
	define("NAME_QUESTIONSWITCHING", "questionswitching");
	define("NAME_NOTES", "notes");
	define("NAME_CODE", "experimentcode");
	define("VALUE_TRUE", "true");
	define("VALUE_FALSE", "false");
	define("NODE_ATTRIBUTES", "attributes");
	define("NODE_ATTRIBUTE", "attribute");
	define("NODE_CHILDREN", "children");
	define("NODE_EXPERIMENT", "experiment");
	define("NODE_CATEGORY", "category");
	define("NODE_QUESTION", "question");
	
	//HTML-Keys - DO NOT USE THEM AS NAMES FOR YOUR
	define("KEY_FORWARD", "nextQuestion");
	define("KEY_EXPERIMENT_CODE", "experiment_code");
	define("KEY_SUBJECT_CODE", "subject_code");	
	define("KEY_SUBJECT", "subject");
	define("KEY_EXPERIMENT", "experiment");
	define("KEY_CATEGORY", "category");
	define("KEY_QUESTION", "question");	
	define("KEY_TIME", "time");
	
	//HTML-Placeholder
	define("HTML_START", "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" /></head><body><form  method=\"post\" action=\"index.php\">");
	define("HTML_DIVIDER", "<br /><br /><hr /><br /><br />");
	define("FOOTER_FORWARD_CAPTION", "Weiter");
	define("FOOTER_END_CATEGORY_CAPTION", "Kategorie Abschließen");
	define("FOOTER_START_EXPERIMENT_CAPTION", "Experiment starten");
	define("FOOTER_FORWARD", "<input name =\"".KEY_FORWARD."\" type=\"submit\" value=\"".FOOTER_FORWARD_CAPTION."\" />");
	define("FOOTER_END_CATEGORY", "<input name =\"".KEY_FORWARD."\" type=\"submit\" value=\"".FOOTER_END_CATEGORY_CAPTION."\" />");
	define("FOOTER_START_EXPERIMENT", "<table><tr><td>Probandencode:</td><td><input name=\"".KEY_SUBJECT_CODE."\" /></td></tr></table>".HTML_DIVIDER."<input name =\"".KEY_FORWARD."\" type=\"submit\" value=\"".FOOTER_START_EXPERIMENT_CAPTION."\" />");
	define("HTML_END", "</form></body></html>");
?>