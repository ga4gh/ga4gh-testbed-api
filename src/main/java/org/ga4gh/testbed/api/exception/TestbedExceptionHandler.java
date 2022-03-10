package org.ga4gh.testbed.api.exception;

import java.time.LocalDateTime;
import org.ga4gh.starterkit.common.constant.DateTimeConstants;
import org.ga4gh.starterkit.common.exception.CustomException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.lang.Nullable;

@ControllerAdvice
public class TestbedExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {CustomException.class})
    protected ResponseEntity<Object> handleCustomException(CustomException ex, WebRequest request) {
        HttpStatus status = ex.getClass().getAnnotation(ResponseStatus.class).value();
        return testbedHandleException(ex, null, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return testbedHandleException(ex, headers, status, request);
    }

    private ResponseEntity<Object> testbedHandleException(Exception ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        TestbedCustomExceptionResponse response = new TestbedCustomExceptionResponse();
        response.setStatusCode(status.value());
        response.setError(status.getReasonPhrase());
        response.setTimestamp(LocalDateTime.now().format(DateTimeConstants.DATE_FORMATTER));
        response.setMessage(ex.getMessage());
        response.setPath(((ServletWebRequest) request).getRequest().getRequestURI().toString());
        
        return ResponseEntity.status(status).headers(headers).body(response);
    }
}