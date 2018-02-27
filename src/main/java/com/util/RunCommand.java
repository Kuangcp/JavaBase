package com.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by https://github.com/kuangcp on 18-2-27  下午6:41
 * Java 执行终端命令
 * @author kuangcp
 */
public class RunCommand {

    public static void main(String[]s){
        run();
    }

    public static void run(){
        String[] arguments = new String[] { "dash", "/home/kcp/Application/Script/shell/check_by_aliases.sh" };
        try {
            //核心
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(arguments);
            Process process = Runtime.getRuntime().exec(arguments);
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line+"\n");
            }
            System.out.println(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
