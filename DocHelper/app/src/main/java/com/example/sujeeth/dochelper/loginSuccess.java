package com.example.sujeeth.dochelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginSuccess extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mDatabase;

    Button Go,addNew;
    EditText patientID;
    TextView noPatientExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        Intent intent = getIntent();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Go = (Button)findViewById(R.id.goButton);
        Go.setOnClickListener(this);
        addNew = (Button)findViewById(R.id.addNewButton);
        addNew.setOnClickListener(this);

        patientID = (EditText)findViewById(R.id.patientIDTxtBox);
        patientID.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);

        noPatientExists = (TextView) findViewById(R.id.newPatientTxtV);
    }

    private void defaultUI()
    {
        patientID.setText("");
        noPatientExists.setText("");
    }

    public void onClick(View v){
        switch(v.getId())
        {
            case R.id.goButton:
                mDatabase.child("patients").child(patientID.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            Intent goIntent = new Intent(loginSuccess.this, patientInfo.class);
                            defaultUI();
                            startActivity(goIntent);
                        }else
                        {
                            noPatientExists.setText("Patient does not exist, please add patient");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
            case R.id.addNewButton:
                Intent addNewIntent = new Intent(this, addPatientInfo.class);
                defaultUI();
                startActivity(addNewIntent);
                break;
        }
    }
}