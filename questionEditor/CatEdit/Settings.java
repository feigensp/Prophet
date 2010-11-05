package questionEditor.CatEdit;

/**
 * A small class, it only stores an final arraylist with strings.
 * Every String represents a feature which could be selected oder deselected (in settingsDialog)
 * 
 * @author Markus Köppen, Andreas Hasselberg
 */

import java.util.ArrayList;

public class Settings {
	@SuppressWarnings("serial")
	public static final ArrayList<SettingsOption> settings = new ArrayList<SettingsOption>() {
		{
			add(new SettingsOption("path", new SettingsPathChooser(), "Pfad der Quelltexte:"));
			add(new SettingsOption("editable", new SettingsCheckBox(), "Quelltexte sind editierbar"));
			add(new SettingsOption("searchfunction", new SettingsCheckBox(), "Suchfunktion"));
			add(new SettingsOption("highlighting", new SettingsCheckBox(), "Syntaxhighlighting"));
			add(new SettingsOption("highlightingswitching", new SettingsCheckBox(), "Syntaxhighlighting ein- und ausschaltbar"));
			add(new SettingsOption("linenumbers", new SettingsCheckBox(), "Zeilennummern"));
			add(new SettingsOption("linenumbersswitching", new SettingsCheckBox(), "Zeilennummern ein- und ausschaltbar"));
			add(new SettingsOption("questionswitching", new SettingsCheckBox(), "Vor- und Zurückblättern erlauben"));
		}
	};
}
