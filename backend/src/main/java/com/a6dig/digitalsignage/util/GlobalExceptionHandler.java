package com.a6dig.digitalsignage.util;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.exception.InvalidLayoutException;
import com.a6dig.digitalsignage.exception.LayoutNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(LayoutNotFoundException.class)
    public ResponseEntity<APIResponse<Void>> handleLayoutNotFound(LayoutNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                APIResponse.notFound(exception.getMessage(), exception.getErrors())
        );
    }

    @ExceptionHandler(InvalidLayoutException.class)
    public ResponseEntity<APIResponse<Void>> handleInvalidLayout(InvalidLayoutException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                APIResponse.badRequest(exception.getMessage(), exception.getErrors())
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>> handleInternalServerErr(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                APIResponse.internalServerError(List.of(
                        ErrorMessage.createErrorMessage(AppConstant.ExceptionMessage.UNEXPECTED_ERROR),
                        ErrorMessage.createErrorMessage(exception.getMessage()),
                        ErrorMessage.createErrorMessage(exception.getLocalizedMessage()
                ))
        ));
    }
}
