package edu.illinois.finalproject.unitdisplay;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import edu.illinois.finalproject.database.Unit;
import edu.illinois.finalproject.R;
import edu.illinois.finalproject.problemdisplay.ViewProblemsActivity;

/**
 * Created by gajan on 12/4/2017.
 */

public class UnitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private View itemView;
    private TextView unitTextView;
    private Unit currentUnit;

    public UnitViewHolder(View itemView) {
        super(itemView);
        this.itemView = itemView;
        this.unitTextView = (TextView) itemView.findViewById(R.id.problemNameTextView);
        itemView.setOnClickListener(this);
    }

    public void bindUnit(Unit unit) {
        this.currentUnit = unit;
        unitTextView.setText(unit.getName());
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        Intent viewProblemsIntent = new Intent(context, ViewProblemsActivity.class);
        viewProblemsIntent.putExtra(Intent.EXTRA_TEXT, currentUnit.getKeyToUnitOfProblems());
        context.startActivity(viewProblemsIntent);
    }
}