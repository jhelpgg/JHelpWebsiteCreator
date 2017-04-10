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
package jhelp.websitecreator.util;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JComponent;

import jhelp.util.debug.Debug;
import jhelp.util.list.HashInt;
import jhelp.websitecreator.model.Project;
import jhelp.websitecreator.resources.Colors;
import jhelp.websitecreator.resources.ResourcesWebSiteCreator;

/**
 * Utilities specific to the application
 */
public class UtilWebsiteCreator
{
    /**
     * URL link pattern
     */
    private static final Pattern        LINK             = Pattern.compile("-\\{(([^}]|\\n)*)\\}-(([^#]|\\n)*)#");
    /**
     * Link replacement rule
     */
    private static final String         LINK_REPLACEMENT = "<a href=\"$1\">$3</a>";
    /**
     * List of known colors
     */
    private static final HashInt<Color> COLORS           = new HashInt<>();

    /**
     * Apply color to component
     *
     * @param component Component to decorate
     * @param color     Color to use. {@code null} mean default color
     */
    public static void applyColor(JComponent component, jhelp.websitecreator.resources.Color color)
    {
        if (color == null)
        {
            component.setBackground(Color.WHITE);
            component.setForeground(Color.BLACK);
        }
        else
        {
            component.setBackground(UtilWebsiteCreator.getColor(color.getBackground()));
            component.setForeground(UtilWebsiteCreator.getColor(color.getColor()));
        }
    }

    /**
     * Obtain color for given ARGB
     *
     * @param argb ARGB
     * @return Corresponding color
     */
    public static Color getColor(int argb)
    {
        Color color = COLORS.get(argb);

        if (color == null)
        {
            color = new Color(argb, true);
            COLORS.put(argb, color);
        }

        return color;
    }

    /**
     * Obtain component color
     *
     * @param component Component where extract color
     * @return The color OR {@code null} if no coresponding CSS color
     */
    public static jhelp.websitecreator.resources.Color extractColor(JComponent component)
    {
        return Colors.COLORS.findColor(component.getForeground()
                                                .getRGB(),
                                       component.getBackground()
                                                .getRGB());
    }

    /**
     * Convert ordinary text to HTML text
     *
     * @param text Text to convert
     * @return Converted text
     */
    public static String convertToHTML(String text)
    {
        text = text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\t", "&nbsp;&nbsp;&nbsp;")
                   .replace("\n", "<br>\n");

        Matcher matcher = LINK.matcher(text);
        text = matcher.replaceAll(LINK_REPLACEMENT);

        return text;
    }

    /**
     * Save a project
     *
     * @param project Paroject to save
     * @return {@code true} if save succeed
     */
    public static boolean save(Project project)
    {
        try
        {
            project.compress();
            project.save();
            ResourcesWebSiteCreator.PREFERENCES.setValue(
                    ResourcesWebSiteCreator.PREFERENCE_LAST_PROJECT,
                    new File(project.getDirectory(), project.getName()));
            ResourcesWebSiteCreator.PREFERENCES.setValue(
                    ResourcesWebSiteCreator.PREFERENCE_LAST_DIRECTORY,
                    project.getDirectory());
            return true;
        }
        catch (IOException e)
        {
            Debug.printException(e, "Failed to save project : " + project.getName());
            return false;
        }
    }

    /**
     * Load a project
     *
     * @param file Project file
     * @return Read project
     */
    public static Project load(File file)
    {
        try
        {
            Project project = Project.load(file);
            ResourcesWebSiteCreator.PREFERENCES.setValue(
                    ResourcesWebSiteCreator.PREFERENCE_LAST_PROJECT,
                    file);
            ResourcesWebSiteCreator.PREFERENCES.setValue(
                    ResourcesWebSiteCreator.PREFERENCE_LAST_DIRECTORY,
                    file.getParentFile());
            return project;
        }
        catch (IOException e)
        {
            Debug.printException(e, "Failed to load project : ", file.getAbsolutePath());
            return null;
        }
    }
}
