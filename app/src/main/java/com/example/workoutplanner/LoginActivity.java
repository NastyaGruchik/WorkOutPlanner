package com.example.workoutplanner;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLoginEmail, editTextLoginPwd;
    private ProgressBar progressBar;
    private FirebaseAuth authProfiles;
    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViews();

        authProfiles = FirebaseAuth.getInstance();
         //Login Button
        Button buttonLogin = findViewById(R.id.button_login);
        buttonLogin.setOnClickListener(v -> {
            String textEmail = editTextLoginEmail.getText().toString();
            String textPwd = editTextLoginPwd.getText().toString();

            if (TextUtils.isEmpty(textEmail)){
                Toast.makeText(LoginActivity.this,"Please enter your email", Toast.LENGTH_SHORT).show();
                editTextLoginEmail.setError("Email is required");
                editTextLoginEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                Toast.makeText(LoginActivity.this,"Please re-enter your email", Toast.LENGTH_SHORT).show();
                editTextLoginEmail.setError("Valid email is required");
                editTextLoginEmail.requestFocus();
            } else if (TextUtils.isEmpty(textPwd)) {
                Toast.makeText(LoginActivity.this,"Please enter your password", Toast.LENGTH_SHORT).show();
                editTextLoginPwd.setError("Password is required");
                editTextLoginPwd.requestFocus();
            }else{
                progressBar.setVisibility(View.VISIBLE);
                loginUser(textEmail,textPwd);
            }
        });
    }

    private void loginUser(String email, String pwd) {
        authProfiles.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(LoginActivity.this,"You are logged in now", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,UserProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }else{
                try{
                    throw task.getException();
                }catch (FirebaseAuthInvalidUserException e){
                    editTextLoginEmail.setError("User does not exists. Please register again");
                    editTextLoginEmail.requestFocus();
                }catch (FirebaseAuthInvalidCredentialsException e){
                    editTextLoginEmail.setError("Invalid credentials.check and re-enter ");
                    editTextLoginEmail.requestFocus();
                }catch (Exception e){
                    Log.e(TAG,e.getMessage());
                    Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(LoginActivity.this,"Something went wrong!", Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void findViews() {
        editTextLoginEmail = findViewById(R.id.editText_login_email);
        editTextLoginPwd = findViewById(R.id.editText_login_pw);
        progressBar = findViewById(R.id.pB_login);
    }
}