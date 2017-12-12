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
import edu.illinois.finalproject.database.Problem;
import edu.illinois.finalproject.R;

public class ProblemDetailActivity extends AppCompatActivity {
    private Problem currentProblem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem_detail);

        Intent intentThatLaunchedThisActivity = getIntent();
        if (intentThatLaunchedThisActivity.hasExtra(Constants.PARCELABLE_EXTRA)) {
            currentProblem = intentThatLaunchedThisActivity.getParcelableExtra(Constants.PARCELABLE_EXTRA);

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

            problemImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewImageInGallery(currentProblem.getProblem());
                }
            });

            solutionImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewImageInGallery(currentProblem.getSolution());
                }
            });
        }
    }

    /**
     * Opens jpeg image at given URL in suitable photo gallery on user's device.
     * @param urlString url string for downloading jpeg image.
     */
    private void viewImageInGallery(String urlString) {
        //Derived from https://stackoverflow.com/questions/5383797/open-an-image-using-uri-in-androids-default-gallery-image-viewer
        Intent viewImageInGalleryIntent = new Intent();
        viewImageInGalleryIntent.setAction(Intent.ACTION_VIEW);
        viewImageInGalleryIntent.setDataAndType(Uri.parse(urlString), "image/jpeg");
        startActivity(viewImageInGalleryIntent);
    }
}
