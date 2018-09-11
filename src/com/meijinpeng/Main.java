package com.meijinpeng;

public class Main {

    /**
     * 程序入口
     * @param args 第一参数：{-c, -w, -l, -s, -a}；第二参数：[fileName]
     */
    public static void main(String[] args) {
        WordCount wordCount = new WordCount();
        if (args == null || args.length == 0) {
            System.out.println("需要参数{-c, -w, -l, -s, -a} [fileName]，请重新输入");
        } else if (Util.isValidFileName(args[args.length - 1])) {
            wordCount.count(args[args.length - 1], new WordCount.Callback<Count>() {
                @Override
                public void onError(String msg) {
                    System.out.println(msg);
                }

                @Override
                public void onSuccess(Count count) {
                    System.out.println("文件名为： " + args[args.length - 1]);
                    if(wordCount.isUsed(count.charCount)) System.out.println("字符总数为：" + count.charCount);
                    if(wordCount.isUsed(count.wordCount)) System.out.println("单词总数为：" + count.wordCount);
                    if(wordCount.isUsed(count.lineCount)) System.out.println("行总数为：" + count.lineCount);
                    if(wordCount.isUsed(count.codeLineCount) && wordCount.isUsed(count.blankLineCount)
                            && wordCount.isUsed(count.commentLineCount)) {
                        System.out.println("代码行总数为：" + count.codeLineCount);
                        System.out.println("空白行总数为：" + count.blankLineCount);
                        System.out.println("注释行总数为：" + count.commentLineCount);
                    }
                }
            }, args);
        } else {
            System.out.println("需要输入正确的文件名");
        }
    }


}
