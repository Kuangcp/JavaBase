package com.github.kuangcp.multicast;

import lombok.extern.slf4j.Slf4j;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * @author https://github.com/kuangcp on 2020-01-15 20:34
 */
@Slf4j
public class Consumer {

    public static void main(String[] args) {
        try {
            InetAddress group = InetAddress.getByName(MulticastConstant.ipAddress);
            MulticastSocket multicastSocket = new MulticastSocket(MulticastConstant.port);
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
