package com.android.mylibrary.MultiThreadDownload.network;

import com.android.mylibrary.MultiThreadDownload.utils.PhoneInfoUtils;
import com.android.mylibrary.base.GApp;

import java.io.File;
import java.io.IOException;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by yld on 2018/5/3.
 */

public class Network {
    private static OkHttpClient mOkHttpClient;
    //设置缓存目录
    private static File cacheDirectory = new File(GApp.application.getCacheDir().getAbsolutePath(), "MyCaches");
    private static Cache cache = new Cache(cacheDirectory, 10 * 1024 * 1024);

    public static Response getOkHttpResponse(String downloadUrl) throws IOException{
        OkHttpClient client = getOkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .header("User-Agent", PhoneInfoUtils.getUserAgent())
                .addHeader("Accept","image/gif,image/jpeg,image/pjpeg,application/x-shockwave-flash,application/xaml+xml,application/vnd.ms-xpsdocument,application/x-ms-xbap," +
                        "application/x-ms-application,application/vnd.ms-excel,application/vnd.ms-powerpoint,application/msword")
                .addHeader("Accept-Language","zh-CN")
                .addHeader("Referer",downloadUrl)
                .addHeader("Charset","UTF-8")
                .addHeader("Connection","Keep-Alive")
                .build();

        return client.newCall(request).execute();
    }

    /**
     * 获取OkHttpClient对象
     *
     * @return
     */
    public static OkHttpClient getOkHttpClient() {

        if (null == mOkHttpClient) {
            //同样okhttp3后也使用build设计模式
            mOkHttpClient = new OkHttpClient.Builder()
                    //设置请求读写的超时时间
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .cache(cache)
                    .build();

        }

        return mOkHttpClient;
    }}
