package com.lody.virtual.client.hook.base;

import java.lang.reflect.Method;

import com.lody.virtual.DebugTool;
import com.lody.virtual.client.hook.utils.MethodParameterUtils;

/**
 * @author Lody
 */

public class ReplaceCallingPkgMethodProxy extends StaticMethodProxy {

	private static final String TAG=ReplaceCallingPkgMethodProxy.class.getSimpleName();

	public ReplaceCallingPkgMethodProxy(String name) {
		super(name);
		DebugTool.i(TAG+"==new=="+name);
	}

	@Override
	public boolean beforeCall(Object who, Method method, Object... args) {
		DebugTool.i(TAG+"==beforeCall=="+method.getName());
		MethodParameterUtils.replaceFirstAppPkg(args);
		return super.beforeCall(who, method, args);
	}
}
