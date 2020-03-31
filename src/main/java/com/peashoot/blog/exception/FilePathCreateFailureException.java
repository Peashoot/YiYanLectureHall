package com.peashoot.blog.exception;

/**
 * 创建文件路径失败
 *
 * @author peashoot
 */
public class FilePathCreateFailureException extends Exception {
    public FilePathCreateFailureException(String filePath) {
        super("Create file \"" + filePath + "\" failure");
    }
}
