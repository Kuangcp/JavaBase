package com.github.kuangcp.inout;

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

  private CopyFile file = new CopyFile();

  private String from = "src/test/resources/test.log";
  private String dest = "src/test/resources/test2.log";
  private Path fromPath = Paths.get(from);
  private Path destPath = Paths.get(dest);

  private String content = "hello world";

  @Test
  public void testFirst() throws IOException {
    file.copyFileWithSixChannel(from, dest);
    validate();
  }

  @Test
  public void testCopyByChar() throws IOException {
    file.copyFileByChar(from, dest);
    validate();
  }

  @Test
  public void testCopyByByte() throws IOException {
    file.copyFileByByte(from, dest);
    validate();
  }

  @Test
  public void testCopyByCharBuffer() throws IOException {
    file.copyFileByCharBuffer(from, dest);
    validate();
  }

  @Test
  public void testCopyByFiles() throws IOException {
    file.copyByFiles(fromPath, destPath);
    validate();
  }

  @Before
  public void createFile() throws IOException {
    Files.createFile(fromPath);
    Files.write(fromPath, content.getBytes());
    assert Files.exists(fromPath);
  }

  private void validate() throws IOException {
    Optional<String> fileContent = Files.lines(fromPath).reduce(String::concat);
    assert fileContent.isPresent();
    log.debug("validate: content={}", fileContent.get());
    assertThat(fileContent.get(), equalTo(content));
  }

  @After
  public void deleteFile() throws IOException {
    Files.delete(fromPath);
    Files.delete(destPath);

    assert !Files.exists(fromPath);
    assert !Files.exists(destPath);
  }
}