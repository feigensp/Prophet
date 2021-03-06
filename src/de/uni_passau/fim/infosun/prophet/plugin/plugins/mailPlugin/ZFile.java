package de.uni_passau.fim.infosun.prophet.plugin.plugins.mailPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * A <code>File</code> that can be zipped.
 */
public class ZFile extends File {

    private static final String EXTENSION = ".zip";

    /**
     * See {@link File#File(String)} for documentation.
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
     * See {@link File#File(String, String)} for documentation.
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
     * See {@link File#File(File, String)} for documentation.
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
     * See {@link File#File(URI)} for documentation.
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
     * @return optionally the created archive <code>File</code>
     */
    public Optional<File> zip() {
        File parent = getParentFile();

        if (parent == null) {
            System.err.println("Aborting zip of " + getAbsolutePath());
            System.err.println("Could not find a parent directory to save the archive to. Use zip(toDirectory).");
            return Optional.empty();
        } else {
            return zip(parent);
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
     * @return optionally the created archive <code>File</code>
     *
     * @throws IllegalArgumentException
     *         if <code>toDirectory</code> exists and is not a directory
     */
    public Optional<File> zip(File toDirectory) {
        return zip(toDirectory, checkArchiveName(getName()));
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
     * @return optionally the created archive <code>File</code>
     *
     * @throws IllegalArgumentException
     *         if <code>toDirectory</code> exists and is not a directory
     */
    public Optional<File> zip(File toDirectory, String withName) {
        String archiveName = checkArchiveName(withName);

        if (!exists()) {
            System.err.println(getAbsolutePath() + " does not exist and can therefore not be zipped.");
            return Optional.empty();
        }

        if (toDirectory.exists() && !toDirectory.isDirectory()) {
            throw new IllegalArgumentException("toDirectory exists and is not a directory.");
        }

        if (!toDirectory.exists() && !toDirectory.mkdirs()) {
            System.err.println("Could not create the target directory " + toDirectory.getAbsolutePath());
            return Optional.empty();
        }

        File archiveFile = new File(toDirectory, archiveName);

        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(archiveFile))) {

            try (Stream<Path> stream = Files.walk(toPath())) {
                stream.forEach(path -> {
                    String name = getName() + '/' + toURI().relativize(path.toUri()).getPath();

                    try {
                        zipOut.putNextEntry(new ZipEntry(name));
                        copy(path.toFile(), zipOut);
                        zipOut.closeEntry();
                    } catch (IOException e) {
                        System.err.println("Could not zip the file " + path);
                        System.err.println(e.getClass().getSimpleName() + " : " + e.getMessage());
                    }
                });
            } catch (IOException e) {
                System.err.println("Could not walk the file tree under " + getAbsolutePath());
                System.err.println(e.getClass().getSimpleName() + " : " + e.getMessage());
                return Optional.empty();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Could not write the archive file " + archiveFile.getAbsolutePath());
            System.err.println(e.getClass().getSimpleName() + " : " + e.getMessage());
            return Optional.empty();
        } catch (IOException ignored) {
            // may occur when closing the ZipOutputStream
        }

        return Optional.of(archiveFile);
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
        return name.endsWith(EXTENSION) ? name : name + EXTENSION;
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
            System.err.println(e.getClass().getSimpleName() + ':' + e.getMessage());
        }
    }
}
