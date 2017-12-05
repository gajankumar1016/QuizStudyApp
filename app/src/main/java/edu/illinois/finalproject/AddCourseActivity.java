package edu.illinois.finalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference coursesRef = database.getReference("Courses");
        final DatabaseReference unitsRef = database.getReference("Units");

        final EditText enterCourseEditText = (EditText) findViewById(R.id.enterCourseEditText);
        final Button createCourseButton = (Button) findViewById(R.id.createCourseButton);
        createCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String courseNameInput = enterCourseEditText.getText().toString();
                boolean isTextFieldEmpty = courseNameInput.equals("");
                if (isTextFieldEmpty) {
                    String errorMessage = "Enter a non-blank course name";
                    Toast.makeText(v.getContext(), errorMessage, Toast.LENGTH_LONG).show();

                } else {
                    String keyToUnits = unitsRef.push().getKey();
                    Course newCourse = new Course(courseNameInput, keyToUnits);
                    coursesRef.push().setValue(newCourse);
                    finish();
                }
            }
        });

    }
}