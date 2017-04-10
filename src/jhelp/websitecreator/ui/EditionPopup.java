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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import jhelp.gui.action.GenericAction;
import jhelp.gui.smooth.JHelpConstantsSmooth;
import jhelp.websitecreator.model.Button;
import jhelp.websitecreator.resources.Color;
import jhelp.websitecreator.resources.Colors;
import jhelp.websitecreator.util.UtilWebsiteCreator;

/**
 * Popup for change color or edit component
 */
public class EditionPopup extends JPopupMenu
{
    /**
     * Special color button for remove colors
     */
    private final ColorButton noColorButton;
    /**
     * Action for edit component
     */
    private final ActionEdit  actionEdit;
    /**
     * Edited button
     */
    private       Button      button;
    /**
     * Edited component
     */
    private       JComponent  component;
    /**
     * Callback to alert when color change or edition done
     */
    private       Callback    callback;
    /**
     * Developer information give back when callback
     */
    private       Object      information;

    /**
     * Create popup
     */
    public EditionPopup()
    {
        this.setLayout(new BorderLayout());
        Set<String> colorsName      = Colors.COLORS.listOfColors();
        int         size            = colorsName.size();
        int         numberOfColumns = (int) Math.ceil(Math.sqrt(size + 1));
        JPanel      panel           = new JPanel(new GridLayout(0, numberOfColumns));

        for (String colorName : colorsName)
        {
            panel.add(new ColorButton(Colors.COLORS.obtainColor(colorName)));
        }

        this.noColorButton = new ColorButton(null);
        panel.add(this.noColorButton);
        this.add(panel, BorderLayout.CENTER);
        this.actionEdit = new ActionEdit();
        JButton button = new JButton(this.actionEdit);
        button.setFont(JHelpConstantsSmooth.FONT_BUTTON.getFont());
        this.add(button, BorderLayout.SOUTH);
    }

    /**
     * Prepare popup for edit a button
     *
     * @param button Button to edit
     */
    public void edit(
            @NotNull
                    Button button)
    {
        this.initialize();
        this.button = button;
        this.actionEdit.setEnabled(true);
    }

    /**
     * Initialize the popup
     */
    private void initialize()
    {
        this.button = null;
        this.component = null;
        this.callback = null;
        this.information = null;
        this.noColorButton.setVisible(false);
        this.actionEdit.setEnabled(false);
    }

    /**
     * Prepare popup for edit a component
     *
     * @param component Component to edit
     */
    public void edit(
            @NotNull
                    JComponent component)
    {
        this.initialize();
        this.component = component;
    }

    /**
     * Prepare popup for a generic usage
     *
     * @param callback       Callback to alert on color choose or on edition
     * @param information    Developer information give back on callback
     * @param canHaveNoColor Indicates if no color can be choose
     * @param canBeEdited    Indicates if edition is enable
     */
    public void edit(
            @NotNull
                    Callback callback, Object information, boolean canHaveNoColor, boolean canBeEdited)
    {
        if (callback == null)
        {
            throw new NullPointerException("callback MUST NOT be null !");
        }

        this.initialize();
        this.callback = callback;
        this.information = information;

        if (canHaveNoColor)
        {
            this.noColorButton.setVisible(true);
        }

        if (canBeEdited)
        {
            this.actionEdit.setEnabled(true);
        }
    }

    /**
     * Call when a color choose
     *
     * @param color Chossen color
     */
    void colorChoose(Color color)
    {
        this.setVisible(false);

        if (this.button != null)
        {
            this.button.setColor(color);
        }
        else if (this.component != null)
        {
            UtilWebsiteCreator.applyColor(this.component, color);
        }
        else if (this.callback != null)
        {
            this.callback.editionPopupColorChoose(color, this.information);
        }
    }

    /**
     * Call to launch edition
     */
    void doEdit()
    {
        this.setVisible(false);
        String text = "";

        if (this.button != null)
        {
            text = this.button.getText();
        }
        else if (this.callback != null)
        {
            text = this.callback.getActualText(this.information);
        }

        text = JOptionPane.showInputDialog(this, "New value", text);

        if (text != null && text.length() > 0)
        {
            if (this.button != null)
            {
                int index = text.indexOf(':');

                if (index < 0)
                {
                    this.button.setText(text);
                }
                else
                {
                    this.button.setText(text.substring(0, index));
                    this.button.setUrl(text.substring(index + 1));
                }
            }
            else if (this.callback != null)
            {
                this.callback.editionPopupNewText(text, this.information);
            }
        }
    }

    /**
     * Callback alert when color choose or edition done
     */
    public static interface Callback
    {
        /**
         * Called when color choose
         *
         * @param color       Choosen color (Can be {@code null} to means no color)
         * @param information Developer information
         */
        public default void editionPopupColorChoose(
                @Nullable
                        Color color, Object information)
        {
        }

        /**
         * Called when edition done
         *
         * @param text        Edited text
         * @param information Developer information
         */
        public default void editionPopupNewText(
                @NotNull
                        String text, Object information)
        {
        }

        /**
         * Ask the actual text befor edition.<br>
         * Contract force to return a non {@code null} result.
         *
         * @param information Developer information
         * @return Actual text
         */
        public default @NotNull
        String getActualText(Object information)
        {
            return "";
        }
    }

    /**
     * Action to choose a color
     */
    class ActionColor extends GenericAction
    {
        /**
         * Color to choose
         */
        private final Color color;

        /**
         * Create action
         *
         * @param color Color
         */
        ActionColor(Color color)
        {
            super("T");
            this.color = color;

            if (this.color == null)
            {
                this.setName("");
                this.setIcons(JHelpConstantsSmooth.ICON_ERROR);
            }
        }

        /**
         * Called when action have to do its action
         *
         * @param actionEvent Action event description
         */
        @Override
        protected void doActionPerformed(ActionEvent actionEvent)
        {
            EditionPopup.this.colorChoose(this.color);
        }
    }

    /**
     * Button to choose a color
     */
    class ColorButton extends JButton
    {
        /**
         * Create button for choose a color
         *
         * @param color Color
         */
        ColorButton(Color color)
        {
            super(new ActionColor(color));
            this.setFont(JHelpConstantsSmooth.FONT_BUTTON.getFont());
            UtilWebsiteCreator.applyColor(this, color);
        }
    }

    /**
     * Action launch an edition
     */
    class ActionEdit extends GenericAction
    {
        /**
         * Create action
         */
        ActionEdit()
        {
            super("Edit");
        }

        /**
         * Called when action have to do its action
         *
         * @param actionEvent Action event description
         */
        @Override
        protected void doActionPerformed(ActionEvent actionEvent)
        {
            EditionPopup.this.doEdit();
        }
    }
}
