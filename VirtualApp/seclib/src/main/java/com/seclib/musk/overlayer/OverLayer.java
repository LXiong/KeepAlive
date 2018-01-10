package com.seclib.musk.overlayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;
import com.musk.demon.R;
import com.seclib.musk.CommonDialog;
import com.seclib.musk.Debugger;
import com.seclib.musk.compat.Huawei;

/**
 * Created by musk on 17/12/28.
 */

public class OverLayer {

    private static View sLayer;
    private static WindowManager.LayoutParams sLayerParams;

    public static void addLayer(final Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = windowManager.getDefaultDisplay().getWidth();
        int screenHeight = windowManager.getDefaultDisplay().getHeight();
        if (sLayer == null) {
            sLayer = LayoutInflater.from(context).inflate(R.layout.layout_over_layer, null);
            sLayer.findViewById(R.id.over_layer).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Huawei.isMe() && Build.VERSION.SDK_INT>=24){
                        Intent intent=new Intent(context, CommonDialog.class);
                        intent.putExtra("title", "友情提示");
                        intent.putExtra("content", "请依次清理!");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }else {
                        Toast.makeText(context, "请依次清理！", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            if (sLayerParams == null) {
                sLayerParams = new LayoutParams();
                sLayerParams.type = LayoutParams.TYPE_PHONE;
                sLayerParams.format = PixelFormat.RGBA_8888;
                sLayerParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                sLayerParams.gravity =Gravity.TOP;
                sLayerParams.height=dip2px(context,100);
                sLayerParams.x = screenWidth/2;
                sLayerParams.y = screenHeight - dip2px(context, 70);
            }
            Debugger.i("==addView==");
            windowManager.addView(sLayer, sLayerParams);
        }
    }

    public static void removeLayer(Context context) {
        if (sLayer != null) {
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.removeView(sLayer);
            sLayer = null;
        }
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}