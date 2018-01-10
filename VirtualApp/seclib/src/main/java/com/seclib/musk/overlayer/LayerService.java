package com.seclib.musk.overlayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.seclib.musk.Debugger;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by musk on 17/12/28.
 */

public class LayerService extends Service {


    private Context context;
    private boolean isShow=false;
    public static final String FLAG_ACTION="showFlag";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context=this.getApplicationContext();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null){
            isShow=intent.getBooleanExtra(FLAG_ACTION,false);
        }
        if(isShow){
            Debugger.i("==addLayer==");
            OverLayer.addLayer(context);
        }else{
            OverLayer.removeLayer(context);
        }
        return super.onStartCommand(intent, flags, startId);
    }
}
