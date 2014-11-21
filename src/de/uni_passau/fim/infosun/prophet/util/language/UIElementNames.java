package de.uni_passau.fim.infosun.prophet.util.language;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Contains static methods to retrieve localized strings from the Prophet <code>ResourceBundle</code>.
 */
public final class UIElementNames {

    private static final String baseName = "de.uni_passau.fim.infosun.prophet.experimentGUI.util.language.LabelsBundle";
    private static ResourceBundle labels = ResourceBundle.getBundle(baseName);

    private UIElementNames() {}

    /**
     * Sets the locale of the <code>ResourceBundle</code> to <code>locale</code>.
     *
     * @param locale the new <code>Locale</code>
     */
    public static void setLocale(Locale locale) {
        labels = ResourceBundle.getBundle(baseName, locale);
    }

    /**
     * Retrieves a localized <code>String</code> for the given key.
     *
     * @param key the key for the <code>String</code>
     * @return the localized <code>String</code>
     */
    public static String getLocalized(String key) {
        return labels.getString(key);
    }

    /**
     * Returns the <code>ResourceBundle</code> currently used by the class.
     *
     * @return the <code>ResourceBundle</code>
     */
    public static ResourceBundle getBundle() {
        return labels;
    }
}
