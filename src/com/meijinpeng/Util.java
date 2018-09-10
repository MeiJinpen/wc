package com.meijinpeng;

import java.io.Closeable;
import java.io.IOException;

public class Util {

    /**
     * 匹配正确的文件名
     * @param fileName 文件名
     * @return 匹配结果
     */
    public static boolean isValidFileName(String fileName) {
        if (fileName == null || fileName.length() > 255) return false;
        else
            return fileName.matches("[^\\s\\\\/:\\*\\?\\\"<>\\|](\\x20|[^\\s\\\\/:\\*\\?\\\"<>\\|])*[^\\s\\\\/:\\*\\?\\\"<>\\|\\.]$");
    }

    /**
     * 关闭IO流
     */
    public static void closeIOs(Closeable... closeables) {
        for (Closeable closeable:closeables) {
            if(closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
