package com.android.mylibrary.MultiThreadDownload.thread;

import java.util.concurrent.ThreadFactory;

/**
 * Created by yld on 2018/5/3.
 */

public class MyThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r);
    }
}
