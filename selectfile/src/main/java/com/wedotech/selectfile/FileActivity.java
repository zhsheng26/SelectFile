package com.wedotech.selectfile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class FileActivity extends AppCompatActivity {

    public static void start(Context context, int pickType) {
        Intent starter = new Intent(context, FileActivity.class);
        starter.putExtra("pickType", pickType);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
    }
}
