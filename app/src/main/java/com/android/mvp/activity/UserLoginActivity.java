package com.android.mvp.activity;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.mvp.R;
import com.android.mvp.presenter.UserLoginPresenter;
import com.android.mvp.view.IUserLoginView;
import com.android.mylibrary.base.BaseRequestActivity;

import butterknife.Bind;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;

public class UserLoginActivity extends BaseRequestActivity implements IUserLoginView {
    UserLoginPresenter userLoginPresenter = new UserLoginPresenter(this);
    @Bind(R.id.id_et_username)
    TextView id_et_username;
    @Bind(R.id.id_et_password)
    TextView id_et_password;
    @Bind(R.id.id_pb_loading)
    TextView id_pb_loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
    }


    @OnClick(R.id.id_btn_login)
    public void login(View view){
        userLoginPresenter.login("www.baidu.com");

    }
    @OnClick(R.id.id_btn_clear)
    public void clear(View view){

    }

    @Override
    public String getUserName() {
        return id_et_username.getText().toString();
    }

    @Override
    public String getPassword() {
        return id_et_password.getText().toString();
    }

    @Override
    public void clearUserName() {
        id_et_username.setText("");
    }

    @Override
    public void clearPassword() {
        id_et_password.setText("");
    }

    @Override
    public void showLoading() {
        id_pb_loading.setVisibility(View.VISIBLE);
    }

}

