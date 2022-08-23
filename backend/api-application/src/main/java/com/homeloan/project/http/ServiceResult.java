package com.homeloan.project.http;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ServiceResult<T> {

    static private String SUCCESS_MESSAGE = "Success";

    @Getter
    @Setter
    T data;

    @Getter @Setter
    Integer statusCode;

    @Getter @Setter
    String message;

    @Getter @Setter
    List<String> errors;



    public ServiceResult(T body) {
        this.data = body;
        this.statusCode = 200;
        this.message = SUCCESS_MESSAGE;
    }


    public ServiceResult(T body, ServiceStatus status) {
        this.data = body;
        this.statusCode = status.getStatusCode();
        this.message = status.getMessage();
        this.errors = status.getErrors();
    }
}
