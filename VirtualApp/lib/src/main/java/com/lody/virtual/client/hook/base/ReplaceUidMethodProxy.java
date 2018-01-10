package com.lody.virtual.client.hook.base;

import com.lody.virtual.DebugTool;

import java.lang.reflect.Method;

public class ReplaceUidMethodProxy extends StaticMethodProxy {

        private final int index;
        public ReplaceUidMethodProxy(String name, int index) {
            super(name);
            this.index = index;
            DebugTool.i("ReplaceUidMethodProxy==new=="+name);
        }

    @Override
    public boolean beforeCall(Object who, Method method, Object... args) {
        DebugTool.i("ReplaceUidMethodProxy==new=="+method.getName());
        int uid = (int) args[index];
        if (uid == getVUid() || uid == getBaseVUid()) {
            args[index] = getRealUid();
        }
        return super.beforeCall(who, method, args);
    }
}