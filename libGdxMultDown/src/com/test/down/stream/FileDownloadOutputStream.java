package com.test.down.stream;

import java.io.IOException;

public interface FileDownloadOutputStream {
    void write(byte b[], int off, int len) throws IOException;
    void flushAndSync() throws IOException;
    void close() throws IOException;
    void seek(long offset) throws IOException, IllegalAccessException;
    void setLength(final long newLength) throws IOException, IllegalAccessException;
}
