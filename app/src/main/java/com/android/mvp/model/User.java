package com.android.mvp.model;

/**
 * Created by yld on 2018/1/26.
 */

public class User {
    private String userName;
    protected String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
