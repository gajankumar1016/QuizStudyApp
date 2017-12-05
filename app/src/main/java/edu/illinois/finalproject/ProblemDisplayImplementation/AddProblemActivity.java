package edu.illinois.finalproject.ProblemDisplayImplementation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.illinois.finalproject.DatabaseObjects.Problem;
import edu.illinois.finalproject.R;

public class AddProblemActivity extends AppCompatActivity {
    private String problemsKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            problemsKey = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference problemsRef = database.getReference("Problems");

        final EditText problemEditText = (EditText) findViewById(R.id.problemEditText);
        final Button captureProblemPhotoButton = (Button) findViewById(R.id.captureProblemPhotoButton);
        final EditText answerEditText = (EditText) findViewById(R.id.answerEditText);
        final EditText solutionEditText = (EditText) findViewById(R.id.solutionEditText);
        final Button captureSolutionPhotoButton = (Button) findViewById(R.id.captureSolutionPhotoButton);
        final Button createButton = (Button) findViewById(R.id.createProblemButton);

        problemEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!problemEditText.getText().toString().equals("")) {
                    captureProblemPhotoButton.setVisibility(View.GONE);
                } else {
                    captureProblemPhotoButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        solutionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!solutionEditText.getText().toString().equals("")) {
                    captureSolutionPhotoButton.setVisibility(View.GONE);
                } else {
                    captureSolutionPhotoButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String problem = problemEditText.getText().toString();
                String answer = answerEditText.getText().toString();
                String solution = solutionEditText.getText().toString();

                if (problem.equals("")) {
                    Toast.makeText(v.getContext(), "Enter a problem", Toast.LENGTH_LONG).show();
                    return;
                }

                if (answer.equals("") && solution.equals("")) {
                    Toast.makeText(v.getContext(), "Enter an answer or solution", Toast.LENGTH_LONG).show();
                    return;
                }

                Problem newProblem = new Problem(problem, answer, solution);
                problemsRef.child(problemsKey).push().setValue(newProblem);
                finish();
            }
        });


    }
}
