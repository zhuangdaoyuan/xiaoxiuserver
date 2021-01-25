package com.xiuxiu.confinement_nurse.model.router;

public class NoFoundActivityException extends Exception {
    public NoFoundActivityException() {
        super("没有找到Activity");
    }
}
