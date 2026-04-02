package com.a6dig.digitalsignage.util;

import com.a6dig.digitalsignage.constant.AppConstant;
import com.a6dig.digitalsignage.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(AdCollectionNotFoundException.class)
    public ResponseEntity<APIResponse<Void>> handleAdCollectionNotFound(AdCollectionNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                APIResponse.notFound(exception.getMessage(), exception.getErrors())
        );
    }


    @ExceptionHandler(AdContentNotFoundException.class)
    public ResponseEntity<APIResponse<Void>> handleAdContentNotFound(AdCollectionNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                APIResponse.notFound(exception.getMessage(), exception.getErrors())
        );
    }


    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<APIResponse<Void>> handleDeviceNotFound(DeviceNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                APIResponse.notFound(exception.getMessage(), exception.getErrors())
        );
    }

    @ExceptionHandler(InvalidDeviceException.class)
    public ResponseEntity<APIResponse<Void>> handleInvalidDevice(InvalidDeviceException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                APIResponse.badRequest(exception.getMessage(), exception.getErrors())
        );
    }

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

    @ExceptionHandler(InvalidLayoutSlotException.class)
    public ResponseEntity<APIResponse<Void>> handleInvalidLayoutSlot(InvalidLayoutSlotException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                APIResponse.badRequest(exception.getMessage(), exception.getErrors())
        );
    }

    @ExceptionHandler(InvalidDomainException.class)
    public ResponseEntity<APIResponse<Void>> handleInvalidDomain(InvalidDomainException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                APIResponse.badRequest(exception.getMessage(), exception.getErrors())
        );
    }



    @ExceptionHandler(InvalidJSONException.class)
    public ResponseEntity<APIResponse<Void>> handleInvalidJSON(InvalidJSONException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                APIResponse.badRequest(exception.getMessage(), exception.getErrors())
        );
    }


    @ExceptionHandler(ModuleNotFoundException.class)
    public ResponseEntity<APIResponse<Void>> handleModuleNotFound(ModuleNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                APIResponse.notFound(exception.getMessage(), exception.getErrors())
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
