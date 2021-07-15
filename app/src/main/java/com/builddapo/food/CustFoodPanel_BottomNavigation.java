package com.builddapo.food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.builddapo.food.customerFoodPanel.CustomerCartFragment;
import com.builddapo.food.customerFoodPanel.CustomerHomeFragment;
import com.builddapo.food.customerFoodPanel.CustomerOrderFragment;
import com.builddapo.food.customerFoodPanel.CustomerProfileFragment;
import com.builddapo.food.customerFoodPanel.CustomerTrackFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CustFoodPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_food_panel__bottom_navigation);

        BottomNavigationView navigationView = findViewById(R.id.cust_buttom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
        String  name = getIntent().getStringExtra("PAGE");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(name!=null){
            if(name.equalsIgnoreCase("Homepage")){
                loadcustomerfragment(new CustomerHomeFragment());
            }else if(name.equalsIgnoreCase("Preparingpage")){
                loadcustomerfragment(new CustomerTrackFragment());
            }else if(name.equalsIgnoreCase("DeliveryOrderpage")){
                loadcustomerfragment(new CustomerTrackFragment());
            }else if(name.equalsIgnoreCase("Thankyoupage")){
                loadcustomerfragment(new CustomerHomeFragment());
            }
        } else {
            loadcustomerfragment(new CustomerTrackFragment());
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.customerHome:
                fragment = new CustomerHomeFragment();
                break;
        }
        switch (item.getItemId()) {
            case R.id.customerOrder:
                fragment = new CustomerOrderFragment();
                break;
        }
        switch (item.getItemId()) {
            case R.id.Cart:
                fragment = new CustomerCartFragment();
                break;
        }
        switch (item.getItemId()) {
            case R.id.customerProfile:
                fragment = new CustomerProfileFragment();
                break;
        }
        switch (item.getItemId()) {
            case R.id.track:
                fragment = new CustomerTrackFragment();
                break;
        }
        return loadcustomerfragment(fragment);
    }

    private boolean loadcustomerfragment(Fragment fragment) {

        if (fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            return true;
        }
        return false;
    }

}