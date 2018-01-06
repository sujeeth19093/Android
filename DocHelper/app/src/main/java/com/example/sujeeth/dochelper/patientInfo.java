package com.example.sujeeth.dochelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class patientInfo extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mDatabase,ref;

    private Button LookUpOtherPatient, FullPatientInfo;
    private TextView PatientName, PatientAge, PatientGender;
    private String p_id;
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        Intent intent = getIntent();
        p_id = intent.getStringExtra("patientID");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = mDatabase.child(("Patients")).child(p_id);

        LookUpOtherPatient = (Button) findViewById(R.id.lookUpOtherPatientButton);
        LookUpOtherPatient.setOnClickListener(this);
        FullPatientInfo = (Button) findViewById(R.id.fullPatientInfoButton);
        FullPatientInfo.setOnClickListener(this);

        PatientName = (TextView)findViewById(R.id.patientNameTxtV);
        PatientAge = (TextView)findViewById(R.id.patientAgeTxtV);
        PatientGender  = (TextView) findViewById(R.id.patientGenderTxtV);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setInfo(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setInfo(DataSnapshot dataSnapshot)
    {
        patient = dataSnapshot.getValue(Patient.class);

        PatientName.setText(patient.first_name + " " + patient.last_name);
        PatientAge.setText(patient.date_of_birth);
        String gen = patient.gender;
        switch(gen)
        {
            case "M":PatientGender.setText("Male"); break;
            case "F":PatientGender.setText("Female"); break;
            case "U":PatientGender.setText("Unspecified"); break;
        }
    }

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.lookUpOtherPatientButton:
                Intent lookUpOtherPatientIntent = new Intent(this,loginSuccess.class);
                startActivity(lookUpOtherPatientIntent);
                break;

            case R.id.fullPatientInfoButton:
                Intent fullPatientIntent = new Intent(this, fullPatientInfo.class);
                fullPatientIntent.putExtra("patientID",p_id);
                startActivity(fullPatientIntent);
                break;
        }
    }
}
