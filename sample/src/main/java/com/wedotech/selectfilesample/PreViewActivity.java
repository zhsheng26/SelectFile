package com.wedotech.selectfilesample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.wedotech.selectfile.PhotoPreViewPager;
import com.wedotech.selectfile.models.Photo;

import java.util.ArrayList;

/**
 * Created by zhsheng on 2016/11/18.
 */

public class PreViewActivity extends AppCompatActivity {

    private PhotoPreViewPager photoPreView;

    public static void start(Context context, ArrayList<Photo> photos) {
        Intent starter = new Intent(context, PreViewActivity.class);
        starter.putExtra("photos", photos);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        photoPreView = new PhotoPreViewPager(this);
        setContentView(photoPreView);
        photoPreView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ArrayList<Photo> photos = getIntent().getParcelableArrayListExtra("photos");
        photoPreView.putData(photos);
    }
}
