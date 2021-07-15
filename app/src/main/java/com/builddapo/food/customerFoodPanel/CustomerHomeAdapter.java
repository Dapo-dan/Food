package com.builddapo.food.customerFoodPanel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builddapo.food.R;
import com.builddapo.food.UpdateDishMenu;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.util.List;
import java.util.PrimitiveIterator;

public class CustomerHomeAdapter extends RecyclerView.Adapter<CustomerHomeAdapter.ViewHolder> {

    private Context mcontext;
    private List<UpdateDishMenu>updateDishMenuList;
    DatabaseReference databaseReference;

    public CustomerHomeAdapter(Context context, List<UpdateDishMenu>updateDishMenuList){

        this.updateDishMenuList = updateDishMenuList;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public CustomerHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.customer_menudish,parent,false);
        return new CustomerHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerHomeAdapter.ViewHolder holder, int position) {

        final UpdateDishMenu updateDishMenu = updateDishMenuList.get(position);
        Glide.with(mcontext).load(updateDishMenu.getImageURL()).into(holder.imageView);
        updateDishMenu.getRandomYID();
        updateDishMenu.getChefId();
        holder.Price.setText("Price: N"+ updateDishMenu.getPrice());
    }

    @Override
    public int getItemCount() {
        return updateDishMenuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView Dishname, Price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.menu_image);
            Dishname = itemView.findViewById(R.id.dishname);
            Price = itemView.findViewById(R.id.dishprice);

        }
    }

}
