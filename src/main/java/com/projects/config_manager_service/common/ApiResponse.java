package com.projects.config_manager_service.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.projects.config_manager_service.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private Metadata metadata;
    private boolean success;
    private List<ErrorDetail> errors;
    private String message;
    private List<T> data;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metadata{

        private Integer page;
        private Integer total;
        private Integer next;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorDetail{

        private String errorCode;
        private String errorMessage;
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(List.of(data))
                .build();
    }

    public static <T> ApiResponse<T> successList(List<T> data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> ApiResponse<T> failure(ErrorCode code) {
        return failure(code, code.getMessage());
    }

    public static <T> ApiResponse<T> failure(ErrorCode code, String message) {
        ErrorDetail error = ErrorDetail.builder()
                .errorCode(code.getCode())
                .errorMessage(!ObjectUtils.isEmpty(message) ? message : code.getMessage())
                .build();

        return ApiResponse.<T>builder()
                .success(false)
                .errors(List.of(error))
                .build();
    }
}
