package com.example.sujeeth.docaid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class addPatientInfo extends AppCompatActivity implements OnClickListener{

    Button Save, Clear;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient_info);
        Intent intent = getIntent();

        Save = (Button) findViewById(R.id.saveButton);
        Save.setOnClickListener(this);
        Clear = (Button) findViewById(R.id.clearButton);
        Clear.setOnClickListener(this);
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
        switch(v.getId())
        {
            case R.id.saveButton:

                break;
            case R.id.clearButton:
                clearFunc((ViewGroup) findViewById(R.id.scrollLayout));
                break;
        }

    }
}
