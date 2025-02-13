package com.example.barcodescanner.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodescanner.R;
import com.example.barcodescanner.storage.StoragePage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.example.barcodescanner.signin.SignUp;

public class LogInMain extends AppCompatActivity {
    private static final String TAG = "LogInMain";
    private Button logInButton, signUpButton;
    private TextInputEditText usernameEt, passwordEt;
    private String email, password;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_page);
        usernameEt = findViewById(R.id.usernameEt);
        passwordEt = findViewById(R.id.paswordEt);
        logInButton = findViewById(R.id.logInBtn);
        signUpButton = findViewById(R.id.signUpBtn);

        mAuth =FirebaseAuth.getInstance();








        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = usernameEt.getText().toString().trim();
                password = passwordEt.getText().toString().trim();

                if (email.equals("")|password.equals("")){
                    Log.d(TAG, "email: " + email + "\n password: " +password);
                    Toast.makeText(LogInMain.this, "VACIO", Toast.LENGTH_SHORT).show();
                }else {
                    Log.d(TAG, "email: " + email + "\n password: " +password);
                    signIn(email, password);
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpPage = new Intent(LogInMain.this, SignUp.class);
                LogInMain.this.startActivity(signUpPage);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (mAuth!=null){
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if(currentUser != null){
                currentUser.reload();
            }
        }
    }


    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
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
                            Toast.makeText(LogInMain.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }

    public void updateUI(FirebaseUser account){

        if(account != null){
            Toast.makeText(this,"You Signed In successfully",Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, StoragePage.class));

        }else {
            Toast.makeText(this,"You Didnt signed in",Toast.LENGTH_LONG).show();
        }

    }

}
