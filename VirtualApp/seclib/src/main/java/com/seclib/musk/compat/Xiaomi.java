package com.seclib.musk.compat;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;

import com.seclib.musk.Debugger;
import com.seclib.musk.SecureService;
import com.seclib.musk.overlayer.LayerService;

import java.util.List;

/**
 * Created by musk on 18/1/5.
 */

public class Xiaomi {

    public static boolean isMe() {
        return Build.BRAND.equalsIgnoreCase("xiaomi");
    }

    public static void guardApp(AccessibilityNodeInfo rootNode, Context context, String appName) {
        if (null == rootNode) {
            return;
        }
        guardUninstall(rootNode, context, appName);
        guardUI(rootNode, context, new String[]{appName, "结束运行", "卸载", "清除数据"});
        guardOneClickClean(rootNode, context, new String[]{/*"com.android.systemui:id/btnToggleTaskManager",*/"com.android.systemui:id/clearAnimView"});
    }

    private static void guardOneClickClean(AccessibilityNodeInfo rootNode, Context context, String[] includeIds) {
        if (includeIds != null && includeIds.length > 0) {
            boolean allFind = true;
            for (int i = 0; i < includeIds.length; i++) {
                List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByViewId(includeIds[i]);
                Debugger.i("===guardOneClickClean==" + includeIds[i]);
                Debugger.i("===" + includeIds[i] + "==find==" + (nodes == null));
                if (nodes != null) {
                    Debugger.i("===" + includeIds[i] + "==find==" + (nodes.size()));
                }
                Debugger.i("===" + includeIds[i] + "==find==" + (nodes == null || nodes.isEmpty()));
                if (nodes == null || nodes.isEmpty()) {
                    allFind = false;
                }
            }

            Debugger.i("==guardOneClickClean==allFind==" + allFind);
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

    private static void guardUninstall(AccessibilityNodeInfo rootNode, Context context, String appName) {

        boolean isDo = false;
        List<AccessibilityNodeInfo> titles = rootNode.findAccessibilityNodeInfosByViewId("com.miui.home:id/title");
        if (titles != null && titles.size() > 0) {
            if (titles.get(0).getText() != null) {
                isDo = titles.get(0).getText().toString().contains(appName);
            }
        }
        if (isDo) {
            List<AccessibilityNodeInfo> buttons = rootNode.findAccessibilityNodeInfosByViewId("com.miui.home:id/btnCancel");
            if (buttons != null && buttons.size() > 0) {
                ((SecureService) context).performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
            }
        }
    }

    /**
     * 阻止进入特征页面
     *
     * @param node         窗口节点
     * @param includeTexts 包含的文字串
     */
    private static void guardUI(AccessibilityNodeInfo node, Context context, String[] includeTexts) {
        try {
            if (node == null) {
                return;
            }
            if (includeTexts != null && includeTexts.length > 0) {
                boolean allFind = true;
                for (int i = 0; i < includeTexts.length; i++) {
                    List<AccessibilityNodeInfo> cancelNodes = node.findAccessibilityNodeInfosByText(includeTexts[i]);
                    if (cancelNodes == null || cancelNodes.isEmpty()) {
                        allFind = false;
                    }
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
}
