package com.example.sujeeth.dochelper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.provider.ContactsContract;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.ValueEventListener;

public class addPatientInfo extends AppCompatActivity implements OnClickListener{

    private DatabaseReference mDatabase, ref;
    private FirebaseAuth mAuth;

    private Button Save, Clear, Cancel, Update, CalenderB;
    private EditText FName,LName, Dob, Height, Weight, Addr1, Addr2, PrimaryN, SecondaryN, Bp_Over, Bp_Under, Email;
    private CheckBox Diabetic;
    private Spinner Gender, BloodGrp, PrimaryNType, SecondaryNtype;

    private int year, month, day;
    private int present_year, present_month, present_day;
    private String p_id;
    private boolean update = false;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_info);
        Intent intent = getIntent();
        if(intent.getExtras() != null)
        {
            p_id = intent.getStringExtra("patientID");
            if(!p_id.equals(""))
            {
                updateUI(1);
                update = true;
            }else
            {
                updateUI(0);
            }
        }else
        {
            updateUI(0);
        }

        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        present_year = cal.get(Calendar.YEAR);
        present_month = cal.get(Calendar.MONTH) + 1;
        present_day = cal.get(Calendar.DAY_OF_MONTH);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Save = (Button) findViewById(R.id.saveButton);
        Save.setOnClickListener(this);
        Clear = (Button) findViewById(R.id.clearButton);
        Clear.setOnClickListener(this);
        Cancel = (Button)findViewById(R.id.cancelButton);
        Cancel.setOnClickListener(this);
        Update = (Button) findViewById(R.id.updateButton);
        Update.setOnClickListener(this);
        CalenderB = (Button) findViewById(R.id.calenderButton);
        CalenderB.setOnClickListener(this);

        FName = (EditText) findViewById(R.id.enterFNameTxtBox);
        LName = (EditText) findViewById(R.id.enterLNameTxtBox);
        Dob = (EditText) findViewById(R.id.enterDOBTxtBox);
        Height = (EditText) findViewById(R.id.heightTxtBox);
        Weight = (EditText) findViewById(R.id.weightTxtBox);
        Addr1 = (EditText) findViewById(R.id.patientAddr1TxtBox);
        Addr2 = (EditText) findViewById(R.id.patientAddr2TxtBox);
        PrimaryN = (EditText) findViewById(R.id.patientPrimaryNTxtBox);
        SecondaryN = (EditText) findViewById(R.id.patientSecondaryNTxtBox);
        Bp_Over = (EditText) findViewById(R.id.upperBPTxtBox);
        Bp_Under = (EditText) findViewById(R.id.lowerBPTxtBox);
        Email = (EditText) findViewById(R.id.patientEmailTxtBox);

        Diabetic = (CheckBox) findViewById(R.id.diabeticChkBox);

        Gender = (Spinner) findViewById(R.id.genderSpinner);
        String gender_string[]={"Gender","Male","Female","Unspecified"};
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, gender_string);
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Gender.setAdapter(myAdapter);

        PrimaryNType = (Spinner) findViewById(R.id.patientPrimaryNumberSpinner);
        SecondaryNtype = (Spinner) findViewById(R.id.patientSecondaryNumberSpinner);

        String phone_type_string[]={"Mobile","Landline"};
        ArrayAdapter<String> phoneAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, phone_type_string);
        phoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        PrimaryNType.setAdapter(phoneAdapter);
        SecondaryNtype.setAdapter(phoneAdapter);

        BloodGrp = (Spinner) findViewById(R.id.bloodGrpSpinner);
        String bld_grp[] = {"Select blood group", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
        ArrayAdapter<String> bloodgrpAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, bld_grp);
        bloodgrpAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        BloodGrp.setAdapter(bloodgrpAdapter);

        if(update){
            ref = mDatabase.child("Patients").child(p_id);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    updateInfo(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void updateUI(int i)
    {
        if(i == 0)
        {
            findViewById(R.id.addLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.updateLayout).setVisibility(View.GONE);
        }

        if(i == 1)
        {
            findViewById(R.id.addLayout).setVisibility(View.GONE);
            findViewById(R.id.updateLayout).setVisibility(View.VISIBLE);
        }
    }

    private void updateInfo(DataSnapshot dataSnapshot)
    {
        String str;
        String[] bp;
        Patient p = dataSnapshot.getValue(Patient.class);

        FName.setText(p.first_name);
        LName.setText(p.last_name);
        Dob.setText(p.date_of_birth);
        Height.setText(p.height);
        Weight.setText(p.weight);
        Addr1.setText(p.address);
        PrimaryN.setText(p.primary_number);
        SecondaryN.setText(p.secondary_number);
        Email.setText(p.email_id);

        str = p.blood_pressure;
        bp = str.split("/");
        Bp_Over.setText(bp[0]);
        Bp_Under.setText(bp[1]);

        if(p.diabetic)
            Diabetic.setChecked(true);
        else
            Diabetic.setChecked(false);

        switch(p.gender)
        {
            case "M": Gender.setSelection(1); break;
            case "F": Gender.setSelection(2); break;
            case "U": Gender.setSelection(3); break;
        }

        switch (p.primary_number_type)
        {
            case "Mobile": PrimaryNType.setSelection(0); break;
            case "Landline": PrimaryNType.setSelection(1); break;
        }

        switch (p.secondary_number_type)
        {
            case "Mobile": SecondaryNtype.setSelection(0); break;
            case "Landline": SecondaryNtype.setSelection(1); break;
        }

        switch (p.blood_group)
        {
            case "A+": BloodGrp.setSelection(1);break;
            case "A-": BloodGrp.setSelection(2);break;
            case "B+": BloodGrp.setSelection(3);break;
            case "B-": BloodGrp.setSelection(4);break;
            case "AB+": BloodGrp.setSelection(5);break;
            case "AB-": BloodGrp.setSelection(6);break;
            case "O+": BloodGrp.setSelection(7);break;
            case "O-": BloodGrp.setSelection(8);break;
        }
    }

    private void addOrUpdatePatient(String fname,String lname,String dob,String gen,String hgt, String wgt,
                               String bgp,String addr,String pn,String pnt,String sn,String snt,String bp,
                               String eml,String con,boolean dia)
    {
        String p_id = lname + ":" + fname;

        final Patient p = new Patient();
        p.first_name = fname;
        p.last_name = lname;
        p.date_of_birth =dob;
        p.gender = gen;
        p.height = hgt;
        p.weight = wgt;
        p.blood_group = bgp;
        p.address = addr;
        p.primary_number = pn;
        p.primary_number_type = pnt;
        p.secondary_number = sn;
        p.secondary_number_type = snt;
        p.blood_pressure = bp;
        p.email_id = eml;
        p.diabetic = dia;

        FirebaseUser user = mAuth.getCurrentUser();

        if(update)
        {
            p.modified_by = user.getUid();
            p.modified_on = con;
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ref.setValue(p);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }else
        {
            p.created_by = user.getUid();
            p.created_on = con;
            mDatabase.child("Patients").child(p_id).setValue(p);
        }
    }

    private void populateDatabase()
    {
        String gen = "",addr = "",bp = "",con = "";
        switch (Gender.getSelectedItemPosition())
        {
            case 1:
                gen = "M";
                break;
            case 2:
                gen = "F";
                break;
            case 3:
                gen = "U";
                break;
        }

        if(Addr2.getText().toString().equals(""))
        {
            addr = Addr1.getText().toString();
        }else
        {
            addr = Addr1.getText().toString() + ", " + Addr2.getText().toString();
        }

        bp = Bp_Over.getText().toString() + "/" + Bp_Under.getText().toString();

        con = present_day + "/" + present_month + "/" + present_year;

        addOrUpdatePatient(FName.getText().toString(),LName.getText().toString(),Dob.getText().toString(),
                    gen,Height.getText().toString(),Weight.getText().toString(),BloodGrp.getSelectedItem().toString(),
                    addr,PrimaryN.getText().toString(),PrimaryNType.getSelectedItem().toString(),SecondaryN.getText().toString(),
                SecondaryNtype.getSelectedItem().toString(),bp,Email.getText().toString(),con,Diabetic.isChecked());
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
                    populateDatabase();
                    myToast = Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_SHORT);
                    myToast.show();
                    clearFunc((ViewGroup) findViewById(R.id.scrollLayout));
                    Intent loginSuccessIntent = new Intent(this, loginSuccess.class);
                    startActivity(loginSuccessIntent);
                }else
                {

                }
                break;
            case R.id.updateButton:
                if(validateForm())
                {
                    populateDatabase();
                    myToast = Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_SHORT);
                    myToast.show();
                    Intent nextIntent = new Intent(this, fullPatientInfo.class);
                    nextIntent.putExtra("patientID",p_id);
                    startActivity(nextIntent);
                }else
                {

                }
                break;
            case R.id.clearButton:
                clearFunc((ViewGroup) findViewById(R.id.scrollLayout));
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
            Dob.setText(day+"/"+month+"/"+year);
        }
    };
}

