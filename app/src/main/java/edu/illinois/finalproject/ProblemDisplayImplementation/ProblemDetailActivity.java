package edu.illinois.finalproject.ProblemDisplayImplementation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.DatabaseObjects.Problem;
import edu.illinois.finalproject.R;

public class ProblemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_detail);

        Intent intentThatLaunchedThisActivity = getIntent();
        if (intentThatLaunchedThisActivity.hasExtra("parcelable_extra")) {
            final Problem currentProblem =
                    intentThatLaunchedThisActivity.getParcelableExtra("parcelable_extra");

            final TextView displayProblemTextView = (TextView) findViewById(R.id.displayProblemTextView);
            final ImageButton problemImageButton = (ImageButton) findViewById(R.id.problemImageButton);
            final TextView displayAnswerTextView = (TextView) findViewById(R.id.displayAnswerTextView);
            final TextView displaySolutionTextView = (TextView) findViewById(R.id.displaySolutionTextView);
            final ImageButton solutionImageButton = (ImageButton) findViewById(R.id.solutionImageButton);

            if (currentProblem.getProblem().startsWith(Constants.FIREBASE_STORAGE_URL)) {
                displayProblemTextView.setVisibility(View.GONE);
                Picasso.with(problemImageButton.getContext())
                        .load(currentProblem.getProblem()).into(problemImageButton);
            } else {
                problemImageButton.setVisibility(View.GONE);
                displayProblemTextView.setText(currentProblem.getProblem());
            }

            displayAnswerTextView.setText(currentProblem.getAnswer());

            if (currentProblem.getSolution().startsWith(Constants.FIREBASE_STORAGE_URL)) {
                displaySolutionTextView.setVisibility(View.GONE);
                Picasso.with(solutionImageButton.getContext())
                        .load(currentProblem.getSolution()).into(solutionImageButton);
            } else {
                solutionImageButton.setVisibility(View.GONE);
                displaySolutionTextView.setText(currentProblem.getSolution());
            }
        }
    }
}
