package edu.illinois.finalproject.problemdisplay;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.database.Problem;
import edu.illinois.finalproject.R;

/**
 * Created by gajan on 12/5/2017.
 * Inspired by/adapted from https://www.learnhowtoprogram.com/android/data-persistence/firebase-recycleradapter
 */
public class ProblemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private View itemView;
    private TextView problemTextView;
    private ImageView problemImageView;
    private Problem currentProblem;

    public ProblemViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        problemTextView = (TextView) itemView.findViewById(R.id.problemNameTextView);
        problemImageView = (ImageView) itemView.findViewById(R.id.problemNameImageView);
        itemView.setOnClickListener(this);
    }

    public void bindProblem(Problem problem) {
        this.currentProblem = problem;
        if (currentProblem.getProblem().startsWith(Constants.FIREBASE_STORAGE_URL)) {
            problemTextView.setVisibility(View.GONE);
            Picasso.with(problemImageView.getContext())
                    .load(currentProblem.getProblem()).into(problemImageView);
        } else {
            problemImageView.setVisibility(View.GONE);
            problemTextView.setText(currentProblem.getProblem());
        }
        problemTextView.setText(problem.getProblem());
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        Intent viewProblemDetailIntent = new Intent(context, ProblemDetailActivity.class);
        viewProblemDetailIntent.putExtra(Constants.PARCELABLE_EXTRA, currentProblem);
        context.startActivity(viewProblemDetailIntent);
    }
}
