package com.peashoot.blog.util.obs.impl;

import com.peashoot.blog.batis.enums.FileTypeEnum;
import com.peashoot.blog.util.obs.ObjectBucketService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

public class AliYunObjectBucketService implements ObjectBucketService {
    /**
     * 访问AccessKey
     */
    @Value("${peashoot.blog.obs.aliyun.access-key}")
    private String accessKey;
    /**
     * 密钥SecretKey
     */
    @Value("${peashoot.blog.obs.aliyun.secret-key}")
    private String secretKey;

    /**
     * 上传回调地址
     */
    @Value("${peashoot.blog.obs.aliyun.callback-url}")
    private String callbackUrl;

    /**
     * 回调返回的host
     */
    @Value("${peashoot.blog.obs.aliyun.callback-host}")
    private String callbackHost;

    /**
     * 桶根目录
     */
    @Value("${peashoot.blog.obs.aliyun.bucket-name}")
    private String bucketRootPath;

    @Override
    public boolean postObjectFromLocal(String localFile, FileTypeEnum fileType, String bucketPath, String objectName) {
        return false;
    }

    @Override
    public boolean postObjectFromRequest(MultipartFile file, FileTypeEnum fileType, String bucketPath, String objectName) {
        return false;
    }

    @Override
    public boolean postObjectFromNet(String fileUrl, FileTypeEnum fileType, String bucketPath, String objectName) {
        return false;
    }
}
