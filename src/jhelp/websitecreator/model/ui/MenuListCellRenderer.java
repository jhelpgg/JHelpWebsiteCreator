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
package jhelp.websitecreator.model.ui;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import jhelp.gui.smooth.JHelpConstantsSmooth;
import jhelp.websitecreator.model.Button;
import jhelp.websitecreator.util.UtilWebsiteCreator;

/**
 * Cell renderer of element in menu list
 */
public class MenuListCellRenderer extends JButton implements ListCellRenderer<Button>
{
    /**
     * Create the renderer
     */
    public MenuListCellRenderer()
    {
        this.setFont(JHelpConstantsSmooth.FONT_BUTTON.getFont());
    }

    /**
     * Return a component that has been configured to display the specified
     * value. That component's <code>paint</code> method is then called to
     * "render" the cell.  If it is necessary to compute the dimensions
     * of a list because the list cells do not have a fixed size, this method
     * is called to generate a component on which <code>getPreferredSize</code>
     * can be invoked.
     *
     * @param list         The JList we're painting.
     * @param value        The value returned by list.getModel().getElementAt(index).
     * @param index        The cells index.
     * @param isSelected   True if the specified cell was selected.
     * @param cellHasFocus True if the specified cell has the focus.
     * @return A component whose paint() method will render the specified value.
     * @see JList
     * @see ListSelectionModel
     * @see ListModel
     */
    @Override
    public Component getListCellRendererComponent(JList<? extends Button> list, Button value, int index,
                                                  boolean isSelected, boolean cellHasFocus)
    {
        UtilWebsiteCreator.applyColor(this, value.getColor());
        this.setText(value.getText());
        return this;
    }
}
