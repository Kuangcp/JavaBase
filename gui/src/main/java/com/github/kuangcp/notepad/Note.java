package com.github.kuangcp.notepad;

import static java.awt.event.InputEvent.CTRL_MASK;
import static java.awt.event.KeyEvent.VK_S;

import com.github.kuangcp.notepad.base.ActionCommand;
import com.github.kuangcp.notepad.base.HandlerType;
import com.github.kuangcp.notepad.handler.NotepadActionListener;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by https://github.com/kuangcp on 17-8-22  下午2:39
 */
@Slf4j
public
class Note extends JFrame {

  public static JTextArea textArea = new JTextArea();

  public static void main(String[] args) {
    float[] hsbArray = Color.RGBtoHSB(60, 63, 65, null);
    textArea.setBackground(Color.getHSBColor(hsbArray[0], hsbArray[1], hsbArray[2]));
    textArea.setForeground(Color.LIGHT_GRAY);
    textArea.setFont(Font.decode("'IBM Plex Mono' 18"));
    new Note().run();
  }

  private void run() {
    createMenuBar();

    JScrollPane scrollPane = new JScrollPane(textArea);
    this.add(scrollPane);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setSize(500, 400);
    this.setVisible(true);
  }

  private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();
    this.setJMenuBar(menuBar);

    JMenu fileMenu = new JMenu("File");
    fileMenu.setMnemonic('F');
    menuBar.add(fileMenu);

    JMenuItem openItem = new JMenuItem("Open");
    openItem.setName(HandlerType.FILE);
    openItem.addActionListener(NotepadActionListener.LISTENER);
    openItem.setActionCommand(ActionCommand.OPEN_FILE);

    JMenuItem saveAsItem = new JMenuItem("Save as");
    saveAsItem.setName(HandlerType.FILE);
    saveAsItem.addActionListener(NotepadActionListener.LISTENER);
    saveAsItem.setActionCommand(ActionCommand.SAVE_AS_FILE);

    JMenuItem saveItem = new JMenuItem("Save");
    saveItem.setName(HandlerType.FILE);
    saveItem.addActionListener(NotepadActionListener.LISTENER);
    saveItem.setActionCommand(ActionCommand.SAVE_FILE);
    saveItem.setAccelerator(KeyStroke.getKeyStroke(VK_S, CTRL_MASK));

    fileMenu.add(openItem);
    fileMenu.add(saveAsItem);
    menuBar.add(saveItem);
  }
}
