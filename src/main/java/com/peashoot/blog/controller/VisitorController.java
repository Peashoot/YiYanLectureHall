package com.peashoot.blog.controller;

import com.peashoot.blog.batis.entity.VisitorDO;
import com.peashoot.blog.batis.mapper.VisitorMapper;
import com.peashoot.blog.context.request.visitor.UserNameAndVisitorDTO;
import com.peashoot.blog.context.request.visitor.VisitorVisitInfoDTO;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.context.response.visitor.VisitorIDAndNamesDTO;
import com.peashoot.blog.util.EncryptUtils;
import com.peashoot.blog.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
public class VisitorController {

    @Autowired
    private VisitorMapper visitorMapper;

    @PostMapping(path = "generate")
    @ApiOperation("生成访问者名称")
    public ApiResp<VisitorIDAndNamesDTO> generateVisitorName(@RequestBody VisitorVisitInfoDTO apiReq) {
        ApiResp<VisitorIDAndNamesDTO> resp = new ApiResp<>();
        resp.setCode(501);
        resp.setMessage("Failure to generate name of visitor.");
        VisitorDO existVisitor = visitorMapper.selectByIPAndBrowser(apiReq.getVisitorIP(), apiReq.getBrowserFingerprint());
        if (existVisitor != null) {
            VisitorIDAndNamesDTO visitorIDAndNames = new VisitorIDAndNamesDTO();
            visitorIDAndNames.setSysUserNickname(existVisitor.getSysUserNickName());
            visitorIDAndNames.setVisitorId(existVisitor.getId());
            visitorIDAndNames.setVisitorName(existVisitor.getVisitor());
            resp.success().setData(visitorIDAndNames);
        } else {
            try {
                String mark = "address:" + apiReq.getVisitorIP() + ";fingerprint:" + apiReq.getBrowserFingerprint() + ";firstVisitTime:" + System.currentTimeMillis();
                String tempVisitorName = StringUtils.Empty;
                boolean isExist = true;
                int tryCount = 0;
                while (isExist && tryCount++ < 3) {
                    tempVisitorName = EncryptUtils.md5EncryptLen16(mark + ";retryTimes:" + tryCount);
                    isExist = visitorMapper.selectByVisitorName(tempVisitorName) != null;
                }
                if (!isExist) {
                    VisitorDO visitor = apiReq.createNewInstance(tempVisitorName, System.currentTimeMillis());
                    long insertId =  visitorMapper.insertWithReturnRecordID(visitor);
                    if (insertId > 0) {
                        VisitorIDAndNamesDTO visitorIDAndNames = new VisitorIDAndNamesDTO();
                        visitorIDAndNames.setVisitorId(visitor.getId());
                        visitorIDAndNames.setVisitorName(visitor.getVisitor());
                        resp.success().setData(visitorIDAndNames);
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
    public ApiResp<Boolean> syncUserInfoToVisitor(@RequestBody UserNameAndVisitorDTO apiReq) {
        VisitorDO visitor = visitorMapper.selectByVisitorName(apiReq.getVisitorName());
        ApiResp<Boolean> resp = new ApiResp<>();
        resp.setCode(501);
        resp.setMessage("Failure to sync user's info to visitor.");
        if (visitor == null) {
            return resp;
        }
        if (apiReq.getSysUsername().equals(visitor.getSysUserName())) {
            resp.success().setData(true);
            return resp;
        }
        visitor.setSysUserName(apiReq.getSysUsername());
        resp.success().setData(visitorMapper.updateByPrimaryKey(visitor) > 0);
        return resp;
    }
}
