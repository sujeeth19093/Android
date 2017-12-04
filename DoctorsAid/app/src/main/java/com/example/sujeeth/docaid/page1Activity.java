package com.example.sujeeth.docaid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class page1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);
    }

    EditText username = (EditText)findViewById(R.id.usernameTBox);
    EditText password = (EditText)findViewById(R.id.passwordTBox);

    public void login(View view)
    {
        if(username.getText().toString().equals("admin")&&password.getText().toString().equals("admin"))
        {
            Intent intent = new Intent(this, DisplayMessageActivity.class);
            startActivity(intent);
        }
    }
}
