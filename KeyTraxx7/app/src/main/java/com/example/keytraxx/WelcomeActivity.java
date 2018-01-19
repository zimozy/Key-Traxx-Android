package com.example.keytraxx;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "WelcomeActivity";
    private boolean loggedIn;

    private FirebaseAuth firebaseAuth;

    private Button buttonSignIn;
    private Button buttonSignOut;
    private TextView textViewLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        buttonSignIn     = findViewById(R.id.buttonSignIn);
        buttonSignOut    = findViewById(R.id.buttonSignOut);
        textViewLoggedIn = findViewById(R.id.textViewLoggedIn);

        updateLoggedInText();

//        buttonSignIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {

                // Choose authentication providers
                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
//                new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
//                new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build(),
//                new AuthUI.IdpConfig.Builder(AuthUI.TWITTER_PROVIDER).build()
                );

                // Create and launch sign-in intent
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setTheme(R.style.FirebaseLogin)
                                .build(),
                        RC_SIGN_IN);
//            }
//        });

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthUI.getInstance()
                        .signOut(WelcomeActivity.this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {

                                Toast.makeText(WelcomeActivity.this, "Now logged out.", Toast.LENGTH_LONG).show();
                                WelcomeActivity.this.updateLoggedInText();

                            }
                        });
            }
        });
    }

    public void updateLoggedInText() {
        //check if logged in
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        loggedIn = firebaseUser != null;

        textViewLoggedIn.setTextColor(
                loggedIn ? 0xAA339933 : 0xAA993333
        );

        textViewLoggedIn.setText(
                loggedIn ? "You are logged in." : "You are logged out"
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == ResultCodes.OK) {
                // Successfully signed in
                Toast.makeText(this, "Succesfully signed in", Toast.LENGTH_LONG).show();
                updateLoggedInText();
            } else {
                // Sign in failed, check response for error code
                Toast.makeText(this, "Sign in FAILED", Toast.LENGTH_LONG).show();
            }
        }
    }

}
