package com.builddapo.food.customerFoodPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.builddapo.food.R;
import com.builddapo.food.UpdateDishMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerHomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    private List<UpdateDishMenu> updateDishMenuList;
    private CustomerHomeAdapter adapter;
    String City, State, Area;
    DatabaseReference dataa, databaseReference;
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cust_home, null);
        getActivity().setTitle("Home");
        recyclerView = v.findViewById(R.id.recycle_menu);
        recyclerView.setHasFixedSize(true);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.move);
        recyclerView.startAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        updateDishMenuList = new ArrayList<>();
        swipeRefreshLayout = v.findViewById(R.id.swipelayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.red);

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                dataa = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
                dataa.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Customer custo = snapshot.getValue(Customer.class);
                        State = custo.getState();
                        City = custo.getCity();
                        Area = custo.getArea();
                        customermenu();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        return v;
    }

    @Override
    public void onRefresh() {
        customermenu();
    }

    private void customermenu() {

        swipeRefreshLayout.setRefreshing(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodDetails").child(State).child(City).child(Area);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                updateDishMenuList.clear();
                for (DataSnapshot snapshot1: snapshot.getChildren()){
                    for (DataSnapshot snapshot2: snapshot1.getChildren()){
                        UpdateDishMenu updateDishMenu = snapshot2.getValue(UpdateDishMenu.class);
                        updateDishMenuList.add(updateDishMenu);
                    }
                }
                adapter = new CustomerHomeAdapter(getContext(),updateDishMenuList);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
