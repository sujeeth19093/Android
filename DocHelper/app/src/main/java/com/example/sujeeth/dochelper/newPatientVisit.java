package com.example.sujeeth.dochelper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class newPatientVisit extends AppCompatActivity implements View.OnClickListener{

    private String p_id;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, ref, pV_ref = null, presc_ref = null;
    private TableLayout Prescription_Table;
    private EditText Height, Weight, Temperature, BP_Over, BP_Under, Next_Visit,
                    Observations, Lab_Recommendations, Medicine_Name,
                    Med_Dose, Med_Repeat, Med_Quantity, Med_Instr;
    private Button Calender_B, Save_Medicine, Cancel_Medicine, Add_Prescription,
                Save_Visit;
    private Spinner Med_Type;
    private CheckBox Before_Breakfast, Breakfast, Lunch, Dinner;
    private int year, month, day;
    private int present_year, present_month, present_day;
    private int row_num;
    private ArrayList<String> presc_ids;
    private ArrayList<Prescription> presc_to_add;

    private String prescDatabase = "Prescriptions", activePrescDatabase = "Active_Prescriptions",
                visitDatabase = "Visits", patientDatabase = "Patients";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_patient_visit);
        Intent intent = getIntent();
        p_id = intent.getStringExtra("patientID");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = mDatabase.child(patientDatabase).child(p_id);

        presc_ids = new ArrayList<String>();
        presc_to_add = new ArrayList<Prescription>();

        final Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        present_year = cal.get(Calendar.YEAR);
        present_month = cal.get(Calendar.MONTH) + 1;
        present_day = cal.get(Calendar.DAY_OF_MONTH);

        Prescription_Table = (TableLayout) findViewById(R.id.pVPresc);

        Height = (EditText) findViewById(R.id.pVheightTxtBox);
        Weight = (EditText) findViewById(R.id.pVweightTxtBox);
        Temperature = (EditText) findViewById(R.id.pVtempTBox);
        BP_Over = (EditText) findViewById(R.id.pVupperBPTBox);
        BP_Under = (EditText) findViewById(R.id.pVlowerBPTxtBox);
        Next_Visit = (EditText) findViewById(R.id.pVnxtVisitTBox);
        Observations = (EditText) findViewById(R.id.pVObservationsTBox);
        Lab_Recommendations = (EditText) findViewById(R.id.pVLabRecTBox);
        Medicine_Name = (EditText) findViewById(R.id.pVmedName);
        Med_Dose = (EditText) findViewById(R.id.pVmedDose);
        Med_Repeat = (EditText) findViewById(R.id.pVmedRepeat);
        Med_Quantity = (EditText) findViewById(R.id.pVmedQuantity);
        Med_Instr = (EditText) findViewById(R.id.pVmedInstr);

        Calender_B = (Button) findViewById(R.id.pVnxtVisitCalButton);
        Calender_B.setOnClickListener(this);
        Save_Medicine = (Button) findViewById(R.id.SAVE_MED_BTN);
        Save_Medicine.setOnClickListener(this);
        Cancel_Medicine = (Button) findViewById(R.id.CANCEL_MED_BTN);
        Cancel_Medicine.setOnClickListener(this);
        Add_Prescription = (Button) findViewById(R.id.pVaddPrescButton);
        Add_Prescription.setOnClickListener(this);
        Save_Visit = (Button) findViewById(R.id.pVsaveVisitButton);
        Save_Visit.setOnClickListener(this);

        Med_Type = (Spinner) findViewById(R.id.pVmedTypeSpinner);
        String[] medicine_ty = {"Tablet", "Capsule", "Syrup", "Ointment"};
        ArrayAdapter<String> medT_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, medicine_ty);
        medT_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Med_Type.setAdapter(medT_adapter);

        Before_Breakfast = (CheckBox) findViewById(R.id.pVbbreakfastChkBox);
        Breakfast = (CheckBox) findViewById(R.id.pVbreakfastChkBox);
        Lunch = (CheckBox) findViewById(R.id.pVlunchChkBox);
        Dinner = (CheckBox) findViewById(R.id.pVdinnerChkBox);

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
                       findViewById(R.id.pVPresc).setVisibility(View.VISIBLE);
                       for(DataSnapshot snapshot : dataSnapshot.getChildren())
                       {
                          if(snapshot.hasChildren())
                          {
                              i++;
                              Prescription p = snapshot.getValue(Prescription.class);
                              addPresTableRow(p,i);
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
    TableRow row;

    private void addPresTableRow(Prescription p, int i)
    {
        row = new TableRow(this);
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

        row.addView(TextV1);
        row.addView(TextV2);
        row.addView(TextV3);

        Prescription_Table.addView(row,i);
    }

    private void formatTxtV(TextView t)
    {
        t.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        t.setTextColor(0xFF000000);
        t.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        t.setBackgroundResource(R.drawable.border_shape);
        t.setPadding(25,0,0,0);
    }

    private String prev = null, next,d;
    private long t = 0;

    private void addNewVisit(String date, String time, String height, String weight, String blood_p, String temperature, String next_visit,
                        String observations, String lab_rec)
    {

        Patient_Visit pv = new Patient_Visit();
        pv.date = date;
        pv.time = time;
        pv.height = height;
        pv.weight = weight;
        pv.blood_pressure = blood_p;
        pv.temperature = temperature;
        pv.next_visit = next_visit;
        pv.observations = observations;
        pv.lab_recommendations = lab_rec;

        FirebaseUser currentUser = mAuth.getCurrentUser();
        pv.doctor_id = currentUser.getUid();
        d = pv.doctor_id;
        pv.created_on = date;

        String visit_id = pv.date + ":" + pv.time;
        next = visit_id;
        String presc_id;
        for(Prescription p : presc_to_add)
        {
            presc_id = p.name;
            mDatabase.child(patientDatabase).child(p_id).child(prescDatabase).child(presc_id).setValue(p);
            pv.prescription_ids.add(presc_id);
        }

        mDatabase.child(patientDatabase).child(p_id).child(prescDatabase).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0;
                        for(DataSnapshot snapshot: dataSnapshot.getChildren())
                        {
                            if(snapshot.hasChildren())
                            {
                                Prescription temp_p = snapshot.getValue(Prescription.class);
                                if(temp_p.status.equals("A"))
                                    i++;
                            }
                        }
                        mDatabase.child(patientDatabase).child(p_id).child(prescDatabase).child(activePrescDatabase).setValue(i);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        mDatabase.child(patientDatabase).child(p_id).child(visitDatabase).child(pv.doctor_id).child("Last_Visit").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            prev = dataSnapshot.getValue(String.class);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        pv.previous_visit_log = prev;
        mDatabase.child(patientDatabase).child(p_id).child(visitDatabase).child(pv.doctor_id).child(visit_id).setValue(pv);
        mDatabase.child(patientDatabase).child(p_id).child(visitDatabase).child(pv.doctor_id).child("Last_Visit").setValue(visit_id);

        if(prev != null)
        {
            Map<String, Object> next_pointer = new HashMap<String, Object>();
            next_pointer.put("next_visit_log",next);
            mDatabase.child(patientDatabase).child(p_id).child(visitDatabase).child(pv.doctor_id).child(prev).updateChildren(next_pointer);
        }

        mDatabase.child(patientDatabase).child(p_id).child(visitDatabase).child("Doctor_List").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                String temp = snapshot.getValue(String.class);
                                if(temp.equals(d))
                                {
                                    t = -1;
                                    break;
                                }
                            }
                            if(t == 0)  t = dataSnapshot.getChildrenCount();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        if(t > -1) mDatabase.child(patientDatabase).child(p_id).child(visitDatabase).child("Doctor_List").child(""+t).setValue(d);
    }

    private void populateDatabase()
    {
        String date = present_day + "-" + present_month + "-" + present_year;
        Calendar cal = Calendar.getInstance();
        int t1 = cal.get(Calendar.HOUR_OF_DAY);
        int t2 = cal.get(Calendar.MINUTE);
        String time = t1 + ":" + t2;
        String bp = BP_Over.getText().toString() + "/" + BP_Under.getText().toString();

        addNewVisit(date, time, Height.getText().toString(), Weight.getText().toString(), bp, Temperature.getText().toString(),
                Next_Visit.getText().toString(), Observations.getText().toString(), Lab_Recommendations.getText().toString());
    }

    private void addNewPresc()
    {
        Prescription p = new Prescription();
        p.name = Medicine_Name.getText().toString();
        p.type = Med_Type.getSelectedItem().toString();
        p.dose = Med_Dose.getText().toString();
        p.quantity = Med_Quantity.getText().toString();
        p.frequency_of_ingestion = Med_Repeat.getText().toString();
        p.instructions = Med_Instr.getText().toString();
        p.before_breakfast = Before_Breakfast.isChecked();
        p.breakfast = Breakfast.isChecked();
        p.lunch = Lunch.isChecked();
        p.dinner = Dinner.isChecked();

        FirebaseUser user = mAuth.getCurrentUser();
        p.created_by = user.getUid();
        p.created_on = present_day + "-" + present_month + "-" + present_year;

        presc_to_add.add(p);

        findViewById(R.id.pVPresc).setVisibility(View.VISIBLE);
        addPresTableRow(p,row_num++);

    }
    private boolean validatePresc()
    {
        return true;
    }

    private void clearPresc()
    {
        Medicine_Name.setText("");
        Med_Type.setSelection(0);
        Med_Instr.setText("");
        Med_Quantity.setText("");
        Med_Repeat.setText("");
        Med_Dose.setText("");
        Before_Breakfast.setChecked(false);
        Breakfast.setChecked(false);
        Lunch.setChecked(false);
        Dinner.setChecked(false);
    }

    private boolean validateForm()
    {
        return true;
    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.pVnxtVisitCalButton:
                showDialog(0);
                break;

            case R.id.pVsaveVisitButton:
                if(validateForm())
                {
                    populateDatabase();
                    Toast t = Toast.makeText(getApplicationContext(), "Created Visit", Toast.LENGTH_LONG);
                    t.show();
                    Intent i = new Intent(this, patientInfo.class);
                    i.putExtra("patientID", p_id);
                    startActivity(i);
                }
                break;

            case R.id.pVaddPrescButton:
                findViewById(R.id.pVPrescAddLayout).setVisibility(View.VISIBLE);
                break;

            case R.id.SAVE_MED_BTN:
                if(validatePresc())
                {
                    addNewPresc();
                    Toast y = Toast.makeText(getApplicationContext(),"Prescription Added", Toast.LENGTH_LONG);
                    y.show();
                    clearPresc();
                    findViewById(R.id.pVPrescAddLayout).setVisibility(View.GONE);
                }
                break;

            case R.id.CANCEL_MED_BTN:
                clearPresc();
                findViewById(R.id.pVPrescAddLayout).setVisibility(View.GONE);
                Toast y = Toast.makeText(getApplicationContext(),"Cancelled", Toast.LENGTH_SHORT);
                y.show();
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
            month = i1+1;
            day = i2;
            Next_Visit.setText(day+"/"+month+"/"+year);
        }
    };
}
