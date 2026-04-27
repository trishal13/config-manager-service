package com.projects.config_manager_service.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

import static com.projects.config_manager_service.common.constants.TRACE_ID_HEADER;
import static com.projects.config_manager_service.common.constants.USER_UUID_HEADER;
import static com.projects.config_manager_service.common.constants.TRACE_ID_MDC_KEY;
import static com.projects.config_manager_service.common.constants.USER_UUID_MDC_KEY;
import static com.projects.config_manager_service.common.constants.ANONYMOUS;

@Slf4j
@Component
public class MdcInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {

        // Pull ONLY the two allowed headers — all others are intentionally ignored
        String traceId  = request.getHeader(TRACE_ID_HEADER);
        String userUuid = request.getHeader(USER_UUID_HEADER);

        // Auto-generate trace-id if caller didn't send one
        if (!StringUtils.hasText(traceId)) {
            traceId = UUID.randomUUID().toString();
            log.debug("No X-Trace-Id received, generated: {}", traceId);
        }

        MDC.put(TRACE_ID_MDC_KEY, traceId);

        if (StringUtils.hasText(userUuid)) {
            MDC.put(USER_UUID_MDC_KEY, userUuid);
        } else {
            MDC.put(USER_UUID_MDC_KEY, ANONYMOUS);
        }

        // Echo trace-id back so caller can correlate
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