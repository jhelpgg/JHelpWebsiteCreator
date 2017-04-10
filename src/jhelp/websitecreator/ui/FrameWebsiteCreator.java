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

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.awt.BorderLayout;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import jhelp.gui.FileChooser;
import jhelp.gui.JHelpFrame;
import jhelp.util.thread.ThreadManager;
import jhelp.websitecreator.model.Project;
import jhelp.websitecreator.resources.ResourcesWebSiteCreator;

/**
 * Main frame of website creator.
 */
public class FrameWebsiteCreator extends JHelpFrame
{
    /**
     * Action for create new project
     */
    private ActionNewProject      actionNewProject;
    /**
     * Action for open project
     */
    private ActionOpenProject     actionOpenProject;
    /**
     * Action for save project
     */
    private ActionSaveProject     actionSaveProject;
    /**
     * Action for generate web site from project
     */
    private ActionGenerateProject actionGenerateProject;
    /**
     * Action for exit frame
     */
    private ActionExit            actionExit;
    /**
     * Edited project
     */
    private Project               project;
    /**
     * Web pages tabbed pane
     */
    private WebPagesTabbedPanel   webPagesTabbedPanel;
    /**
     * File chooser
     */
    private FileChooser           fileChooser;

    /**
     * Create the frame
     */
    public FrameWebsiteCreator()
    {
        super(ResourcesWebSiteCreator.RESOURCE_TEXT.getText(ResourcesWebSiteCreator.TEXT_TITLE),
              true);
        ThreadManager.THREAD_MANAGER.delayedThread(new TaskLoadLastProject(), this, 1024);
    }

    /**
     * Obtain current project
     *
     * @return Current project
     */
    Project getProject()
    {
        return this.project;
    }

    /**
     * Change project
     *
     * @param project New project
     */
    void setProject(
            @NotNull
                    Project project)
    {
        this.project = project;
        this.setTitle(ResourcesWebSiteCreator.RESOURCE_TEXT.getText(
                ResourcesWebSiteCreator.TEXT_TITLE) + " : " + project.getName());
        this.webPagesTabbedPanel.refreshAllTabs();
    }

    /**
     * Add listeners to components
     */
    @Override
    protected void addListeners()
    {
        //Nothing to do
    }

    /**
     * Create components
     */
    @Override
    protected void createComponents()
    {
        this.actionNewProject = new ActionNewProject(this);
        this.actionOpenProject = new ActionOpenProject(this);
        this.actionSaveProject = new ActionSaveProject(this);
        this.actionGenerateProject = new ActionGenerateProject(this);
        this.actionExit = new ActionExit(this);
        this.webPagesTabbedPanel = new WebPagesTabbedPanel(this);
        this.fileChooser = new FileChooser(this);
    }

    /**
     * Layout components inside the frame
     */
    @Override
    protected void layoutComponents()
    {
        this.setLayout(new BorderLayout());
        this.add(this.webPagesTabbedPanel, BorderLayout.CENTER);
        this.setJMenuBar(this.createMenuBar());
    }

    /**
     * Create main menu bar
     *
     * @return Main menu bar
     */
    private JMenuBar createMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(this.createFileMenu());
        return menuBar;
    }

    /**
     * Crete file menu
     *
     * @return File menu
     */
    private JMenu createFileMenu()
    {
        JMenu menu = new JMenu(ResourcesWebSiteCreator.RESOURCE_TEXT.getText(ResourcesWebSiteCreator.TEXT_MENU_FILE));
        menu.add(this.actionNewProject);
        menu.add(this.actionOpenProject);
        menu.add(this.actionSaveProject);
        menu.addSeparator();
        menu.add(this.actionGenerateProject);
        menu.addSeparator();
        menu.add(this.actionExit);
        return menu;
    }

    /**
     * Show an error
     *
     * @param titleKey Key of text for error title
     * @param text     Error message
     */
    public void showError(
            @NotNull
                    String titleKey,
            @NotNull
                    String text)
    {
        JOptionPane.showMessageDialog(this,
                                      text,
                                      ResourcesWebSiteCreator.RESOURCE_TEXT.getText(titleKey),
                                      JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Ask question to user
     *
     * @param titleKey     Key of text for question title
     * @param text         Question text
     * @param questionType Question type
     * @return Question answer
     */
    public @NotNull
    QuestionAnswer askQuestion(
            @NotNull
                    String titleKey,
            @NotNull
                    String text,
            @NotNull
                    QuestionType questionType)
    {
        if (questionType == QuestionType.OK)
        {
            JOptionPane.showMessageDialog(this,
                                          text,
                                          ResourcesWebSiteCreator.RESOURCE_TEXT.getText(titleKey),
                                          JOptionPane.INFORMATION_MESSAGE);
            return QuestionAnswer.OK;
        }

        int type = JOptionPane.DEFAULT_OPTION;

        switch (questionType)
        {
            case OK_CANCEL:
                type = JOptionPane.OK_CANCEL_OPTION;
                break;
            case YES_NO:
                type = JOptionPane.YES_NO_OPTION;
                break;
            case YES_NO_CANCEL:
                type = JOptionPane.YES_NO_CANCEL_OPTION;
                break;
        }

        int answer = JOptionPane.showConfirmDialog(this,
                                                   text,
                                                   ResourcesWebSiteCreator.RESOURCE_TEXT.getText(titleKey),
                                                   type,
                                                   JOptionPane.QUESTION_MESSAGE);

        switch (answer)
        {
            case JOptionPane.YES_OPTION: // It is also JOptionPane.OK_OPTION
                switch (questionType)
                {
                    case YES_NO:
                    case YES_NO_CANCEL:
                        return QuestionAnswer.YES;

                    case OK_CANCEL: //OK alone was already treat
                        return QuestionAnswer.OK;
                }

                break;
            case JOptionPane.NO_OPTION:
                return QuestionAnswer.NO;
            case JOptionPane.CANCEL_OPTION:
                return QuestionAnswer.CANCEL;
        }

        return QuestionAnswer.OK;
    }

    /**
     * Ask user to type test
     *
     * @param message Message
     * @return Typed text OR {@code null} if edition cancel
     */
    public @Nullable
    String inputMessage(
            @NotNull
                    String message)
    {
        return this.inputMessage(message, "");
    }

    /**
     * Ask user to type test
     *
     * @param message      Message
     * @param defaultValue Initial text
     * @return Typed text OR {@code null} if edition cancel
     */
    public @Nullable
    String inputMessage(
            @NotNull
                    String message,
            @NotNull
                    String defaultValue)
    {
        return JOptionPane.showInputDialog(this, message, defaultValue);
    }

    /**
     * Obtain the file chooser
     *
     * @return The file chooser
     */
    FileChooser getFileChooser()
    {
        this.fileChooser.setStartDirectory(
                ResourcesWebSiteCreator.PREFERENCES.getFileValue(ResourcesWebSiteCreator.PREFERENCE_LAST_DIRECTORY));
        return this.fileChooser;
    }
}
