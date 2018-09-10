package com.meijinpeng;

import java.io.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 统计字符数、单词数和行数等
 */
public class WordCount {

    //创建线程池，并发任务
    private static Executor executor = Executors.newFixedThreadPool(3);

    /**
     * 统计一行中有多少字符
     */
    private int getCharCount(String line) {

        return 0;
    }

    /**
     * 统计一行中有多少单词
     */
    private int getWorkCount(String line) {

        return 0;
    }

    /**
     * 添加一行
     */
    private int addLineCount() {
        return 1;
    }

    /**
     * 判断是否是代码行，是则返回1
     */
    private int addCodeLine(String line) {

        return 0;
    }

    /**
     * 判断是否是空白行，是则返回1
     */
    private int addBlankLine(String line) {

        return 0;
    }

    /**
     * 判断是否是注释行，是则返回1
     */
    private int addCommentLine(String line) {

        return 0;
    }

    /**
     * 统计文件的字符数、单词数和行数等
     * @param fileName 文件名
     * @param countCallback 回调接口
     * @param strs 参数数组
     */
    public void count(String fileName, Callback<Count> countCallback, String... strs) {
        Count count = checkParams(strs);
        File file = new File(fileName);
        if(!file.exists()) {
            countCallback.onError("文件不存在，请重试");
            return;
        }
        BufferedReader reader = null;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            reader = new BufferedReader(fileReader);
            String line;
            while ( null != (line = reader.readLine())){
                if(count.lineCount != -1) {
                    count.lineCount = count.lineCount + addLineCount();
                }
                if(count.wordCount != -1) {
                    count.wordCount = count.wordCount + getWorkCount(line);
                }
                if(count.charCount != -1) {
                    count.charCount = count.charCount + getCharCount(line);
                }
                if(count.codeLineCount != -1 && count.blankLineCount != -1
                        && count.commentLineCount != -1) {
                    count.codeLineCount = count.codeLineCount + addCodeLine(line);
                    count.blankLineCount = count.blankLineCount + addBlankLine(line);
                    count.commentLineCount = count.commentLineCount + addCommentLine(line);
                }
            }
            countCallback.onSuccess(count);
        } catch (IOException e) {
            countCallback.onError("文件读取出错，请重试");
        } finally {
            closeIOs(fileReader, reader);
        }
    }

    /**
     * 关闭IO流
     */
    private void closeIOs(Closeable... closeables) {
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

    /**
     * 检查参数选择
     */
    private Count checkParams(String... strs) {
        Count count = new Count();
        if(strs == null || strs.length == 0) return count;
        for (String str: strs) {
            switch (str) {
                case Constant.COUNT_CHAR:
                    count.charCount = 0;
                    break;
                case Constant.COUNT_WORD:
                    count.wordCount = 0;
                    break;
                case Constant.COUNT_LINE:
                    count.lineCount = 0;
                    break;
                case Constant.COUNT_COMPLEX_LINE:
                    count.blankLineCount = 0;
                    count.codeLineCount = 0;
                    count.commentLineCount = 0;
                    break;
            }
        }
        return count;
    }

    public interface Callback<T> {
        void onError(String msg);
        void onSuccess(T count);
    }
}
