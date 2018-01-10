package com.lody.virtual.client.hook.base;

import android.os.IBinder;
import android.os.IInterface;

import com.lody.virtual.DebugTool;

import mirror.RefStaticMethod;
import mirror.android.os.ServiceManager;

/**
 * @author Paulo Costa
 *
 * @see MethodInvocationProxy
 */
public abstract class BinderInvocationProxy extends MethodInvocationProxy<BinderInvocationStub> {

	private static final String TAG=BinderInvocationProxy.class.getSimpleName();

	protected String mServiceName;

	public BinderInvocationProxy(IInterface stub, String serviceName) {
		this(new BinderInvocationStub(stub), serviceName);
		DebugTool.i(TAG+"==new=1="+serviceName);
	}

	public BinderInvocationProxy(RefStaticMethod<IInterface> asInterfaceMethod, String serviceName) {
		this(new BinderInvocationStub(asInterfaceMethod, ServiceManager.getService.call(serviceName)), serviceName);
		DebugTool.i(TAG+"==new=2="+serviceName);

	}

	public BinderInvocationProxy(Class<?> stubClass, String serviceName) {
		this(new BinderInvocationStub(stubClass, ServiceManager.getService.call(serviceName)), serviceName);
		DebugTool.i(TAG+"==new=3="+serviceName);
	}

	public BinderInvocationProxy(BinderInvocationStub hookDelegate, String serviceName) {
		super(hookDelegate);
		this.mServiceName = serviceName;
		DebugTool.i(TAG+"==new=4="+serviceName);
	}

	@Override
	public void inject() throws Throwable {
		DebugTool.i(TAG+"==inject==");
		getInvocationStub().replaceService(mServiceName);
	}

	@Override
	public boolean isEnvBad() {
		IBinder binder = ServiceManager.getService.call(mServiceName);
		DebugTool.i(TAG+"==isEnvBad=="+(binder != null && getInvocationStub() != binder));
		return binder != null && getInvocationStub() != binder;
	}
}
