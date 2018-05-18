package com.android.mylibrary.MultiThreadDownload.manager;

import java.io.File;

/**
 * Created by yld on 2018/5/3.
 */

public abstract class DownloadManager {
    void start(String url,File saveDir,int threadNum){};
    void stop(){};
    void exit(){};
}
