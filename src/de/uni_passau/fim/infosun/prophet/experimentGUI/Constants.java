package de.uni_passau.fim.infosun.prophet.experimentGUI;

/**
 * Class used to store constants used throughout the code
 *
 * @author Andreas Hasselberg
 * @author Markus Köppen
 */
public final class Constants {

    /**
     * Utility class.
     */
    private Constants() {}

    /**
     * File name of the experiment file opened automatically when program starts
     */
    public static final String DEFAULT_FILE = "default.xml";
    /**
     * File name of the file answers are saved in
     */
    public static final String FILE_ANSWERS = "answers.xml";
    /**
     * property name for categories whose content isn't shown and the first question within is started immediately
     */
    public static final String KEY_DONOTSHOWCONTENT = "donotshowcontent";
    /**
     * property name for categories, determines if subjects may go back to previous questions within that category
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
     * are enabled.
     */
    public static final String KEY_TIMING = "timing";

    /**
     * property name for the experiment code (which is used for online usage)
     */
    public static final String KEY_EXPERIMENT_CODE = "experimentcode";

    //constants for names in html-files - DO NOT USE
    /**
     * html name for the button to the next question
     */
    public static final String KEY_FORWARD = "nextQuestion";
    /**
     * html name for the button to the previous question (only shown if allowed)
     */
    public static final String KEY_BACKWARD = "previousQuestion";

    /**
     * html name for the input field used to enter the subject code
     */
    public static final String KEY_SUBJECT = "subjectcode";


}
