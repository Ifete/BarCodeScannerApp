package com.example.barcodescanner.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodescanner.R;
import com.example.barcodescanner.storage.StoragePage;
import com.google.android.material.textfield.TextInputEditText;

public class LogInMain extends AppCompatActivity {
    private static final String TAG = "LogInMain";
    private Button logInButton;
    private TextInputEditText usernameEt;
    private TextInputEditText passwordEt;
    private static String USER = "ire";
    private static String PASS = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_page);
        usernameEt = findViewById(R.id.usernameEt);
        passwordEt = findViewById(R.id.paswordEt);
        logInButton = findViewById(R.id.logInBtn);



        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userNameIntroduced = String.valueOf(usernameEt.getText());
                String passwordIntroduced = String.valueOf(passwordEt.getText());
                if (userNameIntroduced.equals(USER)&&passwordIntroduced.equals(passwordIntroduced)){
                    Intent storagePage = new Intent(LogInMain.this, StoragePage.class);
                    LogInMain.this.startActivity(storagePage);
                }else{
                    Toast.makeText(LogInMain.this, "Username or Password Incorrect",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
