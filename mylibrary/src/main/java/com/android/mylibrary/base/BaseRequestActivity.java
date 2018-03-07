package com.android.mylibrary.base;



import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by yld on 2018/2/8.
 */

public class BaseRequestActivity extends BaseActivity{

    private CompositeDisposable compositeDisposable;


    public void addDisposable(Disposable disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    public void dispose() {
        if (compositeDisposable != null) compositeDisposable.dispose();
    }

}
