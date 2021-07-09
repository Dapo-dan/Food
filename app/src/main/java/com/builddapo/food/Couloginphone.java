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

public class Couloginphone extends AppCompatActivity {

    EditText num;
    Button sendotp, signinmail;
    TextView signup;
    CountryCodePicker cpp;
    FirebaseAuth Fauth;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_couloginphone);

        num = findViewById(R.id.Dnumber);
        sendotp = findViewById(R.id.Dotp);
        cpp = findViewById(R.id.DcountryCode);
        signinmail = findViewById(R.id.DbtnEmail);
        signup = findViewById(R.id.Dacsignup);

        Fauth = FirebaseAuth.getInstance();

        sendotp.setOnClickListener(v -> {
            number = num.getText().toString().trim();
            String Phonenum = cpp.getSelectedCountryCodeWithPlus()+number;
            Intent b = new Intent(Couloginphone.this, Cousendotp.class);

            b.putExtra("Phonenum", Phonenum);
            startActivity(b);
            finish();
        });

        signup.setOnClickListener(v -> {
            startActivity(new Intent(Couloginphone.this, CouRegistration.class));
            finish();
        });

        signinmail.setOnClickListener(v -> {
            startActivity(new Intent(Couloginphone.this, Couloginemail.class));
            finish();
        });

    }
}