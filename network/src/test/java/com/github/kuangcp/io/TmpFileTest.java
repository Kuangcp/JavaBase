package com.github.kuangcp.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author Kuangcp
 * 2024-05-16 21:44
 */
@Slf4j
public class TmpFileTest {
    TmpFile tmpFile = new TmpFile();

    @Test
    public void testCreateFile() throws Exception {
        File result = tmpFile.createFile("test/1");

        FileOutputStream out = null;
        OutputStreamWriter writer = null;
        BufferedWriter buffer = null;
        try {
            out = new FileOutputStream(result);
            writer = new OutputStreamWriter(out);
            buffer = new BufferedWriter(writer);

            for (int i = 0; i < 50; i++) {
                buffer.write(i + "");
                Thread.sleep(1000);
                buffer.flush();
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            ResourceTool.close(buffer, writer, out);
        }
        TimeUnit.SECONDS.sleep(120);
    }
}
