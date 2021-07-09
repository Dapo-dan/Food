package com.builddapo.food;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

public class CouRegistration extends AppCompatActivity {

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
    String role="Courier";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cou_registration);

        Fname = findViewById(R.id.fname);
        Lname = findViewById(R.id.lname);
        Email = findViewById(R.id.emailId);
        Pass = findViewById(R.id.password);
        cpass = findViewById(R.id.conpassword);
        mobileno = findViewById(R.id.mobileno);
        houseno = findViewById(R.id.HouseNo);
        pincode = findViewById(R.id.Pincode);
        Statespin =  findViewById(R.id.State);
        Cityspin =  findViewById(R.id.City);
        area = findViewById(R.id.areaa);

        signup = findViewById(R.id.SignUp);
        Emaill = findViewById(R.id.emaill);
        Phone = findViewById(R.id.phonee);

        Cpp = findViewById(R.id.ctrycode);

        Statespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object value = parent.getItemAtPosition(position);
                statee = value.toString().trim();
                if(statee.equals("Oyo")){
                    ArrayList<String> list = new ArrayList<>();
                    Collections.addAll(list, Oyo);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CouRegistration.this, android.R.layout.simple_spinner_item, list);
                    Cityspin.setAdapter(arrayAdapter);
                }
                if(statee.equals("Osun")){
                    ArrayList<String> list = new ArrayList<>(Arrays.asList(Osun));
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CouRegistration.this, android.R.layout.simple_spinner_item, list);
                    Cityspin.setAdapter(arrayAdapter);
                }
                if(statee.equals("Lagos")){
                    ArrayList<String> list = new ArrayList<>(Arrays.asList(Lagos));
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CouRegistration.this,android.R.layout.simple_spinner_item,list);
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
                final ProgressDialog mDialog = new ProgressDialog(CouRegistration.this);
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

                            firebaseDatabase.getInstance().getReference("Courier")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(hashMap1).addOnCompleteListener(task112 -> {
                                mDialog.dismiss();

                                FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task11 -> {

                                    if(task11.isSuccessful()){
                                        AlertDialog.Builder builder = new AlertDialog.Builder(CouRegistration.this);
                                        builder.setMessage("You Have Registered! Make Sure To Verify Your Email");
                                        builder.setCancelable(false);
                                        builder.setPositiveButton("Ok", (dialog, which) -> {

                                            dialog.dismiss();

                                            String phonenumber = Cpp.getSelectedCountryCodeWithPlus() + mobile;
                                            Intent b = new Intent(CouRegistration.this, CouVerifyPhone.class);
                                            b.putExtra("phonenumber", phonenumber);
                                            startActivity(b);

                                        });
                                        AlertDialog Alert = builder.create();
                                        Alert.show();
                                    }else{
                                        mDialog.dismiss();
                                        ReusableCodeForAll.ShowAlert(CouRegistration.this,"Error", task11.getException().getMessage());
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
            startActivity(new Intent(CouRegistration.this, Couloginemail.class));
            finish();
        });
        Phone.setOnClickListener(v -> {
            startActivity(new Intent(CouRegistration.this, Couloginphone.class));
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