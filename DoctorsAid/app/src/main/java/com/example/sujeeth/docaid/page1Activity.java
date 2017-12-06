package com.example.sujeeth.docaid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class page1Activity extends AppCompatActivity implements View.OnClickListener{

    Button loginButton;
    Button resetButton;

    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);

        loginButton = (Button)findViewById(R.id.loginButton);
        loginButton.setOnClickListener(this);

        username = (EditText)findViewById(R.id.usernameTBox);
        password = (EditText)findViewById(R.id.passwordTBox);
    }



    public void onClick(View view)
    {
        if(username.getText().toString().equals("admin")&&password.getText().toString().equals("admin"))
        {
            Intent intent = new Intent(this, loginSuccess.class);
            startActivity(intent);
        }
    }
}
