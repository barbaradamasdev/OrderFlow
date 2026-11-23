package com.orderflow.exception;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.orderflow.dto.ApiError;

@RestControllerAdvice
public class GlobalExceptionHandler
        extends ResponseEntityExceptionHandler {

    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpectedException(
            Exception exception) {

        log.error(
                "Unexpected request failure. errorCode=INTERNAL_ERROR",
                exception
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createError(
                        "INTERNAL_ERROR",
                        "An unexpected error occurred."
                ));
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception exception,
            Object body,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String code = status.is4xxClientError()
                ? "REQUEST_ERROR"
                : "INTERNAL_ERROR";

        String message = status.is4xxClientError()
                ? "Invalid request."
                : "An unexpected error occurred.";

        if (status.is5xxServerError()) {
            log.error("HTTP request failed. status={}",
                    status.value(), exception);
        } else {
            log.warn(
                    "HTTP request rejected. status={} exceptionType={}",
                    status.value(),
                    exception.getClass().getSimpleName()
            );
        }

        return super.handleExceptionInternal(
                exception,
                createError(code, message),
                headers,
                status,
                request
        );
    }

    private ApiError createError(
            String code,
            String message) {

        return new ApiError(
                code,
                message,
                MDC.get("correlationId"),
                Instant.now()
        );
    }
}