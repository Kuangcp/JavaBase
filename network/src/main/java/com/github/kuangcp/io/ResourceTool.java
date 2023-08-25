package com.github.kuangcp.io;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;

/**
 * @author kuangchengping@sinohealth.cn
 * 2023-08-25 19:37
 */
public class ResourceTool {
    public static void close(Closeable... resources) throws IOException {
        if (Objects.isNull(resources)) {
            return;
        }
        for (Closeable closeable : resources) {
            if (Objects.isNull(closeable)) {
                continue;
            }
            closeable.close();
        }
    }
}
