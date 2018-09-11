package com.meijinpeng;

import java.io.*;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 统计字符数、单词数和行数等
 */
public class WordCount {

    //创建线程池，并发任务
    private static Executor executor = Executors.newFixedThreadPool(3);

    /**
     * 统计一行中有多少字符sss
     */
    private int getCharCount(String line) {
        //空行算一个字符：“\n”
        if(line.isEmpty()) {
            return 1;
        }
        return line.length();
    }

    /**
     * 统计一行中有多少单词
     */
    private int getWorkCount(String line) {
        //把所有除了字母以外的字符都去掉
        line = line.replaceAll("[^a-zA-Z]", " ");
        //把多于两个以上的空格全部转化为一个空格
        line = line.replaceAll("\\s+"," ");
        //去掉首部和尾随的空格
        line = line.trim();
        //用空格分隔单词
        String[] words = line.split("[\\s+,.]");
        //如果为空行，则返回0
        if(words[0].equals("")) {
            return 0;
        }
        return words.length;
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
        if(addBlankLine(line) == 0 && addCommentLine(line) == 0) {
            return 1;
        }
        return 0;
    }

    /**
     * 判断是否是空白行，如果包括代码，则只有不超过一个可显示的字符，例如“{”。
     */
    private int addBlankLine(String line) {
        if(line.isEmpty()) return 1;
        if(!line.matches("[a-zA-Z]") && (line.trim().equals("{") || line.trim().equals("}"))) {
            return 1;
        }
        return 0;
    }

    /**
     * 判断是否是注释行，是则返回1
     */
    private int addCommentLine(String line) {
        line = line.trim();
        //匹配“//”单行注释或“} //”情况
        if(line.matches("}*\\s+//?.+")) {
            return 1;
        }
        //匹配“/**/”和“/** * */”的情况
        if(line.matches("((//?.+)|(/\\*+)|((^\\s)*\\*.+)|((^\\s)*\\*)|((^\\s)*\\*/))+")) {
            return 1;
        }
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
        if(count.isFuzzyQuery) {
            // todo:模糊查询目录下的匹配的文件名
            return;
        }
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
                if(isUsed(count.lineCount))
                    count.lineCount = count.lineCount + addLineCount();
                if(isUsed(count.wordCount))
                    count.wordCount = count.wordCount + getWorkCount(line);
                if(isUsed(count.charCount))
                    count.charCount = count.charCount + getCharCount(line);
                if(isUsed(count.codeLineCount) && isUsed(count.blankLineCount)
                        && isUsed(count.commentLineCount)) {
                    count.codeLineCount = count.codeLineCount + addCodeLine(line);
                    count.blankLineCount = count.blankLineCount + addBlankLine(line);
                    count.commentLineCount = count.commentLineCount + addCommentLine(line);
                }
            }
            countCallback.onSuccess(count);
        } catch (IOException e) {
            countCallback.onError("文件读取出错，请重试");
        } finally {
            Util.closeIOs(fileReader, reader);
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
                case Constant.MULTI_FILE_COUNT:
                    count.isFuzzyQuery = true;
                    break;
            }
        }
        return count;
    }

    public boolean isUsed(int arg) {
        return arg != -1;
    }

    public interface Callback<T> {
        void onError(String msg);
        void onSuccess(T count);
    }
}
