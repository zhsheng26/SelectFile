package com.wedotech.selectfilesample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wedotech.selectfile.models.Photo;
import com.wedotech.selectfile.scissors.CropView;

import java.util.ArrayList;

public class ScissorActivity extends AppCompatActivity {

    private CropView cropView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scissor);
        cropView = (CropView) findViewById(R.id.scissorView);
        startActivityForResult(new Intent(this, PhotoGridActivity.class), 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            ArrayList<Photo> photos = data.getParcelableArrayListExtra("photos");
            if (photos.size() == 0) return;
            cropView.extensions()
                    .load(photos.get(0).getPath());
        }
    }
}
