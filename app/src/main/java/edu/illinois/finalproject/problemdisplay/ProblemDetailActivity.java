package edu.illinois.finalproject.problemdisplay;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.ViewImage;
import edu.illinois.finalproject.database.Problem;
import edu.illinois.finalproject.R;

public class ProblemDetailActivity extends AppCompatActivity {
    private Problem currentProblem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_detail);
        setTitle("Problem Details");

        Intent intentThatLaunchedThisActivity = getIntent();
        if (intentThatLaunchedThisActivity.hasExtra(Constants.PARCELABLE_EXTRA)) {
            currentProblem = intentThatLaunchedThisActivity.getParcelableExtra(Constants.PARCELABLE_EXTRA);

            final TextView displayProblemTextView = (TextView) findViewById(R.id.displayProblemTextView);
            final ImageButton problemImageButton = (ImageButton) findViewById(R.id.problemImageButton);
            final TextView displayAnswerTextView = (TextView) findViewById(R.id.displayAnswerTextView);
            final TextView displaySolutionTextView = (TextView) findViewById(R.id.displaySolutionTextView);
            final ImageButton solutionImageButton = (ImageButton) findViewById(R.id.solutionImageButton);

            //Depict problem as either an image or a TextView
            if (currentProblem.getProblem().startsWith(Constants.FIREBASE_STORAGE_URL)) {
                displayProblemTextView.setVisibility(View.GONE);
                Picasso.with(problemImageButton.getContext())
                        .load(currentProblem.getProblem()).into(problemImageButton);
            } else {
                problemImageButton.setVisibility(View.GONE);
                displayProblemTextView.setText(currentProblem.getProblem());
            }

            displayAnswerTextView.setText(currentProblem.getAnswer());

            //Display solution as either image or TextView
            if (currentProblem.getSolution().startsWith(Constants.FIREBASE_STORAGE_URL)) {
                displaySolutionTextView.setVisibility(View.GONE);
                Picasso.with(solutionImageButton.getContext())
                        .load(currentProblem.getSolution()).into(solutionImageButton);
            } else {
                solutionImageButton.setVisibility(View.GONE);
                displaySolutionTextView.setText(currentProblem.getSolution());
            }

            problemImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewImage.viewImageInGallery(currentProblem.getProblem(), v.getContext(), getPackageManager());
                }
            });

            solutionImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewImage.viewImageInGallery(currentProblem.getSolution(), v.getContext(), getPackageManager());
                }
            });
        }
    }
}
