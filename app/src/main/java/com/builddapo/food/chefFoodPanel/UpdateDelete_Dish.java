package com.builddapo.food.chefFoodPanel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.builddapo.food.ChefFoodPanel_BottomNavigation;
import com.builddapo.food.R;
import com.builddapo.food.UpdateDishMenu;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

public class UpdateDelete_Dish extends AppCompatActivity {

    TextInputLayout desc, qty, pri;
    TextView Dishname;
    ImageButton imageButton;
    Button Update_dish, Delete_dish;
    String ChefId, RandomUID, State, City, Area;
    String description, quantity, price, dishes;
    StorageReference ref;
    private Uri mcropimageuri;
    String ID;
    String dburi;
    Uri imageuri;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,dataa;
    FirebaseAuth Fauth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete__dish);

        Dishname = findViewById(R.id.dish_name);
        imageButton = findViewById(R.id.image_upload);
        desc = findViewById(R.id.description);
        qty = findViewById(R.id.Quantity);
        pri = findViewById(R.id.price);
        Update_dish = findViewById(R.id.Updatedish);
        Delete_dish = findViewById(R.id.Deletedish);

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataa = firebaseDatabase.getInstance().getReference("Chef").child(userid);
        dataa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Chef cheff = snapshot.getValue(Chef.class);
                State = cheff.getState();
                City = cheff.getCity();
                Area = cheff.getArea();

                Update_dish.setOnClickListener(v -> {
                    description = desc.getEditText().toString().trim();
                    quantity = qty.getEditText().toString().trim();
                    price = pri.getEditText().toString().trim();

                    if(isValid()){
                        if(imageuri != null) {
                            uploadImage();
                        }else{
                            updatedesc(dburi);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void updatedesc(String dburi) {

        ChefId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FoodDetails info = new FoodDetails(dishes,quantity,price,description,dburi,ID,ChefId);
        firebaseDatabase.getInstance().getReference("FoodDetails").child(State).child(City).child(Area).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ID)
                .setValue(info).addOnCompleteListener(task -> {
            progressDialog.dismiss();
            Toast.makeText(UpdateDelete_Dish.this, "Dish Updated Successfully", Toast.LENGTH_LONG).show();
        });
    }

    private void uploadImage() {

        if (imageuri != null){
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            RandomUID = UUID.randomUUID().toString();
            ref = storageReference.child(RandomUID);
            ref.putFile(imageuri).addOnSuccessListener(taskSnapshot -> {
                ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    updatedesc(String.valueOf(uri));
                });
            }).addOnFailureListener(e -> {
                progressDialog.dismiss();
                Toast.makeText(UpdateDelete_Dish.this, "Failed" + e.getMessage(), Toast.LENGTH_LONG).show();
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0 * snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    progressDialog.setMessage("Upload "+(int) progress +" %");
                    progressDialog.setCanceledOnTouchOutside(false);
                }
            });
            Delete_dish.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDelete_Dish.this);
                builder.setMessage("Are you sure you want to delete this dish");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseDatabase.getInstance().getReference("FoodDetails").child(State).child(City).child(Area)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(ID).removeValue();
                        AlertDialog.Builder food = new AlertDialog.Builder(UpdateDelete_Dish.this);
                        food.setMessage("Your dish has been deleted!");
                        food.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(UpdateDelete_Dish.this, ChefFoodPanel_BottomNavigation.class));
                            }
                        });
                        AlertDialog alert = food.create();
                        alert.show();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            });

            String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
            progressDialog = new ProgressDialog(UpdateDelete_Dish.this);
            databaseReference = FirebaseDatabase.getInstance().getReference("FoodDetails").child(State).child(City).child(Area)
                    .child(useridd).child(ID);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UpdateDishMenu updateDishMenu = snapshot.getValue(UpdateDishMenu.class);
                    desc.getEditText().setText(updateDishMenu.getDescription());
                    qty.getEditText().setText(updateDishMenu.getQuantity());
                    Dishname.setText("Dish name "+updateDishMenu.getDishes());
                    dishes = updateDishMenu.getDishes();
                    pri.getEditText().setText(updateDishMenu.getPrice());
                    Glide.with(UpdateDelete_Dish.this).load(updateDishMenu.getImageURL()).into(imageButton);
                    dburi = updateDishMenu.getImageURL();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            Fauth = FirebaseAuth.getInstance();
            databaseReference = firebaseDatabase.getInstance().getReference("FoodDetails");
            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();
            imageButton.setOnClickListener(v -> {
                onSelectImageClick(v);
            });
        }
    }

    private boolean isValid() {
        desc.setErrorEnabled(false);
        desc.setError("");
        qty.setErrorEnabled(false);
        qty.setError("");
        pri.setErrorEnabled(false);
        pri.setError("");

        boolean isValid, isValidDescription = false, isValidPrice = false, isValidQuantity = false;
        if(TextUtils.isEmpty(description)){
            desc.setErrorEnabled(true);
            desc.setError("Description is Required");
        }else{
            desc.setError(null);
            isValidDescription = true;
        }
        if(TextUtils.isEmpty(description)){
            pri.setErrorEnabled(true);
            pri.setError("Please mention the price");
        }else{
            pri.setError(null);
            isValidPrice = true;
        }
        if(TextUtils.isEmpty(description)){
            qty.setErrorEnabled(true);
            qty.setError("Enter number of plates or Item");
        }else{
            qty.setError(null);
            isValidQuantity = true;
        }
        isValid = isValidDescription && isValidPrice && isValidQuantity;
        return isValid;
    }

    private void startCropImageActivity (Uri imageuri){
        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON).setMultiTouchEnabled(true).start(this);
    }

    private void onSelectImageClick(View v){
        CropImage.startPickImageActivity(this);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (mcropimageuri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCropImageActivity(mcropimageuri);
        } else {
            Toast.makeText(this, "Canceling! Permission not granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            imageuri = CropImage.getPickImageResultUri(this, data);
            if(CropImage.isReadExternalStoragePermissionsRequired(this,imageuri)){
                mcropimageuri = imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
            }else{
                startCropImageActivity(imageuri);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){
                ((ImageButton)findViewById(R.id.image_upload)).setImageURI(result.getUri());
                Toast.makeText(this, "Cropped Successfully"+result.getSampleSize(), Toast.LENGTH_SHORT).show();
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Toast.makeText(this, "Failed to Crop"+ result.getError(), Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}