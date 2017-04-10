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

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;

import jhelp.gui.JHelpAutoStyledTextArea;
import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.gui.JHelpImage;
import jhelp.util.list.Pair;
import jhelp.util.list.Triplet;
import jhelp.websitecreator.model.text.BlockElement;
import jhelp.websitecreator.model.text.BlockText;
import jhelp.websitecreator.model.text.ElementImage;
import jhelp.websitecreator.model.text.ElementText;
import jhelp.websitecreator.resources.Colors;
import jhelp.websitecreator.util.UtilWebsiteCreator;

/**
 * Editor of {@link BlockText block text}
 */
public class BlockTextEditor extends JEditorPane implements MouseListener, EditionPopup.Callback, DocumentListener
{
    /**
     * Document to add style
     */
    private final DefaultStyledDocument autoStyledDocument;
    /**
     * Images directory
     */
    private final File                  imagesDirectory;
    /**
     * Indicates if modifications are allowed
     */
    private final AtomicBoolean allowModification = new AtomicBoolean(true);
    /**
     * Popup for change color
     */
    private final EditionPopup editionPopup;
    /**
     * Block text to edit
     */
    private       BlockText    blockText;
    /**
     * Indicates if block text aloowed to have no color
     */
    private       boolean      canHaveNoColor;
    /**
     * Indicates if changing color if for all elements in block text
     */
    private       boolean      colorForAllText;

    /**
     * Create block text editor
     *
     * @param imagesDirectory Images directory
     * @param blockText       Block text to edit
     */
    public BlockTextEditor(File imagesDirectory, BlockText blockText)
    {
        this.imagesDirectory = imagesDirectory;
        this.setEditorKit(new StyledEditorKit());
        this.autoStyledDocument = (DefaultStyledDocument) this.getDocument();
        this.createStyle(JHelpAutoStyledTextArea.DEFAULT_STYLE, "Arial", 20, false, false, false, Color.BLACK,
                         Color.WHITE);
        this.canHaveNoColor = true;
        this.colorForAllText = false;
        jhelp.websitecreator.resources.Color color;

        for (String colorName : Colors.COLORS.listOfColors())
        {
            color = Colors.COLORS.obtainColor(colorName);
            this.createStyle(colorName, "Arial", 25, true, false, false, UtilWebsiteCreator.getColor(color.getColor()),
                             UtilWebsiteCreator.getColor(color.getBackground()));
        }

        this.updateDefaultStyle();
        this.setBlockText(blockText);
        this.editionPopup = new EditionPopup();
        this.addMouseListener(this);
        this.autoStyledDocument.addDocumentListener(this);
    }

    /**
     * Update the default style to make properties coherent
     */
    private void updateDefaultStyle()
    {
        final Style style = this.autoStyledDocument.getStyle(JHelpAutoStyledTextArea.DEFAULT_STYLE);
        this.setBackground(StyleConstants.getBackground(style));
        this.setForeground(StyleConstants.getForeground(style));
        int flag = 0;

        if (StyleConstants.isBold(style))
        {
            flag |= Font.BOLD;
        }

        if (StyleConstants.isItalic(style))
        {
            flag |= Font.ITALIC;
        }

        final Font font = new Font(StyleConstants.getFontFamily(style), flag, StyleConstants.getFontSize(style));
        this.setFont(font);

        final Style styleDefault = this.autoStyledDocument.getStyle("default");

        if (styleDefault != null)
        {
            StyleConstants.setBackground(styleDefault, StyleConstants.getBackground(style));
            StyleConstants.setForeground(styleDefault, StyleConstants.getForeground(style));
            StyleConstants.setFontSize(styleDefault, StyleConstants.getFontSize(style));
            StyleConstants.setFontFamily(styleDefault, StyleConstants.getFontFamily(style));
            StyleConstants.setBold(styleDefault, StyleConstants.isBold(style));
            StyleConstants.setItalic(styleDefault, StyleConstants.isItalic(style));
            StyleConstants.setUnderline(styleDefault, StyleConstants.isUnderline(style));
        }
    }

    /**
     * Create a style
     *
     * @param name       Style name
     * @param fontFamily Font family
     * @param fontSize   Font size
     * @param bold       Indicates if bold
     * @param italic     Indicates if italic
     * @param underline  Indicates if underline
     * @param foreground Foreground color
     * @param background Background color
     */
    private void createStyle(final String name,
                             final String fontFamily, final int fontSize,
                             final boolean bold, final boolean italic, final boolean underline,
                             final Color foreground, final Color background)
    {
        if (name == null)
        {
            throw new NullPointerException("name musn't be null");
        }

        if (fontFamily == null)
        {
            throw new NullPointerException("fontFamily musn't be null");
        }

        if (foreground == null)
        {
            throw new NullPointerException("foreground musn't be null");
        }

        if (background == null)
        {
            throw new NullPointerException("background musn't be null");
        }

        final Style style = this.autoStyledDocument.addStyle(name, null);

        StyleConstants.setFontFamily(style, fontFamily);
        StyleConstants.setFontSize(style, fontSize);
        StyleConstants.setBold(style, bold);
        StyleConstants.setItalic(style, italic);
        StyleConstants.setUnderline(style, underline);
        StyleConstants.setForeground(style, foreground);
        StyleConstants.setBackground(style, background);
    }

    /**
     * Change edited bblock text
     *
     * @param blockText Block text to edit
     */
    public void setBlockText(
            @NotNull
                    BlockText blockText)
    {
        if (blockText == null)
        {
            throw new NullPointerException("blockText MUST NOT be null !");
        }

        this.blockText = blockText;
        this.update();
    }

    /**
     * Update the block text editor to see last changes
     */
    public void update()
    {
        this.blockText.compress();
        List<Triplet<Integer, Integer, Style>> areas = new ArrayList<>();
        jhelp.websitecreator.resources.Color   color;

        StringBuilder stringBuilder = new StringBuilder();
        int           size          = this.blockText.numberOfElements();
        Style         defaultStyle  = this.autoStyledDocument.getStyle(JHelpAutoStyledTextArea.DEFAULT_STYLE);

        BlockElement blockElement;
        ElementText  elementText;
        ElementImage elementImage;
        Style        style;
        int          start = 0;
        int          end;

        for (int index = 0; index < size; index++)
        {
            blockElement = this.blockText.getElement(index);

            if (blockElement instanceof ElementText)
            {
                elementText = (ElementText) blockElement;
                color = elementText.getColor();

                if (color == null)
                {
                    style = defaultStyle;
                }
                else
                {
                    if (this.colorForAllText)
                    {
                        this.setBackground(UtilWebsiteCreator.getColor(color.getBackground()));
                    }

                    style = this.autoStyledDocument.getStyle(color.getName());
                }

                stringBuilder.append(elementText.getText());
                end = stringBuilder.length();
                areas.add(new Triplet<>(start, end - start, style));
                start = end;
            }
            else if (blockElement instanceof ElementImage)
            {
                elementImage = (ElementImage) blockElement;
                stringBuilder.append("\n#");
                areas.add(new Triplet<>(start + 1, 1, this.obtainImageStyle(elementImage.getImageName())));
                start += 2;
            }
        }

        this.allowModification.set(false);
        this.setText(stringBuilder.toString());
        this.allowModification.set(true);

        for (Triplet<Integer, Integer, Style> area : areas)
        {
            this.autoStyledDocument.setCharacterAttributes(area.element1, area.element2, area.element3, true);
        }
    }

    /**
     * Obtain style for one image
     *
     * @param imageName Image name
     * @return Style to use
     */
    private Style obtainImageStyle(String imageName)
    {
        String styleName = "$<image:" + imageName + ">$";
        Style  style     = this.autoStyledDocument.getStyle(styleName);

        if (style == null)
        {
            style = this.autoStyledDocument.addStyle(styleName, null);
            StyleConstants.setIcon(style, this.obtainImage(imageName));
        }

        return style;
    }

    /**
     * Obtain image
     *
     * @param imageName Image name
     * @return The image
     */
    private JHelpImage obtainImage(String imageName)
    {
        try
        {
            return JHelpImage.loadImage(new File(this.imagesDirectory, imageName));
        }
        catch (IOException e)
        {
            Debug.printException(e, "Failed to load image : ", imageName);
            return JHelpImage.DUMMY;
        }
    }

    /**
     * Indicates if no color is allowed
     *
     * @return {@code true} if no color is allowed
     */
    public boolean isCanHaveNoColor()
    {
        return this.canHaveNoColor;
    }

    /**
     * Allow/forbid the possiblity to have no color
     *
     * @param canHaveNoColor Use {@code true} to allow no color
     */
    public void setCanHaveNoColor(boolean canHaveNoColor)
    {
        this.canHaveNoColor = canHaveNoColor;
    }

    /**
     * Indicates if color choose for all elements
     *
     * @return {@code true} if color choose for all elements
     */
    public boolean isColorForAllText()
    {
        return this.colorForAllText;
    }

    /**
     * Enable/disable coloration for all
     *
     * @param colorForAllText Use {@code true} for all text will change on color change
     */
    public void setColorForAllText(boolean colorForAllText)
    {
        this.colorForAllText = colorForAllText;

        if (this.colorForAllText)
        {
            this.update();
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
        if (SwingUtilities.isRightMouseButton(mouseEvent))
        {
            int start = this.getSelectionStart();
            int end   = this.getSelectionEnd();

            if (this.colorForAllText)
            {
                start = 0;
                end = this.getText()
                          .length();
            }

            Pair<Integer, Integer> information = new Pair<>(start, end);
            this.editionPopup.edit(this, information, this.canHaveNoColor, false);
            Point position = this.popupPosition();
            this.editionPopup.show(this, position.x, position.y);
        }
    }

    /**
     * Compute popup position
     *
     * @return Popup position
     */
    private Point popupPosition()
    {
        Point location = this.getCaret()
                             .getMagicCaretPosition();

        if (location == null)
        {
            // Sometimes caret answer null for its position, here we try an other way to get it
            try
            {
                final Rectangle box = this.modelToView(this.getCaretPosition());
                location = new Point(box.x, box.y);
            }
            catch (final Exception exception)
            {
                // Note : Since position is sure to be correct, it normally never have exception
                // But in case of thread concurrency (Text become smaller than 'position' between the 'UtilMath.limit'
                // AND the 'modelToView') we just return dumy position
                return new Point(128, 128);
            }
        }

        return location;
    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param mouseEvent Event description
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent)
    {
        //Noting to do for now
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param mouseEvent Event description
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent)
    {
        //Noting to do for now
    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param mouseEvent Event description
     */
    @Override
    public void mouseEntered(MouseEvent mouseEvent)
    {
        //Noting to do for now
    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param mouseEvent Event description
     */
    @Override
    public void mouseExited(MouseEvent mouseEvent)
    {
        //Noting to do for now
    }

    /**
     * Called when a color choose
     *
     * @param color       Choosen color
     * @param information Developer information (Here the current selection)
     */
    @Override
    public void editionPopupColorChoose(jhelp.websitecreator.resources.Color color, Object information)
    {
        Pair<Integer, Integer> selection   = (Pair<Integer, Integer>) information;
        int                    start       = selection.element1;
        int                    end         = selection.element2;
        ElementText            elementText = new ElementText();
        elementText.setColor(color);
        StringBuilder stringBuilder = new StringBuilder();
        int           index         = 0;
        int           size          = this.blockText.numberOfElements();
        String        text;
        BlockElement  blockElement;
        ElementText   element;
        int           i, length;

        for (i = 0; i < size && index <= end; i++)
        {
            blockElement = this.blockText.getElement(i);

            if (blockElement instanceof ElementText)
            {
                element = (ElementText) blockElement;
                text = element.getText();
                length = text.length();

                if (index + length >= start)
                {
                    if (index >= start)
                    {
                        if (index + length <= end)
                        {
                            stringBuilder.append(text);
                            this.blockText.removeElement(i);
                            i--;
                            size--;
                        }
                        else
                        {
                            stringBuilder.append(text.substring(0, end - index));
                            element.setText(text.substring(end - index));
                            i--;
                        }
                    }
                    else
                    {
                        element.setText(text.substring(0, start - index));

                        if (index + length <= end)
                        {
                            stringBuilder.append(text.substring(start - index));
                        }
                        else
                        {
                            stringBuilder.append(text.substring(start - index, end - index));
                            element = element.copy();
                            element.setText(text.substring(end - index));
                            this.blockText.insert(i + 1, element);
                        }
                    }
                }

                index += length;
            }
            else if (blockElement instanceof ElementImage)
            {
                if (index >= start)
                {
                    this.blockText.removeElement(i);
                    i--;
                    size--;
                }

                index += 2;
            }
        }

        if (stringBuilder.length() == 0)
        {
            stringBuilder.append(" ");
        }

        elementText.setText(stringBuilder.toString());
        this.blockText.insert(i, elementText);

        if (this.colorForAllText && color != null)
        {
            this.setBackground(UtilWebsiteCreator.getColor(color.getBackground()));
        }

        this.update();
    }

    /**
     * Gives notification that there was an insert into the document.  The
     * range given by the DocumentEvent bounds the freshly inserted region.
     *
     * @param e the document event
     */
    @Override
    public void insertUpdate(DocumentEvent e)
    {
        if (!this.allowModification.get())
        {
            return;
        }

        int                                     offset  = e.getOffset();
        int                                     length  = e.getLength();
        Triplet<Integer, Integer, BlockElement> triplet = this.blockElementFromIndex(offset - 1);

        if (triplet == null)
        {
            ElementText elementText = new ElementText();
            elementText.setText(this.getText()
                                    .substring(offset, offset + length));
            this.blockText.addElement(elementText);
        }
        else
        {
            BlockElement blockElement = triplet.element3;

            if (blockElement instanceof ElementImage)
            {
                ElementText elementText = new ElementText();
                elementText.setText(this.getText()
                                        .substring(offset, offset + length));
                this.blockText.insert(triplet.element1 + 1, elementText);
            }
            else if (blockElement instanceof ElementText)
            {
                ElementText elementText = (ElementText) blockElement;
                String      text        = elementText.getText();
                elementText.setText(text.substring(0, triplet.element2 + 1)
                                            + this.getText()
                                                  .substring(offset, offset + length)
                                            + text.substring(triplet.element2 + 1));
            }
        }
    }

    /**
     * Compute block element under given index.<br>
     * Returns triplet made dith block element index, relative position inside the element and the element it self
     *
     * @param index Index to serach the element
     * @return Triplet made dith block element index, relative position inside the element and the element it self OR
     * {@code null} if no block match
     */
    private Triplet<Integer, Integer, BlockElement> blockElementFromIndex(int index)
    {
        int          size = this.blockText.numberOfElements();
        BlockElement blockElement;
        int          blockSize;

        for (int i = 0; i < size; i++)
        {
            blockElement = this.blockText.getElement(i);
            blockSize = this.blockSize(blockElement);

            if (index < blockSize)
            {
                return new Triplet<>(i, index, blockElement);
            }

            index -= blockSize;
        }

        return null;
    }

    /**
     * Compute block element size
     *
     * @param blockElement Block element to measure
     * @return Block element size
     */
    private int blockSize(BlockElement blockElement)
    {
        if (blockElement instanceof ElementImage)
        {
            return 2;
        }

        if (blockElement instanceof ElementText)
        {
            return ((ElementText) blockElement).getText()
                                               .length();
        }

        Debug.println(DebugLevel.WARNING, "Not managed block element : ", blockElement.getClass()
                                                                                      .getName());
        return 0;
    }

    /**
     * Gives notification that a portion of the document has been
     * removed.  The range is given in terms of what the view last
     * saw (that is, before updating sticky positions).
     *
     * @param e the document event
     */
    @Override
    public void removeUpdate(DocumentEvent e)
    {
        if (!this.allowModification.get())
        {
            return;
        }

        int                                     offset  = e.getOffset();
        int                                     length  = e.getLength();
        Triplet<Integer, Integer, BlockElement> triplet = this.blockElementFromIndex(offset);
        int                                     blockSize;
        ElementText                             elementText;
        String                                  text;
        int                                     more;

        while (length > 0 && triplet != null)
        {
            blockSize = this.blockSize(triplet.element3);
            more = 0;

            if (triplet.element3 instanceof ElementImage)
            {
                this.blockText.removeElement(triplet.element3);
            }
            else if (triplet.element3 instanceof ElementText)
            {
                elementText = (ElementText) triplet.element3;

                if (blockSize - triplet.element2 <= length)
                {
                    if (triplet.element2 == 0)
                    {
                        this.blockText.removeElement(elementText);
                    }
                    else
                    {
                        elementText.setText(elementText.getText()
                                                       .substring(0, triplet.element2));
                        more = triplet.element2;
                    }
                }
                else
                {
                    text = elementText.getText();
                    elementText.setText(text.substring(0, triplet.element2) + text.substring(triplet.element2 + length));
                }
            }

            length = length - blockSize + more;

            if (length > 0)
            {
                triplet = this.blockElementFromIndex(offset);
            }
        }
    }

    /**
     * Gives notification that an attribute or set of attributes changed.
     *
     * @param e the document event
     */
    @Override
    public void changedUpdate(DocumentEvent e)
    {
        //Nothing to do
    }
}
