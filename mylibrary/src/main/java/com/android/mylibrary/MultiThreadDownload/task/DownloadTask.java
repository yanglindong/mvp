package com.android.mylibrary.MultiThreadDownload.task;

import com.android.mylibrary.MultiThreadDownload.listener.DownloadProgressListener;
import com.android.mylibrary.MultiThreadDownload.manager.FileDownloader;
import com.android.mylibrary.base.GApp;

import java.io.File;

/**
 * Created by yld on 2018/5/3.
 */

 public class DownloadTask implements Runnable{
    private String path;
    private File saveDir;
    private FileDownloader fileDownloader;
    private DownloadProgressListener listener;
    private int threadNum;

    public DownloadTask(String path,File saveDir,int threadNum) {
        this(path,saveDir,threadNum,null);
    }
    public DownloadTask(String path,File saveDir,int threadNum,DownloadProgressListener listener) {
        this.path = path;
        this.saveDir = saveDir;
        this.threadNum = threadNum;
        this.listener = listener;

    }

    @Override
    public void run() {
         fileDownloader = new FileDownloader(GApp.application,path,saveDir,threadNum);
        fileDownloader.initDownload();
        fileDownloader.download(listener);

    }
    public void exit(){
        if(fileDownloader!=null){
            fileDownloader.exit();
        }

    }
}
