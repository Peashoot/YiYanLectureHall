package com.peashoot.blog.util;

import com.peashoot.blog.batis.entity.FileTypeEnum;
import com.peashoot.blog.exception.FilePathCreateFailureException;
import org.apache.commons.codec.binary.Hex;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.io.File.separator;

/**
 * 文件读写扩展类
 *
 * @author peashoot
 */
public class IoUtils {
    /**
     * 从数据流中读取指定编码方式的字符串
     *
     * @param inputStream 数据流
     * @param charset     编码方式
     * @return 读取的字符串
     * @throws IOException 读取异常
     */
    public static String read(InputStream inputStream, String charset) throws IOException {
        int capacity = 1024;
        byte[] buffer = new byte[capacity];
        int readSize = -1;
        StringBuilder strd = new StringBuilder();
        while ((readSize = inputStream.read(buffer, 0, capacity)) > 0) {
            strd.append(StringUtils.convertByteArrayToString(buffer, readSize, charset));
        }
        return strd.toString();
    }

    /**
     * 获取文件后缀路径
     *
     * @param originalFileNameOrPath 原始文件名称或路径
     * @return 文件后缀
     */
    public static String getFileSuffix(@NotNull String originalFileNameOrPath) {
        int lastIndex = originalFileNameOrPath.lastIndexOf('.');
        if (lastIndex == -1 || lastIndex == originalFileNameOrPath.length() - 1) {
            return "";
        }
        return originalFileNameOrPath.substring(lastIndex + 1);
    }

    /**
     * 判断文件后缀是否正确
     *
     * @param suffix   后缀
     * @param fileType 文件类型
     * @return 是否正确
     */
    public static boolean isCorrectSuffix(@NotNull String suffix, FileTypeEnum fileType) {
        suffix = suffix.toLowerCase();
        switch (fileType) {
            case PICTURE:
                return "jpg".equals(suffix) || "tif".equals(suffix) || "png".equals(suffix) || "bmp".equals(suffix) || "svg".equals(suffix);
            case TEXT_FILE:
                return "txt".equals(suffix) || "xml".equals(suffix);
            case MARKDOWN:
                return "md".equals(suffix);
            default:
                return false;
        }
    }

    /**
     * 将文件保存到本地并计算md5
     *
     * @param file     文件
     * @param savePath 保存路径
     * @return MD5
     * @throws IOException                    读写异常
     * @throws FilePathCreateFailureException 创建文件路径失败
     */
    public static String saveFileAndGetMd5(MultipartFile file, String savePath) throws IOException, FilePathCreateFailureException {
        InputStream inputStream = file.getInputStream();
        return readFromStreamAndGetMd5(inputStream, savePath);
    }

    /**
     * 根据文件网络路径获取文件保存到本地并计算md5签名
     *
     * @param urlString url
     * @param savePath  本地保存路径
     * @return MD5
     * @throws IOException                    读写异常
     * @throws FilePathCreateFailureException 创建文件路径失败
     */
    public static String downloadNetFileAndGetMd5(String urlString, String savePath) throws IOException, FilePathCreateFailureException {
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        try (InputStream inputStream = conn.getInputStream()) {
            return readFromStreamAndGetMd5(inputStream, savePath);
        }
    }

    /**
     * 从数据流中读取文件保存到本地并计算md5签名
     *
     * @param inputStream 数据流
     * @param savePath    本地保存路径
     * @return MD5
     * @throws IOException                    读写异常
     * @throws FilePathCreateFailureException 创建文件路径失败
     */
    private static String readFromStreamAndGetMd5(InputStream inputStream, String savePath) throws IOException, FilePathCreateFailureException {
        File localFile = new File(savePath);
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (!localFile.exists()) {
            if (!localFile.exists() && !localFile.mkdirs()) {
                throw new FilePathCreateFailureException(savePath);
            }
        }
        try (FileOutputStream outputStream = new FileOutputStream(localFile)) {
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            return new String(Hex.encodeHex(md5.digest()));
        }
    }

    /**
     * 拼接本地文件路径
     *
     * @param folderPath 根文件夹路径
     * @param fileType   文件类型
     * @param fileName   文件名称
     * @return 完整的文件路径
     */
    public static String combineLocalFilePath(String folderPath, FileTypeEnum fileType, String fileName) {
        if (folderPath.endsWith(File.separator)) {
            return folderPath + fileType.getValue() + File.separator + fileName;
        } else {
            return folderPath + File.separator + fileType.getValue() + File.separator + fileName;
        }
    }



    /**
     * 拼接URL路径
     * @param urlPrefix 网络路径前缀
     * @param fileType 文件类型
     * @param fileName 文件名称
     * @return 拼接后的URL
     */
    public static String combineNetFilePath(String urlPrefix, FileTypeEnum fileType, String fileName) {
        if (urlPrefix.endsWith("/")) {
            return urlPrefix + fileType.getValue() + "/" + fileName;
        } else {
            return urlPrefix + "/" + fileType.getValue() + "/" + fileName;
        }
    }
}
