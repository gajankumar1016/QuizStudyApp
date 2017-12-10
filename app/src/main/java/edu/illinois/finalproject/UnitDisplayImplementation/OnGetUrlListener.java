package edu.illinois.finalproject.UnitDisplayImplementation;

import android.support.annotation.NonNull;


import com.google.firebase.storage.UploadTask;

/**
 * Created by gajan on 12/9/2017.
 */
public interface OnGetUrlListener {
    public void onStart();
    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot);
    public void onFailed(@NonNull Exception e);
}
