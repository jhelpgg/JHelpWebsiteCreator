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
import jhelp.websitecreator.model.text.BlockElement;
import jhelp.websitecreator.model.text.BlockText;
import jhelp.websitecreator.model.text.ElementText;
import jhelp.websitecreator.resources.Color;
import jhelp.websitecreator.resources.Colors;

/**
 * Table header
 */
public class Header implements WritableInHTML, Binarizable
{
    /**
     * Test inside table header
     */
    private final BlockText blockText;
    /**
     * Header color
     */
    private       String    colorName;

    /**
     * Create empty header
     */
    public Header()
    {
        this.colorName = "blue";
        this.blockText = new BlockText();
    }

    /**
     * Obtain color value
     *
     * @return color value
     */
    public Color getColor()
    {
        return Colors.COLORS.obtainColor(this.colorName);
    }

    /**
     * Modify color value
     *
     * @param color New color value
     */
    public void setColor(Color color)
    {
        this.colorName = color.getName();
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
        int          size = this.blockText.numberOfElements();
        BlockElement blockElement;
        Color        color;
        String       colorName;


        for (int i = 0; i < size; i++)
        {
            blockElement = this.blockText.getElement(i);

            if (blockElement instanceof ElementText)
            {
                color = ((ElementText) blockElement).getColor();

                if (color != null)
                {
                    this.colorName = color.getName();
                    ((ElementText) blockElement).setColor(null);
                }
            }
        }

        bufferedWriter.write("<th class=\"");
        bufferedWriter.write(this.colorName);
        bufferedWriter.write("\">");
        this.blockText.writeInHTML(project, bufferedWriter);
        bufferedWriter.write("</th>");
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
        return "[" + this.blockText + "]";
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
