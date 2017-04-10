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
import java.util.ArrayList;
import java.util.List;

import jhelp.util.io.ByteArray;
import jhelp.util.math.UtilMath;
import jhelp.websitecreator.model.ContentElement;
import jhelp.websitecreator.model.Project;

/**
 * Represents a table
 */
public class Table implements ContentElement
{
    /**
     * Tablz headers
     */
    private final List<Header> headers;
    /**
     * Table size
     */
    private final List<Cell>   cells;
    /**
     * Indicates if table is a floating one
     */
    private       boolean      floating;
    /**
     * Number of columns
     */
    private       int          numberOfColums;
    /**
     * Number of rows
     */
    private       int          numberOfLines;

    /**
     * Create empty table with of row, one column
     */
    public Table()
    {
        this.floating = true;
        this.headers = new ArrayList<>();
        this.cells = new ArrayList<>();
        this.numberOfColums = 1;
        this.numberOfLines = 1;
        this.headers.add(new Header());
        this.cells.add(new Cell());
    }

    /**
     * Obtain floating value
     *
     * @return floating value
     */
    public boolean isFloating()
    {
        return this.floating;
    }

    /**
     * Modify floating value
     *
     * @param floating New floating value
     */
    public void setFloating(boolean floating)
    {
        this.floating = floating;
    }

    /**
     * Number of columns
     *
     * @return Number of columns
     */
    public int getNumberOfColums()
    {
        return this.numberOfColums;
    }

    /**
     * Number of rows.<br>
     * Header line is not count.
     *
     * @return Number of rows.
     */
    public int getNumberOfLines()
    {
        return this.numberOfLines;
    }

    /**
     * Obtain a header
     *
     * @param column Column
     * @return Header
     */
    public Header getHeader(int column)
    {
        return this.headers.get(column);
    }

    /**
     * Obtain a cell
     *
     * @param column Cell column
     * @param line   Cell row
     * @return The cell
     */
    public Cell getCell(int column, int line)
    {
        return this.cells.get(column + line * this.numberOfColums);
    }

    /**
     * Add a column
     *
     * @param index Index where insert the column
     */
    public void addColumn(int index)
    {
        index = UtilMath.limit(index, 0, this.numberOfColums);

        if (index == this.numberOfColums)
        {
            this.headers.add(new Header());
        }
        else
        {
            this.headers.add(index, new Header());
        }

        this.numberOfColums++;
        final int size = this.numberOfLines * this.numberOfColums;

        do
        {
            if (index >= size)
            {
                this.cells.add(new Cell());
            }
            else
            {
                this.cells.add(index, new Cell());
            }

            index += this.numberOfColums;
        }
        while (index < size);
    }

    /**
     * Add a line.
     *
     * @param index Index where insert the line.
     */
    public void addLine(int index)
    {
        index = UtilMath.limit(index, 0, this.numberOfLines) * numberOfColums;

        if (index >= this.cells.size())
        {
            for (int i = 0; i < this.numberOfColums; i++)
            {
                this.cells.add(new Cell());
            }

            this.numberOfLines++;
            return;
        }

        for (int i = 0; i < this.numberOfColums; i++)
        {
            this.cells.add(index, new Cell());
        }

        this.numberOfLines++;
    }

    /**
     * Remove a column
     *
     * @param column Column to remove
     */
    public void removeColumn(int column)
    {
        if (this.numberOfColums <= 1 || column < 0 || column >= this.numberOfColums)
        {
            return;
        }

        this.headers.remove(column);
        int index = column + (this.numberOfLines - 1) * this.numberOfColums;

        while (index >= 0)
        {
            this.cells.remove(index);
            index -= this.numberOfColums;
        }

        this.numberOfColums--;
    }

    /**
     * Remove a line
     *
     * @param line Line to remove
     */
    public void removeLine(int line)
    {
        if (this.numberOfLines <= 1 || line < 0 || line >= this.numberOfLines)
        {
            return;
        }

        int index = line * this.numberOfColums + this.numberOfColums - 1;

        for (int i = 0; i < this.numberOfColums; i++, index--)
        {
            this.cells.remove(index);
        }

        this.numberOfLines--;
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

        for (Header header : this.headers)
        {
            stringBuilder.append(header);
        }

        stringBuilder.append('\n');

        int index = 0;

        for (Cell cell : this.cells)
        {
            stringBuilder.append(cell);
            index++;

            if (index % this.numberOfColums == 0)
            {
                stringBuilder.append('\n');
            }
        }

        return stringBuilder.toString();
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
        bufferedWriter.write("<table");

        if (this.floating)
        {
            bufferedWriter.write(" class=\"tableWithFloatingHeader\"");
        }

        bufferedWriter.write(">");
        bufferedWriter.newLine();
        bufferedWriter.write("<tr>");

        for (Header header : this.headers)
        {
            header.writeInHTML(project, bufferedWriter);
        }

        bufferedWriter.write("</tr>");
        bufferedWriter.newLine();
        bufferedWriter.write("<tr>");

        int index = 0;

        for (Cell cell : this.cells)
        {
            cell.writeInHTML(project, bufferedWriter);
            index++;

            if (index % this.numberOfColums == 0)
            {
                bufferedWriter.write("</tr>");
                bufferedWriter.newLine();
                bufferedWriter.write("<tr>");
            }
        }

        bufferedWriter.write("</tr>");
        bufferedWriter.newLine();
        bufferedWriter.write("</table>");
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
        this.floating = byteArray.readBoolean();
        this.numberOfColums = byteArray.readInteger();
        this.numberOfLines = byteArray.readInteger();

        try
        {
            this.headers.clear();
            byteArray.readBinarizableList(this.headers);
            this.cells.clear();
            byteArray.readBinarizableList(this.cells);
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
        byteArray.writeBoolean(this.floating);
        byteArray.writeInteger(this.numberOfColums);
        byteArray.writeInteger(this.numberOfLines);
        byteArray.writeBinarizableList(this.headers);
        byteArray.writeBinarizableList(this.cells);
    }

    /**
     * Try to compress the table
     *
     * @return {@code true} if some compression happen
     */
    @Override
    public boolean compress()
    {
        boolean compress = false;

        for (Header header : this.headers)
        {
            compress |= header.compress();
        }

        for (Cell cell : this.cells)
        {
            compress |= cell.compress();
        }

        return compress;
    }
}
