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
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import edu.illinois.finalproject.DatabaseObjects.Problem;
import edu.illinois.finalproject.DatabaseObjects.Unit;

public class QuizQuestionActivity extends AppCompatActivity {
    private String unitsKey;
    private Map<String, Unit> keyToUnitMap;
    private Map<String, Problem> keyToProblemMap;
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
        quizProblemImageView = (ImageView) findViewById(R.id.quizProblemImageView);
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

        /*final CountDownLatch countDownLatch = new CountDownLatch(1);
        updateUnitsAndProblemsMaps();
        try {
            countDownLatch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        randomQuizProblem = selectRandomProblem();

        if (randomQuizProblem != null) {
            quizProblemTextView.setText(randomQuizProblem.getProblem());
        }*/
    }

    private void updateUnitsAndProblemsMaps() {
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

    private Problem selectRandomProblem() {
        if (keyToProblemMap == null) {
            return null;
        }
        Random random = new Random();
        int randomIndex = random.nextInt(keyToProblemMap.size());
        Problem[] problems = (Problem[]) keyToProblemMap.values().toArray();
        Log.d("Quiz Questions", Arrays.toString(problems));
        return problems[randomIndex];
    }
}
