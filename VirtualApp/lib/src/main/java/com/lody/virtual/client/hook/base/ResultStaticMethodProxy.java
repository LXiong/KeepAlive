package com.lody.virtual.client.hook.base;

import com.lody.virtual.DebugTool;

import java.lang.reflect.Method;

/**
 * @author Lody
 */

public class ResultStaticMethodProxy extends StaticMethodProxy {

	Object mResult;

	public ResultStaticMethodProxy(String name, Object result) {
		super(name);
		mResult = result;
		if(DebugTool.filterString(name))
			DebugTool.i("ResultStaticMethodProxy==new=="+name);
	}

	public Object getResult() {
		DebugTool.i("ResultStaticMethodProxy==getResult=="+mResult);
		return mResult;
	}

	@Override
	public Object call(Object who, Method method, Object... args) throws Throwable {
		DebugTool.i("ResultStaticMethodProxy==call=="+method.getName());
		return mResult;
	}
}
