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
import java.util.ArrayList;
import java.util.List;

import jhelp.util.io.ByteArray;
import jhelp.websitecreator.model.ContentElement;
import jhelp.websitecreator.model.Project;
import jhelp.websitecreator.resources.Color;

/**
 * Block of generic text (Can contains images)
 */
public class BlockText implements ContentElement
{
    /**
     * List of blocks element
     */
    private final List<BlockElement> blockElements;

    /**
     * Create an empty block
     */
    public BlockText()
    {
        this.blockElements = new ArrayList<>();
    }

    /**
     * Add element to block
     *
     * @param blockElement Element to add
     */
    public void addElement(
            @NotNull
                    BlockElement blockElement)
    {
        if (blockElement == null)
        {
            throw new NullPointerException("blockElement MUST NOT be null !");
        }

        this.blockElements.add(blockElement);
    }

    /**
     * Insert element to block
     *
     * @param index        Index where insert
     * @param blockElement Element to insert
     */
    public void insert(int index,
                       @NotNull
                               BlockElement blockElement)
    {
        if (blockElement == null)
        {
            throw new NullPointerException("blockElement MUST NOT be null !");
        }

        index = Math.max(0, index);

        if (index >= this.blockElements.size())
        {
            this.blockElements.add(blockElement);
        }
        else
        {
            this.blockElements.add(index, blockElement);
        }
    }

    /**
     * Add all element of an other block
     *
     * @param blockText Block to copy elements
     */
    public void addAll(
            @NotNull
                    BlockText blockText)
    {
        this.blockElements.addAll(blockText.blockElements);
    }

    /**
     * Obtain an element
     *
     * @param index Element index
     * @return Element at index
     */
    public @NotNull
    BlockElement getElement(int index)
    {
        return this.blockElements.get(index);
    }

    /**
     * Remove an element
     *
     * @param blockElement Element to remove. If {@code null} nothing is removed
     */
    public void removeElement(
            @Nullable
                    BlockElement blockElement)
    {
        this.blockElements.remove(blockElement);
    }

    /**
     * Remove an element
     *
     * @param index Element index
     */
    public void removeElement(int index)
    {
        this.blockElements.remove(index);
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
        for (BlockElement blockElement : this.blockElements)
        {
            blockElement.writeInHTML(project, bufferedWriter);
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
        this.blockElements.clear();

        try
        {
            byteArray.readBinarizableList(this.blockElements);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to parse !", e);
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
        byteArray.writeBinarizableList(this.blockElements);
    }

    /**
     * String representation
     *
     * @return String representation
     */
    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (BlockElement blockElement : this.blockElements)
        {
            stringBuilder.append(blockElement);
        }

        return stringBuilder.toString();
    }

    /**
     * Try to compress the block
     *
     * @return {@code true} if some compression happen. {@code false} if nothing change
     */
    @Override
    public boolean compress()
    {
        int size = this.numberOfElements();

        if (size <= 1)
        {
            return false;
        }

        boolean      compress = false;
        BlockElement first    = this.blockElements.get(0);
        BlockElement second;

        for (int i = 1; i < size; i++)
        {
            second = this.blockElements.get(i);

            if (first.compressibleWith(second))
            {
                first = first.compressWith(second);
                this.blockElements.remove(i);
                this.blockElements.set(i - 1, first);
                size--;
                i--;
                compress = true;
            }
            else
            {
                first = second;
            }
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
        return this.blockElements.size();
    }

    /**
     * Apply color for all element
     *
     * @param color Color to apply. {@code null} means no color
     */
    public void setColorForAll(
            @Nullable
                    Color color)
    {
        for (BlockElement blockElement : this.blockElements)
        {
            if (blockElement instanceof ElementText)
            {
                ((ElementText) blockElement).setColor(color);
            }
        }
    }
}
