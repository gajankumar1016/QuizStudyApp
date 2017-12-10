package edu.illinois.finalproject.UnitDisplayImplementation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import edu.illinois.finalproject.Constants;
import edu.illinois.finalproject.DatabaseObjects.Unit;
import edu.illinois.finalproject.R;

public class AddUnitActivity extends AppCompatActivity {
    private String unitsKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_unit);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
            unitsKey = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference unitsRef = database.getReference(Constants.FIREBASE_UNITS_ROOT);
        final DatabaseReference problemsRef = database.getReference(Constants.FIREBASE_PROBLEMS_ROOT);

        final EditText enterUnitEditText = (EditText) findViewById(R.id.enterUnitEditText);
        final Button createUnitButton = (Button) findViewById(R.id.createUnitButton);
        createUnitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unitNameInput = enterUnitEditText.getText().toString();
                boolean isTextFieldEmpty = unitNameInput.equals("");
                if (isTextFieldEmpty) {
                    String errorMessage = "Enter a non-blank unit name";
                    Toast.makeText(v.getContext(), errorMessage, Toast.LENGTH_LONG).show();

                } else {
                    String keyToProblems = problemsRef.push().getKey();
                    Unit newUnit = new Unit(unitNameInput, keyToProblems);
                    unitsRef.child(unitsKey).push().setValue(newUnit);
                    finish();
                }
            }
        });
    }
}
