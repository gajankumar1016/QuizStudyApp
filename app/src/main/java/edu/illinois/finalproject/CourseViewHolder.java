package edu.illinois.finalproject;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by gajan on 12/3/2017.
 */

public class CourseViewHolder extends RecyclerView.ViewHolder {

    private View itemView;
    private TextView courseTextView;

    public CourseViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.courseTextView = (TextView) itemView.findViewById(R.id.courseNameTextView);
    }

    public void bindCourse(Course course) {
        courseTextView.setText(course.getName());
    }
}
