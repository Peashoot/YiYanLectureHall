package com.peashoot.blog.controller;

import com.peashoot.blog.aspect.annotation.ErrorRecord;
import com.peashoot.blog.batis.entity.FileDO;
import com.peashoot.blog.batis.enums.FileTypeEnum;
import com.peashoot.blog.batis.enums.VisitActionEnum;
import com.peashoot.blog.batis.service.FileService;
import com.peashoot.blog.batis.service.OperateRecordService;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.exception.FilePathCreateFailureException;
import com.peashoot.blog.util.IoUtils;
import com.peashoot.blog.util.IpUtils;
import com.peashoot.blog.util.StringUtils;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping(path = "file")
@ErrorRecord
public class FileController {
    /**
     * 资源网络路径
     */
    @Value("${peashoot.blog.file.resource.directory.net}")
    private String resourceNetDirectory;
    /**
     * 资源本地路径
     */
    @Value("${peashoot.blog.file.resource.directory.local}")
    private String resourceLocalDirectory;
    /**
     * 允许上传文件的最大大小
     */
    @Value("${peashoot.blog.file.max-file-size}")
    private Integer maxFileSize;

    private final FileService fileService;
    private final OperateRecordService operateRecordService;

    public FileController(FileService fileService, OperateRecordService operateRecordService) {
        this.fileService = fileService;
        this.operateRecordService = operateRecordService;
    }

    /**
     * 上传本地文件到服务器
     *
     * @param visitorId 访客id
     * @param sysUserId 系统管理员id
     * @param type      文件类型
     * @param file      文件信息
     * @return 服务器本地文件名
     */
    @PostMapping("local/upload")
    public ApiResp<String> uploadFileInfo(HttpServletRequest request,
                                          @RequestParam Long visitorId, @RequestParam(required = false) Integer sysUserId,
                                          @RequestParam FileTypeEnum type, @RequestParam MultipartFile file)
            throws IOException, FilePathCreateFailureException {
        ApiResp<String> resp = new ApiResp<>();
        resp.setCode(ApiResp.PROCESS_ERROR);
        resp.setMessage("Failure to upload file.");
        String suffix = IoUtils.getFileSuffix(Objects.requireNonNull(file.getOriginalFilename()));
        if (StringUtils.isNullOrEmpty(suffix) || !IoUtils.isCorrectSuffix(suffix, type)) {
            return resp;
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String localFileName = uuid + "." + suffix;
        String localFilePath = IoUtils.combineLocalFilePath(resourceLocalDirectory, type, localFileName);
        String netFileUrl = IoUtils.combineNetFilePath(resourceNetDirectory, type, localFileName);
        String md5 = IoUtils.saveFileAndGetMd5(file, localFilePath);
        FileDO fileEntity = new FileDO(visitorId, sysUserId, type, localFilePath, netFileUrl, md5);
        fileEntity.setOriginalName(file.getOriginalFilename());
        operateRecordService.insertNewRecordAsync(visitorId, uuid, IpUtils.getIpAddr(request), VisitActionEnum.UPLOAD_FILE, new Date(), "Upload local file to " + localFilePath);
        if (fileService.insert(fileEntity) > 0) {
            resp.success().setData(netFileUrl);
        }
        return resp;
    }

    /**
     * 从网络地址下载至服务器
     *
     * @param visitorId      访客id
     * @param sysUserId      系统管理员id
     * @param originalNetUrl 原始网络路径
     * @param type           文件类型
     * @return 服务器本地文件名
     */
    @PostMapping(path = "net/sync")
    public ApiResp<String> downloadFileFrom(HttpServletRequest request,
                                            @RequestParam Long visitorId, @RequestParam(required = false) Integer sysUserId,
                                            @RequestParam String originalNetUrl, @RequestParam FileTypeEnum type)
            throws IOException, FilePathCreateFailureException {
        ApiResp<String> resp = new ApiResp<>();
        resp.setCode(ApiResp.PROCESS_ERROR);
        resp.setMessage("Failure to download file.");
        String suffix = IoUtils.getFileSuffix(originalNetUrl);
        if (StringUtils.isNullOrEmpty(suffix) || !IoUtils.isCorrectSuffix(suffix, type)) {
            resp.setMessage("Unacceptable suffix");
            return resp;
        }
        String uuid = UUID.randomUUID().toString().replace("-", "");
        String localFileName = uuid + "." + suffix;
        String localFilePath = IoUtils.combineLocalFilePath(resourceLocalDirectory, type, localFileName);
        String netFileUrl = IoUtils.combineNetFilePath(resourceNetDirectory, type, localFileName);
        String md5 = IoUtils.downloadNetFileAndGetMd5(originalNetUrl, localFilePath);
        FileDO fileEntity = new FileDO(visitorId, sysUserId, type, localFilePath, netFileUrl, md5);
        fileEntity.setOriginalNetUrl(originalNetUrl);
        fileEntity.setId(uuid);
        operateRecordService.insertNewRecordAsync(visitorId, uuid, IpUtils.getIpAddr(request), VisitActionEnum.UPLOAD_FILE, new Date(), "Download net file to " + localFilePath);
        if (fileService.insert(fileEntity) > 0) {
            resp.success().setData(netFileUrl);
        }
        return resp;
    }
}
