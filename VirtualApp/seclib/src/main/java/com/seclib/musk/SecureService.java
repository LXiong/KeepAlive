package com.seclib.musk;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import com.seclib.musk.compat.Huawei;

/**
 * Created by musk on 18/1/4.
 */

public class SecureService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (null != event.getSource()) {
            EventHandler.dealEvent(getRootInActiveWindow(),this);
            Huawei.cancelUninstallFromSysManager(event,this);
        }
    }

    @Override
    public void onInterrupt() {
        //
    }
}