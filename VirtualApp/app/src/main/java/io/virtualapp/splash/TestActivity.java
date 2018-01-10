package io.virtualapp.splash;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.musk.Utils.AppUtil;
import com.musk.alive.AliveService;
import com.seclib.musk.Skipper;

/**
 * Created by musk on 18/1/2.
 */

public class TestActivity extends Activity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        test();
    }

    private void test() {
        AppUtil.initAssetsData(this);
    }
}