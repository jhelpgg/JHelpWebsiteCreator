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

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import jhelp.gui.action.GenericAction;
import jhelp.gui.layout.CenterLayout;
import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.text.UtilText;
import jhelp.websitecreator.model.Project;
import jhelp.websitecreator.model.WebPage;

/**
 * Tabbed panel of web pages
 */
public class WebPagesTabbedPanel extends JTabbedPane
{
    /**
     * Frame parent
     */
    private final WeakReference<FrameWebsiteCreator> frameParentWeakReference;
    /**
     * Add a web page panel
     */
    private final JPanel                             panelAddWebPage;
    /**
     * Text filed for web page name
     */
    private final JTextField                         webPageName;

    /**
     * Create the tabbed pane
     *
     * @param frameParent Frame parent
     */
    public WebPagesTabbedPanel(FrameWebsiteCreator frameParent)
    {
        super(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
        this.frameParentWeakReference = new WeakReference<FrameWebsiteCreator>(frameParent);
        this.webPageName = new JTextField(30);
        this.panelAddWebPage = new JPanel(new CenterLayout());
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(this.webPageName);
        panel.add(new JButton(new AddWebPageAction()));
        this.panelAddWebPage.add(panel);
    }

    /**
     * Refresh all tabs
     */
    public void refreshAllTabs()
    {
        final FrameWebsiteCreator frameParent = this.frameParentWeakReference.get();

        if (frameParent == null)
        {
            Debug.println(DebugLevel.WARNING, "Reference to frame parent lost");
            return;
        }

        final Project project = frameParent.getProject();

        if (project == null)
        {
            return;
        }

        this.removeAll();
        int     size = project.numberOfPages();
        WebPage webPage;

        for (int i = 0; i < size; i++)
        {
            webPage = project.getPage(i);
            this.addTab(webPage.getName(), new WebPagePanel(project, webPage));
        }

        this.addTab(" + ", this.panelAddWebPage);
        this.setSelectedIndex(0);
    }

    /**
     * Add a web page
     */
    void doAddWebPage()
    {
        String name = UtilText.removeWhiteCharacters(this.webPageName.getText());

        if (name.length() == 0)
        {
            return;
        }

        final FrameWebsiteCreator frameParent = this.frameParentWeakReference.get();

        if (frameParent == null)
        {
            Debug.println(DebugLevel.WARNING, "Reference to frame parent lost");
            return;
        }

        final Project project = frameParent.getProject();

        if (project == null)
        {
            return;
        }

        this.webPageName.setText("");
        WebPage webPage = project.createWebPage(name);
        webPage.setTitle(name);
        webPage.setMainTitle(name);
        final int index = this.getTabCount() - 1;
        this.insertTab(webPage.getName(), null, new WebPagePanel(project, webPage), null, index);
        this.setSelectedIndex(index);
    }

    /**
     * Action for add web page
     */
    class AddWebPageAction extends GenericAction
    {
        /**
         * Create the action
         */
        AddWebPageAction()
        {
            super("   +   ");
        }

        /**
         * Called when action have to do its action
         *
         * @param actionEvent Action event description
         */
        @Override
        protected void doActionPerformed(ActionEvent actionEvent)
        {
            WebPagesTabbedPanel.this.doAddWebPage();
        }
    }
}
