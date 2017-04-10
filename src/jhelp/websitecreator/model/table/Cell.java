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
package jhelp.websitecreator.model.table;

import java.io.BufferedWriter;
import java.io.IOException;

import jhelp.util.io.Binarizable;
import jhelp.util.io.ByteArray;
import jhelp.websitecreator.model.Project;
import jhelp.websitecreator.model.WritableInHTML;
import jhelp.websitecreator.model.text.BlockText;
import jhelp.websitecreator.resources.Color;
import jhelp.websitecreator.resources.Colors;

/**
 * Table cell
 */
public class Cell implements WritableInHTML, Binarizable
{
    /**
     * Block text inside the cell
     */
    private final BlockText blockText;
    /**
     * Indicates if it is a header
     */
    private       boolean   header;
    /**
     * Color name
     */
    private       String    colorName;

    /**
     * Create empty cell
     */
    public Cell()
    {
        this.header = false;
        this.blockText = new BlockText();
    }

    /**
     * Obtain header value
     *
     * @return header value
     */
    public boolean isHeader()
    {
        return this.header;
    }

    /**
     * Modify header value
     *
     * @param header New header value
     */
    public void setHeader(boolean header)
    {
        this.header = header;
    }

    /**
     * Obtain color value
     *
     * @return color value
     */
    public Color getColor()
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
    public void setColor(Color color)
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
     * Obtain blockText value
     *
     * @return blockText value
     */
    public BlockText getBlockText()
    {
        return this.blockText;
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
        if (this.header)
        {
            bufferedWriter.write("<th");
        }
        else
        {
            bufferedWriter.write("<td");
        }

        if (this.colorName != null)
        {
            bufferedWriter.write(" class=\"");
            bufferedWriter.write(this.colorName);
            bufferedWriter.write("\"");
        }

        bufferedWriter.write(">");
        this.blockText.writeInHTML(project, bufferedWriter);

        if (this.header)
        {
            bufferedWriter.write("</th>");
        }
        else
        {
            bufferedWriter.write("</td>");
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
        this.header = byteArray.readBoolean();
        this.colorName = byteArray.readString();
        this.blockText.parseBinary(byteArray);
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
        byteArray.writeBoolean(this.header);
        byteArray.writeString(this.colorName);
        this.blockText.serializeBinary(byteArray);
    }

    /**
     * String representation
     *
     * @return String representation
     */
    @Override
    public String toString()
    {
        return "{" + this.blockText + "}";
    }

    /**
     * Try to compress the cell
     *
     * @return {@code true} if some compression happen
     */
    public boolean compress()
    {
        return this.blockText.compress();
    }
}
