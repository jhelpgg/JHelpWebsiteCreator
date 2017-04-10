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
import jhelp.websitecreator.model.Project;
import jhelp.websitecreator.resources.ResourcesWebSiteCreator;
import jhelp.websitecreator.util.UtilWebsiteCreator;

/**
 * Created ActionSaveProject : TODO describe action
 */
class ActionSaveProject extends GenericAction
{
    /**
     * Frame parent
     */
    private final WeakReference<FrameWebsiteCreator> frameParentWeakReference;

    /**
     * Created ActionSaveProject instance
     *
     * @param frameParent Frame parent
     */
    ActionSaveProject(FrameWebsiteCreator frameParent)
    {
        super(ResourcesWebSiteCreator.TEXT_ACTION_SAVE_PROJECT, ResourcesWebSiteCreator.RESOURCE_TEXT);
        this.frameParentWeakReference = new WeakReference<FrameWebsiteCreator>(frameParent);
        UtilAction.defineShortCut(UtilGUI.createKeyStroke('S', true), this, frameParent);
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

        Project project = frameParent.getProject();

        if (project == null)
        {
            return;
        }

        UtilWebsiteCreator.save(project);
        frameParent.setProject(project);

        //TODO Implements doActionPerformed : Alert user on succed or failure
        Debug.printTodo("Implements doActionPerformed : Alert user on succed or failure");
    }
}
