package com.homeloan.project.http;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class ServiceStatus {

    @Getter
    @Setter
    private Integer statusCode;

    @Getter @Setter
    private String  message;

    @Getter @Setter
    private List<String> errors;

    public ServiceStatus(Integer statusCode) {
        this.statusCode = statusCode;
        this.message = null;
        this.errors = null;
    }

    public ServiceStatus(Integer statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.errors = null;
    }

    public ServiceStatus(Integer statusCode, String message, List<String> errors) {
        this.statusCode = statusCode;
        this.message = message;
        this.errors = errors;
    }

}
