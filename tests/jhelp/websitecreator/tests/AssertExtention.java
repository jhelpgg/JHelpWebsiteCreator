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
package jhelp.websitecreator.tests;

import org.junit.Assert;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

import jhelp.util.debug.Debug;
import jhelp.websitecreator.model.Project;
import jhelp.websitecreator.model.WritableInHTML;

/**
 * Additional assertion methods for test
 */
public class AssertExtention
{
    /**
     * Assert that a {@link WritableInHTML} produce an expected result
     *
     * @param message        Assertion message
     * @param expectedHTML   Expected HTML result
     * @param writableInHTML {@link WritableInHTML} tested
     */
    public static void assertHtmlIs(String message, String expectedHTML, WritableInHTML writableInHTML)
    {
        try
        {
            Project      project      = new Project();
            StringWriter stringWriter = new StringWriter();
            writableInHTML.writeInHTML(project, new BufferedWriter(stringWriter));
            Assert.assertEquals(message, expectedHTML, stringWriter.toString());
        }
        catch (IOException e)
        {
            Debug.printException(e, "Failed to write in string");
            Assert.fail(message + " : Exception happen while writing");
        }
    }
}
