/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 *
 * @author JHelp
 */
package jhelp.websitecreator;

import jhelp.util.gui.UtilGUI;
import jhelp.websitecreator.ui.FrameWebsiteCreator;

/**
 * Launch tool for create web sites.
 */
public class MainWebsiteCreator
{
    /**
     * Launch tool for create web sites.
     *
     * @param args Unsed
     */
    public static void main(String[] args)
    {
        UtilGUI.initializeGUI();
        final FrameWebsiteCreator frameWebsiteCreator = new FrameWebsiteCreator();
        frameWebsiteCreator.setVisible(true);
    }
}
