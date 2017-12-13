package edu.illinois.finalproject.problemdisplay;

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
import edu.illinois.finalproject.database.Problem;
import edu.illinois.finalproject.R;

public class ViewProblemsActivity extends AppCompatActivity {
    private String keyToProblems;
    private Button addNewProblemButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_problems);
        setTitle("Problems");

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            keyToProblems = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference problemsOfThisUnitRef = firebaseDatabase.getReference(Constants.FIREBASE_PROBLEMS_ROOT).child(keyToProblems);

        final RecyclerView unitRecycler = (RecyclerView) findViewById(R.id.problemsRecyclerView);
        FirebaseRecyclerAdapter<Problem, ProblemViewHolder> problemAdapter =
                new FirebaseRecyclerAdapter<Problem, ProblemViewHolder>(
                        Problem.class, R.layout.problem_item, ProblemViewHolder.class, problemsOfThisUnitRef) {
                    @Override
                    protected void populateViewHolder(ProblemViewHolder viewHolder, Problem model, int position) {
                        viewHolder.bindProblem(model);
                    }
                };

        unitRecycler.setHasFixedSize(true);
        unitRecycler.setLayoutManager(new LinearLayoutManager(this));
        unitRecycler.setAdapter(problemAdapter);

        addNewProblemButton = (Button) findViewById(R.id.addNewProblemButton);
        addNewProblemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent addUnitIntent = new Intent(context, AddProblemActivity.class);
                addUnitIntent.putExtra(Intent.EXTRA_TEXT, keyToProblems);
                startActivity(addUnitIntent);
            }
        });
    }
}
