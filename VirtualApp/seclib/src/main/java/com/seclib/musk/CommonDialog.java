package com.seclib.musk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.musk.demon.R;

/**
 * Created by musk on 17/12/18.
 * 通用对话框
 */

public class CommonDialog extends Activity {

    private TextView tvDialogTitle,tvDialogContent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog);
        initView();
    }

    private void initView() {
        tvDialogTitle= (TextView) findViewById(R.id.dialog_title);
        tvDialogContent= (TextView) findViewById(R.id.dialog_content);

        Intent in=getIntent();
        tvDialogTitle.setText(in.getStringExtra("title"));
        tvDialogContent.setText(in.getStringExtra("content"));
    }

    public void clickConfirm(View v){
        finish();
    }
}
