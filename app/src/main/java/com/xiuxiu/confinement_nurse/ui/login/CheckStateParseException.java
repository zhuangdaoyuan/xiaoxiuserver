package com.xiuxiu.confinement_nurse.ui.login;

import com.xiuxiu.confinement_nurse.model.http.bean.login.LoginBean;

import java.text.ParseException;

/**
 * 审核不通过
 */
public class CheckStateParseException extends ParseException {
    /**
     * Constructs a ParseException with the specified detail message and
     * offset.
     * A detail message is a String that describes this particular exception.
     *
     * @param s           the detail message
     * @param errorOffset the position where the error is found while parsing.
     */
    public CheckStateParseException(String s, int errorOffset) {
        super(s, errorOffset);
    }
    private LoginBean loginBean;

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }
}
