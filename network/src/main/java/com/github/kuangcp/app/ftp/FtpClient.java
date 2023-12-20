package com.github.kuangcp.app.ftp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * https://www.baeldung.com/java-ftp-client
 *
 * 2023-12-19 14:18
 */
@Slf4j
public class FtpClient {

    private final String server;
    private final int port;
    private final String user;
    private final String password;
    private FTPClient ftp;

    public FtpClient(String server, int port, String user, String password) {
        this.server = server;
        this.port = port;
        this.user = user;
        this.password = password;
    }


    public void enterLocalPassiveMode(){
        ftp.enterLocalPassiveMode();
    }

    public void open() throws IOException {
        ftp = new FTPClient();

        ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        ftp.connect(server, port);
        int reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }

        boolean login = ftp.login(user, password);
        log.info("login={}", login);
    }

    public void close() throws IOException {
        ftp.disconnect();
    }

    public Collection<String> listFiles(String path) throws IOException {
        // 如果本地启动了ftp服务，需要设置，否则会报425 https://stackoverflow.com/questions/72452185/java-ftp-client-failing-with-425-failed-to-establish-connection
        ftp.enterLocalPassiveMode();

        FTPFile[] files = ftp.listFiles(path);
        return Arrays.stream(files)
                .map(FTPFile::getName)
                .collect(Collectors.toList());
    }

    public Collection<String> listFiles(String path, final FTPFileFilter filter) throws IOException {
        FTPFile[] files = ftp.listFiles(path, filter);
        return Arrays.stream(files)
                .map(FTPFile::getName)
                .collect(Collectors.toList());
    }

    public void downloadFile(String source, String destination) throws IOException {
        FileOutputStream out = new FileOutputStream(destination);
        ftp.retrieveFile(source, out);
    }

    void putFileToPath(File file, String path) throws IOException {
        ftp.storeFile(path, Files.newInputStream(file.toPath()));
    }
}
