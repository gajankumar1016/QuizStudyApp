package edu.illinois.finalproject.coursedisplayimplementation;

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

import edu.illinois.finalproject.databaseobjects.Course;
import edu.illinois.finalproject.R;

public class ViewCoursesActivity extends AppCompatActivity {

    private View itemView;
    private TextView courseTextView;
    private Button addNewCourseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_courses);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference coursesRef = database.getReference("Courses");

        final RecyclerView courseRecycler = (RecyclerView) findViewById(R.id.problemsRecyclerView);
        FirebaseRecyclerAdapter<Course, CourseViewHolder> courseAdapter =
                new FirebaseRecyclerAdapter<Course, CourseViewHolder>
                        (Course.class, R.layout.course_item, CourseViewHolder.class, coursesRef) {
                    @Override
                    protected void populateViewHolder(CourseViewHolder viewHolder, Course model, int position) {
                        viewHolder.bindCourse(model);
                    }
                };

        courseRecycler.setHasFixedSize(true);
        courseRecycler.setLayoutManager(new LinearLayoutManager(this));
        courseRecycler.setAdapter(courseAdapter);

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
}
