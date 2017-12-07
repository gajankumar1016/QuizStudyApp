package edu.illinois.finalproject;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by gajan on 12/6/2017.
 * Derived from https://stackoverflow.com/questions/33723139/wait-firebase-async-retrive-data-in-android
 */

public class DatabaseUtils {

    public void readInFireBaseData(DatabaseReference databaseRef, final OnGetDataListener listener) {

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
    }
}
