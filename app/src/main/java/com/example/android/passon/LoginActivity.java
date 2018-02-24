package com.example.android.passon;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by Eon on 01/26/2018.
 */

/*
This class handles backend of login activity and handling it with firebase auth object
 */
public class LoginActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private int RC_SIGN_IN = 12345;
    String email, password;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPasswordView = (EditText) findViewById(R.id.login_password);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.login_email);
        // mCallbackManager = CallbackManager.Factory.create();


        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("163473474337-7nmlo122es438lpcic95bvqjmlsv7jk7.apps.googleusercontent.com\n")
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, 0);
        email = prefs.getString("Email", null);

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });
        mPasswordView = (EditText) findViewById(R.id.login_password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                attemptLogin();
                return true;
            }
        });
        if(email!=null&&email!="") {
            attemptLogin(email,prefs.getString("Password",null));
        }
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, 0);
                prefs.edit().putString("Email", "").apply();
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("PassOn", "Google sign in failed", e);
                Toast.makeText(this, "SignIn failed. Try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(this,Main2Activity.class);
            finish();
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("PassOn", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "SignIn failed. Try again!", Toast.LENGTH_SHORT).show();
        }
    }

    public void signInExistingUser(View v) {
        attemptLogin();

    }


    // Executed when Register button pressed
    public void registerNewUser(View v) {
        Intent intent = new Intent(this, com.example.android.passon.RegisterActivity.class);
        startActivity(intent);
    }

    // TODO: Complete the attemptLogin() method

    private void attemptLogin() {
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Incomplete credentials", Toast.LENGTH_SHORT).show();
        }
        if (email.isEmpty() || password.isEmpty()) return;
        Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.d("Flash", "signinwithemailandpasswordfailed   " + task.getException());
                    new AlertDialog.Builder(LoginActivity.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Oops").setPositiveButton("ok", null).setMessage("There was a problem signing in").show();
                } else {
                    SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, 0);
                    prefs.edit().putString("Email", email).apply();
                    prefs.edit().putString("Password", password).apply();
                    Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                    finish();
                    startActivity(intent);
                }

            }

            // TODO: Use FirebaseAuth to sign in with email & password


            // TODO: Show error on screen with an alert dialog
        });
    }
    private void attemptLogin(final String PrefsEmail, final String PrefsPass) {
        if (PrefsEmail.isEmpty() || PrefsPass.isEmpty()) {
            Toast.makeText(this, "Incomplete credentials", Toast.LENGTH_SHORT).show();
        }
        if (PrefsEmail.isEmpty() || PrefsPass.isEmpty()) return;
        Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show();
        mAuth.signInWithEmailAndPassword(PrefsEmail, PrefsPass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.d("Flash", "signinwithemailandpasswordfailed   " + task.getException());
                    new AlertDialog.Builder(LoginActivity.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Oops").setPositiveButton("ok", null).setMessage("There was a problem signing in").show();
                } else {
                    Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                    finish();
                    startActivity(intent);
                }

            }

            // TODO: Use FirebaseAuth to sign in with email & password


            // TODO: Show error on screen with an alert dialog
        });
    }
    //   @Override

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Flash", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Flash", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                            finish();
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Flash", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

    public void forgotPassword(View view) {
        if(mEmailView.getText().toString().isEmpty())
        {
            Toast.makeText(this,"Enter email first!" , Toast.LENGTH_SHORT).show();
        }
        else {
            mAuth.sendPasswordResetEmail(mEmailView.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("PassOn", "Email sent.");
                                Toast.makeText(LoginActivity.this, "Check your email for a reset link.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}