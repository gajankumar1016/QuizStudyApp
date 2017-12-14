package edu.illinois.finalproject.problemdisplay;

import android.support.annotation.NonNull;


import com.google.firebase.storage.UploadTask;

/**
 * Provides abstract methods to handle the resulting UploadTask.TaskSnapshot after an image is uploaded to Firebase.
 * Derived from: Derived from https://stackoverflow.com/questions/33723139/wait-firebase-async-retrive-data-in-android
 */
public interface OnGetUrlListener {
    public void onStart();
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot);
    public void onFailed(@NonNull Exception e);
}
