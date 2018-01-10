package com.lody.virtual.client.hook.base;

import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.lody.virtual.DebugTool;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;
import com.lody.virtual.helper.utils.VLog;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Lody
 *         <p>
 *         HookHandler uses Java's {@link Proxy} to create a wrapper for existing services.
 *         <p>
 *         When any method is called on the wrapper, it checks if there is any {@link MethodProxy} registered
 *         and enabled for that method. If so, it calls the startUniformer instead of the wrapped implementation.
 *         <p>
 *         The whole thing is managed by a {@link MethodInvocationProxy} subclass
 */
@SuppressWarnings("unchecked")
public class MethodInvocationStub<T> {

    private static final String TAG = MethodInvocationStub.class.getSimpleName();

    private Map<String, MethodProxy> mInternalMethodProxies = new HashMap<>();
    private T mBaseInterface;
    private T mProxyInterface;
    private String mIdentityName;
    private LogInvocation.Condition mInvocationLoggingCondition = LogInvocation.Condition.NEVER;


    public Map<String, MethodProxy> getAllHooks() {
        return mInternalMethodProxies;
    }


    public MethodInvocationStub(T baseInterface, Class<?>... proxyInterfaces) {
        this.mBaseInterface = baseInterface;
        if (baseInterface != null) {
            if (proxyInterfaces == null) {
                proxyInterfaces = MethodParameterUtils.getAllInterface(baseInterface.getClass());
            }
            mProxyInterface = (T) Proxy.newProxyInstance(baseInterface.getClass().getClassLoader(), proxyInterfaces, new HookInvocationHandler());
        } else {
            VLog.d(TAG, "Unable to build HookDelegate: %s.", getIdentityName());
        }
    }

    public LogInvocation.Condition getInvocationLoggingCondition() {
        return mInvocationLoggingCondition;
    }

    public void setInvocationLoggingCondition(LogInvocation.Condition invocationLoggingCondition) {
        mInvocationLoggingCondition = invocationLoggingCondition;
    }

    public void setIdentityName(String identityName) {
        this.mIdentityName = identityName;
    }

    public String getIdentityName() {
        if (mIdentityName != null) {
            return mIdentityName;
        }
        return getClass().getSimpleName();
    }

    public MethodInvocationStub(T baseInterface) {
        this(baseInterface, (Class[]) null);
    }

    /**
     * Copy all proxies from the input HookDelegate.
     *
     * @param from the HookDelegate we copy from.
     */
    public void copyMethodProxies(MethodInvocationStub from) {
        DebugTool.i(TAG+"==copyMethodProxies==");
        this.mInternalMethodProxies.putAll(from.getAllHooks());
    }

    /**
     * Add a method proxy.
     *
     * @param methodProxy proxy
     */
    public MethodProxy addMethodProxy(MethodProxy methodProxy) {
        DebugTool.i(TAG+"==addMethodProxy=="+methodProxy.getMethodName());
        if (methodProxy != null && !TextUtils.isEmpty(methodProxy.getMethodName())) {
            if (mInternalMethodProxies.containsKey(methodProxy.getMethodName())) {
                VLog.w(TAG, "The Hook(%s, %s) you added has been in existence.", methodProxy.getMethodName(),
                        methodProxy.getClass().getName());
                return methodProxy;
            }
            mInternalMethodProxies.put(methodProxy.getMethodName(), methodProxy);
        }
        return methodProxy;
    }

    /**
     * Remove a method proxy.
     *
     * @param hookName proxy
     * @return The proxy you removed
     */
    public MethodProxy removeMethodProxy(String hookName) {
        DebugTool.i(TAG+"==removeMethodProxy=="+hookName);
        return mInternalMethodProxies.remove(hookName);
    }

    /**
     * Remove a method proxy.
     *
     * @param methodProxy target proxy
     */
    public void removeMethodProxy(MethodProxy methodProxy) {
        if (methodProxy != null) {
            removeMethodProxy(methodProxy.getMethodName());
        }
    }

    /**
     * Remove all method proxies.
     */
    public void removeAllMethodProxies() {
        mInternalMethodProxies.clear();
    }

    /**
     * Get the startUniformer by its name.
     *
     * @param name name of the Hook
     * @param <H>  Type of the Hook
     * @return target startUniformer
     */
    @SuppressWarnings("unchecked")
    public <H extends MethodProxy> H getMethodProxy(String name) {
        if(DebugTool.filterString(name))
            DebugTool.i(TAG+"==getMethodProxy=="+name);
        return (H) mInternalMethodProxies.get(name);
    }

    /**
     * @return Proxy interface
     */
    public T getProxyInterface() {
        return mProxyInterface;
    }

    /**
     * @return Origin Interface
     */
    public T getBaseInterface() {
        return mBaseInterface;
    }

    /**
     * @return count of the hooks
     */
    public int getMethodProxiesCount() {
        return mInternalMethodProxies.size();
    }

    private class HookInvocationHandler implements InvocationHandler {

        private final String TAG=HookInvocationHandler.class.getSimpleName();
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if(DebugTool.filterString(method.getName())){
                DebugTool.i(TAG+"==invoke=="+method.getName());
                DebugTool.i(TAG+"==[args==null]=="+(args==null));
                if(args!=null){
                    for (int i=0;i<args.length;i++){
                        //DebugTool.i(TAG+"==args=value="+args[i]);
                    }
                }
            }
            //getPermissionControllerPackageName通过系统API ApplicationPackageManager获取，VA没有Hook
            if("getPermissionControllerPackageName".equals(method.getName())){
                StackTraceElement[] staces=Thread.currentThread().getStackTrace();
                for(int i=0;i<staces.length;i++){
                    DebugTool.i(TAG+"===strace===getPermissionControllerPackageName==="+(staces[i]));
                }
            }
            MethodProxy methodProxy = getMethodProxy(method.getName());
            boolean useProxy = (methodProxy != null && methodProxy.isEnable());
            boolean mightLog = (mInvocationLoggingCondition != LogInvocation.Condition.NEVER) ||
                    (methodProxy != null && methodProxy.getInvocationLoggingCondition() != LogInvocation.Condition.NEVER);

            String argStr = null;
            Object res = null;
            Throwable exception = null;
            if (mightLog) {
                // Arguments to string is done before the method is called because the method might actually change it
                argStr = Arrays.toString(args);
                argStr = argStr.substring(1, argStr.length()-1);
            }


            try {
                if (useProxy && methodProxy.beforeCall(mBaseInterface, method, args)) {
                    res = methodProxy.call(mBaseInterface, method, args);
                    res = methodProxy.afterCall(mBaseInterface, method, args, res);
                } else {
                    res = method.invoke(mBaseInterface, args);
                }
                if("checkPermission".equals(method.getName())){
                    //筛选方法
                    //
                    if(args!=null){
                        for (int i=0;i<args.length;i++){
                            if(args[i]==null)
                                continue;
                            DebugTool.i(TAG+"==invoke==筛选方法参数=="+args[i].getClass().getSimpleName());
                            DebugTool.i(TAG+"==invoke==筛选方法参数=="+args[i]);
                            if("String".equals(args[i].getClass().getSimpleName())){
                                String params= (String) args[i];
                                if(params.contains("CAMERA")){
                                    //拒绝相机
                                    DebugTool.i(TAG+"==找到这里了=="+args[i]);
                                    //res=PackageManager.PERMISSION_DENIED;
                                }
                            }
                        }
                    }
                }
                return res;

            } catch (Throwable t) {
                exception = t;
                if (exception instanceof InvocationTargetException && ((InvocationTargetException) exception).getTargetException() != null) {
                    exception = ((InvocationTargetException) exception).getTargetException();
                    DebugTool.i(TAG+"==catch is InvocationTargetException=="+exception.getMessage());
                }
                DebugTool.i(TAG+"==catch EX=="+exception.getMessage());
                throw exception;
            } finally {
                if (mightLog) {
                    int logPriority = mInvocationLoggingCondition.getLogLevel(useProxy, exception != null);
                    if (methodProxy != null) {
                        logPriority = Math.max(logPriority, methodProxy.getInvocationLoggingCondition().getLogLevel(useProxy, exception != null));
                    }
                    if (logPriority >= 0) {
                        String retString;
                        if (exception != null) {
                            retString = exception.toString();
                        } else if (method.getReturnType().equals(void.class)) {
                            retString = "void";
                        } else {
                            retString = String.valueOf(res);
                        }

                        Log.println(logPriority, TAG, method.getDeclaringClass().getSimpleName() + "." + method.getName() + "(" + argStr + ") => " + retString);
                    }
                }
            }
        }

    }

    private void dumpMethodProxies() {
        StringBuilder sb = new StringBuilder(50);
        sb.append("*********************");
        for (MethodProxy proxy : mInternalMethodProxies.values()) {
            sb.append(proxy.getMethodName()).append("\n");
        }
        sb.append("*********************");
        DebugTool.i(TAG+"=="+sb.toString());
    }

}
