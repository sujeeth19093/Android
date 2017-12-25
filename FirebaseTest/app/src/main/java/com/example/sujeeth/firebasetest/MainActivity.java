package com.example.sujeeth.firebasetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private DatabaseReference mDatabase;

    Button Save, Clear;
    EditText Name, Age, Gender, Height, Weight, Bp_Over, Bp_Under, History;
    CheckBox Diabetic;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Save = (Button) findViewById(R.id.saveButton);
        Save.setOnClickListener(this);
        Clear = (Button) findViewById(R.id.clearButton);
        Clear.setOnClickListener(this);

        Name = (EditText) findViewById(R.id.enterNameTxtBox);
        Age = (EditText) findViewById(R.id.enterAgeTxtBox);
        Gender = (EditText) findViewById(R.id.genderTxtBox);
        Height = (EditText) findViewById(R.id.heightTxtBox);
        Weight = (EditText) findViewById(R.id.weightTxtBox);
        Bp_Over = (EditText) findViewById(R.id.upperBPTxtBox);
        Bp_Under = (EditText) findViewById(R.id.lowerBPTxtBox);
        History = (EditText) findViewById(R.id.patientHistoryTxtBox);

        Diabetic = (CheckBox) findViewById(R.id.diabeticChkBox);
    }

    @IgnoreExtraProperties
    public static class Patient
    {
        public String name, age, gender, height, weight, bp_over, bp_under, history;
        public boolean diabetic;

        public Patient()
        {

        }
    }

    private void addNewPatient(String p_name,String p_age,String p_gender,String p_height,
                               String p_weight,String p_bp_over,String p_bp_under,String p_history, boolean p_diabetic)
    {
        String p_ID = p_name + "001";

        Patient patient = new Patient();
        patient.name = p_name;
        patient.age = p_age;
        patient.gender = p_gender;
        patient.height = p_height;
        patient.weight = p_weight;
        patient.bp_over = p_bp_over;
        patient.bp_under = p_bp_under;
        patient.history = p_history;
        patient.diabetic = p_diabetic;

        mDatabase.child("patients").child(p_ID).setValue(patient);
    }

    private boolean validateForm()
    {
        boolean valid = true;

        return valid;
    }

    private void clearFunc(ViewGroup group)
    {
        for(int i = 0, count = group.getChildCount(); i < count; ++i)
        {
            View view = group.getChildAt(i);

            if(view instanceof EditText)
            {
                ((EditText)view).setText("");
            }

            if(view instanceof CheckBox)
            {
                ((CheckBox)view).setChecked(false);
            }
        }
    }

    public void onClick(View v)
    {
        Toast myToast;
        switch(v.getId())
        {
            case R.id.saveButton:
                if(validateForm())
                {
                    addNewPatient(Name.getText().toString(),Age.getText().toString(),Gender.getText().toString(),Height.getText().toString(),
                            Weight.getText().toString(),Bp_Over.getText().toString(),Bp_Under.getText().toString(),History.getText().toString(),
                            Diabetic.isChecked());
                    myToast = Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT);
                    myToast.show();
                }else
                {

                }
                break;
            case R.id.clearButton:
                clearFunc((ViewGroup) findViewById(R.id.scrollLayout));
                break;
        }

    }
}