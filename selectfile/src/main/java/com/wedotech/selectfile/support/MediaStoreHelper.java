package com.wedotech.selectfile.support;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.wedotech.selectfile.cursors.DocScannerTask;
import com.wedotech.selectfile.cursors.loadercallbacks.FileResultCallback;
import com.wedotech.selectfile.cursors.loadercallbacks.PhotoDirLoaderCallbacks;
import com.wedotech.selectfile.models.Document;
import com.wedotech.selectfile.models.PhotoDirectory;


public class MediaStoreHelper {

    public static void getPhotoDirs(FragmentActivity activity, Bundle args, FileResultCallback<PhotoDirectory> resultCallback) {
        activity.getSupportLoaderManager()
                .initLoader(0, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
    }

    public static void getDocs(FragmentActivity activity, FileResultCallback<Document> fileResultCallback) {
        new DocScannerTask(activity, fileResultCallback).execute();
    }
}