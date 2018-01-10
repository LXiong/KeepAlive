package com.seclib.musk;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;

/**
 * Created by musk on 17/12/12.
 * 界面跳转
 */

public class Skipper {

    //跳转到辅助功能界面
    public static void turnToAccessibility(Activity context) {
        try {
            if (!Utils.isAccessbilityServiceEnable(context, SecureService.class.getName())) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            //ignore
        }
    }

    //跳转到设备管理界面
//    public static void turnToDevice(Activity context){
//        //配置隐式Intent
//        ComponentName com=new ComponentName(context,DeviceReceiver.class);
//        Intent in=new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
//        in.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,com);
//        //添加附加说明
//        in.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"为使用安全空间完整功能，请允许次功能");
//        context.startActivityForResult(in,1);
//    }
}
