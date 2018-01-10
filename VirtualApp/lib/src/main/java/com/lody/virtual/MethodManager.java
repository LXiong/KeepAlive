package com.lody.virtual;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by musk on 17/12/7.
 */

public class MethodManager {

    //白名单
    private static List<String> whileList= new ArrayList();

    public static void addPackageToWhileList(String pkgName){
        if(!whileList.contains(pkgName))
            whileList.add(pkgName);
    }

    public static boolean checkMethodPermission(String pkgName,String uid){
        return whileList.contains(pkgName);
    }
}
