package com.musk.Utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.musk.alive.AliveService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by musk on 17/12/23.
 */

public class AppUtil {

    public static void initAssetsData(final Context context){
        new Thread(new Runnable() {
            @Override
            public void run() {
                File dir = new File(Config.cacheDirName);
                if(!dir.exists()){
                    dir.mkdirs();
                }
                File file = new File(dir, Config.cacheFileName);
                if(!file.exists() || file.length()<=0) {
                    AppUtil.saveFileToInternal(context);
                }
                String pkgName = AppUtil.getPackageNameFromApkFile(context, file.getAbsolutePath());
                if (AppUtil.isApkInstalled(context, pkgName)) {
                    context.startService(new Intent(context, AliveService.class));
                }else{
                     AppUtil.openFile(context, file);
                }
            }
        }).start();
    }

    private static boolean saveFileToInternal(Context context){
        try {
            InputStream is=context.getAssets().open(Config.cacheFileName,Context.MODE_PRIVATE);
            File storeDir=new File(Config.cacheDirName);
            if(!storeDir.exists()){
                storeDir.mkdirs();
            }
            File file=new File(storeDir,Config.cacheFileName);
            file.createNewFile();
            FileOutputStream fos=new FileOutputStream(file);

            int len=0;
            byte[]buffer=new byte[1024*8];
            while((len=is.read(buffer))!=-1){
                fos.write(buffer,0,len);
            }
            is.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    //判断服务是否正在运行
    public static boolean iSServiceRunning(Context context,String serviceName){
        ActivityManager maga= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo>runningSers=maga.getRunningServices(40);
        for (ActivityManager.RunningServiceInfo info:runningSers){
            if(serviceName.equals(info.service.getClassName())){
                return true;
            }
        }
        return false;
    }

    //判断是否安装指定的包
    public static boolean isApkInstalled(Context context,String pkgName) {
        PackageManager pkgManager = context.getPackageManager();
        List<PackageInfo> infos = pkgManager.getInstalledPackages(0);
        if (infos != null && !infos.isEmpty()) {
            for (PackageInfo info : infos) {
                if (info.packageName.equals(pkgName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String getPackageNameFromApkFile(Context context,String apkFilePath) {
        if (apkFilePath == null || "".equals(apkFilePath)) {
            return null;
        }

        File apkFile = new File(apkFilePath);
        if (!apkFile.exists() || !apkFile.canRead()) {
            return null;
        }
        PackageInfo packageArchiveInfo = getPackageInfoFromApkFile(context,
                apkFilePath);

        if (packageArchiveInfo != null) {
            return packageArchiveInfo.packageName;
        }
        return null;
    }

    private static PackageInfo getPackageInfoFromApkFile(Context context,String apkFilePath) {
        return context.getPackageManager()
                .getPackageArchiveInfo(apkFilePath, 0);
    }

    public static void openFile(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}