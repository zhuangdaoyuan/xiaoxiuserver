package com.xiuxiu.confinement_nurse.model.router;

public class LoginErrorException extends Exception {
    public LoginErrorException() {
        super("登录失败");
    }
}
