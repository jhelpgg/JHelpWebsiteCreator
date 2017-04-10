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
package jhelp.websitecreator.model;

import jhelp.util.io.Binarizable;

/**
 * Element of {@link Content}
 */
public interface ContentElement extends Binarizable, WritableInHTML
{
    /**
     * Try to compress element
     *
     * @return {@code true} if some compression happen OR {@code false} if nothing change
     */
    public default boolean compress()
    {
        return false;
    }
}
