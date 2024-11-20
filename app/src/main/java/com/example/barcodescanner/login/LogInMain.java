package com.example.barcodescanner.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.barcodescanner.MainActivity;
import com.example.barcodescanner.R;

public class LogInMain extends AppCompatActivity {
    private static final String TAG = "LogInMain";
    private Button logInButton;
    private EditText usernameEt;
    private EditText passwordEt;
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
                    Intent scannerPage = new Intent(LogInMain.this, MainActivity.class);
                    LogInMain.this.startActivity(scannerPage);
                }else{
                    Toast.makeText(LogInMain.this, "Username or Password Incorrect",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
