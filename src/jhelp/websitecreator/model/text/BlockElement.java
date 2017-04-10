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
package jhelp.websitecreator.model.text;

import jhelp.util.io.Binarizable;
import jhelp.websitecreator.model.WritableInHTML;

/**
 * Describe an element of {@link BlockText}
 */
public interface BlockElement extends WritableInHTML, Binarizable
{
    /**
     * Indicates if element can be compressed with given one
     *
     * @param blockElement Element to compress with
     * @return {@code true} if compression is allowed
     */
    public boolean compressibleWith(BlockElement blockElement);

    /**
     * Compress this element with given one if possible.<br>
     * It returns {@code null} if compression not possible.<br>
     * See {@link #compressibleWith(BlockElement)} to know if compression allowed
     *
     * @param blockElement Element to compress with
     * @return Compression result OR {@code null} if compression not possible.
     */
    public BlockElement compressWith(BlockElement blockElement);
}
