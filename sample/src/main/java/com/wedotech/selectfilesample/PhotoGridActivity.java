package com.wedotech.selectfilesample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wedotech.selectfile.PhotoDisplayView;

public class PhotoGridActivity extends AppCompatActivity {

    private PhotoDisplayView displayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_grid);
        displayView = (PhotoDisplayView) findViewById(R.id.photos);
        displayView.showAllPhoto(this);
        displayView.setMaxCount(1);
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putParcelableArrayListExtra("photos", displayView.getSelectedPhotos());
        setResult(Activity.RESULT_OK, data);
        super.onBackPressed();
    }
}
