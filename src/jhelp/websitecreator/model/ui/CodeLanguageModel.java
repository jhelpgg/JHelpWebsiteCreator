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
package jhelp.websitecreator.model.ui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import jhelp.websitecreator.model.CodeLanguage;

/**
 * Model of code language
 */
public class CodeLanguageModel implements ComboBoxModel<CodeLanguage>
{
    /**
     * Listeners of changes
     */
    private final List<ListDataListener> listeners;
    /**
     * Current slection
     */
    private       CodeLanguage           selectedCodeLanguage;

    /**
     * Create the model
     */
    public CodeLanguageModel()
    {
        this.listeners = new ArrayList<>();
    }

    /**
     * Returns the length of the list.
     *
     * @return the length of the list
     */
    @Override
    public int getSize()
    {
        return CodeLanguage.values().length;
    }

    /**
     * Returns the value at the specified index.
     *
     * @param index the requested index
     * @return the value at <code>index</code>
     */
    @Override
    public CodeLanguage getElementAt(int index)
    {
        return CodeLanguage.values()[index];
    }

    /**
     * Adds a listener to the list that's notified each time a change
     * to the data model occurs.
     *
     * @param listener the <code>ListDataListener</code> to be added
     */
    @Override
    public void addListDataListener(ListDataListener listener)
    {
        if (listener == null)
        {
            return;
        }

        synchronized (this.listeners)
        {
            if (!this.listeners.contains(listener))
            {
                this.listeners.add(listener);
            }
        }
    }

    /**
     * Removes a listener from the list that's notified each time a
     * change to the data model occurs.
     *
     * @param listener the <code>ListDataListener</code> to be removed
     */
    @Override
    public void removeListDataListener(ListDataListener listener)
    {
        synchronized (this.listeners)
        {
            this.listeners.remove(listener);
        }
    }

    /**
     * Alert listeners of content changes
     */
    protected void fireContentChanged()
    {
        final ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED,
                                                              0, CodeLanguage.values().length - 1);

        synchronized (this.listeners)
        {
            for (ListDataListener listener : this.listeners)
            {
                listener.contentsChanged(listDataEvent);
            }
        }
    }

    /**
     * Set the selected item. The implementation of this  method should notify
     * all registered <code>ListDataListener</code>s that the contents
     * have changed.
     *
     * @param anItem the list object to select or <code>null</code>
     *               to clear the selection
     */
    @Override
    public void setSelectedItem(Object anItem)
    {
        if (anItem == null || !(anItem instanceof CodeLanguage))
        {
            this.selectedCodeLanguage = null;
        }
        else
        {
            this.selectedCodeLanguage = (CodeLanguage) anItem;
        }

        this.fireContentChanged();
    }

    /**
     * Returns the selected item
     *
     * @return The selected item or <code>null</code> if there is no selection
     */
    @Override
    public Object getSelectedItem()
    {
        return this.selectedCodeLanguage;
    }
}
