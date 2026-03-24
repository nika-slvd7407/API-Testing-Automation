package com.solvd.domain;

import java.util.List;
import java.util.Map;

public class ErrorResponse {

    private int statusCode;
    private List<Map<String, Object>> errors;

    public int getStatusCode() {
        return statusCode;
    }

    public List<Map<String, Object>> getErrors() {
        return errors;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setErrors(List<Map<String, Object>> errors) {
        this.errors = errors;
    }
}