package com.hps.anadep.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ErrorHandler {

//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
//        return new ResponseEntity<>(
//                new ErrorResponse(new Date(), exception.getMessage(), String.valueOf(HttpStatus.NOT_FOUND.value())),
//                HttpStatus.NOT_FOUND
//        );
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception exception) {
//        return new ResponseEntity<>(
//                new ErrorResponse(new Date(), exception.getMessage(), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())),
//                HttpStatus.INTERNAL_SERVER_ERROR
//        );
//    }
}
