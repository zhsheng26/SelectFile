package com.wedotech.selectfilesample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.wedotech.selectfile.PhotoDisplayView;
import com.wedotech.selectfile.PhotoGridView;
import com.wedotech.selectfile.models.BaseFile;
import com.wedotech.selectfile.models.Photo;
import com.wedotech.selectfile.support.OnPhotoSelectedListener;

public class GridActivity extends AppCompatActivity {

    private PhotoGridView gridView;
    private PhotoDisplayView photoDisplayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);
        gridView = (PhotoGridView) findViewById(R.id.activity_photo_grid);
        photoDisplayView = (PhotoDisplayView) findViewById(R.id.displayView);
        photoDisplayView.showAllPhoto(this);
        photoDisplayView.setOnPhotoSelectedListener(new OnPhotoSelectedListener() {
            @Override
            public void photoSelected(BaseFile photo, int selectedCount) {
                gridView.addPhoto((Photo) photo);
            }
        });
    }
}
