package com.example.sujeeth.dochelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class fullPatientInfo extends AppCompatActivity implements View.OnClickListener{

    private TextView FName,LName, Dob, Height, Weight, Addr, PrimaryN,
            SecondaryN, Bp, Email, Diabetic, Gender, BloodGrp;

    private Button Edit;
    private String p_id;
    private DatabaseReference mDatabase, ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_patient_info);
        Intent intent = getIntent();
        p_id = intent.getStringExtra("patientID");

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = mDatabase.child("Patients").child(p_id);

        FName = (TextView) findViewById(R.id.fnameTV);
        LName = (TextView) findViewById(R.id.lnameTV);
        Dob = (TextView) findViewById(R.id.dobTV);
        Height = (TextView) findViewById(R.id.hgtTV);
        Weight = (TextView) findViewById(R.id.wgtTV);
        Addr = (TextView) findViewById(R.id.addrTV);
        PrimaryN = (TextView) findViewById(R.id.primTV);
        SecondaryN = (TextView) findViewById(R.id.secTV);
        Bp = (TextView) findViewById(R.id.bpTV);
        Email = (TextView) findViewById(R.id.emlTV);
        Diabetic = (TextView) findViewById(R.id.diaTV);
        Gender = (TextView) findViewById(R.id.genTV);
        BloodGrp = (TextView) findViewById(R.id.bgrpTV);

        Edit = (Button) findViewById(R.id.editFullInfoButton);
        Edit.setOnClickListener(this);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                setData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setData(DataSnapshot dataSnapshot)
    {
        Patient patient = dataSnapshot.getValue(Patient.class);

        FName.setText(patient.first_name);
        LName.setText(patient.last_name);
        Dob.setText(patient.date_of_birth);
        Height.setText(patient.height);
        Weight.setText(patient.weight);
        Bp.setText(patient.blood_pressure);
        BloodGrp.setText(patient.blood_group);
        Addr.setText(patient.address);
        Email.setText(patient.email_id);
        PrimaryN.setText(patient.primary_number + " (" + patient.primary_number_type + ")");
        SecondaryN.setText(patient.secondary_number + " (" + patient.secondary_number_type + ")");
        switch (patient.gender)
        {
            case "M": Gender.setText("Male"); break;
            case "F": Gender.setText("Female"); break;
            case "U": Gender.setText("Unspecified"); break;
        }
        if(patient.diabetic)
        {
            Diabetic.setText("Yes");
        }else
        {
            Diabetic.setText("No");
        }
    }

    public void onClick(View view)
    {
        if(view.getId() == R.id.editFullInfoButton)
        {
            Intent n_intent = new Intent(this, addPatientInfo.class);
            n_intent.putExtra("patientID",p_id);
            startActivity(n_intent);
        }
    }
}
