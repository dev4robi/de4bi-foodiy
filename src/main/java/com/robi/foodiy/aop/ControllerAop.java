package com.robi.foodiy.aop;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.robi.data.ApiResult;
import com.robi.util.RandomUtil;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.AllArgsConstructor;

@Aspect
@Component
@AllArgsConstructor
@SuppressWarnings("unchecked")
public class ControllerAop {

    private final static Logger logger = LoggerFactory.getLogger(ControllerAop.class);

    @Around("execution(* com.robi.foodiy.controller..*.*(..))")
    public Object aroundRestController(ProceedingJoinPoint pjp) {
        // Initialize controller parameters
        final long ctrBgnTimeMs = System.currentTimeMillis();
        final String traceId = RandomUtil.genRandomStr(16, RandomUtil.ALPHABET | RandomUtil.NUMERIC);
        final String oldLayer = MDC.get("layer");

        // Logger init
        MDC.put("tId", traceId);
        MDC.put("layer", "CTR");

        // Logging request
        ServletRequestAttributes servletReqAttr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletReqAttr.getRequest();
        StringBuilder reqSb = new StringBuilder(512);

        String reqInfo = request.getMethod() + " " + request.getRequestURI() + " " + request.getProtocol();
        logger.info("<Req> '{}'", reqInfo);
        logger.info(" -> URL: " + request.getRequestURL().toString());

        Enumeration<String> headerEnum = request.getHeaderNames();
        while (headerEnum.hasMoreElements()) {
            String headerKey = headerEnum.nextElement();
            reqSb.append("'").append(headerKey).append("':'").append(request.getHeader(headerKey)).append("', ");
        }

        if (reqSb.length() > 2) reqSb.setLength(reqSb.length() - 2);
        logger.info(" -> Headers: {" + reqSb.toString() + "}");
        reqSb.setLength(0);

        Enumeration<String> bodyEnum = request.getParameterNames();
        while (bodyEnum.hasMoreElements()) {
            String bodyKey = headerEnum.nextElement();
            reqSb.append("'").append(bodyKey).append("':'").append(request.getParameter(bodyKey)).append("', ");
        }
        
        if (reqSb.length() > 2) reqSb.setLength(reqSb.length() - 2);
        logger.info(" -> Bodies: {" + reqSb.toString() + "}");
        reqSb.setLength(0);
        
        // Controller works
        ApiResult apiRst = ApiResult.make(false);
        apiRst.setTraceId(traceId);

        try {
            Object ctrRtnObj = pjp.proceed();
            
            if (ctrRtnObj instanceof ApiResult) {
                ApiResult ctrRtn = (ApiResult) ctrRtnObj;
                apiRst.setResult(ctrRtn.getResult());
                apiRst.setResultCode(ctrRtn.getResultCode());
                apiRst.setResultMsg(ctrRtn.getResultMsg());
                apiRst.addData(ctrRtn.getData());
            }
            else {
                throw new RuntimeException("Controller must return 'ApiResult' class! (ctrRtnObj: " +
                                            ctrRtnObj.getClass().getName() + ")");
            }
        }
        catch (Throwable e) {
            logger.error("Exception in controller AOP! {}", e);
        }

        // Logging reply
        final long ctrEndTimeMs = System.currentTimeMillis();
        final long timeElapsedMs = ctrEndTimeMs - ctrBgnTimeMs;
        logger.info("<Rpy> '{}' (Time: {}ms)", apiRst.toString(), timeElapsedMs);

        // Logger deinit
        MDC.put("layer", oldLayer);
        MDC.put("tId", "");

        // Return API result
        return apiRst;
    }
}