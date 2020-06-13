package com.github.kuangcp.file;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * created by https://gitee.com/gin9
 *
 * @author kuangcp on 3/17/19-12:34 AM
 */
@Slf4j
public class CopyFileTest {

  private final CopyFile file = new CopyFile();

  private final String root = CopyFileTest.class.getResource("/").getPath();
  private final String from = root + "test.log";
  private final String dest = root + "test2.log";
  private final Path fromPath = Paths.get(from);
  private final Path destPath = Paths.get(dest);

  private final String content = "hello world";

  @Before
  public void createFile() throws IOException {
    Files.createFile(fromPath);
    Files.write(fromPath, content.getBytes());
    assert Files.exists(fromPath);
  }

  @After
  public void deleteFile() throws IOException {
    Files.delete(fromPath);
    Files.delete(destPath);

    assert !Files.exists(fromPath);
    assert !Files.exists(destPath);
  }

  @Test
  public void testFirst() throws IOException {
    file.copyFileWithSixChannel(from, dest);
    validateResultFile();
  }

  @Test
  public void testCopyByChar() throws IOException {
    file.copyFileByChar(from, dest);
    validateResultFile();
  }

  @Test
  public void testCopyByByte() throws IOException {
    file.copyFileByByte(from, dest);
    validateResultFile();
  }

  @Test
  public void testCopyByCharBuffer() throws IOException {
    file.copyFileByCharBuffer(from, dest);
    validateResultFile();
  }

  @Test
  public void testCopyByFiles() throws IOException {
    file.copyByFiles(fromPath, destPath);
    validateResultFile();
  }

  private void validateResultFile() throws IOException {
    Optional<String> fileContent = Files.lines(destPath).reduce(String::concat);
    assert fileContent.isPresent();
    log.debug("validate: content={}", fileContent.get());
    assertThat(fileContent.get(), equalTo(content));
  }
}