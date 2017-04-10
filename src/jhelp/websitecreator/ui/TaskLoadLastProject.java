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

import java.io.File;

import jhelp.util.text.UtilText;
import jhelp.util.thread.ThreadedSimpleTask;
import jhelp.websitecreator.model.Project;
import jhelp.websitecreator.resources.ResourcesWebSiteCreator;
import jhelp.websitecreator.util.UtilWebsiteCreator;

/**
 * Task for load last project
 */
class TaskLoadLastProject extends ThreadedSimpleTask<FrameWebsiteCreator>
{
    /**
     * Create the task
     */
    TaskLoadLastProject()
    {
    }

    /**
     * Do the simple action
     *
     * @param frameWebsiteCreator Parameter to use
     */
    @Override
    protected void doSimpleAction(FrameWebsiteCreator frameWebsiteCreator)
    {
        File lastProject = ResourcesWebSiteCreator.PREFERENCES.getFileValue(
                ResourcesWebSiteCreator.PREFERENCE_LAST_PROJECT);

        if (lastProject == null)
        {
            return;
        }

        Project project = UtilWebsiteCreator.load(lastProject);

        if (project == null)
        {
            String message = ResourcesWebSiteCreator.RESOURCE_TEXT.getText(
                    ResourcesWebSiteCreator.TEXT_LAST_PROJECT_LOAD_FAILED_MESSAGE);
            message = UtilText.replaceHole(message, lastProject.getName());
            frameWebsiteCreator.showError(ResourcesWebSiteCreator.TEXT_LAST_PROJECT_LOAD_FAILED_TITLE, message);
            return;
        }

        frameWebsiteCreator.setProject(project);
    }
}
