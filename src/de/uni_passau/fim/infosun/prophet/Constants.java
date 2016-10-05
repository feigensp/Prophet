package de.uni_passau.fim.infosun.prophet;

import java.util.Locale;

import de.uni_passau.fim.infosun.prophet.util.qTree.QTreeNode;
import de.uni_passau.fim.infosun.prophet.util.qTree.handlers.QTreeXMLHandler;

/**
 * This class contains constants used throughout the code.
 *
 * @author Andreas Hasselberg
 * @author Markus Köppen
 * @author Georg Seibt
 */
public final class Constants {

    /**
     * Utility class.
     */
    private Constants() {}

    /**
     * A <code>File</code> with this name will be opened automatically (if it exists) when the <code>EViewer</code>
     * starts.
     */
    public static final String DEFAULT_FILE = "default.xml";

    /**
     * The answers resulting from an <code>EViewer</code> run will be saved in a file with this name using
     * {@link QTreeXMLHandler#saveAnswerXML(QTreeNode, java.io.File)}
     */
    public static final String FILE_ANSWERS = "answers.xml";

    /**
     * Attribute key for the setting that marks nodes whose content is not to be shown.
     */
    public static final String KEY_DONOTSHOWCONTENT = "donotshowcontent";

    /**
     * Attribute key for the setting that enables/disables switching between questions of a category during an
     * <code>EViewer</code> run.
     */
    public static final String KEY_QUESTIONSWITCHING = "questionswitching";

    /**
     * Attribute key for the setting that determines whether to randomize the direct children of a node
     * before displaying them.
     */
    public static final String KEY_RANDOMIZE_CHILDREN = "randomizeChildren";

    /**
     * Attribute key for the setting that determines how many children of a node are to be shown.
     */
    public static final String KEY_ONLY_SHOW_X_CHILDREN = "onlyShowXChildren";

    /**
     * SubAttribute key for the setting that determines how many children of a node are to be shown.
     */
    public static final String KEY_SHOW_NUMBER_OF_CHILDREN = "showNumberOfChildren";

    /**
     * Attribute key for the setting that determines whether the stopwatches that measure the time for every tree node
     * are visible.
     */
    public static final String KEY_TIMING = "timing_visible";

    /**
     * Attribute key for the setting that determines what language the experiment viewer uses.
     */
    public static final String KEY_VIEWER_LANGUAGE = "viewer_language";

    /**
     * Attribute value for the <code>KEY_VIEWER_LANGUAGE</code> setting specifying that the system language be used
     * for the viewer.
     */
    public static final String KEY_VIEWER_LANGUAGE_SYSTEM = "system_language";

    /**
     * Key for the <code>Attribute</code> that stores the experiment code as its value. Also used as the name of hidden
     * HTML 'input' elements storing the same information.
     */
    public static final String KEY_EXPERIMENT_CODE = "experimentcode";

    /**
     * Value for the 'name' attribute of the HTML button that advances the experiment to the next node.
     */
    public static final String KEY_FORWARD = "nextQuestion";

    /**
     * Value for the 'name' attribute of the HTML button that regresses the experiment to the previous node.
     */
    public static final String KEY_BACKWARD = "previousQuestion";

    /**
     * Value for the 'name' attribute of the HTML input element that accepts the subject code input.
     */
    public static final String KEY_SUBJECT_CODE = "subjectcode";

    /**
     * Key for the <code>Attribute</code> that determines via its value the caption to the left of the
     * input element that takes the subject code input.
     */
    public static final String KEY_SUBJECT_CODE_CAP = "subjectcode_caption";

    /**
     * A <code>Locale</code> representing Brazilian Portuguese.
     */
    public static final Locale PORTUGUES_BR = Locale.forLanguageTag("pt-BR");
}
