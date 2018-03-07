package com.android.mvp.view;

import com.android.mvp.model.User;

/**
 * Created by yld on 2018/1/26.
 */

public interface IUserLoginView {
    String getUserName();

    String getPassword();

    void clearUserName();

    void clearPassword();

}
