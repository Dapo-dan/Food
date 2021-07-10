package com.builddapo.food;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Chefloginemail extends AppCompatActivity {

    TextInputLayout email, pass;
    Button Signin, Signinphone;
    TextView Forgotpassword, signup;
    FirebaseAuth Fauth;
    String emailid, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chefloginemail);

        try {
            email = findViewById(R.id.Lemail);
            pass = findViewById(R.id.Lpassword);
            Signin = findViewById(R.id.button4);
            signup = findViewById(R.id.textView3);
            Forgotpassword = findViewById(R.id.forgot_password);
            Signinphone = findViewById(R.id.btnphone);

            Fauth = FirebaseAuth.getInstance();

            Signin.setOnClickListener(v -> {
                emailid = Objects.requireNonNull(email.getEditText()).getText().toString().trim();
                pwd = Objects.requireNonNull(pass.getEditText()).getText().toString().trim();

                if (isValid()) {

                    final ProgressDialog mDialog = new ProgressDialog(Chefloginemail.this);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setCancelable(false);
                    mDialog.setMessage("Sign in please wait....");
                    mDialog.show();


                    Fauth.signInWithEmailAndPassword(emailid, pwd).addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            mDialog.dismiss();

                            if (Objects.requireNonNull(Fauth.getCurrentUser()).isEmailVerified()) {
                                mDialog.dismiss();
                                Toast.makeText(Chefloginemail.this, "Congratulations! You have successfully signed in", Toast.LENGTH_SHORT).show();
                                Intent Z = new Intent(Chefloginemail.this, ChefFoodPanel_BottomNavigation.class);
                                startActivity(Z);
                                finish();
                            } else {
                                ReusableCodeForAll.ShowAlert(Chefloginemail.this, "Verification failed", "You have not verified your email");
                            }
                        } else {
                            mDialog.dismiss();
                            ReusableCodeForAll.ShowAlert(Chefloginemail.this, "Error", Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
                    signup.setOnClickListener(v1 -> {
                        startActivity(new Intent(Chefloginemail.this, ChefRegistration.class));
                        finish();
                    });
                    Forgotpassword.setOnClickListener(v12 -> {
                        startActivity(new Intent(Chefloginemail.this, ChefForgotPassword.class));
                        finish();
                    });
                    Signinphone.setOnClickListener(v13 -> {
                        startActivity(new Intent(Chefloginemail.this, Chefloginphone.class));
                        finish();
                    });
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public boolean isValid(){

        email.setErrorEnabled(false);
        email.setError("");
        pass.setErrorEnabled(false);
        pass.setError("");

        boolean isvalid, isvalidemail = false, isvalidpassword = false;
        if (TextUtils.isEmpty(emailid)) {
            email.setErrorEnabled(true);
            email.setError("Email is required");
        } else {
            if (emailid.matches(emailpattern)) {
                isvalidemail = true;
            } else {
                email.setErrorEnabled(true);
                email.setError("Invalid email address");
            }
        }
        if (TextUtils.isEmpty(pwd)) {
            pass.setErrorEnabled(true);
            pass.setError("Password is required");
        } else {
            isvalidpassword = true;
        }
        isvalid = isvalidemail && isvalidpassword;
        return isvalid;
    }
}