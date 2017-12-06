package com.example.sujeeth.docaid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class loginSuccess extends AppCompatActivity implements View.OnClickListener{

    Button Go,addNew;
    EditText patientID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        Intent intent = getIntent();

        Go = (Button)findViewById(R.id.goButton);
        Go.setOnClickListener(this);
        addNew = (Button)findViewById(R.id.addNewButton);
        addNew.setOnClickListener(this);

        patientID = (EditText)findViewById(R.id.patientIDTxtBox);
        patientID.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
    }

    public void onClick(View v){
        switch(v.getId())
        {
            case R.id.goButton:
                Intent goIntent = new Intent(this, patientInfo.class);
                startActivity(goIntent);
                break;
            case R.id.addNewButton:
                Intent addNewIntent = new Intent(this, addPatientInfo.class);
                startActivity(addNewIntent);
                break;
        }
    }
}
