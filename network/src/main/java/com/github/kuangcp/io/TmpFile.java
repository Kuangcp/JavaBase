package com.github.kuangcp.io;


import java.io.File;

/**
 * @author Kuangcp
 * 2024-05-16 21:41
 */
public class TmpFile {

    String tmp = System.getProperty("java.io.tmpdir");

    File createFile(String filename) {
        return new File(tmp + "/" + filename);
    }
}
