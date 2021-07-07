package com.builddapo.food;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainMenu extends AppCompatActivity {

    Button signinphone, signinemail, signup;
    ImageView bgimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //to remove title bar and action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main_menu);

        final Animation zoomin = AnimationUtils.loadAnimation(this, R.anim.zoomin);
        final Animation zoomout = AnimationUtils.loadAnimation(this, R.anim.zoomout);

        bgimage=findViewById(R.id.bg_image);
        bgimage.setAnimation(zoomin);
        bgimage.setAnimation(zoomout);

        zoomin.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bgimage.setAnimation(zoomout);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        signinemail= findViewById(R.id.signIn_email);
        signinphone = findViewById(R.id.signIn_phone);
        signup= findViewById(R.id.signUp);

        signinemail.setOnClickListener(v -> {
            Intent signemail = new Intent(MainMenu.this, ChooseOne.class);
            signemail.putExtra("Home", "Email");
            startActivity(signemail);
            finish();
        });

        signinphone.setOnClickListener(v -> {
            Intent signphone = new Intent(MainMenu.this, ChooseOne.class);
            signphone.putExtra("Home", "Phone");
            startActivity(signphone);
            finish();
        });

        signup.setOnClickListener(v -> {
            Intent signUp = new Intent(MainMenu.this, ChooseOne.class);
            signUp.putExtra("Home", "SignUp");
            startActivity(signUp);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
    }
}