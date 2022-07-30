package com.elmohandes.e_comercefood.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.adapters.listeners.CategoryListener;
import com.elmohandes.e_comercefood.databinding.CategoryItemBinding;
import com.elmohandes.e_comercefood.models.CategoryModel;

import java.util.ArrayList;

public class AdminCategoryAdapter extends RecyclerView.Adapter<AdminCategoryAdapter.AdminCategoryVH> {

    private Context context;
    private ArrayList<CategoryModel> categoryList;
    private CategoryListener listener;

    public AdminCategoryAdapter(Context context, ArrayList<CategoryModel> categoryList,
                                CategoryListener listener) {
        this.context = context;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AdminCategoryVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminCategoryVH(LayoutInflater.from(context).inflate(R.layout.category_item
                ,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminCategoryVH holder, int position) {

        CategoryModel model = categoryList.get(position);
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
            Toast.makeText(context, model.getTitle(), Toast.LENGTH_SHORT).show();
            listener.onCategoryClick(position,model);
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class AdminCategoryVH extends RecyclerView.ViewHolder{

        CategoryItemBinding binding;

        public AdminCategoryVH(@NonNull View itemView) {
            super(itemView);

            binding = CategoryItemBinding.bind(itemView);

        }
    }

}
