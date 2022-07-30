package com.elmohandes.e_comercefood.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.databinding.CategoryItemBinding;
import com.elmohandes.e_comercefood.models.CategoryModel;
import com.elmohandes.e_comercefood.ui.ShowProductsActivity;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryVH> {

    private Context context;
    private ArrayList<CategoryModel> category;

    public CategoryAdapter(Context context, ArrayList<CategoryModel> category) {
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    public CategoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CategoryVH(LayoutInflater.from(context).inflate(R.layout.category_item
                ,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryVH holder, int position) {
        CategoryModel model = category.get(position);
        holder.binding.categoryTitle.setText(model.getTitle());
        Glide.with(context).load(model.getPic()).into(holder.binding.categoryImg);

        switch (position){
            case 0:
                holder.binding.categoryLayout.setBackground(ContextCompat
                        .getDrawable(context,R.drawable.category_bg));
                break;

            case 1:
                holder.binding.categoryLayout.setBackground(ContextCompat
                        .getDrawable(context,R.drawable.category_bg2));
                break;

            case 2:
                holder.binding.categoryLayout.setBackground(ContextCompat
                        .getDrawable(context,R.drawable.category_bg3));
                break;

            case 3:
                holder.binding.categoryLayout.setBackground(ContextCompat
                        .getDrawable(context,R.drawable.category_bg4));
                break;

            case 4:
                holder.binding.categoryLayout.setBackground(ContextCompat
                        .getDrawable(context,R.drawable.category_bg5));
                break;
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ShowProductsActivity.class);
            intent.putExtra("cat_name",model.getTitle());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return category.size();
    }

    public class CategoryVH extends RecyclerView.ViewHolder{

        CategoryItemBinding binding;

        public CategoryVH(@NonNull View itemView) {
            super(itemView);
            binding = CategoryItemBinding.bind(itemView);
        }
    }
}
