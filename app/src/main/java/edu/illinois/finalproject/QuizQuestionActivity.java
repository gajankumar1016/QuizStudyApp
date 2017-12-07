package edu.illinois.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.illinois.finalproject.DatabaseObjects.Problem;
import edu.illinois.finalproject.DatabaseObjects.Unit;

public class QuizQuestionActivity extends AppCompatActivity {
    private String unitsKey;
    private Map<String, Unit> keyToUnitMap = new HashMap<>();
    private Map<String, Problem> keyToProblemMap = new HashMap<>();
    private TextView quizProblemTextView;
    private ImageView quizProblemImageView;
    private Problem randomQuizProblem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_question);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            unitsKey = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }

        quizProblemTextView = (TextView) findViewById(R.id.quizProblemTextView);
        quizProblemImageView = (ImageView) findViewById(R.id.quizProblemImageButton);
        final EditText enterAnswerEditText = (EditText) findViewById(R.id.enterAnswerEditText);
        final Button checkAnswerButton = (Button) findViewById(R.id.checkAnswerButton);
        final Button viewSolutionButton = (Button) findViewById(R.id.viewSolutionButton);
        final Button giveUpButton = (Button) findViewById(R.id.giveUpButton);

        /*Hide buttons as necessary
         */

        checkAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userAnswer = enterAnswerEditText.getText().toString();
                if (userAnswer.equals("")) {
                    Toast.makeText(v.getContext(), "Enter an answer", Toast.LENGTH_LONG).show();

                } else {
                    Intent revealAnswerIntent = new Intent(v.getContext(), RevealSolutionActivity.class);
                    //Send problem and the answer the user typed in
                    startActivity(revealAnswerIntent);
                }
            }
        });

        viewSolutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewSolutionIntent = new Intent(v.getContext(), RevealSolutionActivity.class);
                //send problem
                startActivity(viewSolutionIntent);
            }
        });

        giveUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewSolutionIntent = new Intent(v.getContext(), RevealSolutionActivity.class);
                //send problem
                startActivity(viewSolutionIntent);
            }
        });
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference unitsRef = database.getReference("Units").child(unitsKey);
        final DatabaseReference problemsRef = database.getReference("Problems");
        readProblemsFromDatabaseAndDisplayRandomProblem(unitsRef, problemsRef);

    }

    //https://stackoverflow.com/questions/33723139/wait-firebase-async-retrive-data-in-android
    private void readProblemsFromDatabaseAndDisplayRandomProblem(final DatabaseReference unitsRef, final DatabaseReference problemsRef) {
        new DatabaseUtils().readInFireBaseData(unitsRef, new OnGetDataListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //https://firebase.google.com/docs/reference/android/com/google/firebase/database/GenericTypeIndicator
                GenericTypeIndicator<Map<String, Unit>> unitsGti =
                        new GenericTypeIndicator<Map<String, Unit>>() {};
                keyToUnitMap = dataSnapshot.getValue(unitsGti);

                if (keyToUnitMap != null) {
                    Log.d("QuizQuestions", keyToUnitMap.toString());

                    List<Unit> unitsList = new ArrayList<>(keyToUnitMap.values());
                    for (int i = 0; i < unitsList.size(); i++) {

                        String problemsKey = unitsList.get(i).getKeyToUnitOfProblems();
                        DatabaseReference problemsOfThisUnitRef = problemsRef.child(problemsKey);

                        //indicate if this is the last unit so that a random problem can be generated
                        //and displayed to the user
                        if (i == unitsList.size() - 1) {
                            readProblemsData(problemsOfThisUnitRef, true);
                        } else {
                            readProblemsData(problemsOfThisUnitRef, false);
                        }

                    }
                } else {
                    quizProblemTextView.setText(R.string.noProblems);
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }

    //https://stackoverflow.com/questions/33723139/wait-firebase-async-retrive-data-in-android
    private void readProblemsData(DatabaseReference problemsRef, final boolean isLastUnit) {
        new DatabaseUtils().readInFireBaseData(problemsRef, new OnGetDataListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                //https://firebase.google.com/docs/reference/android/com/google/firebase/database/GenericTypeIndicator
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

                if (isLastUnit) {
                    randomQuizProblem = selectRandomProblem();

                    if (randomQuizProblem != null) {
                        quizProblemTextView.setText(randomQuizProblem.getProblem());
                    } else {
                        quizProblemTextView.setText(R.string.noProblems);
                    }
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }

    private Problem selectRandomProblem() {
        if (keyToProblemMap.size() == 0) {
            return null;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(keyToProblemMap.size());
        List<Problem> problemList = new ArrayList<>(keyToProblemMap.values());
        Log.d("Quiz Questions", problemList.get(randomIndex).toString());
        return problemList.get(randomIndex);
    }
}
