package edu.illinois.finalproject;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import edu.illinois.finalproject.UnitDisplayImplementation.OnGetUrlListener;

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

    public void uploadImage(final String pathToFile, StorageReference storageReference, final OnGetUrlListener listener) {
        /*Obtains filename*/
        //The following line is derived from
        // https://stackoverflow.com/questions/26570084/how-to-get-file-name-from-file-path-in-android
        String imageFileName =
                pathToFile.substring(pathToFile.lastIndexOf("/") + 1);

        //Attempt to upload image to Firebase storage
        StorageReference imageStorageRef = storageReference.child(imageFileName);
        imageStorageRef.putFile(Uri.parse(pathToFile))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        listener.onSuccess(taskSnapshot);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onFailed(e);
                    }
                });
    }
}
