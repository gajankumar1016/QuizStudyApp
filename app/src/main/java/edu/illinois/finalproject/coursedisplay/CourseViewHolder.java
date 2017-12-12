package edu.illinois.finalproject.coursedisplay;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import edu.illinois.finalproject.database.Course;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.unitdisplay.ViewUnitsActivity;

/**
 * Created by gajan on 12/3/2017.
 * Inspired by/adapted from https://www.learnhowtoprogram.com/android/data-persistence/firebase-recycleradapter
 */
public class CourseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private View itemView;
    private TextView courseTextView;
    private Course currentCourse;

    public CourseViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.courseTextView = (TextView) itemView.findViewById(R.id.problemNameTextView);
        itemView.setOnClickListener(this);
    }

    public void bindCourse(Course course) {
        this.currentCourse = course;
        courseTextView.setText(course.getName());
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        Intent viewUnitsIntent = new Intent(context, ViewUnitsActivity.class);
        viewUnitsIntent.putExtra(Intent.EXTRA_TEXT, currentCourse.getKeyToCourseOfUnits());
        context.startActivity(viewUnitsIntent);
    }
}
