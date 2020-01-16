package com.github.kuangcp.multicast;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import lombok.extern.slf4j.Slf4j;

/**
 * @author https://github.com/kuangcp on 2020-01-15 20:34
 */
@Slf4j
public class Two {

  private static int port = 8000;
  private static String address = "228.0.0.4";

  public static void main(String[] args) {
    try {
      InetAddress group = InetAddress.getByName(address);
      MulticastSocket multicastSocket = new MulticastSocket(port);
      multicastSocket.joinGroup(group);
      byte[] buffer = new byte[1024];

      while (true) {
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        multicastSocket.receive(packet);

        String str = new String(packet.getData(), 0, packet.getLength());
        log.info("str={}", str);
      }
    } catch (Exception e) {
      log.error("", e);
    }
  }
}
