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
import java.io.File;
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
 * Created ActionOpenProject : TODO describe action
 */
class ActionOpenProject extends GenericAction
{
    /**
     * Frame parent
     */
    private final WeakReference<FrameWebsiteCreator> frameParentWeakReference;

    /**
     * Created ActionOpenProject instance
     *
     * @param frameParent Frame parent
     */
    ActionOpenProject(FrameWebsiteCreator frameParent)
    {
        super(ResourcesWebSiteCreator.TEXT_ACTION_OPEN_PROJECT, ResourcesWebSiteCreator.RESOURCE_TEXT);
        this.frameParentWeakReference = new WeakReference<FrameWebsiteCreator>(frameParent);
        UtilAction.defineShortCut(UtilGUI.createKeyStroke('O', true), this, frameParent);
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

        File file = frameParent.getFileChooser()
                               .showOpenFile();

        if (file == null)
        {
            return;
        }

        Project project = UtilWebsiteCreator.load(file);

        if (project != null)
        {
            frameParent.setProject(project);
        }

        //TODO Implements doActionPerformed : Alert user on succed or failure
        Debug.printTodo("Implements doActionPerformed : Alert user on succed or failure");
    }
}
