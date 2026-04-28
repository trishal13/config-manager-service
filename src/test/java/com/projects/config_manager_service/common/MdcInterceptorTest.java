package com.projects.config_manager_service.common;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.UUID;

import static com.projects.config_manager_service.common.Constants.ANONYMOUS;
import static com.projects.config_manager_service.common.Constants.TRACE_ID_HEADER;
import static com.projects.config_manager_service.common.Constants.TRACE_ID_MDC_KEY;
import static com.projects.config_manager_service.common.Constants.USER_UUID_HEADER;
import static com.projects.config_manager_service.common.Constants.USER_UUID_MDC_KEY;
import static com.projects.config_manager_service.common.Constants.UUID_PATTERN;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MdcInterceptorTest {

    private final MdcInterceptor interceptor = new MdcInterceptor();

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void preHandle_whenHeadersValid_shouldKeepGivenValues() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String traceId = "123e4567-e89b-12d3-a456-426614174000";
        String userUuid = "user:123e4567-e89b-12d3-a456-426614174000";

        request.addHeader(TRACE_ID_HEADER, traceId);
        request.addHeader(USER_UUID_HEADER, userUuid);

        boolean handled = interceptor.preHandle(request, response, new Object());

        assertTrue(handled);
        assertEquals(traceId, MDC.get(TRACE_ID_MDC_KEY));
        assertEquals(userUuid, MDC.get(USER_UUID_MDC_KEY));
        assertEquals(traceId, response.getHeader(TRACE_ID_HEADER));
    }

    @Test
    void preHandle_whenTraceIdMissing_shouldGenerateAndSetAnonymousUser() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        interceptor.preHandle(request, response, new Object());

        String responseTraceId = response.getHeader(TRACE_ID_HEADER);
        assertNotNull(responseTraceId);
        assertTrue(UUID_PATTERN.matcher(responseTraceId).matches());
        assertEquals(responseTraceId, MDC.get(TRACE_ID_MDC_KEY));
        assertEquals(ANONYMOUS, MDC.get(USER_UUID_MDC_KEY));
    }

    @Test
    void preHandle_whenTraceIdInvalid_shouldRegenerateTraceId() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader(TRACE_ID_HEADER, "not-a-uuid");
        request.addHeader(USER_UUID_HEADER, "user:123e4567-e89b-12d3-a456-426614174000");

        interceptor.preHandle(request, response, new Object());

        String responseTraceId = response.getHeader(TRACE_ID_HEADER);
        assertNotNull(responseTraceId);
        assertTrue(UUID_PATTERN.matcher(responseTraceId).matches());
        assertEquals(responseTraceId, MDC.get(TRACE_ID_MDC_KEY));
    }

    @Test
    void preHandle_whenUserUuidInvalid_shouldFallbackToAnonymous() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        String traceId = UUID.randomUUID().toString();

        request.addHeader(TRACE_ID_HEADER, traceId);
        request.addHeader(USER_UUID_HEADER, "user:invalid-uuid");

        interceptor.preHandle(request, response, new Object());

        assertEquals(traceId, MDC.get(TRACE_ID_MDC_KEY));
        assertEquals(ANONYMOUS, MDC.get(USER_UUID_MDC_KEY));
        assertEquals(traceId, response.getHeader(TRACE_ID_HEADER));
    }

    @Test
    void afterCompletion_shouldClearMdc() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        HttpServletRequest servletRequest = request;

        MDC.put(TRACE_ID_MDC_KEY, "some-trace-id");
        MDC.put(USER_UUID_MDC_KEY, "some-user-id");

        interceptor.afterCompletion(servletRequest, response, new Object(), null);

        assertTrue(MDC.getCopyOfContextMap() == null || MDC.getCopyOfContextMap().isEmpty());
    }
}
