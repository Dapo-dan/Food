package com.builddapo.food;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class Custloginphone extends AppCompatActivity {

    EditText num;
    Button sendotp, signinmail;
    TextView signup;
    CountryCodePicker cpp;
    FirebaseAuth Fauth;
    String number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custloginphone);

        num = findViewById(R.id.Cnumber);
        sendotp = findViewById(R.id.Cotp);
        cpp = findViewById(R.id.CcountryCode);
        signinmail = findViewById(R.id.CbtnEmail);
        signup = findViewById(R.id.Cacsignup);

        Fauth = FirebaseAuth.getInstance();

        sendotp.setOnClickListener(v -> {
            number = num.getText().toString().trim();
            String Phonenum = cpp.getSelectedCountryCodeWithPlus()+number;
            Intent b = new Intent(Custloginphone.this, Custsendotp.class);

            b.putExtra("Phonenumber", Phonenum);
            startActivity(b);
            finish();
        });

        signup.setOnClickListener(v -> {
            startActivity(new Intent(Custloginphone.this, CustRegistration.class));
            finish();
        });

        signinmail.setOnClickListener(v -> {
            startActivity(new Intent(Custloginphone.this, Custloginemail.class));
            finish();
        });
    }
}