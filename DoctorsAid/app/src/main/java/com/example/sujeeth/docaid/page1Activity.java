package com.example.sujeeth.docaid;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class page1Activity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private static final String TAG = "DocAidEmailAndPassword";

    Button SignInButton, SignUpButton;
    Button SignOutButton, ContinueButton;
    Button AuthenticateButton;

    EditText usernameField, passwordField;
    EditText signUpEmailField, signUpPasswordField;

    TextView WelcomeMsg;
    TextView AuthenticationStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page1);
        mAuth = FirebaseAuth.getInstance();

        SignInButton = (Button)findViewById(R.id.signInButton);
        SignInButton.setOnClickListener(this);

        SignUpButton = (Button)findViewById(R.id.signUpButton);
        SignUpButton.setOnClickListener(this);

        usernameField = (EditText)findViewById(R.id.usernameTBox);
        passwordField = (EditText)findViewById(R.id.passwordTBox);

        WelcomeMsg = (TextView)findViewById(R.id.welcomeTxtV);

        ContinueButton = (Button)findViewById(R.id.continueButton);
        ContinueButton.setOnClickListener(this);

        SignOutButton = (Button)findViewById(R.id.signOutButton);
        SignOutButton.setOnClickListener(this);

        AuthenticateButton = (Button)findViewById(R.id.authenticateButton);
        AuthenticateButton.setOnClickListener(this);

        signUpEmailField = (EditText)findViewById(R.id.emailTBox);
        signUpPasswordField = (EditText) findViewById(R.id.emailPWordTBox);

        AuthenticationStatus = (TextView)findViewById(R.id.authenticationStatusTxtV);
    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void createUser(String uName, String pWord)
    {
        Log.d(TAG, "createAccount:" + uName);
        if(!validateForm2())
        {
            AuthenticationStatus.setText("Error: Information is missing");
            return;
        }

        mAuth.createUserWithEmailAndPassword(uName,pWord)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }else
                        {
                            Log.d(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(page1Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn(String uName, String pWord)
    {
        Log.d(TAG, "signIn:" + uName);
        if(!validateForm())
        {
            return;
        }

        mAuth.signInWithEmailAndPassword(uName, pWord)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(page1Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        String uName = usernameField.getText().toString();
        if (TextUtils.isEmpty(uName)) {
            usernameField.setError("Required.");
            valid = false;
        } else {
            usernameField.setError(null);
        }

        String pWord = passwordField.getText().toString();
        if (TextUtils.isEmpty(pWord)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }

    private boolean validateForm2() {
        boolean valid = true;

        String uName = signUpEmailField.getText().toString();
        if (TextUtils.isEmpty(uName)) {
            signUpEmailField.setError("Required.");
            valid = false;
        } else {
            signUpEmailField.setError(null);
        }

        String pWord = signUpPasswordField.getText().toString();
        if (TextUtils.isEmpty(pWord)) {
            signUpPasswordField.setError("Required.");
            valid = false;
        } else {
            signUpPasswordField.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            findViewById(R.id.alreadySignedInLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.signInLayout).setVisibility(View.GONE);
            findViewById(R.id.signUpLayout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.alreadySignedInLayout).setVisibility(View.GONE);
            findViewById(R.id.signInLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.signUpLayout).setVisibility(View.GONE);
        }
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.signInButton:
                signIn(usernameField.getText().toString(), passwordField.getText().toString());
                break;

            case R.id.signUpButton:
                findViewById(R.id.signInLayout).setVisibility(View.GONE);
                findViewById(R.id.signUpLayout).setVisibility(View.VISIBLE);
                break;

            case R.id.continueButton:
                Intent loginSuccessIntent = new Intent(this,loginSuccess.class);
                startActivity(loginSuccessIntent);
                break;

            case R.id.signOutButton:
                signOut();
                break;

            case R.id.authenticateButton:
                createUser(signUpEmailField.getText().toString(), signUpPasswordField.getText().toString());
                break;
        }
    }
}
