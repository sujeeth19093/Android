package com.example.sujeeth.dochelper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase,ref;
    private static final String TAG = "EmailPassword";

    private Button SignInButton, SignUpButton;
    private Button SignOutButton, ContinueButton;

    private EditText usernameField, passwordField;

    private TextView WelcomeMsg;

    newUserActivity.Doctor doc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onStart()
    {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
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
                            Toast.makeText(MainActivity.this, "Authentication failed.",
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

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            ref = mDatabase.child("users").child(user.getUid());
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    newUserActivity.Doctor doc = dataSnapshot.getValue(newUserActivity.Doctor.class);
                    WelcomeMsg.setText("Welcome " + doc.first_name);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            findViewById(R.id.alreadySignedInLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.signInLayout).setVisibility(View.GONE);
        } else {
            findViewById(R.id.alreadySignedInLayout).setVisibility(View.GONE);
            findViewById(R.id.signInLayout).setVisibility(View.VISIBLE);
        }
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    public void onClick(View view)
    {
        Intent loginSuccessIntent;
        switch (view.getId())
        {
            case R.id.signInButton:
                signIn(usernameField.getText().toString(), passwordField.getText().toString());
                break;

            case R.id.signUpButton:
                Intent create_user_intent = new Intent(this, newUserActivity.class);
                startActivity(create_user_intent);
                break;

            case R.id.continueButton:
                loginSuccessIntent = new Intent(this,loginSuccess.class);
                startActivity(loginSuccessIntent);
                break;

            case R.id.signOutButton:
                signOut();
                break;
        }
    }
}
