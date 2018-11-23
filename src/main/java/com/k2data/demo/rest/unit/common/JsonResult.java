package com.k2data.demo.rest.unit.common;

public class JsonResult {

    private int code;
    private String message;
    private Object body;

    public int getCode() {
        return code;
    }

    public JsonResult setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public JsonResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getBody() {
        return body;
    }

    public JsonResult setBody(Object body) {
        this.body = body;
        return this;
    }
}
