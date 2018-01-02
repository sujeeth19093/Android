package com.example.sujeeth.dochelper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Calendar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

public class addPatientInfo extends AppCompatActivity implements OnClickListener{

    private DatabaseReference mDatabase;

    private Button Save, Clear, CalenderB;
    private EditText Name, Age, Height, Weight, Bp_Over, Bp_Under, History;
    private CheckBox Diabetic;
    private Spinner Gender;

    private int year, month, day;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_info);
        Intent intent = getIntent();

        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Save = (Button) findViewById(R.id.saveButton);
        Save.setOnClickListener(this);
        Clear = (Button) findViewById(R.id.cancelButton);
        Clear.setOnClickListener(this);
        CalenderB = (Button) findViewById(R.id.calenderButton);
        CalenderB.setOnClickListener(this);

        Name = (EditText) findViewById(R.id.enterFNameTxtBox);
        Age = (EditText) findViewById(R.id.enterAgeTxtBox);
        Height = (EditText) findViewById(R.id.heightTxtBox);
        Weight = (EditText) findViewById(R.id.weightTxtBox);
        Bp_Over = (EditText) findViewById(R.id.upperBPTxtBox);
        Bp_Under = (EditText) findViewById(R.id.lowerBPTxtBox);
        History = (EditText) findViewById(R.id.patientHistoryTxtBox);

        Diabetic = (CheckBox) findViewById(R.id.diabeticChkBox);

        Gender = (Spinner) findViewById(R.id.genderSpinner);
        String gender_string[]={"Gender","Male","Female","Unspecified"};
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, gender_string);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Gender.setAdapter(myAdapter);
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

    private void addNewPatient(String p_name,String p_age/*,String p_gender*/,String p_height,
                               String p_weight,String p_bp_over,String p_bp_under,String p_history, boolean p_diabetic)
    {
        String p_ID = p_name + "001";

        Patient patient = new Patient();
        patient.name = p_name;
        patient.age = p_age;
        patient.gender = "M";
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
                    addNewPatient(Name.getText().toString(),Age.getText().toString(),Height.getText().toString(),
                            Weight.getText().toString(),Bp_Over.getText().toString(),Bp_Under.getText().toString(),History.getText().toString(),
                            Diabetic.isChecked());
                    myToast = Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT);
                    myToast.show();
                    clearFunc((ViewGroup) findViewById(R.id.scrollLayout));
                    Intent loginSuccessIntent = new Intent(this, loginSuccess.class);
                    startActivity(loginSuccessIntent);
                }else
                {

                }
                break;
            case R.id.cancelButton:
                clearFunc((ViewGroup) findViewById(R.id.scrollLayout));
                Intent loginSuccessIntent = new Intent(this, loginSuccess.class);
                startActivity(loginSuccessIntent);
                break;
            case R.id.calenderButton:
                showDialog(0);
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if(id == 0)
        {
            return new DatePickerDialog(this, calenderListener, year, month, day);
        }else
        {
            return null;
        }
    }

    private DatePickerDialog.OnDateSetListener calenderListener
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
            year = i;
            month = i1;
            day = i2;
            Age.setText(day+"/"+month+"/"+year);
        }
    };
}

