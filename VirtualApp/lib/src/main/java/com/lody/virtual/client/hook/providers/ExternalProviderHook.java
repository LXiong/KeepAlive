package com.lody.virtual.client.hook.providers;

import com.lody.virtual.DebugTool;
import com.lody.virtual.client.core.VirtualCore;

import java.lang.reflect.Method;

/**
 * @author Lody
 */

public class ExternalProviderHook extends ProviderHook {

    public ExternalProviderHook(Object base) {
        super(base);
    }

    @Override
    protected void processArgs(Method method, Object... args) {
        DebugTool.i("ExternalProviderHook==processArgs=="+method.getName());
        if (args != null && args.length > 0 && args[0] instanceof String) {
            String pkg = (String) args[0];
            for (int i=0;i<args.length;i++){
                DebugTool.i("ExternalProviderHook==processArgs++=="+args[i]);
            }
            //将插件apk的包名替换成了VA的包名
            if (VirtualCore.get().isAppInstalled(pkg)) {
                args[0] = VirtualCore.get().getHostPkg();
            }
        }
    }
}
