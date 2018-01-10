package com.seclib.musk;

import android.util.Log;

/**
 * Created by musk on 17/12/14.
 */

public class Debugger {
    private static final String TAG="musk";
    private static final boolean debugFlag=true;

    public static void i(String info){
        if(debugFlag)
            Log.i(TAG,"=="+info+"==");
    }
}
