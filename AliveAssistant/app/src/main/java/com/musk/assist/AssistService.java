package com.musk.assist;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import com.musk.demon.IEmpty;
import java.util.List;

/**
 * Created by musk on 17/12/21.
 */

public class AssistService extends Service {

    //测试app start
    //private final String PACAKGE_NAME = "com.musk.alive";
    //private final String SERVICE_PATH = "com.musk.alivetest.AliveService";
    //测试app end

    //正式版本时记得改成这个信息
    private final String PACAKGE_NAME = "cn.appssec.secspace";
    private final String SERVICE_PATH = "com.musk.alive.AliveService";

    private IEmpty binderFromAlive;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binderFromAlive = IEmpty.Stub.asInterface(iBinder);
            if (binderFromAlive != null) {

            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            if (!isServiceRunning(AssistService.this, SERVICE_PATH)) {
                startAlive();
            }
            bindAlive();
        }
    };


    private IEmpty.Stub binderToAlive = new IEmpty.Stub() {
        @Override
        public String getName() throws RemoteException {
            return "空间安全管理服务";
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binderToAlive;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            startForeground(101, new Notification());
        } else if (Build.VERSION.SDK_INT < 25) {
            startService(new Intent(this, XService.class));
            startForeground(101, new Notification());
        }
        startAlive();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindAlive();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancel(101);
        }
    }

    public static class XService extends Service {
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            startForeground(101, new Notification());
            stopSelf();
        }
    }

    private void startAlive() {
        Intent intent = new Intent();
        intent.setClassName(PACAKGE_NAME, SERVICE_PATH);
        startService(intent);
    }

    private void bindAlive() {
        Intent intent = new Intent();
        intent.setClassName(PACAKGE_NAME, SERVICE_PATH);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private boolean isServiceRunning(Context context,String serviceName){
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list = am.getRunningServices(50);
        for (ActivityManager.RunningServiceInfo info : list) {
            if (info.service.getClassName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
}