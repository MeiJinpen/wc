package com.meijinpeng;

/**
 * 记录字符数、单词数、行数、代码行、空行、注释行的数量
 */
public class Count {

    int lineCount;
    int wordCount;
    int charCount;

    int codeLineCount;
    int blankLineCount;
    int commentLineCount;

    /**
     * 默认都为-1，当参数选择计算哪个时再置0
     */
    public Count() {
        lineCount = -1;
        wordCount = -1;
        charCount = -1;
        codeLineCount = -1;
        blankLineCount = -1;
        commentLineCount = -1;
    }
}
