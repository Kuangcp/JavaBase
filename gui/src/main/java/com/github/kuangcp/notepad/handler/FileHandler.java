package com.github.kuangcp.notepad.handler;

import com.github.kuangcp.io.ResourceTool;
import com.github.kuangcp.notepad.Note;
import com.github.kuangcp.notepad.base.ActionCommand;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import javax.swing.JFileChooser;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2019-07-28 13:10
 */
@Slf4j
class FileHandler extends BaseHandler {

  private String currentPath;

  @Override
  void handle(ActionEvent event) {
    switch (event.getActionCommand()) {
      case ActionCommand.OPEN_FILE:
        openFile();
        break;
      case ActionCommand.SAVE_FILE:
        saveFile();
        break;
      case ActionCommand.SAVE_AS_FILE:
        saveAsFile();
        break;
      default:
        log.info("not supported command");
        break;
    }
  }

  private void openFile() {
    //推荐JFileChooser 组件
    JFileChooser jc = new JFileChooser();
    //设置名字
    jc.setDialogTitle("Please select file...");
    //默认方式
    jc.showOpenDialog(null);
    jc.setVisible(true);

    //得知道用户选择的文件 绝对路径
    File selectedFile = jc.getSelectedFile();
    if (Objects.isNull(selectedFile)) {
      log.warn("not select any file");
      return;
    }

    String filePath = selectedFile.getAbsolutePath();
    currentPath = filePath;
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(filePath));

      //从文件读取信息显示到jta
      String s;
      StringBuilder result = new StringBuilder();
      while ((s = br.readLine()) != null) {
        result.append(s).append("\n");
      }

      //输出到 textArea
      Note.textArea.setText(result.toString());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    } finally {
      try {
        ResourceTool.close(br);
      } catch (IOException e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  private void saveFile() {
    if (Objects.isNull(currentPath)) {
      log.warn("not open any file");
      Optional<String> pathOpt = saveAsFile();
      pathOpt.ifPresent(v -> currentPath = v);
      return;
    }
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(currentPath))) {
      bw.write(Note.textArea.getText());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  private Optional<String> saveAsFile() {
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Save as ");

    //按默认方式显示
    chooser.showSaveDialog(null);
    chooser.setVisible(true);

    //得到用户希望把文件保存到何处
    File file = chooser.getSelectedFile();
    if (Objects.isNull(file)) {
      log.warn("please select file: chooser={}", chooser);
      return Optional.empty();
    }

    //准备写入到指定目录下
    String path = file.getAbsolutePath();
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
      bw.write(Note.textArea.getText());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    return Optional.of(path);
  }
}
