package com.lody.virtual.client.hook.base;

import com.lody.virtual.DebugTool;

/**
 * @author Lody
 */

public class StaticMethodProxy extends MethodProxy {

	private String mName;

	public StaticMethodProxy(String name) {
		this.mName = name;

		DebugTool.i("StaticMethodProxy==new=="+name);
	}

	@Override
	public String getMethodName() {
		DebugTool.i("StaticMethodProxy==getMethodName==");
		return mName;
	}
}
