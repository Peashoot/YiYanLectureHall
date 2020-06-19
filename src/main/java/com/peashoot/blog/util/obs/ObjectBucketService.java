package com.peashoot.blog.util.obs;

import com.peashoot.blog.batis.enums.FileTypeEnum;
import org.springframework.web.multipart.MultipartFile;

public interface ObjectBucketService {
    /**
     * 将本地文件上传到OBS服务中
     *
     * @param localFile  文件文件路径（全局）
     * @param fileType   文件类型
     * @param bucketPath 桶中相对路径
     * @param objectName 对象名称
     * @return 是否上传成功
     */
    boolean postObjectFromLocal(String localFile, FileTypeEnum fileType, String bucketPath, String objectName);

    /**
     * 将form-data中请求的文件信息转存到OBS服务中
     *
     * @param file       form-data上传文件信息
     * @param fileType   文件类型
     * @param bucketPath 桶相对路径
     * @param objectName 对象名称
     * @return 是否上传成功
     */
    boolean postObjectFromRequest(MultipartFile file, FileTypeEnum fileType, String bucketPath, String objectName);

    /**
     * 将网络地址映射的文件转存到OBS服务中
     *
     * @param fileUrl    文件url
     * @param fileType   文件类型
     * @param bucketPath 桶内相对路径
     * @param objectName 对象名称
     * @return 是否上传成功
     */
    boolean postObjectFromNet(String fileUrl, FileTypeEnum fileType, String bucketPath, String objectName);
}
