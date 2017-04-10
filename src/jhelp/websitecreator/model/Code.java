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

import java.io.BufferedWriter;
import java.io.IOException;

import jhelp.util.io.ByteArray;


/**
 * Code block
 */
public class Code implements ContentElement
{
    /**
     * BlockText language
     */
    private CodeLanguage codeLanguage;
    /**
     * The code
     */
    private String       code;

    /**
     * Create empty code block
     */
    public Code()
    {
        this.codeLanguage = CodeLanguage.JAVA;
        this.code = "";
    }

    /**
     * Obtain codeLanguage value
     *
     * @return codeLanguage value
     */
    public @NotNull
    CodeLanguage getCodeLanguage()
    {
        return this.codeLanguage;
    }

    /**
     * Modify codeLanguage value
     *
     * @param codeLanguage New codeLanguage value
     */
    public void setCodeLanguage(
            @NotNull
                    CodeLanguage codeLanguage)
    {
        if (codeLanguage == null)
        {
            throw new NullPointerException("codeLanguage MUST NOT be null !");
        }

        this.codeLanguage = codeLanguage;
    }

    /**
     * Obtain code value
     *
     * @return code value
     */
    public @NotNull
    String getCode()
    {
        return this.code;
    }

    /**
     * Modify code value
     *
     * @param code New code value
     */
    public void setCode(
            @NotNull
                    String code)
    {
        if (code == null)
        {
            throw new NullPointerException("code MUST NOT be null !");
        }

        this.code = code;
    }

    /**
     * Write object in HTML
     *
     * @param project        Project reference
     * @param bufferedWriter Stream where write
     * @throws IOException On writing issue
     */
    @Override
    public void writeInHTML(Project project, BufferedWriter bufferedWriter) throws IOException
    {
        bufferedWriter.write("<code lang=\"");
        bufferedWriter.write(this.codeLanguage.getLanguage());
        bufferedWriter.write("\">");
        bufferedWriter.newLine();
        bufferedWriter.write(this.code);
        bufferedWriter.newLine();
        bufferedWriter.write("</code>");
        bufferedWriter.flush();
    }

    /**
     * Parse the array for fill binarizable information.<br>
     * See {@link #serializeBinary(ByteArray)} for fill information
     *
     * @param byteArray Byte array to parse
     */
    @Override
    public void parseBinary(ByteArray byteArray)
    {
        try
        {
            this.setCodeLanguage(byteArray.readEnum(CodeLanguage.class));
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to parse !", e);
        }

        this.setCode(byteArray.readString());
    }

    /**
     * Write the binarizable information inside a byte array.<br>
     * See {@link #parseBinary(ByteArray)} for read information
     *
     * @param byteArray Byte array where write
     */
    @Override
    public void serializeBinary(ByteArray byteArray)
    {
        byteArray.writeEnum(this.codeLanguage);
        byteArray.writeString(this.code);
    }
}
