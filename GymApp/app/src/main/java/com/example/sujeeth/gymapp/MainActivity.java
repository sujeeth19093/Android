package com.example.sujeeth.gymapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.sujeeth.gymapp.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button next_workout = (Button) findViewById(R.id.currentWorkOut);
        next_workout.setOnClickListener(this);
        Button last_workout = (Button) findViewById(R.id.lastWorkOut);
        last_workout.setOnClickListener(this);
        Button log = (Button) findViewById(R.id.workoutLog);
        log.setOnClickListener(this);

    }

    public void onClick(View v) {
        Toast myToast;
        switch (v.getId()){
            case R.id.currentWorkOut:
                Intent intent = new Intent(this, DisplayMessageActivity.class);
                startActivity(intent);
                break;

            case R.id.lastWorkOut:
                myToast = Toast.makeText(getApplicationContext(), "Last", Toast.LENGTH_SHORT);
                myToast.show();
                break;

            case R.id.workoutLog:
                myToast = Toast.makeText(getApplicationContext(), "Workout Log", Toast.LENGTH_SHORT);
                myToast.show();
                break;
        }
    }
}
