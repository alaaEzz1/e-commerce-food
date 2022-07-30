package com.elmohandes.e_comercefood.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.adapters.listeners.FoodListener;
import com.elmohandes.e_comercefood.databinding.PopularFoodItemBinding;
import com.elmohandes.e_comercefood.models.FoodModel;
import com.elmohandes.e_comercefood.ui.ShowDetailsActivity;

import java.util.ArrayList;

public class AdminFoodAdapter extends RecyclerView.Adapter<AdminFoodAdapter.AdminFoodVH> {

    private Context context;
    private ArrayList<FoodModel> foodList;
    private FoodListener listener;

    public AdminFoodAdapter(Context context, ArrayList<FoodModel> foodList, FoodListener listener) {
        this.context = context;
        this.foodList = foodList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminFoodVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminFoodVH(LayoutInflater.from(context).inflate(R.layout.popular_food_item
                ,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminFoodVH holder, int position) {

        FoodModel model = foodList.get(position);
        holder.binding.popularTxtTitle.setText(model.getTitle());
        Glide.with(context).load(model.getPic()).placeholder(R.drawable.ic_picture)
                .into(holder.binding.popularImg);
        holder.binding.popularTxtFee.setText(String.valueOf(model.getFee()));

        holder.binding.popularBtnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(context, ShowDetailsActivity.class);
            intent.putExtra("foodModel",  model);
            context.startActivity(intent);
        });

        holder.itemView.setOnClickListener(v -> listener.onFoodClick(position,model));

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class AdminFoodVH extends RecyclerView.ViewHolder{

        PopularFoodItemBinding binding;

        public AdminFoodVH(@NonNull View itemView) {
            super(itemView);

            binding = PopularFoodItemBinding.bind(itemView);

        }
    }
}
