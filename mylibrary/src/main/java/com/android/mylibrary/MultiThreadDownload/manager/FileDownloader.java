package com.android.mylibrary.MultiThreadDownload.manager;

import android.content.Context;

import com.android.mylibrary.MultiThreadDownload.filedao.FileService;
import com.android.mylibrary.MultiThreadDownload.listener.DownloadProgressListener;
import com.android.mylibrary.MultiThreadDownload.network.Network;
import com.android.mylibrary.MultiThreadDownload.thread.DownloadThread;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Response;

/**
 * Created by yld on 2018/5/3.
 */

public class FileDownloader {
    private static final String TAG = "FileDownloader";
    private FileService fileService;
    private boolean exited;
    private int downloadedSize = 0;
    private int fileSize = 0;
    private DownloadThread[] threads;
    private File saveFile;
    private Map<Integer, Integer> data = new ConcurrentHashMap<>();
    private int block;
    private String downloadUrl;
    private File fileSaveDir;


    /**
     * 构建文件下载器
     *
     * @param context
     * @param downloadUrl 下载路径
     * @param fileSaveDir 文件保存目录
     * @param threadNum   下载线程数
     */
    public FileDownloader(Context context, String downloadUrl, File fileSaveDir, int threadNum) {
        this.downloadUrl = downloadUrl;
        this.threads = new DownloadThread[threadNum];
        this.fileService = new FileService(context);
        this.fileSaveDir = fileSaveDir;

    }
    public void initDownload(){
        try {
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdirs();
            }
            Response response = Network.getOkHttpResponse(downloadUrl);
            if (response.isSuccessful()) {
                this.fileSize = (int) response.body().contentLength();
                if (this.fileSize <= 0) {
                    throw new RuntimeException("Unkown file size");
                }
                this.saveFile = new File(fileSaveDir,  getFileName(response));
                Map<Integer, Integer> map = fileService.getThreadIdAndDownlengthByPath(downloadUrl);
                if (map.size() > 0) {
                    for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                        data.put(entry.getKey(), entry.getValue());
                    }
                }
                if (this.data.size() == this.threads.length) {
                    for (int i = 0; i < this.threads.length; i++) {
                        this.downloadedSize += this.data.get(i + 1);
                    }
                }else{

                }
                this.block = getBlock(this.fileSize,this.threads.length);

            } else {
                throw new RuntimeException("server response error");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getBlock(int fileSize, int threadNum) {
        return fileSize % threadNum == 0 ? fileSize /threadNum : fileSize / threadNum + 1;
    }

    private String getFileName(Response response) {
        String fileName = this.downloadUrl.substring(this.downloadUrl.lastIndexOf("/") + 1);
        if (fileName == null || "".equals(fileName.trim())) {
            for (int i = 0; ; i++) {
                String mine = response.headers().value(i);
                if (mine == null) {
                    break;
                }
                if ("content-disposition".equals(response.headers().value(i).toLowerCase())) {
                    Matcher m = Pattern.compile(".*filename=(.*)").matcher(mine.toLowerCase());
                    if (m.find()) {
                        return m.group(1);
                    }
                }
            }
            fileName = UUID.randomUUID() + ".tmp";
        }
        return fileName;
    }

    /**
     * 获取线程数
     *
     * @return
     */
    public int getThreadSize() {
        return threads.length;
    }

    public boolean getExited() {
        return this.exited;
    }

    /**
     * 退出下载
     */
    public void exit() {
        this.exited = true;
    }

    /**
     * 获取文件的大小
     *
     * @return
     */
    public int getFileSize() {
        return fileSize;
    }

    /**
     * 累计已下载的大小
     *
     * @param size
     */
    public synchronized void append(int size) {
        downloadedSize += size;

    }

    /**
     * 更新指定线程最后的下载位置
     *
     * @param threadId
     * @param pos
     */
    public synchronized void update(int threadId, int pos) {
        this.data.put(threadId, pos);
        this.fileService.update(this.downloadUrl, threadId, pos);
    }


    public int download(DownloadProgressListener listener) {
        try {
            RandomAccessFile randOut = new RandomAccessFile(this.saveFile, "rwd");
            if (this.fileSize > 0) {
                randOut.setLength(this.fileSize);
                randOut.close();
            }
            if (this.data.size() != this.threads.length) {
                this.data.clear();
                for (int i = 0; i < threads.length; i++) {
                    this.data.put(i + 1, 0);
                }
                this.downloadedSize = 0;
            }
            for (int i = 0; i < threads.length; i++) {
                int downloadedLength = this.data.get(i + 1);
                //判断线程是否已经下载完成，否则继续下载
                if (downloadedLength < this.block && this.downloadedSize < this.fileSize) {
                    this.threads[i] = new DownloadThread(this, downloadUrl, this.saveFile, this.block, this.data.get(i + 1), i + 1);
                    //设置线程的优先级，Thread.NORM_PRIORITY = 5 Thread.MIN_PRIORITY = 1 Thread.MAX_PRIORITY = 10
                    this.threads[i].setPriority(7);
                    this.threads[i].start();

                } else {
                    this.threads[i] = null;
                }

            }
            //先删除在添加下载记录
            fileService.delete(this.downloadUrl);
            fileService.save(this.downloadUrl, this.data);
            boolean notFinished = true;
            while (notFinished) {
                Thread.sleep(900);
                notFinished = false;
                for (int i = 0; i < threads.length; i++) {
                    if (this.threads[i] != null && !this.threads[i].isFinished()) {
                        notFinished = true;
                        if (this.threads[i].getDownloadLength() == -1) {
                            this.threads[i] = new DownloadThread(this, downloadUrl, this.saveFile, this.block, this.data.get(i + 1), i + 1);
                            this.threads[i].setPriority(7);
                            this.threads[i].start();
                        }
                    }
                }
                if (listener != null) {
                    listener.onDownloadSize(this.downloadedSize);
                }
            }
            if (downloadedSize == this.fileSize) {
                fileService.delete(this.downloadUrl);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this.downloadedSize;
    }

    public static Map<String, String> getHttpResponseHeader(HttpURLConnection http) {
        //使用LinkedHashMap保证写入和遍历时的顺序相同，而且允许空值存在
        Map<String, String> header = new LinkedHashMap<>();
        for (int i = 0; ; i++) {
            String fieldValue = http.getHeaderField(i);
            if (fieldValue == null) {
                break;
            }
            header.put(http.getHeaderFieldKey(i), fieldValue);
        }
        return header;
    }

}
