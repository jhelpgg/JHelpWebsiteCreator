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

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import jhelp.gui.code.ASMDescriptor;
import jhelp.gui.code.JavaDescriptor;
import jhelp.gui.code.LanguageDescriptor;
import jhelp.gui.code.XMLDescriptor;

/**
 * Actualy supported code languages
 */
public enum CodeLanguage
{
    /**
     * Java
     */
    JAVA("java", new JavaDescriptor()),
    /**
     * Our byte code language
     */
    ASM("asm", new ASMDescriptor()),
    /**
     * XML
     */
    XML("xml", new XMLDescriptor());

    /**
     * Language name
     */
    private final String             language;
    /**
     * Describe the language
     */
    private final LanguageDescriptor languageDescriptor;

    /**
     * Create the language
     *
     * @param language           Language name
     * @param languageDescriptor Describe the language
     */
    CodeLanguage(final String language, final LanguageDescriptor languageDescriptor)
    {
        this.language = language;
        this.languageDescriptor = languageDescriptor;
    }

    /**
     * Get coe language from languge name
     *
     * @param language Langauge name
     * @return Code language OR {@code null} if langauge name not corresponds to a managed language
     */
    public static @Nullable
    CodeLanguage fromLanguage(String language)
    {
        for (CodeLanguage codeLanguage : values())
        {
            if (codeLanguage.language.equalsIgnoreCase(language))
            {
                return codeLanguage;
            }
        }

        return null;
    }

    /**
     * Language name
     *
     * @return Language name
     */
    public @NotNull
    String getLanguage()
    {
        return this.language;
    }

    /**
     * Describe the language
     *
     * @return Describe the language
     */
    public @NotNull
    LanguageDescriptor getLanguageDescriptor()
    {
        return this.languageDescriptor;
    }
}
