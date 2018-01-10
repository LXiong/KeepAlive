package com.musk.alive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.musk.Utils.Config;
import com.musk.Utils.Debugger;

/**
 * Created by musk on 18/1/3.
 */

public class AliveBroad extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Intent in = new Intent(context,AliveService.class);
            context.startService(in);
        }
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            String packageName = intent.getData().getSchemeSpecificPart();
            if (Config.alivePkgName.equals(packageName)) {
                Intent in = new Intent();
                in.setClassName(Config.alivePkgName, Config.aliveClassName);
                context.startService(in);
            }
        }
    }
}
