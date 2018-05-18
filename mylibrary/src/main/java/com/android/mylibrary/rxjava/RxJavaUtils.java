package com.android.mylibrary.rxjava;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by yld on 2018/5/4.
 */

public class RxJavaUtils {
    public RxJavaUtils() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).map(new Function<Integer, Integer>() {
            @Override
            public Integer apply(@NonNull Integer integer) throws Exception {
                return integer*10;
            }
        }).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(@NonNull Integer o) throws Exception {

            }
        });

        Observable.zip(Observable.just(1, 2, 3),
                Observable.just("A", "B", "C"),
                new BiFunction<Integer, String, String>() {
                    @Override
                    public String apply(@NonNull Integer integer, @NonNull String s) throws Exception {
                        return integer + s;
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                    }
                });

        Observable.concat(Observable.just(1, 2, 3), Observable.just(4, 5, 6))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(@NonNull Integer integer) throws Exception {
                    }
                });
    }

}
