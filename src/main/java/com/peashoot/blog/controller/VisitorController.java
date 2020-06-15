package com.peashoot.blog.controller;

import com.peashoot.blog.aspect.annotation.ErrorRecord;
import com.peashoot.blog.aspect.annotation.VisitTimesLimit;
import com.peashoot.blog.batis.enums.VisitActionEnum;
import com.peashoot.blog.batis.entity.VisitorDO;
import com.peashoot.blog.batis.service.OperateRecordService;
import com.peashoot.blog.batis.service.VisitorService;
import com.peashoot.blog.context.request.visitor.ChangeVisitorNameDTO;
import com.peashoot.blog.context.request.visitor.UserNameAndVisitorDTO;
import com.peashoot.blog.context.request.visitor.VisitorVisitInfoDTO;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.context.response.visitor.VisitorIDAndNamesDTO;
import com.peashoot.blog.util.EncryptUtils;
import com.peashoot.blog.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Objects;

/**
 * 访客管理相关接口
 *
 * @author peashoot
 */
@Slf4j
@RestController
@Api(tags = "访客管理相关接口")
@RequestMapping(path = "visitor")
@ErrorRecord
public class VisitorController {

    /**
     * 访客操作类
     */
    private final VisitorService visitorService;
    /**
     * 访客操作记录操作类
     */
    private final OperateRecordService visitRecordService;

    public VisitorController(VisitorService visitorService, OperateRecordService visitRecordService) {
        this.visitorService = visitorService;
        this.visitRecordService = visitRecordService;
    }

    /**
     * 查询或生成访客名称
     *
     * @param apiReq 访问IP和浏览器指纹
     * @return 访客名称和绑定的用户昵称
     */
    @PostMapping(path = "generate")
    @ApiOperation("生成访问者名称")
    public ApiResp<VisitorIDAndNamesDTO> generateVisitorName(@RequestBody @Validated VisitorVisitInfoDTO apiReq) {
        ApiResp<VisitorIDAndNamesDTO> resp = new ApiResp<>();
        resp.setCode(ApiResp.PROCESS_ERROR);
        resp.setMessage("Failure to generate name of visitor.");
        VisitorDO existVisitor = visitorService.selectByIpAndBrowser(apiReq.getVisitorIp(), apiReq.getBrowserFingerprint());
        if (existVisitor != null) {
            VisitorIDAndNamesDTO visitorIdAndNames = new VisitorIDAndNamesDTO();
            visitorIdAndNames.setSysUserNickname(existVisitor.getSysUserNickName());
            visitorIdAndNames.setVisitorId(existVisitor.getId());
            visitorIdAndNames.setVisitorName(existVisitor.getVisitor());
            resp.success().setData(visitorIdAndNames);
        } else {
            try {
                String mark = "address:" + apiReq.getVisitorIp() + ";fingerprint:" + apiReq.getBrowserFingerprint() + ";firstVisitTime:" + System.currentTimeMillis();
                String tempVisitorName = StringUtils.EMPTY;
                boolean isExist = true;
                int tryCount = 0;
                while (isExist && tryCount++ < 3) {
                    tempVisitorName = EncryptUtils.md5EncryptLen16(mark + ";retryTimes:" + tryCount);
                    isExist = visitorService.selectByVisitorName(tempVisitorName) != null;
                }
                if (!isExist) {
                    VisitorDO visitor = apiReq.createNewInstance(tempVisitorName, new Date());
                    long insertId = visitorService.insertWithReturnRecordId(visitor);
                    visitRecordService.insertNewRecordAsync(insertId, String.valueOf(insertId), apiReq.getVisitorIp(),
                            VisitActionEnum.VISITOR_GENERATE, new Date(), "Create an visitor " + tempVisitorName);
                    if (insertId > 0) {
                        VisitorIDAndNamesDTO visitorIdAndNames = new VisitorIDAndNamesDTO();
                        visitorIdAndNames.setVisitorId(visitor.getId());
                        visitorIdAndNames.setVisitorName(visitor.getVisitor());
                        resp.success().setData(visitorIdAndNames);
                    }
                }
            } catch (Exception ex) {
                log.error("An exception appeared while generating name of visitor.", ex);
                return resp;
            }
        }
        return resp;
    }

    /**
     * 同步用户信息到访客信息
     *
     * @param apiReq 用户信息和访客信息
     * @return 是否成功绑定
     */
    @PostMapping(path = "bind")
    @ApiOperation("将访客信息和用户信息进行绑定")
    @VisitTimesLimit(value = 10)
    public ApiResp<Boolean> syncUserInfoToVisitor(@RequestBody @Validated UserNameAndVisitorDTO apiReq) {
        VisitorDO visitor = visitorService.selectByVisitorName(apiReq.getVisitorName());
        ApiResp<Boolean> resp = new ApiResp<>();
        resp.setCode(ApiResp.PROCESS_ERROR);
        resp.setMessage("Failure to sync user's info to visitor.");
        if (visitor == null) {
            return resp;
        }
        if (apiReq.getSysUsername().equals(visitor.getSysUserName())) {
            resp.success().setData(true);
            return resp;
        }
        String record = "Visitor " + apiReq.getVisitorName() + " connect with system user " + apiReq.getSysUsername() + ".";
        visitRecordService.insertNewRecordAsync(visitor.getId(), visitor.getId().toString(), apiReq.getVisitorIp(), VisitActionEnum.BIND_SYS_USER, new Date(), record);
        visitor.setSysUserName(apiReq.getSysUsername());
        resp.success().setData(visitorService.update(visitor) > 0);
        return resp;
    }

    /**
     * 更改访客名称
     *
     * @param apiReq 访客id和新的访客名称
     * @return 返回是否成功
     */
    @RequestMapping(path = "change")
    @ApiOperation("修改访客名称")
    public ApiResp<Boolean> changeVisitorName(@RequestBody @Validated ChangeVisitorNameDTO apiReq) {
        ApiResp<Boolean> resp = new ApiResp<>();
        resp.setCode(ApiResp.PROCESS_ERROR);
        resp.setMessage("Failure to change name of  visitor.");
        VisitorDO existVisitor = visitorService.selectByVisitorName(apiReq.getOldVisitorName());
        if (existVisitor == null) {
            resp.setCode(ApiResp.NO_RECORD_MATCH);
            resp.setMessage("No match record");
            return resp;
        }
        if (existVisitor.getVisitor().equals(apiReq.getNewVisitorName())) {
            resp.success().setData(true);
        } else {
            VisitorDO repeatedVisitor = visitorService.selectByVisitorName(apiReq.getNewVisitorName());
            if (repeatedVisitor != null && !Objects.equals(repeatedVisitor.getId(), existVisitor.getId())) {
                resp.setCode(ApiResp.BAD_REQUEST);
                resp.setMessage("Repeated name.");
                return resp;
            }
            // 新增访客操作记录,并修改访客名称
            String record = "Visitor change name from \"" + apiReq.getOldVisitorName() + "\" to \"" + apiReq.getNewVisitorName() + "\".";
            visitRecordService.insertNewRecordAsync(existVisitor.getId(), existVisitor.getId().toString(), apiReq.getVisitorIp(), VisitActionEnum.CHANGE_NAME, new Date(), record);
            existVisitor.setVisitor(apiReq.getNewVisitorName());
            if (visitorService.update(existVisitor) > 0) {
                resp.success().setData(true);
            }
        }
        return resp;
    }
}
