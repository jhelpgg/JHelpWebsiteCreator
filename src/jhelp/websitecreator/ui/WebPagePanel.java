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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jhelp.gui.FoldLocation;
import jhelp.gui.JHelpFoldablePanel;
import jhelp.gui.action.GenericAction;
import jhelp.gui.layout.VerticalLayout;
import jhelp.gui.smooth.JHelpConstantsSmooth;
import jhelp.util.gui.UtilGUI;
import jhelp.websitecreator.model.Button;
import jhelp.websitecreator.model.Code;
import jhelp.websitecreator.model.Content;
import jhelp.websitecreator.model.ContentElement;
import jhelp.websitecreator.model.Project;
import jhelp.websitecreator.model.WebPage;
import jhelp.websitecreator.model.table.Table;
import jhelp.websitecreator.model.text.BlockElement;
import jhelp.websitecreator.model.text.BlockText;
import jhelp.websitecreator.model.text.ElementImage;
import jhelp.websitecreator.model.text.ElementText;
import jhelp.websitecreator.model.ui.MenuListCellRenderer;
import jhelp.websitecreator.model.ui.MenuModel;
import jhelp.websitecreator.resources.Color;
import jhelp.websitecreator.resources.ResourcesWebSiteCreator;
import jhelp.websitecreator.util.UtilWebsiteCreator;

/**
 * Panel for edit a web page
 */
public class WebPagePanel extends JPanel implements ListSelectionListener, FocusListener, MouseListener,
        PropertyChangeListener, ImageChooserFrame.ImageChooserCallback
{
    /**
     * Current project
     */
    private final Project            project;
    /**
     * Edited web page
     */
    private final WebPage            webPage;
    /**
     * Model of menu
     */
    private final MenuModel          menuModel;
    /**
     * Left panel can be fold
     */
    private final JHelpFoldablePanel foldablePanelMenu;
    /**
     * Popup for choose color or edit text
     */
    private final EditionPopup       editionPopup;
    /**
     * Menu list
     */
    private final JList<Button>      menuList;
    /**
     * Main title text field
     */
    private final JTextField         mainTitleTextField;
    /**
     * Main panel
     */
    private final JPanel             mainPanel;
    /**
     * Panel for add elements
     */
    private final JPanel             panelAddElement;
    /**
     * Web page content
     */
    private final Content            content;

    /**
     * Create the panel
     *
     * @param project Current project
     * @param webPage Edited web page
     */
    public WebPagePanel(Project project, WebPage webPage)
    {
        super(new BorderLayout());
        this.project = project;
        this.webPage = webPage;
        this.webPage.compress();
        this.content = this.webPage.getContent();
        this.menuModel = new MenuModel(this.webPage.getMenu());
        this.editionPopup = new EditionPopup();
        JPanel left = new JPanel(new BorderLayout());
        this.menuList = new JList<>(this.menuModel);
        this.menuList.setCellRenderer(new MenuListCellRenderer());
        this.menuList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.menuList.addListSelectionListener(this);
        left.add(new JButton(new ActionAddMenuButton()), BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(this.menuList);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        left.add(scrollPane, BorderLayout.CENTER);
        this.foldablePanelMenu = new JHelpFoldablePanel("MENU", left, FoldLocation.RIGHT);
        this.add(this.foldablePanelMenu, BorderLayout.WEST);
        this.mainTitleTextField = new JTextField(webPage.getMainTitle(), 64);
        this.mainTitleTextField.setHorizontalAlignment(JTextField.CENTER);
        this.mainTitleTextField.setFont(JHelpConstantsSmooth.FONT_TITLE.getFont());
        this.mainTitleTextField.addFocusListener(this);
        this.mainTitleTextField.addMouseListener(this);
        this.mainTitleTextField.addPropertyChangeListener("background", this);
        this.mainTitleTextField.addPropertyChangeListener("foreground", this);
        this.add(this.mainTitleTextField, BorderLayout.NORTH);
        this.mainPanel = new JPanel(new VerticalLayout(VerticalLayout.EXTENDS_WIDTH));
        this.add(new JScrollPane(this.mainPanel), BorderLayout.CENTER);
        this.panelAddElement = new JPanel(new FlowLayout(FlowLayout.CENTER));
        this.panelAddElement.add(new JButton(new ActionAddText()));
        this.panelAddElement.add(new JButton(new ActionAddImage()));
        this.panelAddElement.add(new JButton(new ActionAddTable()));
        this.panelAddElement.add(new JButton(new ActionAddCode()));
        this.mainPanel.add(this.panelAddElement);
        this.updateComponents();
    }

    /**
     * Update components
     */
    void updateComponents()
    {
        UtilWebsiteCreator.applyColor(this.mainTitleTextField, this.webPage.getMainTitleColor());

        for (ContentElement contentElement : this.content)
        {
            this.appendComponent(this.createEditor(contentElement));
        }
    }

    /**
     * Create an editor for a content element
     *
     * @param contentElement Content element to edit
     * @return Created editor
     */
    private JComponent createEditor(ContentElement contentElement)
    {
        if (contentElement instanceof BlockText)
        {
            return this.createBlockTextEditor((BlockText) contentElement);
        }

        if (contentElement instanceof Table)
        {
            return this.createTableEditor((Table) contentElement);
        }

        if (contentElement instanceof Code)
        {
            return this.createCodeEditor((Code) contentElement);
        }

        return null;
    }

    /**
     * Create block text editor
     *
     * @param blockText Bloack text to edit
     * @return Created editor
     */
    private BlockTextEditor createBlockTextEditor(BlockText blockText)
    {
        return new BlockTextEditor(this.project.imagesDirectory(), blockText);
    }

    /**
     * Create table editor
     *
     * @param table Table to edit
     * @return Created editor
     */
    private TableEditor createTableEditor(Table table)
    {
        return new TableEditor(this.project.imagesDirectory(), table);
    }

    /**
     * Create code editor
     *
     * @param code Code to edit
     * @return Created editor
     */
    private CodeEditor createCodeEditor(Code code)
    {
        return new CodeEditor(code);
    }

    /**
     * Append a component
     *
     * @param component Component to append
     */
    private void appendComponent(JComponent component)
    {
        if (component != null)
        {
            this.mainPanel.add(component, this.mainPanel.getComponentCount() - 1);
        }
    }

    /**
     * Add a menu button
     */
    void addMenuButton()
    {
        String text = JOptionPane.showInputDialog("Content");
        this.menuModel.addButton(text);
        this.foldablePanelMenu.revalidate();
    }

    /**
     * Called whenever the value of the selection changes.
     *
     * @param listSelectionEvent the event that characterizes the change.
     */
    @Override
    public void valueChanged(ListSelectionEvent listSelectionEvent)
    {
        if (listSelectionEvent.getValueIsAdjusting())
        {
            return;
        }

        final int index = this.menuList.getSelectedIndex();

        if (index >= 0 && index < this.menuModel.getSize())
        {
            this.editionPopup.edit(this.menuModel.getElementAt(index));
            final Rectangle rectangle = this.menuList.getCellBounds(index, index);
            this.menuList.clearSelection();
            this.editionPopup.show(this.menuList,
                                   rectangle.x + (rectangle.width >> 1),
                                   rectangle.y + (rectangle.height >> 1));
        }
    }

    /**
     * Invoked when a component gains the keyboard focus.
     *
     * @param focusEvent Event description
     */
    @Override
    public void focusGained(FocusEvent focusEvent)
    {
        //Nothing to do for now
    }

    /**
     * Invoked when a component loses the keyboard focus.
     *
     * @param focusEvent Event description
     */
    @Override
    public void focusLost(FocusEvent focusEvent)
    {
        final String text = this.mainTitleTextField.getText();
        this.webPage.setMainTitle(text);
        this.webPage.setTitle(text);
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
        if (SwingUtilities.isRightMouseButton(mouseEvent))
        {
            final JComponent component = (JComponent) mouseEvent.getComponent();

            if (component == null)
            {
                return;
            }

            Point position = UtilGUI.getLocationOn(component, this);
            assert position != null;
            Dimension size = component.getSize();
            this.editionPopup.edit(component);
            this.editionPopup.show(this, position.x + (size.width >> 1), position.y + (size.height >> 1));
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
        //Nothing to do for now
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param mouseEvent Event description
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent)
    {
        //Nothing to do for now
    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param mouseEvent Event description
     */
    @Override
    public void mouseEntered(MouseEvent mouseEvent)
    {
        //Nothing to do for now
    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param mouseEvent Event description
     */
    @Override
    public void mouseExited(MouseEvent mouseEvent)
    {
        //Nothing to do for now
    }

    /**
     * This method gets called when a bound property is changed.
     *
     * @param propertyChangeEvent A PropertyChangeEvent object describing the event source
     *                            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent propertyChangeEvent)
    {
        final Color color = UtilWebsiteCreator.extractColor(this.mainTitleTextField);

        if (color != null)
        {
            this.webPage.setMainTitleColor(color);
        }
    }

    /**
     * Add a text element
     */
    void addText()
    {
        ContentElement contentElement = this.content.getLastElement();

        if (contentElement != null && (contentElement instanceof BlockText))
        {
            BlockText blockText = (BlockText) contentElement;
            int       size      = blockText.numberOfElements();

            if (size > 0)
            {
                BlockElement blockElement = blockText.getElement(size - 1);

                if (blockElement instanceof ElementText)
                {
                    return;
                }
            }

            blockText.addElement(new ElementText());
            BlockTextEditor blockTextEditor = (BlockTextEditor) this.mainPanel.getComponent(
                    this.mainPanel.getComponentCount() - 2);
            blockTextEditor.update();
            return;
        }

        BlockText blockText = new BlockText();
        blockText.addElement(new ElementText());
        this.content.addElement(blockText);
        this.appendComponent(this.createBlockTextEditor(blockText));
    }

    /**
     * Add an image
     */
    void addImage()
    {
        ImageChooserFrame.chooseImage(this.project, this);
    }

    /**
     * Add code editor
     */
    void addCode()
    {
        Code code = new Code();
        this.content.addElement(code);
        this.appendComponent(this.createCodeEditor(code));
    }

    /**
     * Add table
     */
    void addTable()
    {
        Table table = new Table();
        this.content.addElement(table);
        this.appendComponent(this.createTableEditor(table));
    }

    /**
     * Called when image is choose
     *
     * @param name Image choosen name
     */
    @Override
    public void imageChoose(String name)
    {
        ElementImage elementImage = new ElementImage();
        elementImage.setImageName(name);
        ContentElement contentElement = this.content.getLastElement();

        if (contentElement != null && (contentElement instanceof BlockText))
        {
            ((BlockText) contentElement).addElement(elementImage);
            BlockTextEditor blockTextEditor = (BlockTextEditor) this.mainPanel.getComponent(
                    this.mainPanel.getComponentCount() - 2);
            blockTextEditor.update();
            return;
        }

        BlockText blockText = new BlockText();
        blockText.addElement(elementImage);
        this.content.addElement(blockText);
        this.appendComponent(this.createBlockTextEditor(blockText));
    }

    /**
     * Action for add menu button
     */
    class ActionAddMenuButton extends GenericAction
    {
        /**
         * Create tha action
         */
        ActionAddMenuButton()
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
            WebPagePanel.this.addMenuButton();
        }
    }

    /**
     * Action that add text
     */
    class ActionAddText extends GenericAction
    {
        /**
         * Create action
         */
        ActionAddText()
        {
            super("", ResourcesWebSiteCreator.ICON_TEXT);
        }

        /**
         * Called when action have to do its action
         *
         * @param actionEvent Action event description
         */
        @Override
        protected void doActionPerformed(ActionEvent actionEvent)
        {
            WebPagePanel.this.addText();
        }
    }

    /**
     * Action for add image
     */
    class ActionAddImage extends GenericAction
    {
        /**
         * Create action
         */
        ActionAddImage()
        {
            super("", ResourcesWebSiteCreator.ICON_IMAGE);
        }

        /**
         * Called when action have to do its action
         *
         * @param actionEvent Action event description
         */
        @Override
        protected void doActionPerformed(ActionEvent actionEvent)
        {
            WebPagePanel.this.addImage();
        }
    }

    /**
     * Action create a table
     */
    class ActionAddTable extends GenericAction
    {
        /**
         * Create the action
         */
        ActionAddTable()
        {
            super("", ResourcesWebSiteCreator.ICON_TABLE);
        }

        /**
         * Called when action have to do its action
         *
         * @param actionEvent Action event description
         */
        @Override
        protected void doActionPerformed(ActionEvent actionEvent)
        {
            WebPagePanel.this.addTable();
        }
    }

    /**
     * Action to add code
     */
    class ActionAddCode extends GenericAction
    {
        /**
         * Create action
         */
        ActionAddCode()
        {
            super("", ResourcesWebSiteCreator.ICON_CODE);
        }

        /**
         * Called when action have to do its action
         *
         * @param actionEvent Action event description
         */
        @Override
        protected void doActionPerformed(ActionEvent actionEvent)
        {
            WebPagePanel.this.addCode();
        }
    }
}
