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

import org.junit.Test;

import jhelp.websitecreator.resources.Colors;
import jhelp.websitecreator.tests.AssertExtention;

/**
 * Tests of {@link Button}
 */
public class ButtonTest
{
    /**
     * Test of generation button to HTML
     */
    @Test
    public void testWriteInHTML()
    {
        Button button = new Button();
        button.setId("id");
        AssertExtention.assertHtmlIs("Base button",
                                     "<button id=\"id\" class=\"col-1 col-m-1 green\"></button>\n",
                                     button);
        button.setText("Click me !");
        AssertExtention.assertHtmlIs("Base button + text",
                                     "<button id=\"id\" class=\"col-1 col-m-1 green\">Click me !</button>\n",
                                     button);
        button.setColor(Colors.COLORS.obtainColor("red"));
        AssertExtention.assertHtmlIs("red button + text",
                                     "<button id=\"id\" class=\"col-1 col-m-1 red\">Click me !</button>\n",
                                     button);
        button.setSize(5);
        AssertExtention.assertHtmlIs("red text 5",
                                     "<button id=\"id\" class=\"col-5 col-m-5 red\">Click me !</button>\n",
                                     button);
        button.setAction("ENABLE:button2");
        AssertExtention.assertHtmlIs("red text 5 action",
                                     "<button id=\"id\" class=\"col-5 col-m-5 red\" onClick=\"script:clickOn" +
                                             "('ENABLE:button2')\">Click me !</button>\n",
                                     button);
        button.setUrl("www.jhelp.fr");
        AssertExtention.assertHtmlIs("red text 5 action url",
                                     "<a href=\"www.jhelp.fr\"><button id=\"id\" class=\"col-5 col-m-5 red\" " +
                                             "onClick=\"script:clickOn('ENABLE:button2')\">Click me !</button></a>\n",
                                     button);
    }
}