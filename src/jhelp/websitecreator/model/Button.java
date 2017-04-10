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
import com.sun.istack.internal.Nullable;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import jhelp.util.io.Binarizable;
import jhelp.util.io.ByteArray;
import jhelp.websitecreator.resources.Color;
import jhelp.websitecreator.resources.Colors;

/**
 * Describe a button
 */
public class Button implements WritableInHTML, Binarizable
{
    /**
     * Next button id
     */
    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);
    /**
     * Button id
     */
    private String id;
    /**
     * URL to go on click. If {@code null}, no URL to go
     */
    private String url;
    /**
     * Button text
     */
    private String text;
    /**
     * Action link to button. {@code null} means no action
     */
    private String action;
    /**
     * Buton color name
     */
    private String colorName;
    /**
     * Button size in {1, 2 , ..., 12}
     */
    private int    size;

    /**
     * Create a button
     */
    public Button()
    {
        this.id = "Button_" + NEXT_ID.getAndIncrement();
        this.text = "";
        this.colorName = "green";
        this.size = 1;
    }

    /**
     * Obtain button ID
     *
     * @return Button ID
     */
    public @NotNull
    String getId()
    {
        return this.id;
    }

    /**
     * Modify id value
     *
     * @param id New id value
     */
    public void setId(
            @NotNull
                    String id)
    {
        if (id == null)
        {
            throw new NullPointerException("id MUST NOT be null !");
        }

        this.id = id;
    }

    /**
     * Obtain url value.<br>
     * Returns {@code null} if no URL
     *
     * @return url value OR  {@code null} if no URL
     */
    public @Nullable
    String getUrl()
    {
        return this.url;
    }

    /**
     * Modify url value.<br>
     * Use {@code null} for no URL
     *
     * @param url New url value. Can be {@code null} for no URL
     */
    public void setUrl(
            @Nullable
                    String url)
    {
        this.url = url;
    }

    /**
     * Obtain text value
     *
     * @return text value
     */
    public @NotNull
    String getText()
    {
        return this.text;
    }

    /**
     * Modify text value
     *
     * @param text New text value
     */
    public void setText(
            @NotNull
                    String text)
    {
        if (text == null)
        {
            throw new NullPointerException("text MUST NOT be null !");
        }

        this.text = text;
    }

    /**
     * Obtain action value<br>
     * Returns {@code null} if no action
     *
     * @return action value OR  {@code null} if no action
     */
    public @Nullable
    String getAction()
    {
        return this.action;
    }

    /**
     * Modify action value<br>
     * Use {@code null} for no action
     *
     * @param action New action value. Can be {@code null} for no action
     */
    public void setAction(
            @Nullable
                    String action)
    {
        this.action = action;
    }

    /**
     * Obtain button color
     *
     * @return Button color
     */
    public @NotNull
    Color getColor()
    {
        return Colors.COLORS.obtainColor(this.colorName);
    }

    /**
     * Modify button color
     *
     * @param color New button color
     */
    public void setColor(
            @NotNull
                    Color color)
    {
        this.colorName = color.getName();
    }

    /**
     * Obtain size value
     *
     * @return size value
     */
    public int getSize()
    {
        return this.size;
    }

    /**
     * Modify size value.<br>
     * The size MUST be in {1, 2, ..., 12}
     *
     * @param size New size value
     * @throws IllegalArgumentException If size not in {1, 2, ..., 12}
     */
    public void setSize(int size)
    {
        if (size < 1 || size > 12)
        {
            throw new IllegalArgumentException("Size MUST be in {1, 2, ..., 12} not " + size);
        }

        this.size = size;
    }

    /**
     * Write object in HTML
     *
     * @param project        Project reference
     * @param bufferedWriter Stream where write
     * @throws IOException On writing issue
     */
    @Override
    public void writeInHTML(Project project, BufferedWriter bufferedWriter) throws IOException
    {
        if (this.url != null)
        {
            bufferedWriter.write("<a href=\"");
            bufferedWriter.write(this.url);
            bufferedWriter.write("\">");
        }

        bufferedWriter.write("<button id=\"");
        bufferedWriter.write(this.id);
        bufferedWriter.write("\" class=\"col-");
        bufferedWriter.write(String.valueOf(this.size));
        bufferedWriter.write(" col-m-");
        bufferedWriter.write(String.valueOf(this.size));
        bufferedWriter.write(" ");
        bufferedWriter.write(this.colorName);
        bufferedWriter.write("\"");

        if (this.action != null)
        {
            bufferedWriter.write(" onClick=\"script:clickOn('");
            bufferedWriter.write(this.action);
            bufferedWriter.write("')\"");
        }

        bufferedWriter.write(">");
        bufferedWriter.write(this.text);
        bufferedWriter.write("</button>");

        if (this.url != null)
        {
            bufferedWriter.write("</a>");
        }

        bufferedWriter.newLine();
        bufferedWriter.flush();
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
        this.setId(byteArray.readString());
        this.setUrl(byteArray.readString());
        this.setText(byteArray.readString());
        this.setAction(byteArray.readString());
        this.setColor(Colors.COLORS.obtainColor(byteArray.readString()));
        this.setSize(byteArray.readInteger());
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
        byteArray.writeString(this.id);
        byteArray.writeString(this.url);
        byteArray.writeString(this.text);
        byteArray.writeString(this.action);
        byteArray.writeString(this.colorName);
        byteArray.writeInteger(this.size);
    }
}
