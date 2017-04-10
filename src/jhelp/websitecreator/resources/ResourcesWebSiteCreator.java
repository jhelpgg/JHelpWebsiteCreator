/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any
 * damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.websitecreator.resources;

import jhelp.util.gui.JHelpImage;
import jhelp.util.io.UtilIO;
import jhelp.util.preference.Preferences;
import jhelp.util.resources.ResourceText;
import jhelp.util.resources.Resources;

/**
 * Acces to application ressources.
 */
public class ResourcesWebSiteCreator
{
    /**
     * Text key for application title
     */
    public static final String TEXT_TITLE                                          = "title";
    /**
     * Text key for title of alert user that load of last project failed
     */
    public static final String TEXT_LAST_PROJECT_LOAD_FAILED_TITLE                 = "lastProjectLoadFailedTitle";
    /**
     * Text key for message of alert user that load of last project failed
     */
    public static final String TEXT_LAST_PROJECT_LOAD_FAILED_MESSAGE               = "lastProjectLoadFailedMessage";
    /**
     * Text key for create new project
     */
    public static final String TEXT_ACTION_NEW_PROJECT                             = "actionNewProject";
    /**
     * Text key for open a project
     */
    public static final String TEXT_ACTION_OPEN_PROJECT                            = "actionOpenProject";
    /**
     * Text key for save current project
     */
    public static final String TEXT_ACTION_SAVE_PROJECT                            = "actionSaveProject";
    /**
     * Text key for title of alert user that the directory for create new project is not empty
     */
    public static final String TEXT_ACTION_NEW_PROJECT_DIRECTORY_NOT_EMPTY_TITLE   =
            "actionNewProjectDirectoryNotEmptyTitle";
    /**
     * Text key for message of alert user that the directory for create new project is not empty
     */
    public static final String TEXT_ACTION_NEW_PROJECT_DIRECTORY_NOT_EMPTY_MESSAGE =
            "actionNewProjectDirectoryNotEmptyMessage";
    /**
     * Text key of action for ask name of new projet
     */
    public static final String TEXT_ACTION_NEW_PROJECT_ASK_NAME                    = "actionNewProjectAskName";
    /**
     * Text key of default project name
     */
    public static final String TEXT_ACTION_NEW_PROJECT_DEFAULT_NAME                = "actionNewProjectDefaultName";
    /**
     * Text key of menu file
     */
    public static final String TEXT_MENU_FILE                                      = "menuFile";
    /**
     * Text key for generate website
     */
    public static final String TEXT_ACTION_GENERATE_PROJECT                        = "actionGenerateProject";

    /**
     * Replacement key for scripts relative path
     */
    public static final String REPLACEMENT_SCRIPTS_PATH     = "$SCRIPTS_PATH$";
    /**
     * Replacement key for css relative path
     */
    public static final String REPLACEMENT_CSS_PATH         = "$CSS_PATH$";
    /**
     * Replacement key for page title
     */
    public static final String REPLACEMENT_PAGE_TITLE       = "$PAGE_TITLE$";
    /**
     * Replacement key for main title color
     */
    public static final String REPLACEMENT_MAIN_TITLE_COLOR = "$MAIN_TITLE_COLOR$";
    /**
     * Replacement key for main title
     */
    public static final String REPLACEMENT_MAIN_TITLE       = "$MAIN_TITLE$";
    /**
     * Replacement key for main menu
     */
    public static final String REPLACEMENT_MENU             = "$MENU$";
    /**
     * Replacement key for main content
     */
    public static final String REPLACEMENT_CONTENT          = "$CONTENT$";

    /**
     * Preference key of last open project
     */
    public static final String PREFERENCE_LAST_PROJECT   = "LAST_PROJECT";
    /**
     * Preference key of last open directory
     */
    public static final String PREFERENCE_LAST_DIRECTORY = "LAST_DIRECTORY";
    /**
     * Preference key of last open image
     */
    public static final String PREFERENCE_LAST_IMAGE     = "LAST_IMAGE";

    /**
     * Link to application resources
     */
    public static final Resources    RESOURCES;
    /**
     * Link to application texts
     */
    public static final ResourceText RESOURCE_TEXT;
    /**
     * Application preferences
     */
    public static final Preferences  PREFERENCES;

    /**
     * Edit table icon
     */
    public static final JHelpImage ICON_EDIT;
    /**
     * Add text icon
     */
    public static final JHelpImage ICON_TEXT;
    /**
     * Add image icon
     */
    public static final JHelpImage ICON_IMAGE;
    /**
     * Add table icon
     */
    public static final JHelpImage ICON_TABLE;
    /**
     * Add code icon
     */
    public static final JHelpImage ICON_CODE;

    static
    {
        RESOURCES = new Resources(ResourcesWebSiteCreator.class);
        RESOURCE_TEXT = RESOURCES.obtainResourceText("texts/texts");
        PREFERENCES = new Preferences(UtilIO.obtainExternalFile("jhelp/websiteCreator.pref"));
        ICON_EDIT = RESOURCES.obtainResizedJHelpImage("images/edit.png", 32, 32);
        ICON_TEXT = RESOURCES.obtainResizedJHelpImage("images/text.png", 32, 32);
        ICON_IMAGE = RESOURCES.obtainResizedJHelpImage("images/image.png", 32, 32);
        ICON_TABLE = RESOURCES.obtainResizedJHelpImage("images/table.png", 32, 32);
        ICON_CODE = RESOURCES.obtainResizedJHelpImage("images/code.png", 32, 32);
    }
}
