package edu.illinois.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.illinois.finalproject.DatabaseObjects.Unit;

public class ViewUnitsActivity extends AppCompatActivity {
    private String keyToUnits;
    private Button addNewUnitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_units);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            keyToUnits = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference unitsRef = firebaseDatabase.getReference("Units");

        final RecyclerView unitRecycler = (RecyclerView) findViewById(R.id.unitsRecyclerView);
        FirebaseRecyclerAdapter<Unit, UnitViewHolder> unitAdapter =
                new FirebaseRecyclerAdapter<Unit, UnitViewHolder>(Unit.class, R.layout.unit_item, UnitViewHolder.class, unitsRef) {
                    @Override
                    protected void populateViewHolder(UnitViewHolder viewHolder, Unit model, int position) {
                        viewHolder.bindUnit(model);
                    }
                };

        unitRecycler.setHasFixedSize(true);
        unitRecycler.setLayoutManager(new LinearLayoutManager(this));
        unitRecycler.setAdapter(unitAdapter);

        addNewUnitButton = (Button) findViewById(R.id.addNewUnitButton);
        addNewUnitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        addNewCourseButton = (Button) findViewById(R.id.addNewCourseButton);
//        addNewCourseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Context context = v.getContext();
//                Intent addCourseIntent = new Intent(context, AddCourseActivity.class);
//                startActivity(addCourseIntent);
//            }
//        });
    }
}
