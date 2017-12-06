package edu.illinois.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import edu.illinois.finalproject.DatabaseObjects.Problem;
import edu.illinois.finalproject.DatabaseObjects.Unit;

public class QuizQuestionActivity extends AppCompatActivity {
    private String unitsKey;
    private Map<String, Unit> keyToUnitMap;
    private Map<String, Problem> keyToProblemMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            unitsKey = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference unitsRef = database.getReference("Units").child(unitsKey);
        unitsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //https://firebase.google.com/docs/reference/android/com/google/firebase/database/GenericTypeIndicator
                GenericTypeIndicator<Map<String, Unit>> unitsGti =
                        new GenericTypeIndicator<Map<String, Unit>>() {};
                keyToUnitMap = dataSnapshot.getValue(unitsGti);
                updateKeyToProblemsMap();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateKeyToProblemsMap() {
        keyToProblemMap = new HashMap<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference problemsRef = database.getReference("Problems");

        for (Unit unit : keyToUnitMap.values()) {
            String problemsKey = unit.getKeyToUnitOfProblems();
            DatabaseReference problemsOfThisUnitRef = problemsRef.child(problemsKey);
            problemsOfThisUnitRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<Map<String, Problem>> problemsGti =
                            new GenericTypeIndicator<Map<String, Problem>>() {};
                    Map<String, Problem> tempKeyToProblemMap = dataSnapshot.getValue(problemsGti);

                    //the map could be null if the user hasn't created problems for the current unit yet.
                    if (tempKeyToProblemMap != null) {
                        for (Map.Entry<String, Problem> entry : tempKeyToProblemMap.entrySet()) {
                            Log.d("QuizQuestion", entry.getKey() + " " + entry.getValue());
                            keyToProblemMap.put(entry.getKey(), entry.getValue());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
