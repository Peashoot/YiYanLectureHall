package com.peashoot.blog.aspect;

import com.peashoot.blog.aspect.annotation.ErrorRecord;
import com.peashoot.blog.batis.entity.ExceptionRecordDO;
import com.peashoot.blog.batis.service.ExceptionRecordService;
import com.peashoot.blog.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Date;

@Aspect
@Component
@Slf4j
public class ErrorRecordAspect {
    /**
     * 注入Service用于把日志保存数据库，实际项目入库采用队列做异步
     */
    @Autowired
    private ExceptionRecordService exceptionRecordService;

    /**
     * Controller层切点
     */
    @Pointcut("@within(com.peashoot.blog.aspect.annotation.ErrorRecord)")
    public void handlerAspect() {
    }

    /**
     * 异常记录保存
     */
    @AfterThrowing(pointcut = "handlerAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        try {
            // TODO 异常通知处理
            ExceptionRecordDO exceptionRecord = new ExceptionRecordDO();
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(className);
            Method[] methods = targetClass.getMethods();
            StringBuffer paramWithValues = new StringBuffer();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Parameter[] params = method.getParameters();
                    if (params.length != arguments.length) {
                        continue;
                    }
                    for (int i = 0; i < params.length; i++) {
                        paramWithValues.append(StringUtils.getSpecialEscape(params[i].getName())).append(":")
                                .append(arguments[i] == null ? "null" : StringUtils.getSpecialEscape(arguments[i].toString()));
                        if (i < params.length - 1) {
                            paramWithValues.append(';');
                        }
                    }
                    break;
                }
            }
            exceptionRecord.setClassName(className);
            exceptionRecord.setMethodName(methodName);
            exceptionRecord.setParamValues(paramWithValues.toString());
            exceptionRecord.setExceptionMsg(e.getMessage());
            exceptionRecord.setInvokeStack(StringUtils.getStackTraceString(e));
            exceptionRecord.setAppearDate(new Date());
            exceptionRecordService.insertNewRecordAsync(exceptionRecord);
        } catch (Exception ex) {
            //记录本地异常日志
            log.error("==异常通知异常==");
            log.error("异常信息:{}", ex.getMessage());
        }
    }
}
