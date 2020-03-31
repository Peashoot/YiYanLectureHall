package com.peashoot.blog.context.request.visitor;

import com.peashoot.blog.batis.entity.VisitorDO;
import com.peashoot.blog.context.request.BaseApiReq;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import java.util.Date;

@Setter
@Getter
@ToString(callSuper = true, includeFieldNames = false)
public class VisitorVisitInfoDTO extends BaseApiReq {
    /**
     * 根据IP地址解析的地区名称
     */
    private String location;
    /**
     * 操作系统
     */
    private String os;

    /**
     * 创建访问者信息
     * @param visitorName 访问者名称
     * @param firstVisitTimestamp 第一次访问时间戳
     * @return 访问者数据库对象
     */
    public VisitorDO createNewInstance(String visitorName, long firstVisitTimestamp) {
        VisitorDO visitor =  new VisitorDO();
        visitor.setFirstVisitTime(new Date(firstVisitTimestamp));
        visitor.setIp(getVisitorIP());
        visitor.setVisitor(visitorName);
        visitor.setBrowser(getBrowserFingerprint());
        visitor.setLocation(location);
        visitor.setOs(os);
        return visitor;
    }
}
