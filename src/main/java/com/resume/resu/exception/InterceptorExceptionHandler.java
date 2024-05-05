package com.resume.resu.exception;

import com.resume.resu.interceptor.JwtInterceptor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class InterceptorExceptionHandler {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Void> handleInvalidTokenException(InvalidTokenException ex){
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(MissingTokenException.class)
    public ResponseEntity<Void> handleMissingTokenException(MissingTokenException ex){
        return ResponseEntity.badRequest().build();
    }

}
