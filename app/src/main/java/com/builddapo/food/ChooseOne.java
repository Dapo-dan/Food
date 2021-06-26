package com.builddapo.food;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class ChooseOne extends AppCompatActivity {

    Button Chef, Customer, Courier;
    Intent intent;
    String type;
    ConstraintLayout bgimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //to remove title bar and action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_choose_one);

        AnimationDrawable animationDrawable = new AnimationDrawable();
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.bg8), 3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.img1), 3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.img2), 3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.img3), 3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.img4), 3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.img5), 3000);
        animationDrawable.addFrame(getResources().getDrawable(R.drawable.img6), 3000);

        animationDrawable.setOneShot(false);
        animationDrawable.setEnterFadeDuration(850);
        animationDrawable.setExitFadeDuration(1600);

        bgimage = findViewById(R.id.back8);
        bgimage.setBackgroundDrawable(animationDrawable);
        animationDrawable.start();

        intent = getIntent();
        type = intent.getStringExtra("Home").toString().trim();

        Chef = findViewById(R.id.chef);
        Customer = findViewById(R.id.customer);
        Courier = findViewById(R.id.courier);

        Chef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("Email")){
                    Intent loginemail = new Intent(ChooseOne.this, Chefloginemail.class);
                    startActivity(loginemail);
                    finish();
                }
                if (type.equals("Phone")){
                    Intent loginphone = new Intent(ChooseOne.this, Chefloginphone.class);
                    startActivity(loginphone);
                    finish();
                }
                if (type.equals("SignUp")){
                    Intent Register = new Intent(ChooseOne.this, ChefRegistration.class);
                    startActivity(Register);
                    finish();
                }
            }
        });

        Customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("Email")){
                    Intent loginemailcust = new Intent(ChooseOne.this, Custloginemail.class);
                    startActivity(loginemailcust);
                    finish();
                }
                if (type.equals("Phone")){
                    Intent loginphonecust = new Intent(ChooseOne.this, Custloginphone.class);
                    startActivity(loginphonecust);
                    finish();
                }
                if (type.equals("SignUp")){
                    Intent Registercust = new Intent(ChooseOne.this, CustRegistration.class);
                    startActivity(Registercust);
                    finish();
                }
            }
        });

        Courier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equals("Email")){
                    Intent loginemail = new Intent(ChooseOne.this, Couloginemail.class);
                    startActivity(loginemail);
                    finish();
                }
                if (type.equals("Phone")){
                    Intent loginphonecou = new Intent(ChooseOne.this, Couloginphone.class);
                    startActivity(loginphonecou);
                    finish();
                }
                if (type.equals("SignUp")){
                    Intent Registercou = new Intent(ChooseOne.this, CouRegistration.class);
                    startActivity(Registercou);
                    finish();
                }
            }
        });

    }
}