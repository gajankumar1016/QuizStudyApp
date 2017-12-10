package edu.illinois.finalproject.ProblemDisplayImplementation;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import edu.illinois.finalproject.DatabaseObjects.Problem;
import edu.illinois.finalproject.R;

public class AddProblemActivity extends AppCompatActivity {
    private String problemsKey;
    //Used to save path to current photo when user rotates the device
    private static final String CURRENT_PHOTO_PATH_TEXT_KEY = "currentPhotoPath";
    //Used as request code when launching and then receiving results from the camera app
    private static final int REQUEST_TAKE_PHOTO_PROBLEM = 1;
    private static final int REQUEST_TAKE_PHOTO_SOLUTION = 2;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_problem);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            problemsKey = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }

        database = FirebaseDatabase.getInstance();
        problemsRef = database.getReference("Problems");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        problemEditText = (EditText) findViewById(R.id.problemEditText);
        captureProblemPhotoButton = (Button) findViewById(R.id.captureProblemPhotoButton);
        problemImageButton = (ImageButton) findViewById(R.id.problemImageButton);
        answerEditText = (EditText) findViewById(R.id.answerEditText);
        solutionEditText = (EditText) findViewById(R.id.solutionEditText);
        captureSolutionPhotoButton = (Button) findViewById(R.id.captureSolutionPhotoButton);
        solutionImageButton = (ImageButton) findViewById(R.id.solutionImageButton);
        createButton = (Button) findViewById(R.id.createProblemButton);

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

        captureProblemPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_TAKE_PHOTO_PROBLEM);
            }
        });

        captureSolutionPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent(REQUEST_TAKE_PHOTO_SOLUTION);
            }
        });

        setUpCreateButton();
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

                String problemForNewProblem = "";
                if (mostRecentProblemPhotoPath != null) {
                    pushImageToDatabaseAndGetDownloadUrl(mostRecentProblemPhotoPath);
                    if (currentImageDownloadUrl != null) {
                        problemForNewProblem = currentImageDownloadUrl;
                        //reset for pushing solution to firebase
                        currentImageDownloadUrl = null;
                    }
                } else {
                    problemForNewProblem = problemInputText;
                }

                String solutionForNewProblem = "";
                if (mostRecentSolutionPhotoPath != null) {
                    pushImageToDatabaseAndGetDownloadUrl(mostRecentSolutionPhotoPath);
                    if (currentImageDownloadUrl != null) {
                        solutionForNewProblem = currentImageDownloadUrl;
                        currentImageDownloadUrl = null;
                    } else {
                        solutionForNewProblem = solutionInputText;
                    }
                }

                Problem newProblem = new Problem(problemForNewProblem, answer, solutionForNewProblem);
                problemsRef.child(problemsKey).push().setValue(newProblem);
                finish();
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
        if (!(requestCode == REQUEST_TAKE_PHOTO_PROBLEM || requestCode == REQUEST_TAKE_PHOTO_SOLUTION)) {
            displayToast("An request error occurred");
        }

        //At this point either the request code was for a problem photo or it was for a solution photo
        ImageButton currentImageButton;
        if (requestCode == REQUEST_TAKE_PHOTO_PROBLEM) {
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

    private void pushImageToDatabaseAndGetDownloadUrl(String pathToFile) {

        /*Obtains filename*/
        //The following line is derived from
        // https://stackoverflow.com/questions/26570084/how-to-get-file-name-from-file-path-in-android
        String imageFileName =
                pathToFile.substring(pathToFile.lastIndexOf("/") + 1);

        //Attempt to upload image to Firebase storage
        StorageReference imageStorageRef = mStorageRef.child(imageFileName);
        imageStorageRef.putFile(Uri.parse(pathToFile))
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getDownloadUrl() != null) {
                            currentImageDownloadUrl = taskSnapshot.getDownloadUrl().toString();
                            displayToast("Upload to Firebase succeeded!\n" + currentImageDownloadUrl);
                        } else {
                            currentImageDownloadUrl = null;
                            displayToast("Error: Could not get download URL");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
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
}
