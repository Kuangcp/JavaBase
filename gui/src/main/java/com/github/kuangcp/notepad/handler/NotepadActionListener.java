package com.github.kuangcp.notepad.handler;

import com.github.kuangcp.notepad.base.HandlerType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.swing.JMenuItem;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2019-07-28 13:16
 */
@Slf4j
public class NotepadActionListener implements ActionListener {

  public static final NotepadActionListener LISTENER = new NotepadActionListener();
  private static final Map<String, BaseHandler> handlerMap = new HashMap<>();

  static {
    handlerMap.put(HandlerType.FILE, new FileHandler());
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    log.debug(": event={}", event);
    Object source = event.getSource();
    if (source instanceof JMenuItem) {
      String name = ((JMenuItem) source).getName();
      Optional.ofNullable(handlerMap.get(name)).ifPresent(v -> v.handle(event));
    }
  }
}
