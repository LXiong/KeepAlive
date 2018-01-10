package com.lody.virtual.client.hook.base;

import android.content.Context;

import com.lody.virtual.DebugTool;
import com.lody.virtual.client.core.InvocationStubManager;
import com.lody.virtual.client.core.VirtualCore;
import com.lody.virtual.client.interfaces.IInjector;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

/**
 * @author Lody
 *         <p>
 *         This class is responsible with:
 *         - Instantiating a {@link MethodInvocationStub.HookInvocationHandler} on {@link #getInvocationStub()} ()}
 *         - Install a bunch of {@link MethodProxy}s, either with a @{@link Inject} annotation or manually
 *         calling {@link #addMethodProxy(MethodProxy)} from {@link #onBindMethods()}
 *         - Install the hooked object on the Runtime via {@link #inject()}
 *         <p>
 *         All {@link MethodInvocationProxy}s (plus a couple of other @{@link IInjector}s are installed by
 *         {@link InvocationStubManager}
 * @see Inject
 */
public abstract class MethodInvocationProxy<T extends MethodInvocationStub> implements IInjector {

    private static final String TAG=MethodInvocationProxy.class.getSimpleName();

    protected T mInvocationStub;

    public MethodInvocationProxy(T invocationStub) {
        DebugTool.i(TAG+"==new==");
        this.mInvocationStub = invocationStub;
        onBindMethods();
        afterHookApply(invocationStub);

        LogInvocation loggingAnnotation = getClass().getAnnotation(LogInvocation.class);
        if (loggingAnnotation != null) {
            invocationStub.setInvocationLoggingCondition(loggingAnnotation.value());
        }
    }

    protected void onBindMethods() {
        DebugTool.i(TAG+"==onBindMethods==");
        if (mInvocationStub == null) {
            return;
        }
        Class<? extends MethodInvocationProxy> clazz = getClass();
        Inject inject = clazz.getAnnotation(Inject.class);
        if (inject != null) {
            Class<?> proxiesClass = inject.value();
            Class<?>[] innerClasses = proxiesClass.getDeclaredClasses();
            for (Class<?> innerClass : innerClasses) {
                if (!Modifier.isAbstract(innerClass.getModifiers())
                        && MethodProxy.class.isAssignableFrom(innerClass)
                        && innerClass.getAnnotation(SkipInject.class) == null) {
                    addMethodProxy(innerClass);
                }
            }

        }
    }

    private void addMethodProxy(Class<?> hookType) {
        try {
            DebugTool.i(TAG+"==addMethodProxy=1="+hookType.getSimpleName());
            Constructor<?> constructor = hookType.getDeclaredConstructors()[0];
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            MethodProxy methodProxy;
            if (constructor.getParameterTypes().length == 0) {
                methodProxy = (MethodProxy) constructor.newInstance();
            } else {
                methodProxy = (MethodProxy) constructor.newInstance(this);
            }
            mInvocationStub.addMethodProxy(methodProxy);
        } catch (Throwable e) {
            DebugTool.i(TAG+"==addMethodProxy=="+ e.getMessage());
            throw new RuntimeException("Unable to instance Hook : " + hookType + " : " + e.getMessage());
        }
    }

    public MethodProxy addMethodProxy(MethodProxy methodProxy) {
        if(DebugTool.filterString(methodProxy.getMethodName())){
            DebugTool.i(TAG+"==addMethodProxy=2="+methodProxy.getMethodName());
        }
        return mInvocationStub.addMethodProxy(methodProxy);
    }

    protected void afterHookApply(T delegate) {
    }

    @Override
    public abstract void inject() throws Throwable;

    public Context getContext() {
        return VirtualCore.get().getContext();
    }

    public T getInvocationStub() {
        return mInvocationStub;
    }
}
