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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import jhelp.util.debug.Debug;
import jhelp.util.text.UtilText;

/**
 * List of defined colors in colors.css.
 */
public class Colors
{
    /**
     * Singleton instance
     */
    public static final Colors COLORS = new Colors();
    /**
     * Map of colors
     */
    private final Map<String, Color> colors;

    /**
     * Create the list
     */
    private Colors()
    {
        this.colors = new HashMap<>();
        this.parseColors();
    }

    /**
     * Parse colors form colors.css
     */
    private void parseColors()
    {
        BufferedReader bufferedReader = null;

        try
        {
            bufferedReader = new BufferedReader(new InputStreamReader(
                    ResourcesWebSiteCreator.RESOURCES.obtainResourceStream(
                            "pages/css/colors.css")));
            String  line   = bufferedReader.readLine();
            String  name   = null;
            boolean active = false;
            boolean inside = false;
            int     start, end;
            String  read;
            Color   color  = null;

            while (line != null)
            {
                line = line.trim();

                if (line.length() > 0)
                {
                    start = 0;

                    if (name == null)
                    {
                        end = line.indexOf('{');

                        if (end < 0)
                        {
                            end = line.length();
                        }
                        else
                        {
                            inside = true;
                        }

                        name = line.substring(start + 1, end)
                                   .trim();

                        if (name.endsWith(":active"))
                        {
                            active = true;
                            name = name.substring(0, name.length() - 7);
                        }

                        color = this.colors.get(name);

                        if (color == null)
                        {
                            color = new Color(name);
                            this.colors.put(name, color);
                        }

                        start = end + 1;
                    }
                    else if (!inside)
                    {
                        start = line.indexOf('{');

                        if (start >= 0)
                        {
                            start++;
                            inside = true;
                        }
                    }

                    if (inside)
                    {
                        end = UtilText.indexOf(line, start, ';', '}');

                        if (end > start)
                        {
                            read = line.substring(start, end)
                                       .trim();
                        }
                        else
                        {
                            read = line.substring(start)
                                       .trim();
                        }

                        if (read.startsWith("color:"))
                        {
                            color.setColor(Colors.parseColor(read.substring(6)
                                                                 .trim()));
                        }
                        else if (read.startsWith("background-color:"))
                        {
                            if (active)
                            {
                                color.setBackgroundActive(Colors.parseColor(read.substring(17)
                                                                                .trim()));
                            }
                            else
                            {
                                color.setBackground(Colors.parseColor(read.substring(17)
                                                                          .trim()));
                            }
                        }

                        if (end >= 0 && line.charAt(end) == '}')
                        {
                            inside = false;
                            color = null;
                            active = false;
                            name = null;
                        }
                    }
                }

                line = bufferedReader.readLine();
            }
        }
        catch (Exception exception)
        {
            Debug.printException(exception, "Failed to parse CSS colors !");
        }
        finally
        {
            if (bufferedReader != null)
            {
                try
                {
                    bufferedReader.close();
                }
                catch (Exception ignored)
                {
                }
            }
        }
    }

    /**
     * Parse one color String to color
     *
     * @param color Color String to parse
     * @return Parsed color
     */
    private static int parseColor(String color)
    {
        if ("white".equals(color))
        {
            return 0xFFFFFFFF;
        }

        if ("black".equals(color))
        {
            return 0xFF000000;
        }

        if ("red".equals(color))
        {
            return 0xFFFF0000;
        }

        if ("green".equals(color))
        {
            return 0xFF00FF00;
        }

        if ("blue".equals(color))
        {
            return 0xFF0000FF;
        }

        if (color.startsWith("#"))
        {
            color = color.substring(1)
                         .toUpperCase();
            final char[] characters = color.toCharArray();
            int          length     = characters.length;
            int          red        = 0;
            int          green      = 0;
            int          blue       = 0;
            int          alpha      = 0;

            switch (length)
            {
                case 3:
                    alpha = 0xFF;
                    red = Colors.parseHexa(characters[0]);
                    red = red << 4 | red;
                    green = Colors.parseHexa(characters[1]);
                    green = green << 4 | green;
                    blue = Colors.parseHexa(characters[2]);
                    blue = blue << 4 | blue;
                    break;
                case 4:
                    alpha = Colors.parseHexa(characters[3]);
                    alpha = alpha << 4 | alpha;
                    red = Colors.parseHexa(characters[0]);
                    red = red << 4 | red;
                    green = Colors.parseHexa(characters[1]);
                    green = green << 4 | green;
                    blue = Colors.parseHexa(characters[2]);
                    blue = blue << 4 | blue;
                    break;
                case 6:
                    alpha = 0xFF;
                    red = Colors.parseHexa(characters[0]) << 4 | Colors.parseHexa(characters[1]);
                    green = Colors.parseHexa(characters[2]) << 4 | Colors.parseHexa(characters[3]);
                    blue = Colors.parseHexa(characters[4]) << 4 | Colors.parseHexa(characters[5]);
                    break;
                case 8:
                    alpha = Colors.parseHexa(characters[6]) << 4 | Colors.parseHexa(characters[7]);
                    red = Colors.parseHexa(characters[0]) << 4 | Colors.parseHexa(characters[1]);
                    green = Colors.parseHexa(characters[2]) << 4 | Colors.parseHexa(characters[3]);
                    blue = Colors.parseHexa(characters[4]) << 4 | Colors.parseHexa(characters[5]);
                    break;
            }

            return alpha << 24 | red << 16 | green << 8 | blue;
        }

        return 0;
    }

    /**
     * Parse an hexa character
     *
     * @param character Character to pase
     * @return Hexa value
     */
    private static int parseHexa(char character)
    {
        if (character >= '0' && character <= '9')
        {
            return character - '0';
        }

        if (character >= 'A' && character <= 'F')
        {
            return 10 + character - 'A';
        }

        return 0;
    }

    /**
     * List of colors names
     *
     * @return Colors name
     */
    public Set<String> listOfColors()
    {
        return this.colors.keySet();
    }

    /**
     * Obtain a color
     *
     * @param name Color name
     * @return The color OR {@code null} if color not exists
     */
    public Color obtainColor(String name)
    {
        return this.colors.get(name);
    }

    /**
     * String representation
     *
     * @return String representation
     */
    @Override
    public String toString()
    {
        return this.colors.toString();
    }

    /**
     * Search corresponding color
     * @param foreground Foreground in ARGB
     * @param background Background in ARGB
     * @return Match color OR {@code null} if not found
     */
    public Color findColor(int foreground, int background)
    {
        for (Color color : this.colors.values())
        {
            if (color.getColor() == foreground && color.getBackground() == background)
            {
                return color;
            }
        }

        return null;
    }
}
