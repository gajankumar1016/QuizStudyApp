package edu.illinois.finalproject.ProblemDisplayImplementation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

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
            final TextView displayAnswerTextView = (TextView) findViewById(R.id.displayAnswerTextView);
            final TextView displaySolutionTextView = (TextView) findViewById(R.id.displaySolutionTextView);

            displayProblemTextView.setText(currentProblem.getProblem());
            displayAnswerTextView.setText(currentProblem.getAnswer());
            displaySolutionTextView.setText(currentProblem.getSolution());

        }
    }
}
