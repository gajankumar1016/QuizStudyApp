package edu.illinois.finalproject.unitdisplay;

import android.content.Context;
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

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.database.Unit;
import edu.illinois.finalproject.QuizQuestionActivity;
import edu.illinois.finalproject.R;

public class ViewUnitsActivity extends AppCompatActivity {
    private String keyToUnits;
    private Button addNewUnitButton;
    private Button receiveQuizQuestionButton;
    private FirebaseRecyclerAdapter unitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_units);
        setTitle("Units");

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            keyToUnits = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }

        setUpFirebaseAdapter();

        addNewUnitButton = (Button) findViewById(R.id.addNewProblemButton);
        addNewUnitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent addUnitIntent = new Intent(context, AddUnitActivity.class);
                addUnitIntent.putExtra(Intent.EXTRA_TEXT, keyToUnits);
                startActivity(addUnitIntent);
            }
        });

        receiveQuizQuestionButton = (Button) findViewById(R.id.receiveQuizQuestionButton);
        receiveQuizQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent quizQuestionIntent = new Intent(context, QuizQuestionActivity.class);
                quizQuestionIntent.putExtra(Intent.EXTRA_TEXT, keyToUnits);
                startActivity(quizQuestionIntent);
            }
        });
    }

    private void setUpFirebaseAdapter() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference unitsOfThisCourseRef = firebaseDatabase.getReference(Constants.FIREBASE_UNITS_ROOT).child(keyToUnits);

        final RecyclerView unitRecycler = (RecyclerView) findViewById(R.id.problemsRecyclerView);
        unitAdapter = new FirebaseRecyclerAdapter<Unit, UnitViewHolder>
                (Unit.class, R.layout.unit_item, UnitViewHolder.class, unitsOfThisCourseRef) {
                    @Override
                    protected void populateViewHolder(
                            UnitViewHolder viewHolder, Unit model, int position) {
                        viewHolder.bindUnit(model);
                    }
                };

        unitRecycler.setHasFixedSize(true);
        unitRecycler.setLayoutManager(new LinearLayoutManager(this));
        unitRecycler.setAdapter(unitAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unitAdapter.cleanup();
    }
}
