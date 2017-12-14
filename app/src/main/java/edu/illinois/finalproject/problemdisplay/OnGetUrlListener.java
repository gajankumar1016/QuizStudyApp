package edu.illinois.finalproject.problemdisplay;

import android.support.annotation.NonNull;


import com.google.firebase.storage.UploadTask;

/**
 * Provides abstract methods to handle the resulting UploadTask.TaskSnapshot after an image is uploaded to Firebase.
 * Derived from: Derived from https://stackoverflow.com/questions/33723139/wait-firebase-async-retrive-data-in-android
 */
public interface OnGetUrlListener {
    public void onStart();
    /**Takes in/handles UploadTask.TaskSnapshot when an image is uploaded to Firebase successfully.*/
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot);
    /**Takes in/handles an Exception that arises when uploading image to Firebase*/
    public void onFailed(@NonNull Exception e);
}
