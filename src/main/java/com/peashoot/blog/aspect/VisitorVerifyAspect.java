package com.peashoot.blog.aspect;

import com.peashoot.blog.aspect.annotation.VisitTimesLimit;
import com.peashoot.blog.aspect.annotation.VisitorVerify;
import com.peashoot.blog.batis.entity.VisitorDO;
import com.peashoot.blog.batis.service.VisitorService;
import com.peashoot.blog.context.request.BaseApiReq;
import com.peashoot.blog.context.request.BaseApiReqWithVisitor;
import com.peashoot.blog.context.response.ApiResp;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class VisitorVerifyAspect {
    /**
     * 访客信息操作类
     */
    private final VisitorService visitorService;

    public VisitorVerifyAspect(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    /**
     * 访问限制方法切面
     */
    @Pointcut("@annotation(com.peashoot.blog.aspect.annotation.VisitorVerify)")
    public void verifyVisitorMethodHandlerAspect() {
    }

    @Around("verifyVisitorMethodHandlerAspect()")
    public Object verifyVisitorBeforeRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Class typeOfClass = joinPoint.getTarget().getClass();
        Method currentMethod = null;
        for (Method method : typeOfClass.getMethods()) {
            if (method.getName().equals(methodName)) {
                currentMethod = method;
            }
        }
        // 如果返回参数为空，或者返回参数不继承ApiResp，或者请求参数小于1，或者第一个请求参数不继承BaseApiReq，不做处理
        if (currentMethod == null || currentMethod.getReturnType() == null ||
                !ApiResp.class.isAssignableFrom(currentMethod.getReturnType()) ||
                currentMethod.getParameterTypes().length < 1 ||
                !BaseApiReqWithVisitor.class.isAssignableFrom(currentMethod.getParameterTypes()[0])) {
            return joinPoint.proceed();
        }
        VisitorVerify annotation = currentMethod.getDeclaredAnnotation(VisitorVerify.class);
        BaseApiReqWithVisitor apiReq = (BaseApiReqWithVisitor) joinPoint.getArgs()[0];
        VisitorDO visitorDO = visitorService.selectByVisitorName(apiReq.getVisitor());
        if (visitorDO == null) {
            ApiResp resp = (ApiResp) currentMethod.getReturnType().newInstance();
            resp.setCode(ApiResp.NO_VISITOR_MATCH);
            resp.setMessage("No matched visitor");
            return resp;
        }
        apiReq.setVisitorDO(visitorDO);
        return joinPoint.proceed();
    }
}
