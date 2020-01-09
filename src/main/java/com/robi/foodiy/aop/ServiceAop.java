package com.robi.foodiy.aop;

import com.robi.data.ApiResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

@Aspect
@Component
@AllArgsConstructor
@SuppressWarnings("unchecked")
public class ServiceAop {

    private final static Logger logger = LoggerFactory.getLogger(ControllerAop.class);

    @Around("execution(* com.robi.foodiy.service..*.*(..))")
    public Object aroundRestService(ProceedingJoinPoint pjp) {
        // Initialize controller parameters
        final long svcBgnTimeMs = System.currentTimeMillis();
        final String oldLayer = MDC.get("layer");

        // Logger init
        MDC.put("layer", "SVC");
        final Signature sign = pjp.getSignature();
        logger.info("<<SvcBgn>> '{}.{}()'", sign.getDeclaringTypeName(), sign.getName());

        // Service works
        ApiResult apiRst = ApiResult.make(false);

        try {
            Object svcRtnObj = pjp.proceed();
            
            if (svcRtnObj instanceof ApiResult) {
                ApiResult svcRtn = (ApiResult) svcRtnObj;
                apiRst.setResult(svcRtn.getResult());
                apiRst.setResultCode(svcRtn.getResultCode());
                apiRst.setResultMsg(svcRtn.getResultMsg());
                apiRst.addData(svcRtn.getData());
            }
            else {
                throw new RuntimeException("Service must return 'ApiResult' class! (svcRtnObj: " +
                                            svcRtnObj.getClass().getName() + ")");
            }
        }
        catch (Throwable e) {
            logger.error("Exception in service AOP! {}", e);
            apiRst.setResultMsg("SVC_INTERNAL_ERROR");
        }

        // Logging reply
        final long svcEndTimeMs = System.currentTimeMillis();
        final long timeElapsedMs = svcEndTimeMs - svcBgnTimeMs;
        logger.info("<<SvcEnd>> (SvcTime: {}ms)", timeElapsedMs);

        // Logger deinit
        MDC.put("layer", oldLayer);

        // Return API result
        return apiRst;
    }
}