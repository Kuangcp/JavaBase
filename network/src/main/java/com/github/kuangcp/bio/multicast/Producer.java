package com.github.kuangcp.bio.multicast;

import lombok.extern.slf4j.Slf4j;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * @author <a href="https://github.com/kuangcp">Kuangcp</a> on 2020-01-15 20:30
 */
@Slf4j
public class Producer {

    public static void main(String[] args) {
        try {
            InetAddress group = InetAddress.getByName(MulticastConstant.ipAddress);
            MulticastSocket multicastSocket = new MulticastSocket(MulticastConstant.port);
            multicastSocket.joinGroup(group);
            while (true) {
                byte[] buffer = "One".getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group,
                        MulticastConstant.port);
                multicastSocket.send(packet);
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }
}
