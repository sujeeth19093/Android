package com.example.sujeeth.dochelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class patientInfo extends AppCompatActivity implements View.OnClickListener{

    Button Go;
    EditText patientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        Intent intent = getIntent();

        Go = (Button) findViewById(R.id.goButton2);
        Go.setOnClickListener(this);

        patientID = (EditText) findViewById(R.id.patientIDTxtBox2);
        patientID.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.goButton2:

                break;
        }
    }
}
