package com.android.mvp.activity.model;

import com.android.mvp.callback.DataCallBack;

/**
 * Created by yld on 2018/2/26.
 */

public interface LoginModel {
    void login(String url,String userName,String password, DataCallBack callBack);
}
