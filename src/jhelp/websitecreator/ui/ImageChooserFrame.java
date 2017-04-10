/*
 * License :
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any
 * damage it may cause.
 * You can use, modify, the code as your need for any usage.
 * But you can't do any action that avoid me or other person use, modify this code.
 * The code is free for usage and modification, you can't change that fact.
 * JHelp
 */

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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jhelp.gui.FileChooser;
import jhelp.gui.JHelpFrame;
import jhelp.gui.LabelJHelpImage;
import jhelp.gui.action.GenericAction;
import jhelp.util.debug.Debug;
import jhelp.util.gui.JHelpImage;
import jhelp.util.gui.UtilGUI;
import jhelp.util.io.UtilIO;
import jhelp.util.text.UtilText;
import jhelp.websitecreator.model.Project;
import jhelp.websitecreator.resources.ResourcesWebSiteCreator;

/**
 * Frame for choose an image
 */
public class ImageChooserFrame extends JHelpFrame implements MouseListener
{
    /**
     * Image preview size
     */
    private static final int               IMAGE_SIZE                = 128;
    /**
     * Minimum window size
     */
    private static final int               MINIMUM_SIZE              = 750;
    /**
     * Minimum number of columns
     */
    private static final int               MINIMUM_NUMBER_OF_COLUMNS = 4;
    /**
     * Image chooser instance
     */
    private static final ImageChooserFrame IMAGE_CHOOSER_FRAME       = new ImageChooserFrame();
    /**
     * Callbaack to alert when image choose
     */
    private ImageChooserCallback imageChooserCallback;
    /**
     * Current project
     */
    private Project              project;
    /**
     * Main panel with images
     */
    private JPanel               mainPanel;

    /**
     * Create the frame
     */
    private ImageChooserFrame()
    {
        super("Choose image", false, false);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setExitAllOnClose(false);
        this.setDisposeOnClose(false);
    }

    /**
     * Launch the choice of an image
     *
     * @param project              Current projact
     * @param imageChooserCallback Callback to know choosen image
     */
    public static void chooseImage(
            @NotNull
                    Project project,
            @NotNull
                    ImageChooserCallback imageChooserCallback)
    {
        if (project == null)
        {
            throw new NullPointerException("project MUST NOT be null !");
        }

        if (imageChooserCallback == null)
        {
            throw new NullPointerException("imageChooserCallback MUST NOT be null !");
        }

        IMAGE_CHOOSER_FRAME.initializeChooser(project, imageChooserCallback);
        UtilGUI.packedSize(IMAGE_CHOOSER_FRAME);
        Dimension dimension = IMAGE_CHOOSER_FRAME.getSize();
        dimension.width = Math.max(MINIMUM_SIZE, dimension.width);
        dimension.height = Math.max(MINIMUM_SIZE, dimension.height);
        IMAGE_CHOOSER_FRAME.setSize(dimension);
        UtilGUI.centerOnScreen(IMAGE_CHOOSER_FRAME);
        IMAGE_CHOOSER_FRAME.setAlwaysOnTop(true);
        IMAGE_CHOOSER_FRAME.setVisible(true);
    }

    /**
     * Initialize the image chooser
     *
     * @param project              Current project
     * @param imageChooserCallback Callback to alert
     */
    void initializeChooser(Project project, ImageChooserCallback imageChooserCallback)
    {
        this.project = project;
        this.imageChooserCallback = imageChooserCallback;
        this.mainPanel.removeAll();
        File[] images = this.project.imagesDirectory()
                                    .listFiles();

        if (images != null && images.length > 0)
        {
            this.mainPanel.setLayout(
                    new GridLayout(0, Math.max(MINIMUM_NUMBER_OF_COLUMNS, (int) Math.ceil(Math.sqrt(images.length)))));

            for (File image : images)
            {
                this.addImage(image);
            }
        }
        else
        {
            this.mainPanel.setLayout(new GridLayout(0, MINIMUM_NUMBER_OF_COLUMNS));
        }
    }

    /**
     * Add image to image list
     *
     * @param fileImage Image to add
     */
    private void addImage(File fileImage)
    {
        LabelJHelpImage label = new LabelJHelpImage(IMAGE_SIZE, IMAGE_SIZE);
        label.setResize(true);

        try
        {
            JHelpImage image = JHelpImage.loadImageThumb(fileImage, IMAGE_SIZE, IMAGE_SIZE);
            label.setJHelpImage(image);
        }
        catch (IOException e)
        {
            Debug.printException(e, "Failed to load image : ", fileImage.getAbsolutePath());
        }

        label.setName(fileImage.getName());
        label.addMouseListener(this);
        this.mainPanel.add(label);
        this.mainPanel.revalidate();
        this.mainPanel.repaint();
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
        this.mainPanel = new JPanel();
    }

    /**
     * Layout components inside the frame
     */
    @Override
    protected void layoutComponents()
    {
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(this.mainPanel), BorderLayout.CENTER);
        this.add(new JButton(new ActionImportImage()), BorderLayout.SOUTH);
    }

    /**
     * Import an image
     */
    void importImage()
    {
        this.setAlwaysOnTop(false);
        File imageFile = FileChooser.loadAnImage(
                ResourcesWebSiteCreator.PREFERENCES.getFileValue(ResourcesWebSiteCreator.PREFERENCE_LAST_IMAGE));
        this.setAlwaysOnTop(true);

        if (imageFile == null)
        {
            return;
        }

        ResourcesWebSiteCreator.PREFERENCES.setValue(ResourcesWebSiteCreator.PREFERENCE_LAST_IMAGE, imageFile);
        String name   = imageFile.getName();
        int    index  = name.indexOf('.');
        String suffix = "";

        if (index >= 0)
        {
            suffix = name.substring(index);
            name = name.substring(0, index);
        }

        List<String> names = new ArrayList<>();

        name = UtilText.computeNotInsideName(name, names);
        name += suffix;

        try
        {
            File destination = new File(this.project.imagesDirectory(), name);
            UtilIO.copy(imageFile, destination);
            this.addImage(destination);
        }
        catch (IOException e)
        {
            Debug.printException(e, "Failed to copy image : ", imageFile.getAbsolutePath());
        }
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param mouseEvent Event description
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent)
    {
        Component component = mouseEvent.getComponent();

        if (component != null)
        {
            String name = component.getName();

            if (name != null)
            {
                this.imageChooserCallback.imageChoose(name);
                this.closeFrame();
            }
        }
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param mouseEvent Event description
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent)
    {
        //Nothing to do
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param mouseEvent Event description
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent)
    {
        //Nothing to do
    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param mouseEvent Event description
     */
    @Override
    public void mouseEntered(MouseEvent mouseEvent)
    {
        Component component = mouseEvent.getComponent();

        if (component != null && (component instanceof LabelJHelpImage))
        {
            ((LabelJHelpImage) component).setSelected(true);
        }
    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param mouseEvent Event description
     */
    @Override
    public void mouseExited(MouseEvent mouseEvent)
    {
        Component component = mouseEvent.getComponent();

        if (component != null && (component instanceof LabelJHelpImage))
        {
            ((LabelJHelpImage) component).setSelected(false);
        }
    }

    /**
     * Callback to know when image choose
     */
    public static interface ImageChooserCallback
    {
        /**
         * Called when image choose
         *
         * @param name Image choosen name
         */
        public void imageChoose(String name);
    }

    /**
     * Action for import image
     */
    class ActionImportImage extends GenericAction
    {
        /**
         * Create the action
         */
        ActionImportImage()
        {
            super("Import ...");
        }

        /**
         * Called when action have to do its action
         *
         * @param actionEvent Action event description
         */
        @Override
        protected void doActionPerformed(ActionEvent actionEvent)
        {
            ImageChooserFrame.this.importImage();
        }
    }
}
