package com.builddapo.food;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.builddapo.food.chefFoodPanel.ChefHomeFragment;
import com.builddapo.food.chefFoodPanel.ChefOrderFragment;
import com.builddapo.food.chefFoodPanel.ChefPendingOrderFragment;
import com.builddapo.food.chefFoodPanel.ChefProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChefFoodPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_food_panel__bottom_navigation);
        BottomNavigationView navigationView = findViewById(R.id.chef_buttom_navigation);
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()){
            case R.id.chefHome:
                fragment = new ChefHomeFragment();
                break;
            case R.id.pendingOrders:
                fragment = new ChefPendingOrderFragment();
                break;
            case R.id.Orders:
                fragment = new ChefOrderFragment();
                break;
            case R.id.chefProfile:
                fragment = new ChefProfileFragment();
                break;
        }
        return loadcheffragment(fragment);
    }

    private boolean loadcheffragment(Fragment fragment) {

        if (fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            return true;
        }
        return false;
    }
}