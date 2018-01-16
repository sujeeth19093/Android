package com.example.sujeeth.dochelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class patientInfo extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference mDatabase,ref;
    private FirebaseAuth mAuth;
    private TableLayout Prescription_Table;
    private Button LookUpOtherPatient, FullPatientInfo, VisitLog, Deactivate,
                NewVisit;
    private TextView PatientName, PatientAge, PatientGender;
    private String p_id;
    private Patient patient;
    private int row_num = 0;
    private ArrayList<String> presc_ids;
    private ArrayList<Integer> del_ids;
    private Map<Integer,Boolean> stat;
    private String prescDatabase = "Prescriptions", activePrescDatabase = "Active_Prescriptions",
            visitDatabase = "Visits", patientDatabase = "Patients";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        Intent intent = getIntent();
        p_id = intent.getStringExtra("patientID");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = mDatabase.child(("Patients")).child(p_id);

        presc_ids = new ArrayList<String>();
        del_ids = new ArrayList<Integer>();
        stat = new HashMap<Integer, Boolean>();

        Prescription_Table = (TableLayout) findViewById(R.id.prescriptionTableLayout);

        LookUpOtherPatient = (Button) findViewById(R.id.lookUpOtherPatientButton);
        LookUpOtherPatient.setOnClickListener(this);
        FullPatientInfo = (Button) findViewById(R.id.fullPatientInfoButton);
        FullPatientInfo.setOnClickListener(this);
        VisitLog = (Button) findViewById(R.id.logVisitButton);
        VisitLog.setOnClickListener(this);
        Deactivate = (Button) findViewById(R.id.deactivatePrescButton);
        Deactivate.setOnClickListener(this);
        NewVisit = (Button) findViewById(R.id.newVisitButton);
        NewVisit.setOnClickListener(this);

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

        updatePrescTable();
    }

    private void updatePrescTable()
    {
        mDatabase.child(patientDatabase).child(p_id).child(prescDatabase).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    if(dataSnapshot.child(activePrescDatabase).getValue(int.class) > 0)
                    {
                        int i = 0;
                        findViewById(R.id.prescriptionTableLayout).setVisibility(View.VISIBLE);
                        for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        {
                            if(snapshot.hasChildren())
                            {
                                Prescription p = snapshot.getValue(Prescription.class);
                                if(p.status.equals("A"))
                                {
                                    i++;
                                    presc_ids.add(snapshot.getKey());
                                    addPresTableRow(p,i);
                                }
                            }
                        }
                        row_num = i;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    TextView TextV1, TextV2, TextV3;
    Button btn;
    TableRow row;

    private void addPresTableRow(Prescription p, int i)
    {
        row = new TableRow(this);
        row.setId(100 + i);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);

        TextV1 = new TextView(this);
        String col1 = p.name + "\n" + p.type + "\nDose: " + p.dose;
        formatTxtV(TextV1);
        TextV2 = new TextView(this);
        String col2 = "Qty:" + p.quantity + "\nRep:" + p.frequency_of_ingestion + "\n";
        formatTxtV(TextV2);
        TextV3 = new TextView(this);
        String col3 = "";
        int count = 0;
        if(p.before_breakfast)
        {
            col3 = col3 + "B.Breakfast";
            count++;
        }
        if(p.breakfast)
        {
            if(count > 0) col3 = col3 + "\n";
            col3 = col3 + "Breakfast";
            count++;
        }
        if(p.lunch)
        {
            if(count > 0) col3 = col3 + "\n";
            col3 = col3 + "Lunch";
            count++;
        }
        if(p.dinner)
        {
            if(count > 0) col3 = col3 + "\n";
            col3 = col3 + "Dinner";
            count++;
        }

        if(count == 4)
        {
            col1 = col1 + "\n";
            col2 = col2 + "\n";
        }else if(count < 3)
        {
            while(count < 3)
            {
                col3 = col3 + "\n";
                count++;
            }
        }

        TextV1.setText(col1);
        TextV2.setText(col2);
        TextV3.setText(col3);
        formatTxtV(TextV3);

        btn = new Button(this);
        btn.setId(i);
        btn.setText("\n-\n");
        btn.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        btn.setGravity(Gravity.CENTER_HORIZONTAL);
        btn.setPadding(1,0,1,0);
        btn.setOnClickListener(this);
        stat.put(i,false);

        row.addView(TextV1);
        row.addView(TextV2);
        row.addView(TextV3);
        row.addView(btn);

        Prescription_Table.addView(row,i);
    }

    private void formatTxtV(TextView t)
    {
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        t.setTextColor(0xFF000000);
        t.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        t.setBackgroundResource(R.drawable.border_shape);
        t.setPadding(1,1,1,1);
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

            case R.id.logVisitButton:
                Intent lVintent = new Intent(this, visitLog.class);
                lVintent.putExtra("patientID",p_id);
                startActivity(lVintent);
                break;

            case R.id.deactivatePrescButton:
                if(del_ids.isEmpty())
                {
                    Toast d = Toast.makeText(getApplicationContext(),"Nothing to deactivate",Toast.LENGTH_LONG);
                    d.show();
                }else
                {

                     FirebaseUser user = mAuth.getCurrentUser();
                     String mod_by = user.getUid();
                     String mod_on = Calendar.DAY_OF_MONTH + "-" + Calendar.MONTH + "-" + Calendar.YEAR;
                    for(int i : del_ids)
                    {
                        Map<String,Object> m = new HashMap<String, Object>();
                        m.put("status","D");
                        m.put("modified_by",mod_by);
                        m.put("modified_on",mod_on);
                        mDatabase.child(patientDatabase).child(p_id).child(prescDatabase).child(presc_ids.get(i - 1)).updateChildren(m);
                        findViewById(100+i).setVisibility(View.GONE);
                    }

                    mDatabase.child(patientDatabase).child(p_id).child(prescDatabase).child(activePrescDatabase).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    int i = dataSnapshot.getValue(int.class);
                                    i = i - del_ids.size();
                                    Map<String,Object> s = new HashMap<String, Object>();
                                    s.put(activePrescDatabase,i);
                                    mDatabase.child(patientDatabase).child(p_id).child(prescDatabase).updateChildren(s);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            }
                    );

                    Toast d = Toast.makeText(getApplicationContext(),"Deactivated",Toast.LENGTH_LONG);
                    d.show();
                }
                break;

            case R.id.newVisitButton:
                Intent nVintent = new Intent(this, newPatientVisit.class);
                nVintent.putExtra("patientID",p_id);
                startActivity(nVintent);
                break;
        }

        for(int i = 1; i <= row_num; i++)
        {
            if(v.getId() == i)
                if(stat.get(i))
                {
                    stat.put(i,false);
                    del_ids.remove(new Integer(i));
                    btn = (Button) findViewById(i);
                    btn.setText("-");
                    btn.setTextColor(getResources().getColor(R.color.black));
                    btn.setBackgroundResource(R.drawable.border_shape);
                    findViewById(100 + i).setBackgroundColor(0);
                }else
                {
                    stat.put(i,true);
                    del_ids.add(i);
                    btn = (Button) findViewById(i);
                    btn.setText("+");
                    btn.setTextColor(getResources().getColor(R.color.white));
                    btn.setBackgroundColor(getResources().getColor(R.color.black));
                    findViewById(100 + i).setBackgroundColor(getResources().getColor(R.color.red));
                }
        }
    }
}
