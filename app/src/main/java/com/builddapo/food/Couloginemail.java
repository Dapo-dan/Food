package com.builddapo.food;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Couloginemail extends AppCompatActivity {

    TextInputLayout email, pass;
    Button Signin, Signinphone;
    TextView Forgotpassword, signup;
    FirebaseAuth Fauth;
    String emailid, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couloginemail);

        try {
            email = findViewById(R.id.Demail);
            pass = findViewById(R.id.Dpassword);
            Signin = findViewById(R.id.DLogin);
            signup = findViewById(R.id.DtextView3);
            Forgotpassword = findViewById(R.id.Dforgotpass);
            Signinphone = findViewById(R.id.Dbtnphone);

            Fauth = FirebaseAuth.getInstance();

            Signin.setOnClickListener(v -> {
                emailid = Objects.requireNonNull(email.getEditText()).getText().toString().trim();
                pwd = Objects.requireNonNull(pass.getEditText()).getText().toString().trim();

                if (isValid()) {

                    final ProgressDialog mDialog = new ProgressDialog(Couloginemail.this);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setCancelable(false);
                    mDialog.setMessage("Sign in please wait....");
                    mDialog.show();


                    Fauth.signInWithEmailAndPassword(emailid, pwd).addOnCompleteListener(task -> {

                        if (task.isSuccessful()) {
                            mDialog.dismiss();

                            if (Objects.requireNonNull(Fauth.getCurrentUser()).isEmailVerified()) {
                                mDialog.dismiss();
                                Toast.makeText(Couloginemail.this, "Congratulations! You have successfully signed in", Toast.LENGTH_SHORT).show();
                                Intent Z = new Intent(Couloginemail.this, CouFoodPanel_BottomNavigation.class);
                                startActivity(Z);
                                finish();
                            } else {
                                ReusableCodeForAll.ShowAlert(Couloginemail.this, "Verification failed", "You have not verified your email");
                            }
                        } else {
                            mDialog.dismiss();
                            ReusableCodeForAll.ShowAlert(Couloginemail.this, "Error", Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
                    signup.setOnClickListener(v1 -> {
                        startActivity(new Intent(Couloginemail.this, CouRegistration.class));
                        finish();
                    });
                    Forgotpassword.setOnClickListener(v12 -> {
                        startActivity(new Intent(Couloginemail.this, CouForgotPassword.class));
                        finish();
                    });
                    Signinphone.setOnClickListener(v13 -> {
                        startActivity(new Intent(Couloginemail.this, Couloginphone.class));
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