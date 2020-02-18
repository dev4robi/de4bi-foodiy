package com.robi.foodiy.aop;

import com.robi.data.ApiResult;
import com.robi.exception.ApiException;

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
        catch (ApiException e) {
            // Rpy exception msg
            logger.error("ApiException in controller!", e);
            apiRst.setResultMsg(e.getMessage());
        }
        catch (Throwable e) {
            // Rpy common msg
            logger.error("Exception in service AOP!", e);
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