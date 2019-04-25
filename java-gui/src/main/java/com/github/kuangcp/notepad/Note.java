package com.github.kuangcp.notepad;

import com.github.kuangcp.io.ResourceTool;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by https://github.com/kuangcp on 17-8-22  下午2:39
 */
@Slf4j
class Note extends JFrame implements ActionListener {

  private JTextArea textArea;

  public static void main(String[] args) {
    new Note().init();
  }

  private void init() {
    textArea = new JTextArea();
    JMenuBar menuBar = new JMenuBar();
    this.setJMenuBar(menuBar);

    JMenu fileMenu = new JMenu("文件");
    fileMenu.setMnemonic('F');

    JMenu saveMenu = new JMenu("保存");

    JMenuItem jmi1 = new JMenuItem("打开", new ImageIcon());
    JMenuItem jmi2 = new JMenuItem("保存");

    //注册监听
    jmi1.addActionListener(this);
    jmi1.setActionCommand(ICommand.OPEN_FILE);
    jmi2.addActionListener(this);
    jmi2.setActionCommand(ICommand.SAVE_FILE);

    //把jm1放入jmb
    menuBar.add(fileMenu);
    menuBar.add(saveMenu);

    //把item放入到Menu
    fileMenu.add(jmi1);
    saveMenu.add(jmi2);

    JScrollPane jsp = new JScrollPane(textArea);

    this.add(jsp);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setSize(400, 300);
    this.setVisible(true);
  }

  public void actionPerformed(ActionEvent event) {
    switch (event.getActionCommand()) {
      case ICommand.OPEN_FILE:
        openFile();
        break;
      case ICommand.SAVE_FILE:
        saveFile();
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
    jc.setDialogTitle("请选择文件...");
    //默认方式
    jc.showOpenDialog(null);
    jc.setVisible(true);

    //得知道用户选择的文件 绝对路径
    String filePath = jc.getSelectedFile().getAbsolutePath();
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
      textArea.setText(result.toString());
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
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("另存为...");

    //按默认方式显示
    chooser.showSaveDialog(null);
    chooser.setVisible(true);

    //得到用户希望把文件保存到何处
    File file = chooser.getSelectedFile();
    if (Objects.isNull(file)) {
      log.warn("please select file: chooser={}", chooser);
      return;
    }

    //准备写入到指定目录下
    String path = file.getAbsolutePath();
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
      bw.write(this.textArea.getText());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
