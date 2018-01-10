package com.lody.virtual.client.hook.base;

import com.lody.virtual.DebugTool;

import java.lang.reflect.Method;

/**
 * @author Lody
 */

public class ReplaceSpecPkgMethodProxy extends StaticMethodProxy {

	private int index;

	public ReplaceSpecPkgMethodProxy(String name, int index) {
		super(name);
		this.index = index;
		DebugTool.i("ReplaceSpecPkgMethodProxy==new=="+name);
	}

	@Override
	public boolean beforeCall(Object who, Method method, Object... args) {
		DebugTool.i("ReplaceSpecPkgMethodProxy==beforeCall=="+method.getName());
		if (args != null) {
			int i = index;
			if (i < 0) {
				i += args.length;
			}
			if (i >= 0 && i < args.length && args[i] instanceof String) {
				args[i] = getHostPkg();
			}
		}
		return super.beforeCall(who, method, args);
	}
}
