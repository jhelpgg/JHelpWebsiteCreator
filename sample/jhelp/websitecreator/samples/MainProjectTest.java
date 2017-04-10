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
package jhelp.websitecreator.samples;

import java.io.File;

import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.io.UtilIO;
import jhelp.websitecreator.model.Code;
import jhelp.websitecreator.model.CodeLanguage;
import jhelp.websitecreator.model.Project;
import jhelp.websitecreator.model.WebPage;
import jhelp.websitecreator.model.table.Table;
import jhelp.websitecreator.model.text.BlockText;
import jhelp.websitecreator.model.text.ElementImage;
import jhelp.websitecreator.model.text.ElementText;
import jhelp.websitecreator.resources.Colors;

/**
 * Simple test of HTML generation
 */
public class MainProjectTest
{
    /**
     * Launch the test
     *
     * @param args Unused
     */
    public static void main(String[] args)
    {
        try
        {
            File directory = UtilIO.createTemporaryDirectory();
            Debug.println(DebugLevel.INFORMATION, directory.getAbsolutePath());
            Project project = Project.newProject("project", directory);
            WebPage webPage = project.createWebPage("page");
            webPage.getMenu()
                   .createNewButton()
                   .setText("button");
            Table table = new Table();
            table.addColumn(0);
            table.addColumn(0);
            table.addLine(0);
            table.addLine(0);

            table.getHeader(0)
                 .getBlockText()
                 .addElement(createText("head 0"));
            table.getHeader(1)
                 .getBlockText()
                 .addElement(createText("head 1"));
            table.getHeader(2)
                 .getBlockText()
                 .addElement(createText("head 2"));

            for (int j = 0; j < 3; j++)
            {
                for (int i = 0; i < 3; i++)
                {
                    table.getCell(i, j)
                         .getBlockText()
                         .addElement(createText(i + ", " + j));
                }
            }

            webPage.getContent()
                   .addElement(table);

            project.importImage(new File("/home/jhelp/Images/1.jpg"));
            BlockText blockText = new BlockText();
            blockText.addElement(createText("Some text."));
            ElementText elementText = createText("\n This text is very informative !\n");
            elementText.setColor(Colors.COLORS.obtainColor("yellow"));
            blockText.addElement(elementText);
            ElementImage elementImage = new ElementImage();
            elementImage.setImageName("1.jpg");
            blockText.addElement(elementImage);
            blockText.addElement(createText("Some <i>other</i> text.\nAn other text with a very usefull information !"));
            webPage.getContent()
                   .addElement(blockText);
            Code code = new Code();
            code.setCodeLanguage(CodeLanguage.JAVA);
            code.setCode(
                    "<b>pom</b> public class Main()\n{\n\tpublic static void main(String[] args)\n\t{\n\t\t// 1 < 2 && 3" +
                            " > 2 <br> <b>pom</b>\n\t}\n}");
            webPage.getContent()
                   .addElement(code);

            project.generate();
            UtilIO.readUserInputInConsole();
            UtilIO.delete(directory);
        }
        catch (Exception e)
        {
            Debug.printException(e, "Failed !");
        }

    }

    /**
     * Create an element text
     *
     * @param text Text inside
     * @return Created element text
     */
    private static ElementText createText(String text)
    {
        ElementText elementText = new ElementText();
        elementText.setText(text);
        return elementText;
    }
}
