package jhelp.websitecreator.model.table; /**
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

import org.junit.Test;

import jhelp.websitecreator.model.text.ElementText;

/**
 * Tests of {@link Table}
 */
public class TableTest
{
    /**
     * Add tests
     */
    @Test
    public void testAdd()
    {
        Table table = new Table();

        table.getHeader(0)
             .getBlockText()
             .addElement(createText("0"));
        table.getCell(0, 0)
             .getBlockText()
             .addElement(createText("0,0"));

        table.addColumn(1);
        table.getHeader(1)
             .getBlockText()
             .addElement(createText("1"));
        table.getCell(1, 0)
             .getBlockText()
             .addElement(createText("1,0"));

        table.addLine(1);
        table.getCell(0, 1)
             .getBlockText()
             .addElement(createText("0,1"));
        table.getCell(1, 1)
             .getBlockText()
             .addElement(createText("1,1"));

        table.addColumn(1);
        table.addLine(1);

        table.addColumn(0);
        table.addLine(0);
    }

    /**
     * Create element text
     *
     * @param text Embed text
     * @return Created element text
     */
    private static ElementText createText(String text)
    {
        ElementText elementText = new ElementText();
        elementText.setText(text);
        return elementText;
    }
}