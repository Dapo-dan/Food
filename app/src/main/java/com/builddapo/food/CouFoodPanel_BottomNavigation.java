package com.builddapo.food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.builddapo.food.courierFoodPanel.CourierPendingOrderFragment;
import com.builddapo.food.courierFoodPanel.CourierShippingFragment;
import com.builddapo.food.customerFoodPanel.CustomerHomeFragment;
import com.builddapo.food.customerFoodPanel.CustomerOrderFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CouFoodPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cou_food_panel__bottom_navigation);

        BottomNavigationView navigationView = findViewById(R.id.cou_buttom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.pendingorders:
                fragment = new CourierPendingOrderFragment();
                break;
        }
        switch (item.getItemId()) {
            case R.id.shiporders:
                fragment = new CourierShippingFragment();
                break;
        }
        return loadcourierfragment(fragment);
    }

    private boolean loadcourierfragment(Fragment fragment) {

        if (fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            return true;
        }
        return false;
    }

}