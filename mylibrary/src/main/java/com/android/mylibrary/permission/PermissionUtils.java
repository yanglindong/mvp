package com.android.mylibrary.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/11/16.
 */
public class PermissionUtils {

    private static String[] PERMISSIONS_CAMERA_AND_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    public static boolean isCameraPermission(Activity context, int requestCode){
        if (Build.VERSION.SDK_INT >= 23) {
            int storagePermission = ActivityCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            if (storagePermission != PackageManager.PERMISSION_GRANTED || cameraPermission!= PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(context, PERMISSIONS_CAMERA_AND_STORAGE,
                        requestCode);
                return false;
            }
        }
        return true;
    }

    public static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    // 判断权限集合
    public static void requestPermissions(Activity context, int PERMISSION_REQUEST_CODE, String... permissions) {
        boolean check=false;
        for (String p: permissions){
            int storagePermission = ActivityCompat.checkSelfPermission(context, p);
            if(storagePermission!= PackageManager.PERMISSION_GRANTED ) {
                check=true;
            }
        }
        if(check) {
            ActivityCompat.requestPermissions(context, permissions, PERMISSION_REQUEST_CODE);
        }
    }
    // 判断权限集合
    public static boolean lacksPermissions(Activity context,String... permissions) {
        if (Build.VERSION.SDK_INT >= 23) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) ==
                        PackageManager.PERMISSION_DENIED) {
                    return true;
                }
            }
        }

        return false;
    }




}
