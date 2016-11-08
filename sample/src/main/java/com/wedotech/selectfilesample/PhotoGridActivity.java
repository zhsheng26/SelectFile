package com.wedotech.selectfilesample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wedotech.selectfile.PhotoDisplayView;
import com.wedotech.selectfile.models.BaseFile;
import com.wedotech.selectfile.models.Photo;
import com.wedotech.selectfile.support.OnPhotoSelectedListener;

import java.util.ArrayList;

public class PhotoGridActivity extends AppCompatActivity {
    public static void start(Context context, ArrayList<Photo> files) {
        Intent starter = new Intent(context, PhotoGridActivity.class);
        starter.putParcelableArrayListExtra("photos", files);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_grid);
        ArrayList<Photo> photos = getIntent().getParcelableArrayListExtra("photos");
        PhotoDisplayView displayView = (PhotoDisplayView) findViewById(R.id.photos);
        displayView.showAllPhoto(this);
        /*displayView.setupPhotos(photos);
        displayView.setOnPhotoSelectedListener(new OnPhotoSelectedListener() {
            @Override
            public void photoSelected(BaseFile photo, int selectedCount) {

            }
        });*/

    }
}
