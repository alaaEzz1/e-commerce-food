package com.elmohandes.e_comercefood.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.databinding.CategoryItemBinding;
import com.elmohandes.e_comercefood.databinding.PopularFoodItemBinding;
import com.elmohandes.e_comercefood.models.CategoryModel;
import com.elmohandes.e_comercefood.models.FoodModel;
import com.elmohandes.e_comercefood.ui.ShowDetailsActivity;

import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.CategoryVH> {

    private Context context;
    private ArrayList<FoodModel> foodList;

    public PopularAdapter(Context context, ArrayList<FoodModel> foodList) {
        this.context = context;
        this.foodList = foodList;
    }

    @NonNull
    @Override
    public CategoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryVH(LayoutInflater.from(context).inflate(R.layout.popular_food_item
                ,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVH holder, int position) {
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

    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    public class CategoryVH extends RecyclerView.ViewHolder{

        PopularFoodItemBinding binding;

        public CategoryVH(@NonNull View itemView) {
            super(itemView);
            binding = PopularFoodItemBinding.bind(itemView);
        }
    }
}
