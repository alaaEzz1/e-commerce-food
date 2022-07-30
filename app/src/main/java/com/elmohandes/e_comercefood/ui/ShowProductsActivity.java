package com.elmohandes.e_comercefood.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.adapters.PopularAdapter;
import com.elmohandes.e_comercefood.databinding.ActivityShowProductsBinding;
import com.elmohandes.e_comercefood.models.FoodModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class ShowProductsActivity extends AppCompatActivity {

    ActivityShowProductsBinding binding;
    FirebaseFirestore firestore;
    PopularAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_products);

        binding = ActivityShowProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String cat_name = getIntent().getStringExtra("cat_name");
        firestore = FirebaseFirestore.getInstance();

        if (!cat_name.isEmpty()){
            showProductsData(cat_name);
        }

    }

    private void showProductsData(String cat_name) {

        GridLayoutManager manager = new GridLayoutManager(this,2);
        ArrayList<FoodModel> foodList = new ArrayList<>();

        firestore.collection("food").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        foodList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()){
                            FoodModel foodModel = document.toObject(FoodModel.class);
                            if (foodModel.getCategoryName().equals(cat_name)){
                                foodList.add(foodModel);
                                adapter = new PopularAdapter(this,foodList);
                                binding.showProductRv.setLayoutManager(manager);
                                binding.showProductRv.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                });

    }
}