package com.lody.virtual.client.hook.base;

import com.lody.virtual.DebugTool;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Lody
 */

@SuppressWarnings("unchecked")
public class MethodBox {
    private static final String TAG=MethodBox.class.getSimpleName();
    public final Method method;
    public final Object who;
    public final Object[] args;

    public MethodBox(Method method, Object who, Object[] args) {
        DebugTool.i(TAG+"==new=="+method.getName());
        this.method = method;
        this.who = who;
        this.args = args;
    }

    public <T> T call() throws InvocationTargetException {
        try {
            DebugTool.i(TAG+"==call==");
            return (T) method.invoke(who, args);
        } catch (IllegalAccessException e) {
            DebugTool.i(TAG+"==call=="+e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public <T> T callSafe() {
        try {
            DebugTool.i(TAG+"==callSafe==");
            return (T) method.invoke(who, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            DebugTool.i(TAG+"==callSafe=="+e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            DebugTool.i(TAG+"==callSafe=="+e.getMessage());
        }
        return null;
    }
}
