package com.builddapo.food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class CustVerifyPhone extends AppCompatActivity {

    String verificationId;
    Button resend, verify;
    FirebaseAuth FAuth;
    EditText entercode;
    String phoneno;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_verify_phone);

        phoneno = getIntent().getStringExtra("phonenumber").trim();

        resend = findViewById(R.id.resendOTPP);
        verify =  findViewById(R.id.verifyOTPP);
        txt =  findViewById(R.id.textt);
        entercode = findViewById(R.id.codee);
        FAuth = FirebaseAuth.getInstance();

        resend.setVisibility(View.INVISIBLE);
        txt.setVisibility(View.INVISIBLE);

        sendverificationcode(phoneno);

        verify.setOnClickListener(v -> {
            String code = entercode.getText().toString().trim();
            resend.setVisibility(View.INVISIBLE);

            if (code.length() == 0){
                entercode.setError("Enter code");
                entercode.requestFocus();
            }
        });

        new CountDownTimer(60000, 1000){

            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {

                txt.setVisibility(View.VISIBLE);
                txt.setText("Resend code within "+millisUntilFinished/1000+" Seconds");
            }

            @Override
            public void onFinish() {
                resend.setVisibility(View.VISIBLE);
                txt.setVisibility(View.VISIBLE);

            }
        }.start();

        resend.setOnClickListener(v -> {

            resend.setVisibility(View.VISIBLE);
            resendOTP(phoneno);

            new CountDownTimer(60000, 1000){

                @Override
                public void onTick(long millisUntilFinished) {

                    txt.setVisibility(View.VISIBLE);
                    txt.setText("Resend code within"+millisUntilFinished/1000+"Seconds");
                }

                @Override
                public void onFinish() {
                    resend.setVisibility(View.VISIBLE);
                    txt.setVisibility(View.VISIBLE);

                }
            }.start();
        });
    }
    private void resendOTP(String phonenum) {

        sendverificationcode(phonenum);

    }

    private void sendverificationcode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number, 60,
                TimeUnit.SECONDS,
                (Activity) TaskExecutors.MAIN_THREAD,
                mcallBack
        );
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            String code = phoneAuthCredential.getSmsCode();
            if (code!= null){
                entercode.setText(code); // Auto verification
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(CustVerifyPhone.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResending){

            super.onCodeSent(s, forceResending);
            verificationId = s;

        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        linkCredential(credential);
    }

    private void linkCredential(PhoneAuthCredential credential) {
        FAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(CustVerifyPhone.this, task -> {
            if(task.isSuccessful()){
                Intent intent = new Intent(CustVerifyPhone.this, MainMenu.class);
                startActivity(intent);
                finish();
            } else{
                ReusableCodeForAll.ShowAlert(CustVerifyPhone.this, "Error", Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

}