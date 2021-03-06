package com.builddapo.food;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class ChefRegistration extends AppCompatActivity {

    String [] Oyo = {"Ibadan","Ogbomosho",""};
    String [] Osun = {"Ife","Ilesha","Ikire"};
    String [] Lagos = {"Ikeja","Ojuelegba","Magodo"};

    TextInputLayout Fname,Lname,Email,Pass,cpass,mobileno,houseno,area,pincode;
    Spinner Statespin,Cityspin;
    Button signup, Emaill, Phone;
    CountryCodePicker Cpp;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String fname,lname,emailid,password,confpassword,mobile,house,Area,Pincode,statee,cityy;
    String role="Chef";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_registration);

        Fname = findViewById(R.id.firstname);
        Lname = findViewById(R.id.lastname);
        Email = findViewById(R.id.Email);
        Pass = findViewById(R.id.Pwd);
        cpass = findViewById(R.id.Cpass);
        mobileno = findViewById(R.id.Mobileno);
        houseno = findViewById(R.id.houseNo);
        pincode = findViewById(R.id.PinCode);
        Statespin =  findViewById(R.id.Statee);
        Cityspin =  findViewById(R.id.City);
        area = findViewById(R.id.area);

        signup = findViewById(R.id.Signup);
        Emaill = findViewById(R.id.email);
        Phone = findViewById(R.id.phone);

        Cpp = findViewById(R.id.countryCode);

        Statespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object value = parent.getItemAtPosition(position);
                statee = value.toString().trim();
                if(statee.equals("Oyo")){
                    ArrayList<String> list = new ArrayList<>();
                    Collections.addAll(list, Oyo);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ChefRegistration.this, android.R.layout.simple_spinner_item, list);
                    Cityspin.setAdapter(arrayAdapter);
                }
                if(statee.equals("Osun")){
                    ArrayList<String> list = new ArrayList<>(Arrays.asList(Osun));
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ChefRegistration.this, android.R.layout.simple_spinner_item, list);
                    Cityspin.setAdapter(arrayAdapter);
                }
                if(statee.equals("Lagos")){
                    ArrayList<String> list = new ArrayList<>(Arrays.asList(Lagos));
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ChefRegistration.this,android.R.layout.simple_spinner_item,list);
                    Cityspin.setAdapter(arrayAdapter);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Cityspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object value = parent.getItemAtPosition(position);
                cityy = value.toString().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Chef");
        FAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(v -> {

            fname = Objects.requireNonNull(Fname.getEditText()).getText().toString().trim();
            lname = Objects.requireNonNull(Lname.getEditText()).getText().toString().trim();
            emailid = Objects.requireNonNull(Email.getEditText()).getText().toString().trim();
            mobile = Objects.requireNonNull(mobileno.getEditText()).getText().toString().trim();
            password = Objects.requireNonNull(Pass.getEditText()).getText().toString().trim();
            confpassword = Objects.requireNonNull(cpass.getEditText()).getText().toString().trim();
            Area = Objects.requireNonNull(area.getEditText()).getText().toString().trim();
            house = Objects.requireNonNull(houseno.getEditText()).getText().toString().trim();
            Pincode = Objects.requireNonNull(pincode.getEditText()).getText().toString().trim();

            if (isValid()){
                final ProgressDialog mDialog = new ProgressDialog(ChefRegistration.this);
                mDialog.setCancelable(false);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setMessage("Registration in progress please wait......");
                mDialog.show();

                FAuth.createUserWithEmailAndPassword(emailid,password).addOnCompleteListener(task -> {

                    if (task.isSuccessful()){
                        String useridd = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(useridd);
                        final HashMap<String , String> hashMap = new HashMap<>();
                        hashMap.put("Role",role);
                        databaseReference.setValue(hashMap).addOnCompleteListener(task1 -> {

                            HashMap<String , String> hashMap1 = new HashMap<>();
                            hashMap1.put("Mobile No",mobile);
                            hashMap1.put("First Name",fname);
                            hashMap1.put("Last Name",lname);
                            hashMap1.put("EmailId",emailid);
                            hashMap1.put("City",cityy);
                            hashMap1.put("Area",Area);
                            hashMap1.put("Password",password);
                            hashMap1.put("Pincode",Pincode);
                            hashMap1.put("State",statee);
                            hashMap1.put("Confirm Password",confpassword);
                            hashMap1.put("House",house);

                            firebaseDatabase.getInstance().getReference("Chef")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(hashMap1).addOnCompleteListener(task112 -> {
                                        mDialog.dismiss();

                                        FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task11 -> {

                                            if(task11.isSuccessful()){
                                                AlertDialog.Builder builder = new AlertDialog.Builder(ChefRegistration.this);
                                                builder.setMessage("You Have Registered! Make Sure To Verify Your Email");
                                                builder.setCancelable(false);
                                                builder.setPositiveButton("Ok", (dialog, which) -> {

                                                    dialog.dismiss();

                                                    String phonenumber = Cpp.getSelectedCountryCodeWithPlus() + mobile;
                                                    Intent b = new Intent(ChefRegistration.this, ChefVerifyPhone.class);
                                                    b.putExtra("phonenumber", phonenumber);
                                                    startActivity(b);

                                                });
                                                AlertDialog Alert = builder.create();
                                                Alert.show();
                                            }else{
                                                mDialog.dismiss();
                                                ReusableCodeForAll.ShowAlert(ChefRegistration.this,"Error", task11.getException().getMessage());
                                            }
                                        });

                                    });

                        });
                    }
                });
            }
//
        });
        Email.setOnClickListener(v -> {
            startActivity(new Intent(ChefRegistration.this, Chefloginemail.class));
            finish();
        });
        Phone.setOnClickListener(v -> {
            startActivity(new Intent(ChefRegistration.this, Chefloginphone.class));
            finish();
        });
    }

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public boolean isValid(){
        Email.setErrorEnabled(false);
        Email.setError("");
        Fname.setErrorEnabled(false);
        Fname.setError("");
        Lname.setErrorEnabled(false);
        Lname.setError("");
        Pass.setErrorEnabled(false);
        Pass.setError("");
        mobileno.setErrorEnabled(false);
        mobileno.setError("");
        cpass.setErrorEnabled(false);
        cpass.setError("");
        area.setErrorEnabled(false);
        area.setError("");
        houseno.setErrorEnabled(false);
        houseno.setError("");
        pincode.setErrorEnabled(false);
        pincode.setError("");

        boolean isValid,isValidhouseno=false,isValidlname=false,isValidname=false,isValidemail=false,isValidpassword=false,isValidconfpassword=false,isValidmobilenum=false,isValidarea=false,isValidpincode=false;
        if(TextUtils.isEmpty(fname)){
            Fname.setErrorEnabled(true);
            Fname.setError("Enter First Name");
        }else{
            isValidname = true;
        }
        if(TextUtils.isEmpty(lname)){
            Lname.setErrorEnabled(true);
            Lname.setError("Enter Last Name");
        }else{
            isValidlname = true;
        }
        if(TextUtils.isEmpty(emailid)){
            Email.setErrorEnabled(true);
            Email.setError("Email Is Required");
        }else{
            if(emailid.matches(emailpattern)){
                isValidemail = true;
            }else{
                Email.setErrorEnabled(true);
                Email.setError("Enter a Valid Email Id");
            }
        }
        if(TextUtils.isEmpty(password)){
            Pass.setErrorEnabled(true);
            Pass.setError("Enter Password");
        }else{
            if(password.length()<8){
                Pass.setErrorEnabled(true);
                Pass.setError("Password is Weak");
            }else{
                isValidpassword = true;
            }
        }
        if(TextUtils.isEmpty(confpassword)){
            cpass.setErrorEnabled(true);
            cpass.setError("Enter Password Again");
        }else{
            if(!password.equals(confpassword)){
                cpass.setErrorEnabled(true);
                cpass.setError("Password Dosen't Match");
            }else{
                isValidconfpassword = true;
            }
        }
        if(TextUtils.isEmpty(mobile)){
            mobileno.setErrorEnabled(true);
            mobileno.setError("Mobile Number Is Required");
        }else{
            if(mobile.length()<10){
                mobileno.setErrorEnabled(true);
                mobileno.setError("Invalid Mobile Number");
            }else{
                isValidmobilenum = true;
            }
        }
        if(TextUtils.isEmpty(Area)){
            area.setErrorEnabled(true);
            area.setError("Area Is Required");
        }else{
            isValidarea = true;
        }
        if(TextUtils.isEmpty(Pincode)){
            pincode.setErrorEnabled(true);
            pincode.setError("Please Enter Pincode");
        }else{
            isValidpincode = true;
        }
        if(TextUtils.isEmpty(house)){
            houseno.setErrorEnabled(true);
            houseno.setError("Fields Can't Be Empty");
        }else{
            isValidhouseno = true;
        }

        isValid = isValidarea && isValidconfpassword && isValidpassword && isValidpincode && isValidemail && isValidmobilenum && isValidname && isValidhouseno && isValidlname;
        return isValid;

    }
}