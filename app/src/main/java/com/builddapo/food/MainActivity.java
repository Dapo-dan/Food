package com.builddapo.food;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    TextView  textView1;
    TextView textView2;
    FirebaseAuth Fauth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //to remove title bar and action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_main);

        textView1 = findViewById(R.id.app_name);
        textView2 = findViewById(R.id.developer);

        textView1.animate().alpha(0f).setDuration(0);
        textView2.animate().alpha(0f).setDuration(0);

        textView1.animate().alpha(1f).setDuration(1000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                textView2.animate().alpha(1f).setDuration(800);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });

        new Handler().postDelayed(() -> {

            Fauth = FirebaseAuth.getInstance();
            if(Fauth.getCurrentUser()!= null){
                if(Fauth.getCurrentUser().isEmailVerified()){
                    Fauth = FirebaseAuth.getInstance();

                    databaseReference = FirebaseDatabase.getInstance().getReference("User").child(FirebaseAuth.getInstance().getUid()+"/Role");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String role = snapshot.getValue(String.class);
                            assert role != null;
                            if(role.equals("Chef")){
                                startActivity(new Intent(MainActivity.this, ChefFoodPanel_BottomNavigation.class));
                                finish();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(MainActivity.this, error.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Check whether you have verified your detail, Otherwise please verify");
                    builder.setCancelable(false);
                    builder.setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                        Intent intent = new Intent(MainActivity.this, MainMenu.class);
                        startActivity(intent);
                        finish();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    Fauth.signOut();
                }
            } else {
                Intent intent = new Intent(MainActivity.this, MainMenu.class);
                startActivity(intent);
                finish();
            }

        },3000);
    }
}