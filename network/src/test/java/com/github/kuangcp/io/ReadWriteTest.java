package com.github.kuangcp.io;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Kuangcp
 * 2024-04-29 11:44
 */
@Slf4j
public class ReadWriteTest {

    static void convertLine(String inPath, String outPath) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outPath))) {
            List<String> rows = Files.readAllLines(Paths.get(inPath));
            for (int i = 0; i < rows.size(); i++) {
                String row = rows.get(i);
                writer.write((i + 1) + "," + row);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Test
    public void testReadWriteSimple() throws Exception {
        convertLine("source.csv", "target.csv");
    }
}
