package de.uni_passau.fim.infosun.prophet.experimentGUI.util.language;

import java.util.Locale;
import java.util.ResourceBundle;

public final class UIElementNames {

    private static final String baseName = "de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.LabelsBundle";
    private static ResourceBundle labels = ResourceBundle.getBundle(baseName);

    private UIElementNames() {}

    public static void setLocale(Locale locale) {
        labels = ResourceBundle.getBundle(baseName, locale);
    }

    public static String get(String key) {
        return labels.getString(key);
    }

    public static ResourceBundle getBundle() {
        return labels;
    }
}
