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
package jhelp.websitecreator.ui;

import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;

import jhelp.gui.action.GenericAction;
import jhelp.gui.action.UtilAction;
import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.gui.UtilGUI;

/**
 * Created ActionExit : TODO describe action
 */
class ActionExit extends GenericAction
{
    /**
     * Frame parent
     */
    private final WeakReference<FrameWebsiteCreator> frameParentWeakReference;

    /**
     * Created ActionExit instance
     *
     * @param frameParent Frame parent
     */
    ActionExit(FrameWebsiteCreator frameParent)
    {
        super("Exit");
        this.frameParentWeakReference = new WeakReference<FrameWebsiteCreator>(frameParent);
        UtilAction.defineShortCut(UtilGUI.createKeyStroke('X', true, true), this, frameParent);
    }

    /**
     * Called when action have to do its action
     *
     * @param actionEvent Action event description
     */
    @Override
    protected void doActionPerformed(ActionEvent actionEvent)
    {
        final FrameWebsiteCreator frameParent = this.frameParentWeakReference.get();

        if (frameParent == null)
        {
            Debug.println(DebugLevel.WARNING, "Reference to frame parent lost");
            return;
        }

        frameParent.closeFrame();
    }
}
