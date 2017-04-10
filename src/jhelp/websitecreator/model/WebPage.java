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
package jhelp.websitecreator.model;

import com.sun.istack.internal.NotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import jhelp.util.io.Binarizable;
import jhelp.util.io.ByteArray;
import jhelp.websitecreator.resources.Color;
import jhelp.websitecreator.resources.Colors;
import jhelp.websitecreator.resources.ResourcesWebSiteCreator;

/**
 * Represents a web page
 */
public class WebPage implements WritableInHTML, Binarizable
{
    /**
     * Web page menu
     */
    private final Menu    menu;
    /**
     * Web page content
     */
    private final Content content;
    /**
     * Web page name
     */
    private       String  name;
    /**
     * Web page title
     */
    private       String  title;
    /**
     * Main title color name
     */
    private       String  mainTitleColorName;
    /**
     * Main title
     */
    private       String  mainTitle;

    /**
     * Create a web page
     */
    public WebPage()
    {
        this.name = "";
        this.title = "";
        this.mainTitle = "";
        this.mainTitleColorName = "white";
        this.menu = new Menu();
        this.content = new Content();
    }

    /**
     * Obtain name value
     *
     * @return name value
     */
    public @NotNull
    String getName()
    {
        return this.name;
    }

    /**
     * Modify name value
     *
     * @param name New name value
     */
    public void setName(
            @NotNull
                    String name)
    {
        if (name == null)
        {
            throw new NullPointerException("name MUST NOT be null !");
        }

        this.name = name;
    }

    /**
     * Obtain title value
     *
     * @return title value
     */
    public @NotNull
    String getTitle()
    {
        return this.title;
    }

    /**
     * Modify title value
     *
     * @param title New title value
     */
    public void setTitle(
            @NotNull
                    String title)
    {
        if (title == null)
        {
            throw new NullPointerException("title MUST NOT be null !");
        }

        this.title = title;
    }

    /**
     * Obtain mainTitle value
     *
     * @return mainTitle value
     */
    public @NotNull
    String getMainTitle()
    {
        return this.mainTitle;
    }

    /**
     * Modify mainTitle value
     *
     * @param mainTitle New mainTitle value
     */
    public void setMainTitle(
            @NotNull
                    String mainTitle)
    {
        if (mainTitle == null)
        {
            throw new NullPointerException("mainTitle MUST NOT be null !");
        }

        this.mainTitle = mainTitle;
    }

    /**
     * Main title color
     *
     * @return Main title color
     */
    public @NotNull
    Color getMainTitleColor()
    {
        return Colors.COLORS.obtainColor(this.mainTitleColorName);
    }

    /**
     * Modify main title color
     *
     * @param mainTitleColor New main title color
     */
    public void setMainTitleColor(
            @NotNull
                    Color mainTitleColor)
    {
        this.mainTitleColorName = mainTitleColor.getName();
    }

    /**
     * Web page main menu
     *
     * @return Web page main menu
     */
    public @NotNull
    Menu getMenu()
    {
        return this.menu;
    }

    /**
     * Web page content
     *
     * @return Web page content
     */
    public @NotNull
    Content getContent()
    {
        return this.content;
    }

    /**
     * Try to compress the page
     *
     * @return {@code true} if some compression happen
     */
    public boolean compress()
    {
        return this.content.compress();
    }

    /**
     * Write object in HTML
     *
     * @param project        Project parent
     * @param bufferedWriter Stream where write
     * @throws IOException On writing issue
     */
    public void writeInHTML(Project project, BufferedWriter bufferedWriter) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(ResourcesWebSiteCreator.RESOURCES.obtainResourceStream("pages/template.html")));
        String line = bufferedReader.readLine();
        int    start;

        while (line != null)
        {
            line = line.replace(ResourcesWebSiteCreator.REPLACEMENT_CSS_PATH, project.getCssPath())
                       .replace(ResourcesWebSiteCreator.REPLACEMENT_MAIN_TITLE, this.mainTitle)
                       .replace(ResourcesWebSiteCreator.REPLACEMENT_MAIN_TITLE_COLOR, this.mainTitleColorName)
                       .replace(ResourcesWebSiteCreator.REPLACEMENT_PAGE_TITLE, this.title)
                       .replace(ResourcesWebSiteCreator.REPLACEMENT_SCRIPTS_PATH, project.getScriptsPath());

            start = line.indexOf(ResourcesWebSiteCreator.REPLACEMENT_CONTENT);

            if (start > 0)
            {
                bufferedWriter.write(line.substring(0, start));
                this.content.writeInHTML(project, bufferedWriter);
                bufferedWriter.write(line.substring(start + ResourcesWebSiteCreator.REPLACEMENT_CONTENT.length()));
            }
            else
            {
                start = line.indexOf(ResourcesWebSiteCreator.REPLACEMENT_MENU);

                if (start > 0)
                {
                    bufferedWriter.write(line.substring(0, start));
                    this.menu.writeInHTML(project, bufferedWriter);
                    bufferedWriter.write(line.substring(start + ResourcesWebSiteCreator.REPLACEMENT_MENU.length()));
                }
                else
                {
                    bufferedWriter.write(line);
                }
            }

            bufferedWriter.newLine();
            line = bufferedReader.readLine();
        }

        bufferedWriter.flush();
        bufferedReader.close();
    }

    /**
     * Parse the array for fill binarizable information.<br>
     * See {@link #serializeBinary(ByteArray)} for fill information
     *
     * @param byteArray Byte array to parse
     */
    @Override
    public void parseBinary(ByteArray byteArray)
    {
        this.name = byteArray.readString();
        this.title = byteArray.readString();
        this.mainTitleColorName = byteArray.readString();
        this.mainTitle = byteArray.readString();
        this.menu.parseBinary(byteArray);
        this.content.parseBinary(byteArray);
    }

    /**
     * Write the binarizable information inside a byte array.<br>
     * See {@link #parseBinary(ByteArray)} for read information
     *
     * @param byteArray Byte array where write
     */
    @Override
    public void serializeBinary(ByteArray byteArray)
    {
        byteArray.writeString(this.name);
        byteArray.writeString(this.title);
        byteArray.writeString(this.mainTitleColorName);
        byteArray.writeString(this.mainTitle);
        this.menu.serializeBinary(byteArray);
        this.content.serializeBinary(byteArray);
    }
}
