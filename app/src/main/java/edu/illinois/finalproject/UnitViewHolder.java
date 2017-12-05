package edu.illinois.finalproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import edu.illinois.finalproject.DatabaseObjects.Unit;

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
        this.unitTextView = (TextView) itemView.findViewById(R.id.unitNameTextView);
        itemView.setOnClickListener(this);
    }

    public void bindUnit(Unit unit) {
        this.currentUnit = unit;
        unitTextView.setText(unit.getName());
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();

    }
}

//
//    @Override
//    public void onClick(View v) {
//        Context context = v.getContext();
//        Intent viewUnitsIntent = new Intent(context, ViewUnitsActivity.class);
//        viewUnitsIntent.putExtra(Intent.EXTRA_TEXT, currentCourse.getKeyToCourseOfUnits());
//        context.startActivity(viewUnitsIntent);
//    }
//}