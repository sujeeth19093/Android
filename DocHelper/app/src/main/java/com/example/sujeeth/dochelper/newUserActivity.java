package com.example.sujeeth.dochelper;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Calendar;

import org.w3c.dom.Text;

public class newUserActivity extends AppCompatActivity implements OnClickListener {

    private TextView  FormCompleteStatus;

    private EditText Email,Password,UserName,FName, LName, Qualification,
                    Specialization, Department, Address1,
                    Address2, PrimaryNumber, SecondaryNumber;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser nw_user;
    private static final String TAG = "EmailPassword";

    private Button Create, Clear, Cancel;

    private Spinner OccupationSpinner, Gender, PrimaryN, SecondaryN;

    private String  user_email, user_password;

    private int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        Intent intent = getIntent();

        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        FormCompleteStatus = (TextView) findViewById(R.id.formStatusTxtV);

        Create = (Button) findViewById(R.id.createButton);
        Create.setOnClickListener(this);
        Clear = (Button) findViewById(R.id.clearButton);
        Clear.setOnClickListener(this);
        Cancel = (Button) findViewById(R.id.cancelButton);
        Cancel.setOnClickListener(this);


        Email = (EditText)findViewById(R.id.newUserEmailTxtBox);
        Password = (EditText)findViewById(R.id.newUserPasswordTxtBox);
        UserName = (EditText) findViewById(R.id.IdTxtBox);
        FName = (EditText) findViewById(R.id.fnameTxtBox);
        LName = (EditText) findViewById(R.id.lnameTxtBox);
        Qualification = (EditText) findViewById(R.id.qualificationTxtBox);
        Specialization = (EditText) findViewById(R.id.specializationTxtBox);
        Department = (EditText) findViewById(R.id.DeptTxtBox);
        Address1 = (EditText) findViewById(R.id.Addr1TxtBox);
        Address2 = (EditText) findViewById(R.id.Addr2TxtBox);
        PrimaryNumber = (EditText) findViewById(R.id.PrimaryNTxtBox);
        SecondaryNumber = (EditText) findViewById(R.id.SecondaryNTxtBox);

        OccupationSpinner = (Spinner) findViewById(R.id.occupationSpinner);
        Gender = (Spinner) findViewById(R.id.GenderSpinner);
        PrimaryN = (Spinner) findViewById(R.id.primaryNumberSpinner);
        SecondaryN = (Spinner) findViewById(R.id.secondaryNumberSpinner);

        String occupation[]={"Select Occupation","Doctor","Technician"};
        String gender[] = {"Select Gender","Male","Female","Unspecified"};
        String phone_type[] = {"Mobile","Landline"};

        ArrayAdapter<String> occupationAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,occupation);
        occupationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        OccupationSpinner.setAdapter(occupationAdapter);

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,gender);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Gender.setAdapter(genderAdapter);

        ArrayAdapter<String> phoneAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,phone_type);
        phoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PrimaryN.setAdapter(phoneAdapter);
        SecondaryN.setAdapter(phoneAdapter);
    }


    @IgnoreExtraProperties
    public static class Doctor
    {
        public String user_name,first_name,last_name,qualification,specialization,dept,gender,addr,
                primary_number,secondary_number,primary_phone_type,secondary_phone_type,created_by,
                created_on,role;


        public Doctor()
        {
            user_name = "N/A";
            first_name = "N/A";
            last_name = "N/A";
            qualification = "N/A";
            specialization = "N/A";
            dept = "N/A";
            gender = "N/A";
            addr = "N/A";
            primary_number = "N/A";
            secondary_number = "N/A";
            primary_phone_type = "N/A";
            secondary_phone_type = "N/A";
            created_on = "N/A";
            created_by = "N/A";
            role = "N/A";
        }
    }

    //NEED TO FINISH
    private boolean validateForm()
    {
        return true;
    }



    private void addNewUser(String usname, String frname, String lsname, String qual, String sp,
                            String dep, String gen, String ad, String pri, String pri_ty, String sec,
                            String sec_ty,String r_id)
    {
        Doctor doc = new Doctor();
        doc.user_name = usname;
        doc.first_name = frname;
        doc.last_name = lsname;
        doc.qualification = qual;
        doc.specialization = sp;
        doc.dept = dep;
        doc.gender = gen;
        doc.addr = ad;
        doc.primary_number = pri;
        doc.primary_phone_type = pri_ty;
        doc.secondary_number = sec;
        doc.secondary_phone_type = sec_ty;
        doc.role = r_id;

        doc.created_by = "Admin";
        doc.created_on = String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year);

        mDatabase.child("users").child(nw_user.getUid()).setValue(doc);
    }

    private void createUser(String uName, String pWord)
    {
        Log.d(TAG, "createAccount:" + uName);

        mAuth.createUserWithEmailAndPassword(uName,pWord)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(newUserActivity.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                        }else
                        {
                            Log.d(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(newUserActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void populateDatabase()
    {
        String ad,gen = "",pri_ty,sec_ty,r_id;

        if(Address2.getText().toString().equals(""))
        {
             ad = Address1.getText().toString();
        }else
        {
             ad = Address1.getText().toString() + " " + Address2.getText().toString();
        }

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

        pri_ty = PrimaryN.getSelectedItem().toString();
        sec_ty = SecondaryN.getSelectedItem().toString();
        r_id = OccupationSpinner.getSelectedItem().toString();

        addNewUser(UserName.getText().toString(), FName.getText().toString(), LName.getText().toString(),
                Qualification.getText().toString(),Specialization.getText().toString(),Department.getText().toString(),
                gen,ad,PrimaryNumber.getText().toString(),pri_ty,SecondaryNumber.getText().toString(),sec_ty,r_id);
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

            if(view instanceof Spinner)
            {
                ((Spinner)view).setSelection(0);
            }
        }
    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.createButton:

                user_email = Email.getText().toString();
                user_password = Password.getText().toString();

                createUser(user_email,user_password);
                while(true)
                {
                    nw_user = mAuth.getCurrentUser();
                    if(nw_user != null)
                    {
                        populateDatabase();
                        break;
                    }
                }
                Intent success_intent = new Intent(this, MainActivity.class);
                startActivity(success_intent);
                break;

            case R.id.clearButton:
                clearFunc((ViewGroup)findViewById(R.id.docLayout));
                break;

            case R.id.cancelButton:
                clearFunc((ViewGroup)findViewById(R.id.docLayout));
                Intent cancel_intent = new Intent(this, MainActivity.class);
                startActivity(cancel_intent);
                break;
        }
    }
}
