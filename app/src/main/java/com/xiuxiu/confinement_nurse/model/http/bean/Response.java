package com.xiuxiu.confinement_nurse.model.http.bean;

import java.io.Serializable;

public class Response<T>  implements Serializable {
    private boolean success;
    private int erroCode;
    private T results;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getErroCode() {
        return erroCode;
    }

    public void setErroCode(int erroCode) {
        this.erroCode = erroCode;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
