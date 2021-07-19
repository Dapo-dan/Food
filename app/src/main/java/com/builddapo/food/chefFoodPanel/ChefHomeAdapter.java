package com.builddapo.food.chefFoodPanel;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.builddapo.food.R;
import com.builddapo.food.UpdateDishMenu;

import java.util.List;

public class ChefHomeAdapter extends RecyclerView.Adapter<ChefHomeAdapter.ViewHolder> {

    private Context mcont;
    private List <UpdateDishMenu> updateDishMenuList;
    public ChefHomeAdapter(Context context, List<UpdateDishMenu>updateDishMenuList){
        this.updateDishMenuList = updateDishMenuList;
        this.mcont = context;
    }
    @NonNull
    @Override
    public ChefHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcont).inflate(R.layout.chefmenu_update_delete, parent, false);
        return new ChefHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChefHomeAdapter.ViewHolder holder, int position) {

        final UpdateDishMenu updateDishMenu = updateDishMenuList.get(position);
        holder.dishes.setText(updateDishMenu.getDishes());
        updateDishMenu.getRandomYID();
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mcont,UpdateDelete_Dish.class);
            intent.putExtra("updatedeletedish",updateDishMenu.getRandomYID());
            mcont.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return updateDishMenuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dishes;

        public ViewHolder(View itemView){
            super(itemView);
            dishes = itemView.findViewById(R.id.dish_name);
        }
    }
}
