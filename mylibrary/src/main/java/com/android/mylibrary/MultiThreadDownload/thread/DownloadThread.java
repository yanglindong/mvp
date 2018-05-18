package com.android.mylibrary.MultiThreadDownload.thread;

import com.android.mylibrary.MultiThreadDownload.manager.FileDownloader;
import com.android.mylibrary.MultiThreadDownload.network.Network;
import com.android.mylibrary.MultiThreadDownload.utils.PhoneInfoUtils;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yld on 2018/5/3.
 */

public class DownloadThread extends Thread{
    private FileDownloader downloader;
    private String downUrl;
    private  File saveFile;
    private int block;
    private int downloadLength;
    private int threadId;
    private boolean finished = false;
   public DownloadThread(FileDownloader downloader, String downUrl, File saveFile,int block,int downloadLength,int threadId){
        this.downloader = downloader;
       this.downUrl = downUrl;
       this.saveFile = saveFile;
       this.block = block;
       this.downloadLength = downloadLength;
       this.threadId = threadId;

   }


    @Override
    public void run() {
        if(downloadLength<block){
            try{
                int startPos = block*(threadId-1)+downloadLength;
                int endPost = block*threadId-1;
                OkHttpClient client = Network.getOkHttpClient();
                Request request = new Request.Builder()
                        .url(downUrl)
                        .header("User-Agent", PhoneInfoUtils.getUserAgent())
                        .addHeader("Accept","image/gif,image/jpeg,image/pjpeg,application/x-shockwave-flash,application/xaml+xml,application/vnd.ms-xpsdocument,application/x-ms-xbap," +
                                "application/x-ms-application,application/vnd.ms-excel,application/vnd.ms-powerpoint,application/msword")
                        .addHeader("Accept-Language","zh-CN")
                        .addHeader("Referer",downUrl)
                        .addHeader("Charset","UTF-8")
                        .addHeader("Connection","Keep-Alive")
                        .addHeader("Range","bytes="+startPos+"-"+endPost)
                        .build();
                Response response = client.newCall(request).execute();
                if(response.isSuccessful()){
                    InputStream inputStream = response.body().byteStream();
                    byte[] buffer = new byte[1024];
                    int offset = 0;
                    RandomAccessFile threadFile = new RandomAccessFile(this.saveFile,"rwd");
                    threadFile.seek(startPos);
                    while (!downloader.getExited()&&(offset = inputStream.read(buffer,0,1024))!=-1){
                        threadFile.write(buffer,0,offset);
                        downloadLength+=offset;
                        downloader.update(this.threadId,downloadLength);
                        downloader.append(offset);
                    }
                    threadFile.close();
                    inputStream.close();
                    if(downloader.getExited()){

                    }else{

                    }
                    this.finished = true;
                }


            }catch (Exception e){
                this.downloadLength = -1;
            }
        }
    }

    /**
     * 下载是否完成
     * @return
     */
    public boolean isFinished(){
        return  finished;
    }
    public  long getDownloadLength(){
        return  downloadLength;
    }
}
