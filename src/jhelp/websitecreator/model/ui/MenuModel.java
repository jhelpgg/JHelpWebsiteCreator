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

import com.sun.istack.internal.NotNull;

import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import jhelp.websitecreator.model.Button;
import jhelp.websitecreator.model.Menu;

/**
 * Model for menu buttons
 */
public class MenuModel implements ListModel<Button>
{
    /**
     * Listeners of model events
     */
    private final List<ListDataListener> listeners;
    /**
     * Embed menu
     */
    private       Menu                   menu;

    /**
     * Create the model
     *
     * @param menu Menu to represents
     */
    public MenuModel(
            @NotNull
                    Menu menu)
    {
        if (menu == null)
        {
            throw new NullPointerException("menu MUST NOT be null !");
        }

        this.menu = menu;
        this.listeners = new ArrayList<>();
    }

    /**
     * Alert listenners taht some lines removed
     *
     * @param index0 First index of removing
     * @param index1 Last index of removing
     */
    protected void fireIntervalRemoved(int index0, int index1)
    {
        final ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.INTERVAL_REMOVED, index0, index1);

        synchronized (this.listeners)
        {
            for (ListDataListener listener : this.listeners)
            {
                listener.intervalRemoved(listDataEvent);
            }
        }
    }

    /**
     * Returns the length of the list.
     *
     * @return the length of the list
     */
    @Override
    public int getSize()
    {
        return this.menu.numberOfButtons();
    }

    /**
     * Returns the value at the specified index.
     *
     * @param index the requested index
     * @return the value at <code>index</code>
     */
    @Override
    public Button getElementAt(int index)
    {
        return this.menu.getButton(index);
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
     * Add a button
     *
     * @param text Button text
     * @return Added button
     */
    public Button addButton(String text)
    {
        int    index  = this.menu.numberOfButtons();
        Button button = this.menu.createNewButton();
        button.setText(text);
        this.fireIntervalAdded(index, index);
        return button;
    }

    /**
     * Alert listeners that some lines added
     *
     * @param index0 First added index
     * @param index1 Last added index
     */
    protected void fireIntervalAdded(int index0, int index1)
    {
        final ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, index0, index1);

        synchronized (this.listeners)
        {
            for (ListDataListener listener : this.listeners)
            {
                listener.intervalAdded(listDataEvent);
            }
        }
    }

    /**
     * Update a button
     *
     * @param index Button index
     */
    public void updateButton(int index)
    {
        this.fireContentChanged(index, index);
    }

    /**
     * Alert listeners that some lines changed
     *
     * @param index0 First change index
     * @param index1 Last change index
     */
    protected void fireContentChanged(int index0, int index1)
    {
        final ListDataEvent listDataEvent = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, index0, index1);

        synchronized (this.listeners)
        {
            for (ListDataListener listener : this.listeners)
            {
                listener.contentsChanged(listDataEvent);
            }
        }
    }
}
