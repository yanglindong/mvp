package com.android.mvp.presenter;


import android.app.ProgressDialog;
import android.view.Window;

import com.android.mvp.activity.model.LoginModelImp;
import com.android.mvp.callback.DataCallBack;
import com.android.mvp.view.IUserLoginView;
import com.android.mylibrary.base.GApp;
import com.android.mylibrary.okrx.ServerApi;
import com.android.mylibrary.toast.CustomToast;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by yld on 2018/1/26.
 */

public class UserLoginPresenter {

    private IUserLoginView userLoginView;
    public UserLoginPresenter(IUserLoginView userLoginView) {
        this.userLoginView = userLoginView;
    }
    public void clear() {
        userLoginView.clearUserName();
        userLoginView.clearPassword();
    }
    public void login(String url) {
        new LoginModelImp().login(url, userLoginView.getUserName(), userLoginView.getPassword(), new DataCallBack() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFailure(String msg) {

            }

            @Override
            public void onError(Throwable e) {

            }
        });
    }
}
