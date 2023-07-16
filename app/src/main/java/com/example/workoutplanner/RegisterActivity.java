package com.example.workoutplanner;



import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextRegisterFullName, editTextRegisterEmail,editTextRegisterDoB,editTextRegisterPassword, editTextRegisterConfirmPassword;
    private ProgressBar progressBar;
    private Button buttonRegister;
    private static final String TAG = "RegisterActivity";
    private DatePickerDialog picker;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViews();

        //setting up datePicker on editText
        editTextRegisterDoB.setOnClickListener(v -> {
            final Calendar  calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            //Date picker dialog
            picker = new DatePickerDialog(RegisterActivity.this, (view, year1, month1, dayOfMonth) -> editTextRegisterDoB.setText(dayOfMonth+"/"+(month1 +1)+"/"+ year1), year,month,day);
            picker.show();
        });

        Toast.makeText(RegisterActivity.this,"You can register now", Toast.LENGTH_LONG).show();
        buttonRegister.setOnClickListener(v -> valuesCheck());
    }

    private void valuesCheck() {
        String fullName = editTextRegisterFullName.getText().toString();
        String email = editTextRegisterEmail.getText().toString();
        String doB = editTextRegisterDoB.getText().toString();
        String password = editTextRegisterPassword.getText().toString();
        String confirmPassword = editTextRegisterConfirmPassword.getText().toString();

        if(TextUtils.isEmpty(fullName)){
            Toast.makeText(RegisterActivity.this,"Please enter your full name", Toast.LENGTH_LONG).show();
            editTextRegisterFullName.setError("Full Name is required");
            editTextRegisterFullName.requestFocus();
        }else if(TextUtils.isEmpty(email)){
            Toast.makeText(RegisterActivity.this,"Please enter your email", Toast.LENGTH_LONG).show();
            editTextRegisterEmail.setError("Email is required");
            editTextRegisterEmail.requestFocus();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(RegisterActivity.this,"Please re-enter your email", Toast.LENGTH_LONG).show();
            editTextRegisterEmail.setError("Valid email is required");
            editTextRegisterEmail.requestFocus();
        }else if(TextUtils.isEmpty(doB)){
            Toast.makeText(RegisterActivity.this,"Please enter your date of birth", Toast.LENGTH_LONG).show();
            editTextRegisterDoB.setError("Date of birth is required");
            editTextRegisterDoB.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this,"Please enter your password", Toast.LENGTH_LONG).show();
            editTextRegisterPassword.setError("Password is required");
            editTextRegisterPassword.requestFocus();
        }else if(password.length()<6){
            Toast.makeText(RegisterActivity.this,"Password should be at lest 6 digits", Toast.LENGTH_LONG).show();
            editTextRegisterPassword.setError("Password too weak");
            editTextRegisterPassword.requestFocus();
        }else if(TextUtils.isEmpty(confirmPassword)){
            Toast.makeText(RegisterActivity.this,"Please confirm your password", Toast.LENGTH_LONG).show();
            editTextRegisterConfirmPassword.setError("Password Confirmation is required");
            editTextRegisterConfirmPassword.requestFocus();
        }else if(!password.equals(confirmPassword)){
            Toast.makeText(RegisterActivity.this,"Please enter same password", Toast.LENGTH_LONG).show();
            editTextRegisterConfirmPassword.setError("Password Confirmation is required");
            editTextRegisterConfirmPassword.requestFocus();
            editTextRegisterPassword.clearComposingText();
            editTextRegisterConfirmPassword.clearComposingText();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            registerUser(fullName,email,doB,password);
        }
    }

    private void registerUser(String fullName, String email, String doB, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(RegisterActivity.this, task -> {
            if(task.isSuccessful()){

                FirebaseUser firebaseUser = auth.getCurrentUser();

                //enter user data into the firebase realtime database
                ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(fullName,doB,email,password);

                DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
                referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        firebaseUser.sendEmailVerification();
                        Toast.makeText(RegisterActivity.this,"User registered successfully", Toast.LENGTH_LONG).show();
                        //open user profile
                        Intent intent = new Intent(RegisterActivity.this,UserProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(RegisterActivity.this,"User registered failed. Please try again", Toast.LENGTH_LONG).show();
                        firebaseUser.delete();
                    }
                    progressBar.setVisibility(View.GONE);
                });

            }else {
                try {
                    throw task.getException();
                }catch (FirebaseAuthUserCollisionException e){
                    editTextRegisterEmail.setError("User is already registered with this email");
                    editTextRegisterEmail.requestFocus();
                }catch(Exception e){
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void findViews() {
        editTextRegisterFullName = findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail = findViewById(R.id.editText_register_email);
        editTextRegisterDoB = findViewById(R.id.editText_register_dob);
        editTextRegisterPassword = findViewById(R.id.editText_register_password);
        editTextRegisterConfirmPassword = findViewById(R.id.editText_register_confirm_password);
        buttonRegister = findViewById(R.id.button_register);
        progressBar = findViewById(R.id.progressBar);
    }
}