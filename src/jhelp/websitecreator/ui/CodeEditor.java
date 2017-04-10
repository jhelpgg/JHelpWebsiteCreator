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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import jhelp.gui.JHelpAutoStyledTextArea;
import jhelp.gui.code.DefaultDecorator;
import jhelp.gui.smooth.JHelpConstantsSmooth;
import jhelp.websitecreator.model.Code;
import jhelp.websitecreator.model.CodeLanguage;
import jhelp.websitecreator.model.ui.CodeLanguageModel;

/**
 * Editor of {@link Code}
 */
public class CodeEditor extends JPanel implements ActionListener, DocumentListener
{
    /**
     * Code to edit
     */
    private final Code                    code;
    /**
     * Text editor with syntax coloration
     */
    private final JHelpAutoStyledTextArea autoStyledTextArea;
    /**
     * Combobox for choose language
     */
    private final JComboBox<CodeLanguage> comboBoxLanguage;

    /**
     * Create the editor
     *
     * @param code Code to edit
     */
    public CodeEditor(
            @NotNull
                    Code code)
    {
        super(new BorderLayout());
        this.code = code;
        this.autoStyledTextArea = new JHelpAutoStyledTextArea();
        this.autoStyledTextArea.decorate(new DefaultDecorator());
        this.autoStyledTextArea.describeLanguage(this.code.getCodeLanguage()
                                                          .getLanguageDescriptor());
        this.autoStyledTextArea.setText(this.code.getCode());
        this.autoStyledTextArea.getDocument()
                               .addDocumentListener(this);
        this.comboBoxLanguage = new JComboBox<>(new CodeLanguageModel());
        this.comboBoxLanguage.setFont(JHelpConstantsSmooth.FONT_MENU.getFont());
        this.comboBoxLanguage.setSelectedItem(this.code.getCodeLanguage());
        this.comboBoxLanguage.addActionListener(this);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(this.comboBoxLanguage, BorderLayout.WEST);
        this.add(panel, BorderLayout.NORTH);
        this.add(this.autoStyledTextArea, BorderLayout.CENTER);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e Event description
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        CodeLanguage codeLanguage = (CodeLanguage) this.comboBoxLanguage.getSelectedItem();
        this.code.setCodeLanguage(codeLanguage);
        this.autoStyledTextArea.describeLanguage(codeLanguage.getLanguageDescriptor());
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
        this.code.setCode(this.autoStyledTextArea.getText());
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
        this.code.setCode(this.autoStyledTextArea.getText());
    }

    /**
     * Gives notification that an attribute or set of attributes changed.
     *
     * @param e the document event
     */
    @Override
    public void changedUpdate(DocumentEvent e)
    {
        this.code.setCode(this.autoStyledTextArea.getText());
    }
}
