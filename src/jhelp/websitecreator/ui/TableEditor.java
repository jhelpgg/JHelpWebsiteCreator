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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import jhelp.gui.JHelpTable;
import jhelp.gui.action.GenericAction;
import jhelp.websitecreator.model.table.Header;
import jhelp.websitecreator.model.table.Table;
import jhelp.websitecreator.model.text.BlockText;
import jhelp.websitecreator.model.text.ElementText;
import jhelp.websitecreator.resources.ResourcesWebSiteCreator;

/**
 * Editor of table
 */
public class TableEditor extends JPanel
        implements JHelpTable.CellComponentCreator, JHelpTable.ColumnAndRowsModificationListener

{
    /**
     * Table to edit
     */
    private final Table      table;
    /**
     * Table UI
     */
    private final JHelpTable tableUI;
    /**
     * Images' directory
     */
    private       File       imagesDirectory;

    /**
     * Create the editor
     *
     * @param imagesDirectory Images' directory
     * @param table           Table to edit
     */
    public TableEditor(
            @NotNull
                    File imagesDirectory,
            @NotNull
                    Table table)
    {
        super(new BorderLayout());

        if (imagesDirectory == null)
        {
            throw new NullPointerException("imagesDirectory MUST NOT be null !");
        }

        if (table == null)
        {
            throw new NullPointerException("table MUST NOT be null !");
        }

        this.imagesDirectory = imagesDirectory;
        this.table = table;
        this.tableUI = new JHelpTable(this.table.getNumberOfColums(),
                                      this.table.getNumberOfLines() + 1,
                                      this);
        this.tableUI.setCanUnifySplitOrMove(false);
        JButton button = new JButton(new ActionEdit());
        button.setMargin(new Insets(0, 0, 0, 0));
        this.add(this.tableUI, BorderLayout.CENTER);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(button, BorderLayout.NORTH);
        this.add(panel, BorderLayout.EAST);
        this.tableUI.registerColumnAndRowsModificationListener(this);
    }

    /**
     * Switch edition mode
     */
    void switchEdition()
    {
        this.tableUI.setShowColumnsRowsManipulation(!this.tableUI.isShowColumnsRowsManipulation());
    }

    /**
     * Create component for given cell
     *
     * @param x Cell X
     * @param y Cell Y
     * @return Created component
     */
    @Override
    public JComponent createComponentForCell(int x, int y)
    {
        if (y == 0)
        {
            Header    header    = this.table.getHeader(x);
            BlockText blockText = header.getBlockText();
            blockText.compress();

            if (blockText.numberOfElements() == 0)
            {
                ElementText elementText = new ElementText();
                elementText.setText(" ");
                elementText.setColor(header.getColor());
                blockText.addElement(elementText);
            }

            blockText.setColorForAll(header.getColor());
            BlockTextEditor blockTextEditor = new BlockTextEditor(this.imagesDirectory, blockText);
            blockTextEditor.setCanHaveNoColor(false);
            blockTextEditor.setColorForAllText(true);
            return blockTextEditor;
        }

        return new BlockTextEditor(this.imagesDirectory, this.table.getCell(x, y - 1)
                                                                   .getBlockText());
    }

    /**
     * Called when a column is about to be insert.<br>
     * It is called just before insertion
     *
     * @param table  Table will be modified
     * @param column Column will be insert
     */
    @Override
    public void tableColumnWillBeInsert(JHelpTable table, int column)
    {
        this.table.addColumn(column);
    }

    /**
     * Called when a column is inserted.<br>
     * It is called ust after insertion
     *
     * @param table  Modified table
     * @param column Inserted column
     */
    @Override
    public void tableColumnInserted(JHelpTable table, int column)
    {
        //Nothing to do
    }

    /**
     * Called when a column is about to be delete.<br>
     * It is called just before deletion
     *
     * @param table  Table will be modified
     * @param column Column will be delete
     */
    @Override
    public void tableColumnWillBeDelete(JHelpTable table, int column)
    {
        this.table.removeColumn(column);
    }

    /**
     * Called when a column is deleted.<br>
     * It is called ust after deletion
     *
     * @param table  Modified table
     * @param column Deleted column
     */
    @Override
    public void tableColumnDeleted(JHelpTable table, int column)
    {
        //Nothing to do
    }

    /**
     * Called when a row is about to be insert.<br>
     * It is called just before insertion
     *
     * @param table Table will be modified
     * @param row   Row will be insert
     */
    @Override
    public void tableRowWillBeInsert(JHelpTable table, int row)
    {
        this.table.addLine(Math.max(0, row - 1));
    }

    /**
     * Called when a row is inserted.<br>
     * It is called ust after insertion
     *
     * @param table Modified table
     * @param row   Inserted row
     */
    @Override
    public void tableRowInserted(JHelpTable table, int row)
    {
        //Nothing to do
    }

    /**
     * Called when a row is about to be delete.<br>
     * It is called just before deletion
     *
     * @param table Table will be modified
     * @param row   Row will be delete
     */
    @Override
    public void tableRowWillBeDelete(JHelpTable table, int row)
    {
        if (row > 0)
        {
            this.table.removeLine(row - 1);
        }
    }

    /**
     * Called when a row is deleted.<br>
     * It is called ust after deletion
     *
     * @param table Modified table
     * @param row   Deleted row
     */
    @Override
    public void tableRowDeleted(JHelpTable table, int row)
    {
        if (row <= 0)
        {
            this.tableUI.addRow(0);
        }
    }

    /**
     * Edit action
     */
    class ActionEdit extends GenericAction
    {
        /**
         * Create the action
         */
        ActionEdit()
        {
            super("", ResourcesWebSiteCreator.ICON_EDIT);
        }

        /**
         * Called when action have to do its action
         *
         * @param actionEvent Action event description
         */
        @Override
        protected void doActionPerformed(ActionEvent actionEvent)
        {
            TableEditor.this.switchEdition();
        }
    }
}
