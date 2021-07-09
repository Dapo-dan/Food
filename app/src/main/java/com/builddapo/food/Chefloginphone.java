package com.builddapo.food;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.hbb20.CountryCodePicker;

public class Chefloginphone extends AppCompatActivity {

    EditText num;
    Button sendotp, signinmail;
    TextView signup;
    CountryCodePicker cpp;
    FirebaseAuth Fauth;
    String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chefloginphone);

        num = findViewById(R.id.number);
        sendotp = findViewById(R.id.otp);
        cpp = findViewById(R.id.countryCode);
        signinmail = findViewById(R.id.Email);
        signup = findViewById(R.id.acsignup);

        Fauth = FirebaseAuth.getInstance();

        sendotp.setOnClickListener(v -> {
            number = num.getText().toString().trim();
            String Phonenum = cpp.getSelectedCountryCodeWithPlus()+number;
            Intent b = new Intent(Chefloginphone.this, Chefsendotp.class);

            b.putExtra("Phonenum", Phonenum);
            startActivity(b);
            finish();
        });

        signup.setOnClickListener(v -> {
            startActivity(new Intent(Chefloginphone.this, ChefRegistration.class));
            finish();
        });

        signinmail.setOnClickListener(v -> {
            startActivity(new Intent(Chefloginphone.this, Chefloginemail.class));
            finish();
        });
    }
}