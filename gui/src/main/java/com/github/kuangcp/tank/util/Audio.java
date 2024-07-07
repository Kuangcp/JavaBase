package com.github.kuangcp.tank.util;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.File;
import java.io.IOException;

/**
 * 类初始化时 把路径传入 播放声音的类
 * 只能播放无损（无压缩的音乐文件）即WAV不能播放MP3
 * TODO fix
 */
@Slf4j
public class Audio extends Thread {

    private final String filename;
    @Getter
    @Setter
    private boolean live = true;

    //构造器
    public Audio(String wavFile) {
        filename = wavFile;
    }

    public void run() {
        File soundFile = new File(filename);

        AudioInputStream audioInputStream;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e) {
            log.error("", e);
            return;
        }

        AudioFormat format = audioInputStream.getFormat();
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

        SourceDataLine auline;
        try {
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (Exception e) {
            log.error("", e);
            return;
        }

        auline.start();
        int nBytesRead = 0;
        //这是缓冲
        byte[] abData = new byte[512];

        try {
            while (nBytesRead != -1) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0)
                    auline.write(abData, 0, nBytesRead);
            }
        } catch (IOException e) {
            log.error("", e);
        } finally {
            auline.drain();
            auline.close();
        }
    }
}