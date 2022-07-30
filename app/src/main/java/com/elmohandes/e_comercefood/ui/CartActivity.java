package com.elmohandes.e_comercefood.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.adapters.CartListAdapter;
import com.elmohandes.e_comercefood.adapters.listeners.OnCartChangeListener;
import com.elmohandes.e_comercefood.databinding.ActivityCartBinding;
import com.elmohandes.e_comercefood.models.CartModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements OnCartChangeListener {

    private ActivityCartBinding binding;
    private double tax;
    private RecyclerView.Adapter adapter;
    ArrayList<CartModel> cartModels;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        cartModels = new ArrayList<>();
        setupCartListViaFirebase();
        calculateCart(cartModels);
        bottomNavigation();

    }

    private void setupCartListViaFirebase() {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        binding.cartRv.setLayoutManager(manager);

        firestore.collection("cart").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        cartModels.clear();
                        for (QueryDocumentSnapshot snapshot: task.getResult()){
                            CartModel model = snapshot.toObject(CartModel.class);
                            if (model.getUid().equals(uid)){
                                cartModels.add(model);
                                adapter = new CartListAdapter(this, cartModels,this);
                                calculateCart(cartModels);
                                binding.cartRv.setAdapter(adapter);
                                if (cartModels.isEmpty()){
                                    binding.cartTxtEmpty.setVisibility(View.VISIBLE);
                                    binding.cartScroll.setVisibility(View.INVISIBLE);

                                }else {
                                    binding.cartTxtEmpty.setVisibility(View.GONE);
                                    binding.cartScroll.setVisibility(View.VISIBLE);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }else {
                        binding.cartTxtEmpty.setVisibility(View.VISIBLE);
                        binding.cartScroll.setVisibility(View.INVISIBLE);
                    }
                });

    }

    private void bottomNavigation() {

        binding.cartHome.setOnClickListener(v -> finish());

    }

    private void calculateCart(ArrayList<CartModel> models) {
        double fee = 0;
        double percentTax = 0.02;
        double delivery = 10;
        for (int i=0 ; i < models.size() ;i++){
            fee = fee + (models.get(i).getFoodModel().getFee() * models.get(i).
                    getFoodModel().getNumberInCart());
        }
        tax = Math.round((fee * percentTax)*100) / 100.0;
        double total = Math.round((fee + tax + delivery)*100) / 100.0;
        double itemTotal = Math.round(fee * 100) / 100.0;

        binding.cartTxtTotalItemsResult.setText("LE "+ itemTotal);
        binding.cartTxtTotalResult.setText("LE " + total);
        binding.cartTxtTaxResult.setText("LE "+ tax);
        binding.cartTxtDeliveryResult.setText("LE "+ delivery);

        if (models.isEmpty()){
            binding.cartTxtEmpty.setVisibility(View.VISIBLE);
            binding.cartScroll.setVisibility(View.INVISIBLE);

        }else {
            binding.cartTxtEmpty.setVisibility(View.GONE);
            binding.cartScroll.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onDataChangeListener(int position, ArrayList<CartModel> cartModelList) {
        double fee = 0;
        double percentTax = 0.02;
        double delivery = 10;
        fee = fee + (cartModelList.get(position).getFoodModel().getFee() *
                cartModelList.get(position).getFoodModel().getNumberInCart());
        tax = Math.round((fee * percentTax)*100) / 100.0;
        double total = Math.round((fee + tax + delivery)*100) / 100.0;
        double itemTotal = Math.round(fee * 100) / 100.0;

        binding.cartTxtTotalItemsResult.setText("LE "+ itemTotal);
        binding.cartTxtTotalResult.setText("LE " + total);
        binding.cartTxtTaxResult.setText("LE "+ tax);
        binding.cartTxtDeliveryResult.setText("LE "+ delivery);

        if (cartModelList.isEmpty()){
            binding.cartTxtEmpty.setVisibility(View.VISIBLE);
            binding.cartScroll.setVisibility(View.INVISIBLE);

        }else {
            binding.cartTxtEmpty.setVisibility(View.GONE);
            binding.cartScroll.setVisibility(View.VISIBLE);
        }
    }
}