package com.lody.virtual;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by musk on 17/12/4.
 */

public class DebugTool {


    public static void i(String info){
        Log.i("musk","=="+info+"==");
    }
    public static boolean filterString(String str){

        //先关闭过滤看看
        if(true){
            return true;
        }

        if(null==str)
            return false;
        if(str.contains("check")){
            return true;
        }else if(str.contains("Check")){
            return true;
        }else if(str.contains("permission")){
            return true;
        }else if(str.contains("Permission")){
            return true;
        }else {
            return false;
        }
    }
}
