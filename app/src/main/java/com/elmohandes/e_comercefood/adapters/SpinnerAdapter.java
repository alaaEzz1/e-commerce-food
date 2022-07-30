package com.elmohandes.e_comercefood.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.databinding.SpinnerItemBinding;
import com.elmohandes.e_comercefood.models.CategoryModel;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    private Context context;
    private List<CategoryModel> categoryModels;

    public SpinnerAdapter(Context context, List<CategoryModel> categoryModels) {
        this.context = context;
        this.categoryModels = categoryModels;
    }

    @Override
    public int getCount() {
        return categoryModels.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.spinner_item,parent,false);
        SpinnerItemBinding binding = SpinnerItemBinding.bind(view);
        CategoryModel model = categoryModels.get(position);

        Glide.with(context).load(model.getPic()).into(binding.spinnerImg);
        binding.spinnerTxt.setText(model.getTitle());

        return view;
    }
}
