package edu.illinois.finalproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edu.illinois.finalproject.coursedisplay.ViewCoursesActivity;

public class MainActivity extends AppCompatActivity {

    private Button goToCoursesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");

        goToCoursesButton = (Button) findViewById(R.id.goToCoursesButton);
        goToCoursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent viewCoursesIntent = new Intent(context, ViewCoursesActivity.class);
                startActivity(viewCoursesIntent);
            }
        });

    }
}
