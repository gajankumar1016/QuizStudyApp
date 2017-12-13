package edu.illinois.finalproject.coursedisplay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.database.Course;
import edu.illinois.finalproject.R;

public class AddCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        setTitle("Add Course");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference coursesRef = database.getReference(Constants.FIREBASE_COURSES_ROOT);
        final DatabaseReference unitsRef = database.getReference(Constants.FIREBASE_UNITS_ROOT);

        final EditText enterCourseEditText = (EditText) findViewById(R.id.enterUnitEditText);
        final Button createCourseButton = (Button) findViewById(R.id.createUnitButton);
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
