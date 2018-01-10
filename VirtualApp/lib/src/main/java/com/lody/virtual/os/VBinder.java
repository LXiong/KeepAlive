package com.lody.virtual.os;

import android.os.Binder;

import com.lody.virtual.DebugTool;
import com.lody.virtual.client.ipc.VActivityManager;

/**
 * @author Lody
 */

public class VBinder {
    private static final String TAG=VBinder.class.getSimpleName();

    public static int getCallingUid() {
        DebugTool.i(TAG+"==getCallingUid==");
        return VActivityManager.get().getUidByPid(Binder.getCallingPid());
    }

    public static int getBaseCallingUid() {
        DebugTool.i(TAG+"==getBaseCallingUid==");
        return VUserHandle.getAppId(getCallingUid());
    }

    public static int getCallingPid() {
        DebugTool.i(TAG+"==getCallingPid==");
        return Binder.getCallingPid();
    }

    public static VUserHandle getCallingUserHandle() {
        DebugTool.i(TAG+"==getCallingUserHandle==");
        return new VUserHandle(VUserHandle.getUserId(getCallingUid()));
    }
}
