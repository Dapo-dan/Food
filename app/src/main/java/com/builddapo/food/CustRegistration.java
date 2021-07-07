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

public class CustRegistration extends AppCompatActivity {


    String [] Oyo = {"Ibadan","Ogbomosho",""};
    String [] Osun = {"Ife","Ilesha","Ikire"};
    String [] Lagos = {"Ikeja","Ojuelegba","Magodo"};

    TextInputLayout Fname,Lname,Email,Pass,cpass,mobileno,localaddress,area,pincode;
    Spinner Statespin,Cityspin;
    Button signup, Emaill, Phone;
    CountryCodePicker Cpp;
    FirebaseAuth FAuth;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    String fname,lname,emailid,password,confpassword,mobile,Localaddress,Area,Pincode,statee,cityy;
    String role="Customer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_registration);

        Fname = findViewById(R.id.Fname);
        Lname = findViewById(R.id.Lname);
        Email = findViewById(R.id.EmailId);
        Pass = findViewById(R.id.Password);
        cpass = findViewById(R.id.Confirmpass);
        mobileno = findViewById(R.id.Mobilenumber);
        localaddress = findViewById(R.id.Localaddress);
        pincode = findViewById(R.id.PinCodee);
        Statespin =  findViewById(R.id.Statee);
        Cityspin =  findViewById(R.id.City);
        area = findViewById(R.id.area);

        signup = findViewById(R.id.Signupbtn);
        Emaill = findViewById(R.id.emaill);
        Phone = findViewById(R.id.phonee);

        Cpp = findViewById(R.id.countryCode);

        Statespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Object value = parent.getItemAtPosition(position);
                statee = value.toString().trim();
                if(statee.equals("Oyo")){
                    ArrayList<String> list = new ArrayList<>();
                    Collections.addAll(list, Oyo);
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CustRegistration.this, android.R.layout.simple_spinner_item, list);
                    Cityspin.setAdapter(arrayAdapter);
                }
                if(statee.equals("Osun")){
                    ArrayList<String> list = new ArrayList<>(Arrays.asList(Osun));
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CustRegistration.this, android.R.layout.simple_spinner_item, list);
                    Cityspin.setAdapter(arrayAdapter);
                }
                if(statee.equals("Lagos")){
                    ArrayList<String> list = new ArrayList<>(Arrays.asList(Lagos));
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(CustRegistration.this,android.R.layout.simple_spinner_item,list);
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

        databaseReference = FirebaseDatabase.getInstance().getReference("Customer");
        FAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(v -> {
            fname = Fname.getEditText().getText().toString().trim();
            lname = Lname.getEditText().getText().toString().trim();
            emailid = Email.getEditText().getText().toString().trim();
            mobile = mobileno.getEditText().getText().toString().trim();
            password = Pass.getEditText().getText().toString().trim();
            confpassword = cpass.getEditText().getText().toString().trim();
            Area = area.getEditText().getText().toString().trim();
            Localaddress = localaddress.getEditText().getText().toString().trim();
            Pincode = pincode.getEditText().getText().toString().trim();

            if (isValid()){
                final ProgressDialog mDialog = new ProgressDialog(CustRegistration.this);
                mDialog.setCancelable(false);
                mDialog.setCanceledOnTouchOutside(false);
                mDialog.setMessage("Registration in progress please wait......");
                mDialog.show();

                FAuth.createUserWithEmailAndPassword(emailid,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
                                hashMap1.put("Local Address",Localaddress);

                                firebaseDatabase.getInstance().getReference("Customer")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(hashMap1).addOnCompleteListener(task11 -> {
                                            mDialog.dismiss();

                                            FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task111 -> {

                                                if(task111.isSuccessful()){
                                                    AlertDialog.Builder builder = new AlertDialog.Builder(CustRegistration.this);
                                                    builder.setMessage("You Have Registered! Make Sure To Verify Your Email");
                                                    builder.setCancelable(false);
                                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

                                                            dialog.dismiss();

                                                            String phonenumber = Cpp.getSelectedCountryCodeWithPlus() + mobile;
                                                            Intent b = new Intent(CustRegistration.this, CustVerifyPhone.class);
                                                            b.putExtra("phonenumber", phonenumber);
                                                            startActivity(b);

                                                        }
                                                    });
                                                    AlertDialog Alert = builder.create();
                                                    Alert.show();
                                                }else{
                                                    mDialog.dismiss();
                                                    ReusableCodeForAll.ShowAlert(CustRegistration.this,"Error", task111.getException().getMessage());
                                                }
                                            });

                                        });

                            });
                        }
                    }
                });
            }

        });
        Email.setOnClickListener(v -> {
            startActivity(new Intent(CustRegistration.this, Custloginemail.class));
            finish();
        });
        Phone.setOnClickListener(v -> {
            startActivity(new Intent(CustRegistration.this, Custloginphone.class));
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
        localaddress.setErrorEnabled(false);
        localaddress.setError("");
        pincode.setErrorEnabled(false);
        pincode.setError("");

        boolean isValid,isValidlocaladd=false,isValidlname=false,isValidname=false,isValidemail=false,isValidpassword=false,isValidconfpassword=false,isValidmobilenum=false,isValidarea=false,isValidpincode=false;
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
        if(TextUtils.isEmpty(Localaddress)){
            localaddress.setErrorEnabled(true);
            localaddress.setError("Fields Can't Be Empty");
        }else{
            isValidlocaladd = true;
        }

        isValid = isValidarea && isValidconfpassword && isValidpassword && isValidpincode && isValidemail && isValidmobilenum && isValidname && isValidlocaladd && isValidlname;
        return isValid;

    }

}