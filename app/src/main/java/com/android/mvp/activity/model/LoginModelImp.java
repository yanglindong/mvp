package com.android.mvp.activity.model;

import android.app.ProgressDialog;
import android.view.Window;

import com.android.mvp.callback.DataCallBack;
import com.android.mylibrary.base.GApp;
import com.android.mylibrary.okrx.ServerApi;
import com.android.mylibrary.toast.CustomToast;
import com.lzy.okgo.model.HttpParams;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yld on 2018/2/26.
 */

public class LoginModelImp implements LoginModel{
    @Override
    public void login(String url, String userName, String password, final DataCallBack callBack){
        HttpParams params = new HttpParams();
        params.put("userName",userName);
        params.put("password",password);
        ServerApi.getString(url,null, null)//
                .subscribeOn(Schedulers.io())//
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showLoading();
                    }
                })//
                .observeOn(AndroidSchedulers.mainThread())  //
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        callBack.onSuccess(s);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        callBack.onError(e);
                        CustomToast.makeText(GApp.application,"请求失败",1000).show();
                    }

                    @Override
                    public void onComplete() {
                        dismissLoading();
                    }
                });

    }

    private ProgressDialog dialog;

    public void showLoading() {
        if (dialog != null && dialog.isShowing()) return;
        dialog = new ProgressDialog(GApp.application);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("请求网络中...");
        dialog.show();
    }

    public void dismissLoading() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
