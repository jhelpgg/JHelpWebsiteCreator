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

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import jhelp.util.gui.JHelpImage;
import jhelp.util.io.ByteArray;
import jhelp.util.math.UtilMath;
import jhelp.websitecreator.model.Project;

/**
 * Element of image
 */
public class ElementImage implements BlockElement
{
    /**
     * Image default name
     */
    public static final String IMAGE_DEFAULT = "--IMAGE_DEFAULT---";
    /**
     * Image size (in {1, 2, ..., 12}
     */
    private int    size;
    /**
     * Image name
     */
    private String imageName;

    /**
     * Create empty image
     */
    public ElementImage()
    {
        this.size = 12;
        this.imageName = IMAGE_DEFAULT;
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
     * Modify size value
     *
     * @param size New size value
     */
    public void setSize(int size)
    {
        if (size < 1 || size > 12)
        {
            throw new IllegalArgumentException("size MUST be in {1, 2, .., 12} not " + size);
        }

        this.size = size;
    }

    /**
     * Obtain imageName value
     *
     * @return imageName value
     */
    public String getImageName()
    {
        return this.imageName;
    }

    /**
     * Modify imageName value
     *
     * @param imageName New imageName value
     */
    public void setImageName(String imageName)
    {
        if (imageName == null)
        {
            throw new NullPointerException("imageName MUST NOT be null !");
        }

        this.imageName = imageName;
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
        int size = 12;

        try
        {
            JHelpImage image = JHelpImage.loadImage(new File(project.imagesDirectory(), this.imageName));
            size = UtilMath.limit((image.getWidth() * 12) >> 10, 1, 12);
        }
        catch (Exception ignored)
        {
        }


        bufferedWriter.write("<image src=\"");
        bufferedWriter.write(project.getImagesPath());
        bufferedWriter.write(this.imageName);
        bufferedWriter.write("\" class=\"col-");
        bufferedWriter.write(String.valueOf(size));
        bufferedWriter.write(" col-m-");
        bufferedWriter.write(String.valueOf(size));
        bufferedWriter.write("\"/>");
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
        this.size = byteArray.readInteger();
        this.imageName = byteArray.readString();
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
        byteArray.writeInteger(this.size);
        byteArray.writeString(this.imageName);
    }

    /**
     * String representation
     *
     * @return String representation
     */
    @Override
    public String toString()
    {
        return "IMAGE:" + this.imageName;
    }

    /**
     * Indicates if compressible with given element
     *
     * @param blockElement Element to compress with
     * @return {@code false} because can't compress image with anything
     */
    @Override
    public boolean compressibleWith(BlockElement blockElement)
    {
        return false;
    }

    /**
     * Try compress with an other element
     *
     * @param blockElement Element to compress with
     * @return {@code null} beacuse compress with image always failed
     */
    @Override
    public BlockElement compressWith(BlockElement blockElement)
    {
        return null;
    }
}
