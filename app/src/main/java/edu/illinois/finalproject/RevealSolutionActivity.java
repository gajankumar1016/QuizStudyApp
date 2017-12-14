package edu.illinois.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.illinois.finalproject.database.Problem;

public class RevealSolutionActivity extends AppCompatActivity {
    private TextView answerHeaderTextView;
    private TextView displayAnswerTextView;
    private TextView solutionHeaderTextView;
    private TextView displaySolutionTextView;
    private ImageButton solutionImageButton;
    private Problem quizProblem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reveal_solution);
        setTitle("Answer/Solution");

        Intent intentThatLaunchedThisActivity = getIntent();
        if (intentThatLaunchedThisActivity.hasExtra(Constants.PARCELABLE_EXTRA)) {
            quizProblem = intentThatLaunchedThisActivity.getParcelableExtra(Constants.PARCELABLE_EXTRA);

            answerHeaderTextView = (TextView) findViewById(R.id.answerHeaderTextView);
            displayAnswerTextView = (TextView) findViewById(R.id.displayAnswerTextView);
            solutionHeaderTextView = (TextView) findViewById(R.id.solutionHeaderTextView);
            displaySolutionTextView = (TextView) findViewById(R.id.displaySolutionTextView);
            solutionImageButton = (ImageButton) findViewById(R.id.solutionImageButton);

            setUpGuiComponents();
        }
    }

    private void setUpGuiComponents() {
        //Set up/hide necessary answer TextViews
        if (quizProblem.getAnswer().equals("")) {
            answerHeaderTextView.setVisibility(View.GONE);
            displayAnswerTextView.setVisibility(View.GONE);
        } else {
            displayAnswerTextView.setText(quizProblem.getAnswer());
        }

        if (quizProblem.getSolution().equals("")) {
            //handles case if there is no solution and only an answer
            solutionHeaderTextView.setVisibility(View.GONE);
            displaySolutionTextView.setVisibility(View.GONE);
            solutionImageButton.setVisibility(View.GONE);
        } else {
            //load solution into TextView or ImageButton depending on type of solution
            if (quizProblem.getSolution().startsWith(Constants.FIREBASE_STORAGE_URL)) {
                displaySolutionTextView.setVisibility(View.GONE);
                Picasso.with(solutionImageButton.getContext())
                        .load(quizProblem.getSolution()).into(solutionImageButton);
            } else {
                solutionImageButton.setVisibility(View.GONE);
                displaySolutionTextView.setText(quizProblem.getSolution());
            }
        }

        //The button will be GONE if the solution is not a download url
        solutionImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewImage.viewImageInGallery(quizProblem.getSolution(), v.getContext(), getPackageManager());
            }
        });
    }
}
