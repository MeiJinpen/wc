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
        } else if (FileUtil.isValidFileName(args[args.length - 1])) {
            wordCount.count(args[args.length - 1], args);
        } else {
            System.out.println("需要输入正确的文件名");
        }
    }


}
