package com.peashoot.blog.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class IOUtils {
    public static String read(InputStream inputStream, String charset) throws IOException {
        int capacity = 1024;
        byte[] buffer = new byte[capacity];
        int readSize = -1;
        StringBuilder strd = new StringBuilder();
        while ((readSize = inputStream.read(buffer, 0, capacity)) > 0) {
            strd.append(StringUtils.convertByteArrayToString(buffer, readSize, charset));
        }
        return strd.toString();
    }
}
