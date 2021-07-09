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

import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Cousendotp extends AppCompatActivity {

    String verificationId;
    Button resend, verify;
    FirebaseAuth FAuth;
    EditText entercode;
    String phoneno;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cousendotp);

        phoneno = getIntent().getStringExtra("Phonenum").trim();

        resend=  findViewById(R.id.DresendOTP);
        verify=  findViewById(R.id.DverifyOTP);
        txt =  findViewById(R.id.Dtext);
        entercode =  findViewById(R.id.Dcode);
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

            resend.setVisibility(View.INVISIBLE);
            resendOTP(phoneno);

            new CountDownTimer(60000, 1000){

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
            Toast.makeText(Cousendotp.this, e.getMessage(), Toast.LENGTH_LONG).show();

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResending){

            super.onCodeSent(s, forceResending);
            verificationId = s;

        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhone(credential);
    }

    private void signInWithPhone(PhoneAuthCredential credential) {
        FAuth.signInWithCredential(credential).addOnCompleteListener(Cousendotp.this, task -> {
            if(task.isSuccessful()){
                startActivity(new Intent(Cousendotp.this, ChefFoodPanel_BottomNavigation.class));
                finish();
            } else{
                ReusableCodeForAll.ShowAlert(Cousendotp.this, "Error", task.getException().getMessage());
            }
        });
    }

}