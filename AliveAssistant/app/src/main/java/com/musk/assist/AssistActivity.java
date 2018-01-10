package com.musk.assist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by musk on 17/12/21.
 * 这里有一个坑:
 * 两个参数的onCreate方法，不会正常按照生命周期回调，只会在特定条件下被动回调！！！！切记
 * PersistableBundle persistentState
 */

public class AssistActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, AssistService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
    }
}
