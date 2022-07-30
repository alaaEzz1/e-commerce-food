package com.elmohandes.e_comercefood.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.databinding.ActivityShowDetailsBinding;
import com.elmohandes.e_comercefood.models.CartModel;
import com.elmohandes.e_comercefood.models.FoodModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowDetailsActivity extends AppCompatActivity {

    ActivityShowDetailsBinding binding;
    int numberOfItems = 1;
    private FirebaseFirestore firestore;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);

        binding = ActivityShowDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        getData();
    }

    private void getData() {
        FoodModel foodModel = (FoodModel) getIntent().getSerializableExtra("foodModel");
        Glide.with(this).load(foodModel.getPic()).placeholder(R.mipmap.ic_burger)
                .into(binding.showDetailsImg);
        binding.showDetailsTitle.setText(foodModel.getTitle());
        binding.showDetailsDesc.setText(foodModel.getDescription());
        binding.showDetailsPrice.setText("LE "+foodModel.getFee());
        binding.showDetailsNumber.setText(String.valueOf(numberOfItems));

        binding.showDetailsImgPlus.setOnClickListener(v -> {
            numberOfItems++;
            binding.showDetailsNumber.setText(String.valueOf(numberOfItems));
            binding.showDetailsPrice.setText("LE "+(foodModel.getFee() * numberOfItems));
        });

        binding.showDetailsImgMinus.setOnClickListener(v -> {
            if (numberOfItems > 1){
                numberOfItems--;
            }

            binding.showDetailsNumber.setText(String.valueOf(numberOfItems));
            binding.showDetailsPrice.setText("LE "+(foodModel.getFee() * numberOfItems));
        });

        binding.showDetailsBtnAdd.setOnClickListener(v -> {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            id = firestore.collection("cart").document().getId();
            CartModel model = new CartModel(id,uid,foodModel);
            foodModel.setNumberInCart(numberOfItems);
            firestore.collection("cart").document(id).set(model).
                    addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(this, "added to cart",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

            Intent intent = new Intent(getApplicationContext(),CartActivity.class);
            startActivity(intent);
            finish();
        });
    }
}