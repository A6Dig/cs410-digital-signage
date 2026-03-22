package com.a6dig.digitalsignage.util;

import com.a6dig.digitalsignage.constant.AppConstant;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class APIResponse <T> {
    private int status;
    private String message;
    private T data;

    private List<Map<String, String>> errors;

    private APIResponse(){}
    private APIResponse(int status, T data) {
        this.status = status;
        this.message = "";
        this.data = data;
        this.errors = new ArrayList<>();
    }

    private APIResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = new ArrayList<>();
    }

    private APIResponse(int status, String message, T data, List<Map<String, String>> errors) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.errors = errors;
    }

    public static <T> APIResponse<T> success(String message) {
        return new APIResponse<>(HttpStatus.OK.value(), message, null);
    }
    public static <T> APIResponse<T> success(T data) {
        return new APIResponse<>(HttpStatus.OK.value(), data);
    }
    public static <T> APIResponse<T> success(String message, T data) {
        return new APIResponse<>(HttpStatus.OK.value(), message, data);
    }

    public static <T> APIResponse<T> created(T data) {
        return new APIResponse<>(HttpStatus.CREATED.value(), AppConstant.SuccessMessage.LAYOUT_CREATED, data);
    }

    public static <T> APIResponse<T> created(String message, T data) {
        return new APIResponse<>(HttpStatus.CREATED.value(), message, data);
    }

    public static <T> APIResponse<T> notFound(String message) {
        return new APIResponse<>(HttpStatus.NOT_FOUND.value(), message, null);
    }
    public static <T> APIResponse<T> notFound(String message, List<Map<String, String>> errors) {
        return new APIResponse<>(HttpStatus.NOT_FOUND.value(), message, null, errors);
    }
    public static <T> APIResponse<T> badRequest(String message) {
        return new APIResponse<>(HttpStatus.BAD_REQUEST.value(), message, null);
    }
    public static <T> APIResponse<T> badRequest(String message, List<Map<String, String>> errors) {
        return new APIResponse<>(HttpStatus.BAD_REQUEST.value(), message, null, errors);
    }

    public static <T> APIResponse<T> error(int status, String message) {
        return new APIResponse<>(status, message, null);
    }
    public static <T> APIResponse<T> error(int status, String message, List<Map<String, String>> errors) {
        return new APIResponse<>(status, message, null, errors);
    }


    public static <T> APIResponse<T> internalServerError(List<Map<String, String>> errors) {
        return new APIResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), AppConstant.ExceptionMessage.INTERNAL_SERVER_ERROR, null, errors);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<Map<String, String>> getErrors() {
        return errors;
    }

    public void setErrors(List<Map<String, String>> errors) {
        this.errors = errors;
    }
}
