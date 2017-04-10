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

import jhelp.gui.DirectoryChooser;
import jhelp.gui.action.GenericAction;
import jhelp.gui.action.UtilAction;
import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.gui.UtilGUI;
import jhelp.util.io.UtilIO;
import jhelp.util.text.UtilText;
import jhelp.websitecreator.model.Project;
import jhelp.websitecreator.resources.ResourcesWebSiteCreator;
import jhelp.websitecreator.util.UtilWebsiteCreator;

/**
 * Created ActionNewProject : TODO describe action
 */
class ActionNewProject extends GenericAction
{
    /**
     * Frame parent
     */
    private final WeakReference<FrameWebsiteCreator> frameParentWeakReference;

    /**
     * Created ActionNewProject instance
     *
     * @param frameParent Frame parent
     */
    ActionNewProject(FrameWebsiteCreator frameParent)
    {
        super(ResourcesWebSiteCreator.TEXT_ACTION_NEW_PROJECT, ResourcesWebSiteCreator.RESOURCE_TEXT);
        this.frameParentWeakReference = new WeakReference<FrameWebsiteCreator>(frameParent);
        UtilAction.defineShortCut(UtilGUI.createKeyStroke('N', true), this, frameParent);
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

        File last = ResourcesWebSiteCreator.PREFERENCES.getFileValue(ResourcesWebSiteCreator.PREFERENCE_LAST_DIRECTORY);
        File directory;

        if (last == null)
        {
            directory = DirectoryChooser.DIRECTORY_CHOOSER.chooseOneDirectory(frameParent);
        }
        else
        {
            directory = DirectoryChooser.DIRECTORY_CHOOSER.chooseOneDirectory(frameParent, last);
        }

        if (directory == null)
        {
            return;
        }

        ResourcesWebSiteCreator.PREFERENCES.setValue(ResourcesWebSiteCreator.PREFERENCE_LAST_DIRECTORY, directory);

        if (directory.exists())
        {
            String[] list = directory.list();

            if (list != null && list.length > 0)
            {
                String message = ResourcesWebSiteCreator.RESOURCE_TEXT.getText(
                        ResourcesWebSiteCreator.TEXT_ACTION_NEW_PROJECT_DIRECTORY_NOT_EMPTY_MESSAGE);
                message = UtilText.replaceHole(message, directory.getAbsolutePath());
                QuestionAnswer questionAnswer = frameParent.askQuestion(
                        ResourcesWebSiteCreator.TEXT_ACTION_NEW_PROJECT_DIRECTORY_NOT_EMPTY_TITLE,
                        message,
                        QuestionType.YES_NO);

                if (questionAnswer == QuestionAnswer.NO)
                {
                    return;
                }
            }
        }

        UtilIO.delete(directory);
        UtilIO.createDirectory(directory);

        String name = frameParent.inputMessage(
                ResourcesWebSiteCreator.RESOURCE_TEXT.getText(
                        ResourcesWebSiteCreator.TEXT_ACTION_NEW_PROJECT_ASK_NAME),
                ResourcesWebSiteCreator.RESOURCE_TEXT.getText(
                        ResourcesWebSiteCreator.TEXT_ACTION_NEW_PROJECT_DEFAULT_NAME));
        name = UtilText.removeWhiteCharacters(name)
                       .toLowerCase();

        if (name.length() == 0)
        {
            return;
        }

        frameParent.setProject(Project.newProject(name, directory));
        UtilWebsiteCreator.save(frameParent.getProject());
    }
}
