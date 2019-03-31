package com.demo.bean;

import com.google.gson.Gson;

public class BaseResponse<T> {

    private int resultcode;
    private String reason;

    private T result;

    public BaseResponse(int resultcode, String reason) {
        this.resultcode = resultcode;
        this.reason = reason;
    }

    public BaseResponse(T result) {
        this.result = result;
        this.resultcode = 0;
        this.reason = "ok";
    }

    public BaseResponse(int resultcode, String reason, T result) {
        this.resultcode = resultcode;
        this.reason = reason;
        this.result = result;
    }

    public int getResultcode() {
        return resultcode;
    }

    public void setResultcode(int resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "resultcode=" + resultcode +
                ", reason='" + reason + '\'' +
                ", result=" + result +
                '}';
    }
}
