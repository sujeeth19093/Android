package com.example.sujeeth.dochelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class patientInfo extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mDatabase;

    Button LookUpOtherPatient;
    EditText patientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        Intent intent = getIntent();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        LookUpOtherPatient = (Button) findViewById(R.id.lookUpOtherPatientButton);
        LookUpOtherPatient.setOnClickListener(this);

    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.lookUpOtherPatientButton:
                Intent lookUpOtherPatientIntent = new Intent(this,loginSuccess.class);
                //defaultUI();
                startActivity(lookUpOtherPatientIntent);
                break;
        }
    }
}
