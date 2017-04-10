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

import jhelp.util.text.UtilText;

/**
 * Describe a CSS color.<br>
 * Color is composed of a name, foreground, background and background if active (clik on it)
 */
public class Color
{
    /**
     * Color name
     */
    private final String name;
    /**
     * Foreground
     */
    private       int    color;
    /**
     * Background
     */
    private       int    background;
    /**
     * Background if active
     */
    private       int    backgroundActive;

    /**
     * Create a color
     *
     * @param name Color name
     */
    Color(String name)
    {
        this.name = name;
    }

    /**
     * Background color
     *
     * @return Background color
     */
    public int getBackground()
    {
        return this.background;
    }

    /**
     * Change background color
     *
     * @param background New background color
     */
    public void setBackground(int background)
    {
        this.background = background;
    }

    /**
     * Background if active color
     *
     * @return Background if active color
     */
    public int getBackgroundActive()
    {
        if (this.backgroundActive == 0)
        {
            return this.background;
        }

        return this.backgroundActive;
    }

    /**
     * Change background if active color
     *
     * @param backgroundActive New background if active color
     */
    public void setBackgroundActive(int backgroundActive)
    {
        this.backgroundActive = backgroundActive;
    }

    /**
     * Foreground
     *
     * @return Foreground
     */
    public int getColor()
    {
        return this.color;
    }

    /**
     * Change foreground
     *
     * @param color New foreground
     */
    public void setColor(int color)
    {
        this.color = color;
    }

    /**
     * Color name
     *
     * @return Color name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * String representation
     *
     * @return String representation
     */
    @Override
    public String toString()
    {
        return UtilText.concatenate('{', this.name,
                                    " color=", this.color, ":",
                                    Integer.toHexString(this.color),
                                    " background=", this.background, ":",
                                    Integer.toHexString(this.background),
                                    " active=", this.backgroundActive, ":",
                                    Integer.toHexString(this.backgroundActive), '}');
    }
}