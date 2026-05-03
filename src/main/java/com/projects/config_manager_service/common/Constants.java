package com.projects.config_manager_service.common;

import java.util.regex.Pattern;

public class Constants {

    public static final String TRACE_ID_HEADER  = "X-Trace-Id";
    public static final String USER_UUID_HEADER = "X-User-Uuid";
    public static final String TRACE_ID_MDC_KEY  = "traceId";
    public static final String USER_UUID_MDC_KEY = "userUuid";
    public static final String ANONYMOUS = "anonymous";

    public static final Pattern UUID_PATTERN = Pattern.compile(
            "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$"
    );
    public static final Pattern USER_UUID_PATTERN = Pattern.compile(
            "^user:[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$"
    );

    public static final String CONFIG_IDENTIFIER = "config_identifier";
    public static final String CONFIG_TYPE = "config_type";
    public static final String CONFIG_DATA = "config_data";
    public static final String CREATED_BY = "created_by";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_BY = "updated_by";
    public static final String UPDATED_AT = "updated_at";

}
