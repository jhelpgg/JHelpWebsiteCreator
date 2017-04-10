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

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jhelp.util.io.Binarizable;
import jhelp.util.io.ByteArray;
import jhelp.websitecreator.model.text.BlockText;

/**
 * Web page content
 */
public class Content implements WritableInHTML, Binarizable, Iterable<ContentElement>
{
    /**
     * List of elements
     */
    private final List<ContentElement> contentElements;

    /**
     * Create web page content
     */
    public Content()
    {
        this.contentElements = new ArrayList<>();
    }

    /**
     * Add an element
     *
     * @param contentElement Element to add
     */
    public void addElement(
            @NotNull
                    ContentElement contentElement)
    {
        if (contentElement == null)
        {
            throw new NullPointerException("contentElement MUST NOT be null !");
        }

        this.contentElements.add(contentElement);
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
        for (ContentElement contentElement : this.contentElements)
        {
            contentElement.writeInHTML(project, bufferedWriter);
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
        this.contentElements.clear();

        try
        {
            byteArray.readBinarizableList(this.contentElements);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to parse", e);
        }
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
        byteArray.writeBinarizableList(this.contentElements);
    }

    /**
     * Try to compress the content
     *
     * @return {@code true} if some compression made OR {@code false} if nothing change
     */
    public boolean compress()
    {
        boolean        compress = false;
        ContentElement contentElement1, contentElement2;

        for (int i = this.contentElements.size() - 1; i >= 1; i--)
        {
            contentElement1 = this.contentElements.get(i);

            if (contentElement1 instanceof BlockText)
            {
                contentElement2 = this.contentElements.get(i - 1);

                if (contentElement2 instanceof BlockText)
                {
                    ((BlockText) contentElement2).addAll((BlockText) contentElement1);
                    this.contentElements.remove(i);
                    compress = true;
                }
            }
        }

        for (ContentElement contentElement : this.contentElements)
        {
            compress |= contentElement.compress();
        }

        return compress;
    }

    /**
     * Number of elements
     *
     * @return Number of elements
     */
    public int numberOfElements()
    {
        return this.contentElements.size();
    }

    /**
     * Obtain an element
     *
     * @param index Element index
     * @return Desired element
     */
    public ContentElement getElement(int index)
    {
        return this.contentElements.get(index);
    }

    /**
     * Last elemnt in content
     *
     * @return Last elemnt in content OR {@code null} if content is empty
     */
    public ContentElement getLastElement()
    {
        int size = this.contentElements.size();

        if (size == 0)
        {
            return null;
        }

        return this.contentElements.get(size - 1);
    }

    /**
     * Returns an iterator over elements of type {@code ContentElement}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<ContentElement> iterator()
    {
        return this.contentElements.iterator();
    }
}
