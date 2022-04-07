package me.lijf.blank.controller;

import java.io.Serializable;

public class Result implements Serializable {
    private String code;
    private Object data;

    public Result(String code, Object data) {
        this.code = code;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code='" + code + '\'' +
                ", data='" + data + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
