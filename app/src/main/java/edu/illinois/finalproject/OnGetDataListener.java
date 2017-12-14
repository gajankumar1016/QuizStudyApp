package edu.illinois.finalproject;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Provides abstract methods to handle
 * Derived from https://stackoverflow.com/questions/33723139/wait-firebase-async-retrive-data-in-android
 */
public interface OnGetDataListener {
    public void onStart();
    public void onSuccess(DataSnapshot dataSnapshot);
    public void onFailed(DatabaseError databaseError);
}
