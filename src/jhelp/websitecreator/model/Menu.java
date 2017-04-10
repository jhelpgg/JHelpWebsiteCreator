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

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jhelp.util.io.Binarizable;
import jhelp.util.io.ByteArray;


/**
 * Web page menu
 */
public class Menu implements WritableInHTML, Binarizable
{
    /**
     * Menu buttons
     */
    private final List<Button> buttons;

    /**
     * Create a menu
     */
    public Menu()
    {
        this.buttons = new ArrayList<>();
    }

    /**
     * Create and add a new button.<br>
     * Created button will have good size for menu.
     *
     * @return Created button
     */
    public Button createNewButton()
    {
        final Button button = new Button();
        button.setSize(12);
        this.buttons.add(button);
        return button;
    }

    /**
     * Number of buttons
     *
     * @return Number of buttons
     */
    public int numberOfButtons()
    {
        return this.buttons.size();
    }

    /**
     * Return a button
     *
     * @param index Button index
     * @return Desired button
     */
    public Button getButton(int index)
    {
        return this.buttons.get(index);
    }

    /**
     * Remove a button
     *
     * @param button Button to remove
     */
    public void removeButton(Button button)
    {
        this.buttons.remove(button);
    }

    /**
     * Remove a button
     *
     * @param index Button index to remove
     */
    public void removeButton(int index)
    {
        this.buttons.remove(index);
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
        for (Button button : this.buttons)
        {
            button.writeInHTML(project, bufferedWriter);
        }
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
        try
        {
            this.buttons.clear();
            byteArray.readBinarizableList(this.buttons);
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
        byteArray.writeBinarizableList(this.buttons);
    }
}
