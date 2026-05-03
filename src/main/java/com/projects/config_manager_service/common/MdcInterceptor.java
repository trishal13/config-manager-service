package com.projects.config_manager_service.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

import static com.projects.config_manager_service.common.Constants.TRACE_ID_HEADER;
import static com.projects.config_manager_service.common.Constants.USER_UUID_HEADER;
import static com.projects.config_manager_service.common.Constants.TRACE_ID_MDC_KEY;
import static com.projects.config_manager_service.common.Constants.USER_UUID_MDC_KEY;
import static com.projects.config_manager_service.common.Constants.ANONYMOUS;
import static com.projects.config_manager_service.common.Constants.UUID_PATTERN;
import static com.projects.config_manager_service.common.Constants.USER_UUID_PATTERN;

@Slf4j
@Component
public class MdcInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        String traceId  = request.getHeader(TRACE_ID_HEADER);
        String userUuid = request.getHeader(USER_UUID_HEADER);

        // Moved to DEBUG to avoid leaking user identifiers in production logs
        log.debug("[MdcInterceptor.preHandle] Received {}: {} and {}: {}", TRACE_ID_HEADER, traceId, USER_UUID_HEADER, userUuid);

        boolean isTraceIdValid   = !ObjectUtils.isEmpty(traceId)  && UUID_PATTERN.matcher(traceId).matches();
        boolean isUserUuidValid  = !ObjectUtils.isEmpty(userUuid) && USER_UUID_PATTERN.matcher(userUuid).matches();

        if (!StringUtils.hasText(traceId) || !isTraceIdValid) {
            traceId = UUID.randomUUID().toString();
            log.debug("[MdcInterceptor.preHandle] Missing or invalid {}, generated: {}", TRACE_ID_HEADER, traceId);
        }

        MDC.put(TRACE_ID_MDC_KEY, traceId);

        if (StringUtils.hasText(userUuid) && isUserUuidValid) {
            MDC.put(USER_UUID_MDC_KEY, userUuid);
        } else {
            MDC.put(USER_UUID_MDC_KEY, ANONYMOUS);
        }

        response.setHeader(TRACE_ID_HEADER, traceId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                Exception ex) {
        // CRITICAL: always clear MDC to avoid leaking values across threads (thread pool reuse)
        MDC.clear();
    }
}