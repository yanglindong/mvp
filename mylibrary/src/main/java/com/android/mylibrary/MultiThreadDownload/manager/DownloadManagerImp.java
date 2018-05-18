package com.android.mylibrary.MultiThreadDownload.manager;

import com.android.mylibrary.MultiThreadDownload.listener.DownloadProgressListener;
import com.android.mylibrary.MultiThreadDownload.task.DownloadTask;
import com.android.mylibrary.MultiThreadDownload.thread.MyThreadFactory;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by yld on 2018/5/3.
 */

public class DownloadManagerImp extends DownloadManager {
    private static DownloadManagerImp downloadManager = new DownloadManagerImp();
    private ExecutorService executorService;
    private DownloadManagerImp(){
        executorService = Executors.newSingleThreadExecutor(new MyThreadFactory());
    }
    public DownloadManagerImp getInstance(){
        return downloadManager;
    }


    @Override
    public void start(String url, File saveDir,int threadNum) {
        this.start(url,saveDir,threadNum,null);
    }
    public void start(String url, File saveDir,int threadNum,DownloadProgressListener listener){
        if(listener!=null){
            executorService.execute(new DownloadTask(url,saveDir,threadNum,listener));
        }else{
            executorService.execute(new DownloadTask(url,saveDir,threadNum));
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void exit() {

    }
}
