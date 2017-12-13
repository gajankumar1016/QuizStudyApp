package edu.illinois.finalproject.coursedisplay;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.database.Course;
import edu.illinois.finalproject.R;

/**
 * Activity for viewing courses.
 * Derived from https://www.learnhowtoprogram.com/android/data-persistence/firebase-recycleradapter
 */
public class ViewCoursesActivity extends AppCompatActivity {

    private Button addNewCourseButton;
    private FirebaseRecyclerAdapter courseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_courses);
        setTitle("Courses");

        setUpFirebaseAdapter();

        addNewCourseButton = (Button) findViewById(R.id.addNewCourseButton);
        addNewCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent addCourseIntent = new Intent(context, AddCourseActivity.class);
                startActivity(addCourseIntent);
            }
        });
    }

    /**
     * Sets up FirebaseRecyclerAdapter and sets a RecyclerView with the adapter.
     */
    private void setUpFirebaseAdapter() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference coursesRef = database.getReference(Constants.FIREBASE_COURSES_ROOT);

        final RecyclerView courseRecycler = (RecyclerView) findViewById(R.id.problemsRecyclerView);
        courseAdapter = new FirebaseRecyclerAdapter<Course, CourseViewHolder>
                        (Course.class, R.layout.course_item, CourseViewHolder.class, coursesRef) {
                    @Override
                    protected void populateViewHolder(CourseViewHolder viewHolder, Course model, int position) {
                        viewHolder.bindCourse(model);
                    }
                };

        courseRecycler.setHasFixedSize(true);
        courseRecycler.setLayoutManager(new LinearLayoutManager(this));
        courseRecycler.setAdapter(courseAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        courseAdapter.cleanup();
    }
}
