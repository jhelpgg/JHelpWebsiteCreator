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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import jhelp.util.io.Binarizable;
import jhelp.util.io.ByteArray;
import jhelp.util.io.UtilIO;
import jhelp.util.resources.ResourceDirectory;
import jhelp.util.resources.ResourceElement;
import jhelp.util.resources.ResourceFile;
import jhelp.util.resources.ResourcesSystem;
import jhelp.websitecreator.resources.ResourcesWebSiteCreator;

/**
 * Represents a project
 */
public class Project implements Binarizable
{
    /**
     * Project wepages
     */
    private final List<WebPage> webPages;
    /**
     * Project name
     */
    private       String        name;
    /**
     * Images relative path
     */
    private       String        imagesPath;
    /**
     * CSS relative path
     */
    private       String        cssPath;
    /**
     * Scripts relative path
     */
    private       String        scriptsPath;
    /**
     * Project  directory
     */
    private       File          directory;
    /**
     * Project export directory
     */
    private       File          exportDirectory;

    /**
     * Create a project
     */
    public Project()
    {
        this.name = "";
        this.cssPath = "css/";
        this.scriptsPath = "scripts/";
        this.imagesPath = "images/";
        this.webPages = new ArrayList<>();
    }

    /**
     * Load a project from a file
     *
     * @param projectFile File to parse
     * @return Read project
     * @throws IOException On reading issue or file is invalid project file
     */
    public static Project load(
            @NotNull
                    File projectFile) throws IOException
    {
        InputStream inputStream = null;

        try
        {
            inputStream = new FileInputStream(projectFile);
            Project project = UtilIO.readBinarizable(Project.class, inputStream);
            File    parent  = projectFile.getParentFile();

            if (!parent.equals(project.directory))
            {
                if (project.exportDirectory != null)
                {
                    String path = UtilIO.computeRelativePath(project.directory, project.exportDirectory);
                    project.setExportDirectory(UtilIO.obtainFile(parent, path));
                }

                project.setDirectory(parent);
            }

            return project;
        }
        finally
        {
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (Exception ignored)
                {
                }
            }
        }
    }

    /**
     * Create a new project
     *
     * @param name      Project name
     * @param directory Project's directory
     * @return Created project
     * @throws IllegalArgumentException If name is empty or given directory can't be created
     */
    public static @NotNull
    Project newProject(
            @NotNull
                    String name,
            @NotNull
                    File directory)
    {
        if (name == null || name.length() == 0)
        {
            throw new IllegalArgumentException("name MUST NOT be nulll or empty");
        }

        if (!UtilIO.delete(directory) || !UtilIO.createDirectory(directory))
        {
            throw new IllegalArgumentException("Can't create directory : " + directory.getAbsolutePath());
        }

        Project project = new Project();
        project.setName(name);
        project.setDirectory(directory);
        project.setExportDirectory(new File(directory, "output"));
        return project;
    }

    /**
     * Obtain name value
     *
     * @return name value
     */
    public @NotNull
    String getName()
    {
        return this.name;
    }

    /**
     * Modify name value
     *
     * @param name New name value
     */
    public void setName(
            @NotNull
                    String name)
    {
        if (name == null)
        {
            throw new NullPointerException("name MUST NOT be null !");
        }

        this.name = name;
    }

    /**
     * Obtain imagesPath value
     *
     * @return imagesPath value
     */
    public @NotNull
    String getImagesPath()
    {
        return this.imagesPath;
    }

    /**
     * Modify imagesPath value
     *
     * @param imagesPath New imagesPath value
     */
    public void setImagesPath(
            @NotNull
                    String imagesPath)
    {
        if (imagesPath == null)
        {
            throw new NullPointerException("imagesPath MUST NOT be null !");
        }

        this.imagesPath = imagesPath;
    }

    /**
     * Obtain cssPath value
     *
     * @return cssPath value
     */
    public @NotNull
    String getCssPath()
    {
        return this.cssPath;
    }

    /**
     * Modify cssPath value
     *
     * @param cssPath New cssPath value
     */
    public void setCssPath(
            @NotNull
                    String cssPath)
    {
        if (cssPath == null)
        {
            throw new NullPointerException("cssPath MUST NOT be null !");
        }

        this.cssPath = cssPath;
    }

    /**
     * Obtain scriptsPath value
     *
     * @return scriptsPath value
     */
    public @NotNull
    String getScriptsPath()
    {
        return this.scriptsPath;
    }

    /**
     * Modify scriptsPath value
     *
     * @param scriptsPath New scriptsPath value
     */
    public void setScriptsPath(
            @NotNull
                    String scriptsPath)
    {
        this.scriptsPath = scriptsPath;
    }

    /**
     * Number of webpages
     *
     * @return Number of webpages
     */
    public int numberOfPages()
    {
        return this.webPages.size();
    }

    /**
     * Obtain a webpage
     *
     * @param index Page index
     * @return Web page
     */
    public WebPage getPage(int index)
    {
        return this.webPages.get(index);
    }

    /**
     * Create a new web page
     *
     * @param name Web page name
     * @return Created web page
     */
    public WebPage createWebPage(
            @NotNull
                    String name)
    {
        if (name == null)
        {
            throw new NullPointerException("name MUST NOT be null !");
        }

        WebPage webPage = new WebPage();
        webPage.setName(name);
        this.webPages.add(webPage);
        return webPage;
    }

    /**
     * Remove a web page
     *
     * @param index Web page index
     */
    public void removePage(int index)
    {
        this.webPages.remove(index);
    }

    /**
     * Remove a web page
     *
     * @param webPage Web page to remove
     */
    public void removePage(WebPage webPage)
    {
        this.webPages.remove(webPage);
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
        this.name = byteArray.readString();
        String directoryPath = byteArray.readString();

        if (directoryPath == null)
        {
            this.directory = null;
        }
        else
        {
            this.directory = new File(directoryPath);
        }

        directoryPath = byteArray.readString();

        if (directoryPath == null)
        {
            this.exportDirectory = null;
        }
        else
        {
            this.exportDirectory = new File(directoryPath);
        }

        this.imagesPath = byteArray.readString();
        this.cssPath = byteArray.readString();
        this.scriptsPath = byteArray.readString();

        try
        {
            this.webPages.clear();
            byteArray.readBinarizableList(this.webPages);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Failed to parse !", e);
        }
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
        byteArray.writeString(this.name);

        if (this.directory == null)
        {
            byteArray.writeString(null);
        }
        else
        {
            byteArray.writeString(this.directory.getAbsolutePath());
        }

        if (this.exportDirectory == null)
        {
            byteArray.writeString(null);
        }
        else
        {
            byteArray.writeString(this.exportDirectory.getAbsolutePath());
        }

        byteArray.writeString(this.imagesPath);
        byteArray.writeString(this.cssPath);
        byteArray.writeString(this.scriptsPath);
        byteArray.writeBinarizableList(this.webPages);
    }

    /**
     * Save the project
     *
     * @throws IOException On writing issue
     */
    public void save() throws IOException
    {
        if (this.directory == null)
        {
            throw new IOException("No specified directory !");
        }

        if (this.name.length() == 0)
        {
            this.name = "project";
        }

        File         project      = new File(this.directory, this.name);
        OutputStream outputStream = null;

        try
        {
            outputStream = new FileOutputStream(project);
            UtilIO.writeBinarizable(this, outputStream);
        }
        catch (Exception e)
        {
            throw new IOException("Failed to save " + project.getAbsolutePath(), e);
        }
        finally
        {
            if (outputStream != null)
            {
                try
                {
                    outputStream.flush();
                }
                catch (Exception ignored)
                {
                }

                try
                {
                    outputStream.close();
                }
                catch (Exception ignored)
                {
                }
            }
        }
    }

    /**
     * Import image to project
     *
     * @param image Image file to import
     * @return Image name
     * @throws IOException On importing issue
     */
    public String importImage(File image) throws IOException
    {
        if (this.directory == null)
        {
            throw new IOException("No specified directory !");
        }

        File fileImage = new File(new File(this.directory, "images"), image.getName());
        UtilIO.copy(image, fileImage);
        return fileImage.getName();
    }

    /**
     * Generate project to web site
     *
     * @throws IOException On generation issue
     */
    public void generate() throws IOException
    {
        if (this.directory == null)
        {
            throw new IOException("No project directory");
        }

        File exportDirectory = this.getExportDirectory();

        if (!UtilIO.delete(exportDirectory) || !UtilIO.createDirectory(exportDirectory))
        {
            throw new IOException("Failed to create exportDirectory : " + exportDirectory.getAbsolutePath());
        }

        File source = this.imagesDirectory();

        if (source.exists())
        {
            File destination = new File(exportDirectory, this.imagesPath);
            UtilIO.copy(source, destination);
        }

        ResourcesSystem resourcesSystem = ResourcesWebSiteCreator.RESOURCES.obtainResourcesSystem();
        copyResourceDirectory(resourcesSystem, "pages/css", new File(exportDirectory, this.cssPath));
        copyResourceDirectory(resourcesSystem, "pages/scripts", new File(exportDirectory, this.scriptsPath));
        BufferedWriter bufferedWriter;
        File           fileWebPage;

        for (WebPage webPage : this.webPages)
        {
            bufferedWriter = null;

            try
            {
                fileWebPage = new File(exportDirectory, webPage.getName()
                                                               .replace(' ', '_') + ".html");
                UtilIO.createFile(fileWebPage);
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileWebPage), "UTF-8"));
                webPage.writeInHTML(this, bufferedWriter);
            }
            finally
            {
                if (bufferedWriter != null)
                {
                    try
                    {
                        bufferedWriter.flush();
                    }
                    catch (Exception ignored)
                    {
                    }

                    try
                    {
                        bufferedWriter.close();
                    }
                    catch (Exception ignored)
                    {
                    }
                }
            }
        }
    }

    /**
     * Project images directory
     *
     * @return Project images directory
     */
    public File imagesDirectory()
    {
        if (this.directory == null)
        {
            return null;
        }

        return new File(this.directory, "images");
    }

    /**
     * Copy resource directory to output directory
     *
     * @param resourcesSystem Resource system source
     * @param source          Directory source path
     * @param destination     Directory where copy
     * @throws IOException On copy issue
     */
    private void copyResourceDirectory(ResourcesSystem resourcesSystem, String source, File destination)
            throws IOException
    {
        ResourceDirectory sourceResource = (ResourceDirectory) resourcesSystem.obtainElement(source);

        for (ResourceElement resourceElement : resourcesSystem.obtainList(sourceResource))
        {
            if (!resourceElement.isDirectory())
            {
                this.copyResourceFile(resourcesSystem, resourceElement, destination);
            }
        }
    }

    /**
     * Copy a resource file
     *
     * @param resourcesSystem Resource system
     * @param resourceElement Resource file to copy
     * @param destination     Destination directory
     * @throws IOException On copy issue
     */
    private void copyResourceFile(ResourcesSystem resourcesSystem, ResourceElement resourceElement, File destination)
            throws IOException
    {
        InputStream inputStream = resourcesSystem.obtainInputStream((ResourceFile) resourceElement);

        try
        {
            UtilIO.write(inputStream, new File(destination, resourceElement.getName()));
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch (Exception ignored)
            {
            }
        }
    }

    /**
     * Obtain exportDirectory value
     *
     * @return exportDirectory value
     */
    public @Nullable
    File getExportDirectory()
    {
        return this.exportDirectory;
    }

    /**
     * Modify exportDirectory value
     *
     * @param exportDirectory New exportDirectory value
     */
    public void setExportDirectory(
            @NotNull
                    File exportDirectory)
    {
        if (exportDirectory == null)
        {
            throw new NullPointerException("exportDirectory MUST NOT be null !");
        }

        if (!UtilIO.createDirectory(exportDirectory))
        {
            throw new IllegalArgumentException("Can't create exportDirectory : " + exportDirectory.getAbsolutePath());
        }

        this.exportDirectory = exportDirectory;
    }

    /**
     * Obtain directory value
     *
     * @return directory value
     */
    public @Nullable
    File getDirectory()
    {
        return this.directory;
    }

    /**
     * Modify directory value
     *
     * @param directory New directory value
     */
    public void setDirectory(
            @NotNull
                    File directory)
    {
        if (directory == null)
        {
            throw new NullPointerException("directory MUST NOT be null !");
        }

        if (!UtilIO.createDirectory(directory))
        {
            throw new IllegalArgumentException("Can't create directory : " + directory.getAbsolutePath());
        }

        this.directory = directory;
    }

    /**
     * Try to compress all pages of the project
     *
     * @return {@code true} if some compression happen
     */
    public boolean compress()
    {
        boolean compress = false;

        for (WebPage webPage : this.webPages)
        {
            compress |= webPage.compress();
        }

        return compress;
    }
}
