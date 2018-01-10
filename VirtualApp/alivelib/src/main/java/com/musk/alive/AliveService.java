package com.musk.alive;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import com.musk.Utils.AppUtil;
import com.musk.Utils.Config;
import com.musk.demon.IEmpty;

/**
 * Created by musk on 17/12/21.
 */

public class AliveService extends Service {

    private IEmpty binderFromAssisit;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            binderFromAssisit = IEmpty.Stub.asInterface(iBinder);
            if (binderFromAssisit != null) {
                //ignore
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            startAssistService();
            bindAssisitService();
        }
    };


    private IEmpty.Stub binderToAssisitService = new IEmpty.Stub() {
        @Override
        public String getName() throws RemoteException {
            return "安全空间";
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return binderToAssisitService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!AppUtil.isApkInstalled(this, Config.alivePkgName)) {
            stopSelf();
            return;
        }
        startAssistService();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        bindAssisitService();
        return START_STICKY;
    }

    private void startAssistService(){
        Intent intent = new Intent();
        intent.setClassName(Config.alivePkgName, Config.aliveClassName);
        startService(intent);
    }
    private void bindAssisitService() {
        try {
            Intent intent = new Intent();
            intent.setClassName(Config.alivePkgName, Config.aliveClassName);
            bindService(intent, connection, Context.BIND_AUTO_CREATE);
        } catch (Exception e) {
        }
    }
}