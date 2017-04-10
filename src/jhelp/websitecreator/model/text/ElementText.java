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
package jhelp.websitecreator.model.text;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.io.BufferedWriter;
import java.io.IOException;

import jhelp.util.io.ByteArray;
import jhelp.websitecreator.model.Project;
import jhelp.websitecreator.resources.Color;
import jhelp.websitecreator.resources.Colors;
import jhelp.websitecreator.util.UtilWebsiteCreator;

/**
 * Element of text
 */
public class ElementText implements BlockElement
{
    /**
     * Text color, {@code null} for normal text
     */
    private String colorName;
    /**
     * Text
     */
    private String text;

    /**
     * Create element of text
     */
    public ElementText()
    {
        this.text = "";
    }

    /**
     * Obtain color value<br>
     * {@code null} for normal text
     *
     * @return color value OR {@code null} for normal text
     */
    public @Nullable
    Color getColor()
    {
        if (this.colorName == null)
        {
            return null;
        }

        return Colors.COLORS.obtainColor(this.colorName);
    }

    /**
     * Modify color value
     *
     * @param color New color value
     */
    public void setColor(
            @Nullable
                    Color color)
    {
        if (color == null)
        {
            this.colorName = null;
        }
        else
        {
            this.colorName = color.getName();
        }
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
     * Write object in HTML
     *
     * @param project        Project reference
     * @param bufferedWriter Stream where write
     * @throws IOException On writing issue
     */
    @Override
    public void writeInHTML(Project project, BufferedWriter bufferedWriter) throws IOException
    {
        if (this.colorName != null)
        {
            bufferedWriter.write("<b class=\"");
            bufferedWriter.write(this.colorName);
            bufferedWriter.write("\">");
        }

        bufferedWriter.write(UtilWebsiteCreator.convertToHTML(this.text));

        if (this.colorName != null)
        {
            bufferedWriter.write("</b>");
        }

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
        this.colorName = byteArray.readString();
        this.text = byteArray.readString();
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
        byteArray.writeString(this.colorName);
        byteArray.writeString(this.text);
    }

    /**
     * String representation
     *
     * @return String representation
     */
    @Override
    public String toString()
    {
        return this.colorName + ":" + this.text;
    }

    /**
     * Create a copy
     *
     * @return Copy created
     */
    public ElementText copy()
    {
        ElementText elementText = new ElementText();
        elementText.text = this.text;
        elementText.colorName = this.colorName;
        return elementText;
    }

    /**
     * Indicates if compressible with given element
     *
     * @param blockElement Element to compress with
     * @return {@code true} if compressible with given element
     */
    @Override
    public boolean compressibleWith(BlockElement blockElement)
    {
        if (!(blockElement instanceof ElementText))
        {
            return false;
        }

        String colorName = ((ElementText) blockElement).colorName;

        if (this.colorName == null)
        {
            return colorName == null;
        }

        return this.colorName.equals(colorName);
    }

    /**
     * Try compress with given element
     *
     * @param blockElement Element to compress with
     * @return Compression result OR {@code null} if compression not possible
     */
    @Override
    public BlockElement compressWith(BlockElement blockElement)
    {
        if (!this.compressibleWith(blockElement))
        {
            return null;
        }

        ElementText elementText = (ElementText) blockElement;
        this.text += elementText.text;
        return this;
    }
}
