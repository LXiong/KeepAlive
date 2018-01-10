package com.seclib.musk.compat;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CheckBox;

import com.seclib.musk.SecureService;
import com.seclib.musk.Utils;
import java.util.List;

/**
 * Created by musk on 17/12/15.
 * 华为机型适配
 */

public class Huawei {

    private static final String SYSMANAGER="com.huawei.appmarket";

    public static void cancelUninstallFromSysManager(AccessibilityEvent event,Context context){
        if(!isMe()){
            return;
        }
        if(SYSMANAGER.equals(event.getPackageName())){
            cancelSystemManagerUninstall(event,context);
            cancelSystemManagerUninstallList(event,context);
        }
    }
    public static boolean isMe(){
        return Build.BRAND.equalsIgnoreCase("huawei")||Build.BRAND.equalsIgnoreCase("honor");
    }



    /**
     * 从系统管家卸载－－－－单个卸载入口
     */
    private static void cancelSystemManagerUninstall(AccessibilityEvent event, Context context){
        List<AccessibilityNodeInfo>infos= event.getSource().findAccessibilityNodeInfosByViewId("android:id/message");
        if(infos!=null && !infos.isEmpty()){
            String text=infos.get(0).getText().toString();
            if(text!=null){
                if(text.contains("卸载") && text.contains(Utils.getAppName(context))){
                    //正在卸载我的应用
                    ((SecureService)context).performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                }
            }
        }
    }
    /**
     * 从系统管家卸载－－－－批量卸载入口
     */
    private static void cancelSystemManagerUninstallList(AccessibilityEvent event, Context context){

        List<AccessibilityNodeInfo>temp= event.getSource().findAccessibilityNodeInfosByViewId("com.huawei.appmarket:id/applistview");
        if(temp!=null && !temp.isEmpty()){
            AccessibilityNodeInfo listView=temp.get(0);
            for (int i=0;i<listView.getChildCount();i++){
                AccessibilityNodeInfo itemContainer=listView.getChild(i);
                if(itemContainer==null){
                    continue;
                }
                //检查item
                boolean findItem=false;

                List<AccessibilityNodeInfo>itemName= event.getSource().findAccessibilityNodeInfosByViewId("com.huawei.appmarket:id/localpackage_item_name");
                if(itemName!=null && !itemName.isEmpty()){
                    String text=itemName.get(0).getText().toString();
                    if(text!=null){
                        if(text.equals(Utils.getAppName(context))){
                            findItem=true;
                        }
                    }
                }
                if(findItem){
                    //检查开关控件
                    List<AccessibilityNodeInfo>checks= event.getSource().findAccessibilityNodeInfosByViewId("com.huawei.appmarket:id/button_check_box");
                    if(checks!=null && !checks.isEmpty()){
                        if(CheckBox.class.getName().equals(checks.get(0).getClassName().toString())){
                            if(checks.get(0).isChecked()){
                                itemContainer.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            }
                        }
                    }
                }
            }
        }
    }
}