package com.peashoot.blog.aspect;

import com.peashoot.blog.aspect.annotation.VisitLimit;
import com.peashoot.blog.context.request.BaseApiReq;
import com.peashoot.blog.context.response.ApiResp;
import com.peashoot.blog.redis.service.VisitLimitRedisService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class VisitLimitAspect {
    /**
     * 访问限制Redis操作服务
     */
    private VisitLimitRedisService visitLimitRedisService;

    public VisitLimitAspect(VisitLimitRedisService visitLimitRedisService) {
        this.visitLimitRedisService = visitLimitRedisService;
    }

    /**
     * 访问限制方法切面
     */
    @Pointcut("@annotation(com.peashoot.blog.aspect.annotation.VisitLimit)")
    public void visitLimitMethodHandlerAspect() {
    }

    @Around("visitLimitMethodHandlerAspect()")
    public Object judgeVisitLimitBeforeRequest(ProceedingJoinPoint joinPoint) throws Throwable {
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
                !BaseApiReq.class.isAssignableFrom(currentMethod.getParameterTypes()[0])) {
            return joinPoint.proceed();
        }
        VisitLimit annotation = currentMethod.getDeclaredAnnotation(VisitLimit.class);
        BaseApiReq apiReq = (BaseApiReq) joinPoint.getArgs()[0];

        if (visitLimitRedisService.isAllowVisit(apiReq.getVisitorIP(), apiReq.getBrowserFingerprint(), new Date(), annotation)) {
            return joinPoint.proceed();
        }
        ApiResp<String> resp = new ApiResp<>();
        resp.setCode(HttpStatus.METHOD_NOT_ALLOWED.value());
        resp.setMessage("Server refused to respond");
        resp.setData("Access restricted");
        return resp;
    }
}
