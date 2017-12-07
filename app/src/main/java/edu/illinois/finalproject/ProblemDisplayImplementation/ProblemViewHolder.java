package edu.illinois.finalproject.ProblemDisplayImplementation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import edu.illinois.finalproject.DatabaseObjects.Problem;
import edu.illinois.finalproject.R;

/**
 * Created by gajan on 12/5/2017.
 * Inspired by/adapted from https://www.learnhowtoprogram.com/android/data-persistence/firebase-recycleradapter
 */
public class ProblemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private View itemView;
    private TextView problemTextView;
    private Problem currentProblem;

    public ProblemViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        problemTextView = (TextView) itemView.findViewById(R.id.problemNameTextView);
        itemView.setOnClickListener(this);
    }

    public void bindProblem(Problem problem) {
        this.currentProblem = problem;
        problemTextView.setText(problem.getProblem());
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        Intent viewProblemDetailIntent = new Intent(context, ProblemDetailActivity.class);
        viewProblemDetailIntent.putExtra("parcelable_extra", currentProblem);
        context.startActivity(viewProblemDetailIntent);
    }
}
