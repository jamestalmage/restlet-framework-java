/*
 * Copyright 2005-2007 Noelios Consulting.
 * 
 * The contents of this file are subject to the terms of the Common Development
 * and Distribution License (the "License"). You may not use this file except in
 * compliance with the License.
 * 
 * You can obtain a copy of the license at
 * http://www.opensource.org/licenses/cddl1.txt See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing Covered Code, include this CDDL HEADER in each file and
 * include the License file at http://www.opensource.org/licenses/cddl1.txt If
 * applicable, add the following below this CDDL HEADER, with the fields
 * enclosed by brackets "[]" replaced with your own identifying information:
 * Portions Copyright [yyyy] [name of copyright owner]
 */

package org.restlet.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Date;

import org.restlet.data.LocalReference;
import org.restlet.data.MediaType;
import org.restlet.util.ByteUtils;

/**
 * Representation based on a file.
 * 
 * @author Jerome Louvel (contact@noelios.com)
 */
public class FileRepresentation extends Representation {
    /**
     * Creates a new file by detecting if the name is a URI or a simple path
     * name.
     * 
     * @param path
     *            The path name or file URI of the represented file.
     * @return The associated File instance.
     */
    private static File createFile(String path) {
        if (path.startsWith("file://")) {
            return new LocalReference(path).getFile();
        } else {
            return new File(path);
        }
    }

    /** The file handle. */
    private File file;

    /**
     * Constructor.
     * 
     * @param file
     *            The represented file.
     * @param mediaType
     *            The representation's media type.
     * @param timeToLive
     *            The time to live before it expires (in seconds).
     */
    public FileRepresentation(File file, MediaType mediaType, int timeToLive) {
        super(mediaType);
        this.file = file;
        setModificationDate(new Date(file.lastModified()));
        setExpirationDate(new Date(System.currentTimeMillis()
                + (1000L * timeToLive)));
        setMediaType(mediaType);
    }

    /**
     * Constructor.
     * 
     * @param path
     *            The path name or file URI of the represented file.
     * @param mediaType
     *            The representation's media type.
     * @param timeToLive
     *            The time to live before it expires (in seconds).
     * @see java.io.File#File(String)
     */
    public FileRepresentation(String path, MediaType mediaType, int timeToLive) {
        this(createFile(path), mediaType, timeToLive);
    }

    /**
     * Returns a readable byte channel. If it is supported by a file a read-only
     * instance of FileChannel is returned.
     * 
     * @return A readable byte channel.
     */
    public FileChannel getChannel() throws IOException {
        try {
            return new FileInputStream(file).getChannel();
        } catch (FileNotFoundException fnfe) {
            throw new IOException("Couldn't get the channel. File not found");
        }
    }

    /**
     * Returns the file handle.
     * 
     * @return the file handle.
     */
    public File getFile() {
        return file;
    }

    @Override
    public Reader getReader() throws IOException {
        return new FileReader(file);
    }

    @Override
    public long getSize() {
        if (super.getSize() != UNKNOWN_SIZE) {
            return super.getSize();
        } else {
            return this.file.length();
        }
    }

    @Override
    public FileInputStream getStream() throws IOException {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException fnfe) {
            throw new IOException("Couldn't get the stream. File not found");
        }
    }

    @Override
    public String getText() throws IOException {
        return ByteUtils.toString(getStream(), this.getCharacterSet());
    }

    /**
     * Sets the file handle.
     * 
     * @param file
     *            The file handle.
     */
    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void write(OutputStream outputStream) throws IOException {
        ByteUtils.write(getStream(), outputStream);
    }

    /**
     * Writes the representation to a byte channel. Optimizes using the file
     * channel transferTo method.
     * 
     * @param writableChannel
     *            A writable byte channel.
     */
    public void write(WritableByteChannel writableChannel) throws IOException {
        FileChannel fc = getChannel();
        long position = 0;
        long count = fc.size();
        long written = 0;

        while (count > 0) {
            written = fc.transferTo(position, count, writableChannel);
            position += written;
            count -= written;
        }
    }

    @Override
    public void write(Writer writer) throws IOException {
        ByteUtils.write(getReader(), writer);
    }

}