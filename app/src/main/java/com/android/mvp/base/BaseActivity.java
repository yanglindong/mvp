package com.android.mvp.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import com.android.mvp.R;
import com.android.mvp.statusbar.Eyes;
import com.android.mylibrary.ui.SystemBarTintManager;
import com.android.mylibrary.utils.XAtyTask;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.ButterKnife;

/**
 * Activity基类
 *
 * Author: nanchen
 * Email: liushilin520@foxmail.com
 * Date: 2017-06-20  14:21
 */

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 获取布局ID
     *
     * @return  布局id
     */
    protected abstract int getContentViewLayoutID();



    /**
     * 界面初始化前期准备
     */
    protected void beforeInit() {

    }

    /**
     * 初始化布局以及View控件
     */
    protected abstract void initView(Bundle savedInstanceState);

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeInit();
        if (getContentViewLayoutID() != 0) {
            setContentView(getContentViewLayoutID());
            initSystemBarTint();
            initView(savedInstanceState);
        }
        XAtyTask.getInstance().addAty(this);
    }

    /** 设置状态栏颜色 */
    protected void initSystemBarTint() {
        if(isTranslucentStatusBar()){
            Eyes.translucentStatusBar(this,true);
            return;
        }
        if(isChangeStatusColor()){
            initStatusBarColor();
        }
    }
    protected boolean isTranslucentStatusBar(){
        return false;
    }
    protected void initStatusBarColor(){
        Eyes.setStatusBarColor(this, ContextCompat.getColor(this, R.color.colorAccent));
    }


    protected boolean isChangeStatusColor() {
        return true;
    }

    /** 子类可以重写改变状态栏颜色 */
    protected int setStatusBarColor() {
        return R.color.colorAccent;
    }

    private Toast mToast;

    protected void showToast(String desc){
        if (mToast == null){
            mToast = Toast.makeText(this.getApplicationContext(),desc, Toast.LENGTH_SHORT);
        }else{
            mToast.setText(desc);
        }
        mToast.show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        XAtyTask.getInstance().killAty(this);
    }
}
