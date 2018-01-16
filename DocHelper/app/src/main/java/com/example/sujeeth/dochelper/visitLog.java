package com.example.sujeeth.dochelper;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class visitLog extends AppCompatActivity implements View.OnClickListener{

    private String p_id;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private TableLayout TLayout;
    private String prescDatabase = "Prescriptions", activePrescDatabase = "Active_Prescriptions",
            visitDatabase = "Visits", patientDatabase = "Patients";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_log);
        Intent intent = getIntent();
        p_id = intent.getStringExtra("patientID");

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        TLayout = (TableLayout) findViewById(R.id.logLayout);

        updateUI();
    }

    private void updateUI()
    {
        mDatabase.child(patientDatabase).child(p_id).child(visitDatabase).child("Doctor_List").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            int i = 0;
                            for(DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                i++;
                                setButton(snapshot,i);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private TableRow row;
    private Button btn;
    private String name;

    private void setButton(DataSnapshot snapshot, int i)
    {
        String doc_id = snapshot.getValue(String.class);

        mDatabase.child("users").child(doc_id).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        name = dataSnapshot.child("first_name").getValue(String.class);
                        name = name + " " + dataSnapshot.child("last_name").getValue(String.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        row = new TableRow(this);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);

        btn = new Button(this);
        btn.setId(i);
        btn.setText(name);
        btn.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        btn.setGravity(Gravity.CENTER_HORIZONTAL);
        btn.setPadding(1,0,1,0);
        btn.setOnClickListener(this);

        row.addView(btn);
        TLayout.addView(row,i);
    }

    public void onClick(View v)
    {

    }
}
