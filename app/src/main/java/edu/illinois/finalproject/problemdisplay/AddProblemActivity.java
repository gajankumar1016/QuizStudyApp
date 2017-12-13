package edu.illinois.finalproject.problemdisplay;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.ViewImage;
import edu.illinois.finalproject.database.Problem;
import edu.illinois.finalproject.DatabaseUtils;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.unitdisplay.OnGetUrlListener;

public class AddProblemActivity extends AppCompatActivity {
    private String problemsKey;

    //used to check for image path if user rotates device
    private static final String MOST_RECENT_PROBLEM_PHOTO_PATH_EXTRA = "most recent problem photo path";
    private static final String MOST_RECENT_SOLUTION_PHOTO_PATH_EXTRA = "most recent solution photo path";

    //used as request code when launching and then receiving results from the camera app
    private static final int REQUEST_PROBLEM_IMAGE = 1;
    private static final int REQUEST_SOLUTION_IMAGE = 2;

    private StorageReference mStorageRef;
    //holds path to most recent jpg file for most recent picture taken by the camera
    private String mostRecentPhotoPath;
    private String mostRecentProblemPhotoPath;
    private String mostRecentSolutionPhotoPath;
    private String currentImageDownloadUrl;

    private ImageButton problemImageButton;
    private ImageButton solutionImageButton;

    private EditText problemEditText;
    private Button captureProblemPhotoButton;
    private EditText answerEditText;
    private EditText solutionEditText;
    private Button captureSolutionPhotoButton;
    private Button createButton;

    FirebaseDatabase database;
    DatabaseReference problemsRef;

    private Problem currentProblem = new Problem();
    private CountDownLatch countDownLatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);
        setTitle("Add Problem");

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            problemsKey = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }

        database = FirebaseDatabase.getInstance();
        problemsRef = database.getReference(Constants.FIREBASE_PROBLEMS_ROOT);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        problemEditText = (EditText) findViewById(R.id.problemEditText);
        captureProblemPhotoButton = (Button) findViewById(R.id.captureProblemPhotoButton);
        problemImageButton = (ImageButton) findViewById(R.id.problemImageButton);
        answerEditText = (EditText) findViewById(R.id.answerEditText);
        solutionEditText = (EditText) findViewById(R.id.solutionEditText);
        captureSolutionPhotoButton = (Button) findViewById(R.id.captureSolutionPhotoButton);
        solutionImageButton = (ImageButton) findViewById(R.id.solutionImageButton);
        createButton = (Button) findViewById(R.id.createProblemButton);

        setUpTextWatchers();

        setUpCapturePhotoButtons();

        setUpImageButtons();

        setUpCreateButton();

        //Reload images if the user has just rotated the device
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOST_RECENT_PROBLEM_PHOTO_PATH_EXTRA)) {
                mostRecentProblemPhotoPath = savedInstanceState.getString(MOST_RECENT_PROBLEM_PHOTO_PATH_EXTRA);
                Picasso.with(problemImageButton.getContext())
                        .load(mostRecentProblemPhotoPath).into(problemImageButton);
                problemImageButton.setVisibility(View.VISIBLE);
            }

            if (savedInstanceState.containsKey(MOST_RECENT_SOLUTION_PHOTO_PATH_EXTRA)) {
                mostRecentSolutionPhotoPath = savedInstanceState.getString(MOST_RECENT_SOLUTION_PHOTO_PATH_EXTRA);
                Picasso.with(solutionImageButton.getContext())
                        .load(mostRecentSolutionPhotoPath).into(solutionImageButton);
                solutionImageButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void setUpCapturePhotoButtons() {
        captureProblemPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_PROBLEM_IMAGE);
            }
        });

        captureSolutionPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_SOLUTION_IMAGE);
            }
        });
    }

    private void setUpImageButtons() {
        /*These buttons will only be visible if there is a valid path to a photo*/
        problemImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mostRecentProblemPhotoPath != null) {
                    ViewImage.viewImageInGallery(mostRecentProblemPhotoPath, v.getContext());
                }
            }
        });

        solutionImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mostRecentSolutionPhotoPath != null) {
                    ViewImage.viewImageInGallery(mostRecentSolutionPhotoPath, v.getContext());
                }
            }
        });
    }

    private void setUpTextWatchers() {
        problemEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!problemEditText.getText().toString().equals("")) {
                    captureProblemPhotoButton.setVisibility(View.GONE);
                    problemImageButton.setVisibility(View.GONE);
                } else {
                    captureProblemPhotoButton.setVisibility(View.VISIBLE);
                    problemImageButton.setVisibility(View.VISIBLE);
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
                    solutionImageButton.setVisibility(View.GONE);
                } else {
                    captureSolutionPhotoButton.setVisibility(View.VISIBLE);
                    solutionImageButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setUpCreateButton() {
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String problemInputText = problemEditText.getText().toString();
                String answer = answerEditText.getText().toString();
                String solutionInputText = solutionEditText.getText().toString();

                if (problemInputText.equals("") && mostRecentProblemPhotoPath == null) {
                    Toast.makeText(v.getContext(), "Enter a problem", Toast.LENGTH_LONG).show();
                    return;
                }

                if (answer.equals("") && solutionInputText.equals("") && mostRecentSolutionPhotoPath == null) {
                    Toast.makeText(v.getContext(), "Enter an answer or solution", Toast.LENGTH_LONG).show();
                    return;
                }

                /*At this point the problem is valid*/
                //the relevant sets will be overridden if the user inputted an image
                currentProblem.setProblem(problemInputText);
                currentProblem.setAnswer(answer);
                currentProblem.setSolution(solutionInputText);

                if (mostRecentProblemPhotoPath != null) {
                    boolean isLastUpload = mostRecentSolutionPhotoPath == null;
                    pushImageToDatabaseAndSetDownloadUrl(mostRecentProblemPhotoPath, currentProblem, isLastUpload, REQUEST_PROBLEM_IMAGE);
                }

                if (mostRecentSolutionPhotoPath != null) {
                    pushImageToDatabaseAndSetDownloadUrl(mostRecentSolutionPhotoPath, currentProblem, true, REQUEST_SOLUTION_IMAGE);
                } else {
                    problemsRef.child(problemsKey).push().setValue(currentProblem);
                    finish();
                }
            }
        });
    }

    /**
     * Launches camera app to allow user to take picture.
     * Almost exactly copied from https://developer.android.com/training/camera/photobasics.html
     */
    private void dispatchTakePictureIntent(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
                displayToast("Error occurred while creating the File to hold the picture.");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "edu.illinois.finalproject.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, requestCode);
            }
        }
    }

    /**
     * Creates JPG file to store picture that user will take.
     * Almost exactly copied from https://developer.android.com/training/camera/photobasics.html
     * @return a File object corresponding to the created JPG file
     * @throws IOException if the file cannot be created
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mostRecentPhotoPath = "file:" + imageFile.getAbsolutePath();
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            displayToast("An error occurred creating photo");
        }
        if (!(requestCode == REQUEST_PROBLEM_IMAGE || requestCode == REQUEST_SOLUTION_IMAGE)) {
            displayToast("An request error occurred");
        }

        //At this point either the request code was for a problem photo or it was for a solution photo
        ImageButton currentImageButton;
        if (requestCode == REQUEST_PROBLEM_IMAGE) {
            currentImageButton = problemImageButton;
            mostRecentProblemPhotoPath = mostRecentPhotoPath;
        } else {
            currentImageButton = solutionImageButton;
            mostRecentSolutionPhotoPath = mostRecentPhotoPath;
        }

        currentImageButton.setVisibility(View.VISIBLE);
        //Display the captured image in the ImageView
        Picasso.with(currentImageButton.getContext())
                .load(mostRecentPhotoPath).into(currentImageButton);
    }

    private void pushImageToDatabaseAndSetDownloadUrl(String pathToFile, final Problem newProblem,
                                                      final boolean isLastUpload, final int requestCode) {
        new DatabaseUtils().uploadImage(pathToFile, mStorageRef, new OnGetUrlListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                if (taskSnapshot.getDownloadUrl() != null) {
                    String downloadUrl = taskSnapshot.getDownloadUrl().toString();
                    if (requestCode == REQUEST_PROBLEM_IMAGE) {
                        newProblem.setProblem(downloadUrl);

                        if (isLastUpload) {
                            problemsRef.child(problemsKey).push().setValue(currentProblem);
                            finish();
                        }
                    } else if (requestCode == REQUEST_SOLUTION_IMAGE) {
                        newProblem.setSolution(downloadUrl);

                        //If the solution is set, it is ready to upload and we can exit this activity
                        problemsRef.child(problemsKey).push().setValue(currentProblem);
                        finish();
                    }
                    displayToast("Upload to Firebase succeeded!\n" + downloadUrl);
                } else {
                    currentImageDownloadUrl = null;
                    displayToast("Error: Could not get download URL");
                }
            }

            @Override
            public void onFailed(@NonNull Exception e) {
                e.printStackTrace();
                displayToast("Upload to Firebase failed.");
            }
        });
    }

    /**
     * Displays a Toast.LENGTH_LONG toast for the given message in the context of
     * this AddProblemActivity.
     * @param message String message to be displayed as a toast
     */
    private void displayToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mostRecentProblemPhotoPath != null) {
            outState.putString(MOST_RECENT_PROBLEM_PHOTO_PATH_EXTRA, mostRecentProblemPhotoPath);
        }

        if (mostRecentSolutionPhotoPath != null) {
            outState.putString(MOST_RECENT_SOLUTION_PHOTO_PATH_EXTRA, mostRecentSolutionPhotoPath);
        }
    }
}
