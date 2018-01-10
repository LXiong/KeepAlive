package com.seclib.musk;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.seclib.musk.compat.Xiaomi;
import com.seclib.musk.overlayer.LayerService;
import java.util.List;

/**
 * Created by musk on 17/12/13.
 * 事件处理器
 */

public class EventHandler {

    public static void dealEvent(AccessibilityNodeInfo nodeInfo, Context context) {
        if (nodeInfo == null) {
            return;
        }
        String myAppName=Utils.getAppName(context);
        if(Xiaomi.isMe()){
            //适配小米机型{主app＋守护app}
            Xiaomi.guardApp(nodeInfo,context,myAppName);
            Xiaomi.guardApp(nodeInfo,context,"空间安全管理服务");
        }else{
            //阻止进入卸载界面并弹出提示信息框[应用名称＋取消]
            guardUIShowDialog(nodeInfo, context, new String[]{myAppName, "取消"}, new String[]{"K","允许","继续安装","安装来源","激活此管理器可允许应用", "启动设备管理器"},
                    "友情提示", "安全空间有保护政策，请联系管理员！");
            //保护设备管理器
            guardUI(nodeInfo, context, new String[]{myAppName, "取消激活", "解除激活"}, null);
            //保护守护app
            guardUI(nodeInfo, context, new String[]{"空间安全管理服务", "取消"}, new String[]{"K","允许","继续安装","安装来源"});
        }
        //保护多任务栈
        guardRecentTask(nodeInfo, context, new String[]{"可用", "G"});

    }

    /**
     * 阻止进入特征页面
     *
     * @param node         窗口节点
     * @param context      上下文对象：辅助服务
     * @param includeTexts 包含的文字串
     * @param excludetexts 要排除的文字串
     */
    private static void guardUI(AccessibilityNodeInfo node, Context context, String[] includeTexts, String[] excludetexts) {
        try {
            if (includeTexts != null && includeTexts.length > 0) {
                boolean allFind = true;
                for (int i = 0; i < includeTexts.length; i++) {
                    List<AccessibilityNodeInfo> cancelNodes = node.findAccessibilityNodeInfosByText(includeTexts[i]);
                    if (cancelNodes == null || cancelNodes.isEmpty()) {
                        allFind = false;
                    }
                }
                if (filterExcludeTexts(node, excludetexts)) {
                    return;
                }
                if (allFind) {
                    ((SecureService) context).performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //阻止进入特征页面并弹框
    private static void guardUIShowDialog(AccessibilityNodeInfo node, Context context, String[] includeTexts, String[] excludetexts, String dialogTitle, String dialogContent) {
        try {
            if (includeTexts != null && includeTexts.length > 0) {
                boolean allFind = true;
                for (int i = 0; i < includeTexts.length; i++) {
                    List<AccessibilityNodeInfo> cancelNodes = node.findAccessibilityNodeInfosByText(includeTexts[i]);
                    if (cancelNodes == null || cancelNodes.isEmpty()) {
                        allFind = false;
                    }
                }
                if (filterExcludeTexts(node, excludetexts)) {
                    return;
                }
                if (allFind) {
                    boolean isSuccess = ((SecureService) context).performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                    if (isSuccess)
                        openDialog(context, dialogTitle, dialogContent);
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * //这里检测'排除文字串'是为了，避免对设备管理器激活页面误判，导致无法绑定
     *
     * @param node
     * @param excludetexts
     * @return
     */
    private static boolean filterExcludeTexts(AccessibilityNodeInfo node, String[] excludetexts) {
        //这里检测'排除文字串'是为了，避免对设备管理器激活页面误判，导致无法绑定
        if (excludetexts != null && excludetexts.length > 0) {
            for (int i = 0; i < excludetexts.length; i++) {
                List<AccessibilityNodeInfo> excludeNodes = node.findAccessibilityNodeInfosByText(excludetexts[i]);
                if (excludeNodes != null && !excludeNodes.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    //屏蔽一键清理按钮
    private static void guardRecentTask(AccessibilityNodeInfo node, Context context, String[] includeTexts) {
        if (includeTexts != null && includeTexts.length > 0) {
            boolean allFind = true;
            for (int i = 0; i < includeTexts.length; i++) {
                List<AccessibilityNodeInfo> cancelNodes = node.findAccessibilityNodeInfosByText(includeTexts[i]);
                if (cancelNodes == null || cancelNodes.isEmpty()) {
                    allFind = false;
                }
            }
            if (allFind) {
                Intent intent = new Intent(context, LayerService.class);
                intent.putExtra(LayerService.FLAG_ACTION, true);
                context.startService(intent);
            } else {
                Intent intent = new Intent(context, LayerService.class);
                intent.putExtra(LayerService.FLAG_ACTION, false);
                context.startService(intent);
            }
        }
    }

    //显示提示框
    private static void openDialog(Context context, String title, String content) {
        Intent intent = new Intent(context, CommonDialog.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}