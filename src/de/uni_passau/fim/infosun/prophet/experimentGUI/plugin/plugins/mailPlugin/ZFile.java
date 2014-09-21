package de.uni_passau.fim.infosun.prophet.experimentGUI.plugin.plugins.mailPlugin;

import java.io.*;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * A <code>File</code> that can be zipped.
 */
public class ZFile extends File {

    private static final String EXTENSION = ".zip";

    /**
     * Creates a new <code>File</code> instance by converting the given
     * pathname string into an abstract pathname.  If the given string is
     * the empty string, then the result is the empty abstract pathname.
     *
     * @param pathname
     *         A pathname string
     *
     * @throws NullPointerException
     *         If the <code>pathname</code> argument is <code>null</code>
     */
    public ZFile(String pathname) {
        super(pathname);
    }

    /**
     * Creates a new <code>File</code> instance from a parent pathname string
     * and a child pathname string.
     * <p>
     * <p> If <code>parent</code> is <code>null</code> then the new
     * <code>File</code> instance is created as if by invoking the
     * single-argument <code>File</code> constructor on the given
     * <code>child</code> pathname string.
     * <p>
     * <p> Otherwise the <code>parent</code> pathname string is taken to denote
     * a directory, and the <code>child</code> pathname string is taken to
     * denote either a directory or a file.  If the <code>child</code> pathname
     * string is absolute then it is converted into a relative pathname in a
     * system-dependent way.  If <code>parent</code> is the empty string then
     * the new <code>File</code> instance is created by converting
     * <code>child</code> into an abstract pathname and resolving the result
     * against a system-dependent default directory.  Otherwise each pathname
     * string is converted into an abstract pathname and the child abstract
     * pathname is resolved against the parent.
     *
     * @param parent
     *         The parent pathname string
     * @param child
     *         The child pathname string
     *
     * @throws NullPointerException
     *         If <code>child</code> is <code>null</code>
     */
    public ZFile(String parent, String child) {
        super(parent, child);
    }

    /**
     * Creates a new <code>File</code> instance from a parent abstract
     * pathname and a child pathname string.
     * <p>
     * <p> If <code>parent</code> is <code>null</code> then the new
     * <code>File</code> instance is created as if by invoking the
     * single-argument <code>File</code> constructor on the given
     * <code>child</code> pathname string.
     * <p>
     * <p> Otherwise the <code>parent</code> abstract pathname is taken to
     * denote a directory, and the <code>child</code> pathname string is taken
     * to denote either a directory or a file.  If the <code>child</code>
     * pathname string is absolute then it is converted into a relative
     * pathname in a system-dependent way.  If <code>parent</code> is the empty
     * abstract pathname then the new <code>File</code> instance is created by
     * converting <code>child</code> into an abstract pathname and resolving
     * the result against a system-dependent default directory.  Otherwise each
     * pathname string is converted into an abstract pathname and the child
     * abstract pathname is resolved against the parent.
     *
     * @param parent
     *         The parent abstract pathname
     * @param child
     *         The child pathname string
     *
     * @throws NullPointerException
     *         If <code>child</code> is <code>null</code>
     */
    public ZFile(File parent, String child) {
        super(parent, child);
    }

    /**
     * Creates a new <tt>File</tt> instance by converting the given
     * <tt>file:</tt> URI into an abstract pathname.
     * <p>
     * <p> The exact form of a <tt>file:</tt> URI is system-dependent, hence
     * the transformation performed by this constructor is also
     * system-dependent.
     * <p>
     * <p> For a given abstract pathname <i>f</i> it is guaranteed that
     * <p>
     * <blockquote><tt>
     * new File(</tt><i>&nbsp;f</i><tt>.{@link #toURI() toURI}()).equals(</tt><i>&nbsp;f</i><tt>.{@link
     * #getAbsoluteFile()
     * getAbsoluteFile}())
     * </tt></blockquote>
     * <p>
     * so long as the original abstract pathname, the URI, and the new abstract
     * pathname are all created in (possibly different invocations of) the same
     * Java virtual machine.  This relationship typically does not hold,
     * however, when a <tt>file:</tt> URI that is created in a virtual machine
     * on one operating system is converted into an abstract pathname in a
     * virtual machine on a different operating system.
     *
     * @param uri
     *         An absolute, hierarchical URI with a scheme equal to
     *         <tt>"file"</tt>, a non-empty path component, and undefined
     *         authority, query, and fragment components
     *
     * @throws NullPointerException
     *         If <tt>uri</tt> is <tt>null</tt>
     * @throws IllegalArgumentException
     *         If the preconditions on the parameter do not hold
     * @see #toURI()
     * @see java.net.URI
     * @since 1.4
     */
    public ZFile(URI uri) {
        super(uri);
    }

    /**
     * Zips this file and stores the resulting archive in its parent directory.
     * The archive will have the same name as this <code>File</code>.
     * This method will do nothing (except print a warning) for files without a parent.
     *
     * @throws FileNotFoundException
     *         if a file with the archive name<br>
     *         <ul>
     *             <li>exists but is a directory rather than a regular file</li>
     *             <li>does not exist but cannot be created</li>
     *             <li>cannot be opened for any other reason</li>
     *         </ul>
     */
    public void zip() throws FileNotFoundException {
        File parent = getParentFile();

        if (parent == null) {
            System.err.println("Aborting zip of " + getAbsolutePath());
            System.err.println("Could not find a parent directory to save the archive to. Use zip(toDirectory).");
        } else {
            zip(parent);
        }
    }

    /**
     * Zips this file and stores the resulting archive in the given directory.
     * <code>toDirectory</code> will be created if it does not exist.
     * The archive will have the same name as this <code>File</code>.
     *
     * @param toDirectory
     *         the directory to save the resulting archive in
     *
     * @throws IllegalArgumentException
     *         if <code>toDirectory</code> exists and is not a directory
     * @throws FileNotFoundException
     *         <ul>
     *           <li>
     *               if a file with the archive name<br>
     *              <ul>
     *               <li>exists but is a directory rather than a regular file</li>
     *               <li>does not exist but cannot be created</li>
     *               <li>cannot be opened for any other reason</li>
     *              </ul>
     *           </li>
     *           <li>if toDirectory does not exist and can not be created</li>
     *         </ul>
     */
    public void zip(File toDirectory) throws FileNotFoundException {
        zip(toDirectory, checkArchiveName(getName()));
    }

    /**
     * Zips this file and stores the resulting archive in the given directory.
     * <code>toDirectory</code> will be created if it does not exist.
     *
     * @param toDirectory
     *         the directory to save the resulting archive in
     * @param withName
     *         the name for the archive (without extension)
     *
     * @throws IllegalArgumentException
     *         if <code>toDirectory</code> exists and is not a directory
     * @throws FileNotFoundException
     *         <ul>
     *           <li>
     *               if a file with the archive name<br>
     *              <ul>
     *               <li>exists but is a directory rather than a regular file</li>
     *               <li>does not exist but cannot be created</li>
     *               <li>cannot be opened for any other reason</li>
     *              </ul>
     *           </li>
     *           <li>if toDirectory does not exist and can not be created</li>
     *         </ul>
     */
    public void zip(File toDirectory, String withName) throws FileNotFoundException {
        String archiveName = checkArchiveName(withName);

        if (toDirectory.exists() && !toDirectory.isDirectory()) {
            throw new IllegalArgumentException("toDirectory exists and is not a directory.");
        }

        if (!toDirectory.exists() && !toDirectory.mkdirs()) {
            throw new FileNotFoundException("Could not create the target directory " + toDirectory.getAbsolutePath());
        }

        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(new File(toDirectory, archiveName)))) {

            try (Stream<Path> stream = Files.walk(toPath())) {
                stream.forEach(path -> {
                    String name = getName() + "/" + toURI().relativize(path.toUri()).getPath();

                    try {
                        zipOut.putNextEntry(new ZipEntry(name));
                        copy(path.toFile(), zipOut);
                        zipOut.closeEntry();
                    } catch (IOException e) {
                        System.err.println("Could not zip the file " + path);
                        System.err.println(e.getMessage());
                    }
                });
            } catch (IOException e) {
                System.err.println("Could not walk the file tree under " + getAbsolutePath());
                System.err.println(e.getMessage());
            }
        } catch (FileNotFoundException exception) {
            throw exception;
        } catch (IOException ignored) {
            // may occur when closing the stream
        }
    }

    /**
     * Copies the given <code>File</code> to the <code>OutputStream</code>. Does nothing for directories.
     *
     * @param file
     *         the <code>File</code> to be copied
     * @param outputStream
     *         the <code>OutputStream</code> to be copied to
     */
    private void copy(File file, OutputStream outputStream) {
        ByteBuffer byteBuffer;
        FileChannel channel;
        byte[] buffer;
        int numRead;

        if (file.isDirectory()) {
            return;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            channel = fis.getChannel();
            buffer = new byte[256 * 1024];
            byteBuffer = ByteBuffer.wrap(buffer);

            while ((numRead = channel.read(byteBuffer)) != -1) {
                outputStream.write(buffer, 0, numRead);
                byteBuffer.clear();
            }
        } catch (IOException e) {
            System.err.println("Could not copy " + file.getAbsolutePath() + " to the given OutputStream.");
            System.err.println(e.getMessage());
        }
    }

    /**
     * Checks whether a given name ends with ".zip" and appends that suffix if it is missing.
     *
     * @param name
     *         the name to check
     *
     * @return the resulting name
     */
    private String checkArchiveName(String name) {
        return (name.endsWith(EXTENSION)) ? name : name + EXTENSION;
    }
}
