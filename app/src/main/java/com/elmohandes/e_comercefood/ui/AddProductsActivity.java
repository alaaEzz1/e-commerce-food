package com.elmohandes.e_comercefood.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.adapters.AdminFoodAdapter;
import com.elmohandes.e_comercefood.adapters.PopularAdapter;
import com.elmohandes.e_comercefood.adapters.SpinnerAdapter;
import com.elmohandes.e_comercefood.adapters.listeners.FoodListener;
import com.elmohandes.e_comercefood.databinding.ActivityAddProductsBinding;
import com.elmohandes.e_comercefood.models.CategoryModel;
import com.elmohandes.e_comercefood.models.FoodModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AddProductsActivity extends AppCompatActivity implements FoodListener {

    ActivityAddProductsBinding binding;
    FirebaseFirestore firestore;
    StorageReference uploadProfile;
    String category = "",id,productImage="";
    ProgressDialog loadingDialog;
    AdminFoodAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);

        binding = ActivityAddProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();
        uploadProfile = FirebaseStorage.getInstance().getReference();
        id = firestore.collection("food").document().getId();
        loadingDialog = new ProgressDialog(this);

        Glide.with(this).load(productImage).placeholder(R.drawable.ic_picture)
                .into(binding.addProductImage);

        addCategoryToSpinner();
        binding.addProductSave.setOnClickListener(v -> addProductToFirestore());
        binding.addProductImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            launcher.launch(intent);
        });
        showProductsData();

    }

    private void addCategoryToSpinner() {
        List<CategoryModel> categoryModels = new ArrayList<>();

        firestore.collection("categories").get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful()){
                       categoryModels.clear();
                       for (QueryDocumentSnapshot document : task.getResult()){
                           CategoryModel model = document.toObject(CategoryModel.class);
                           categoryModels.add(model);
                           SpinnerAdapter adapter = new SpinnerAdapter(this,categoryModels);
                           binding.addProductCategory.setAdapter(adapter);
                           adapter.notifyDataSetChanged();

                           String[] results = new String[categoryModels.size()];
                           binding.addProductCategory.setOnItemSelectedListener
                                   (new AdapterView.OnItemSelectedListener() {
                               @Override
                               public void onItemSelected(AdapterView<?> parent, View view
                                       , int position, long id) {
                                   results[position] = categoryModels.get(position).getTitle();
                                   category = results[position];
                               }

                               @Override
                               public void onNothingSelected(AdapterView<?> parent) {

                               }
                           });
                       }
                   }
                });

    }

    private void addProductToFirestore() {
        String title = binding.addProductTitle.getText().toString();
        String desc = binding.addProductDesc.getText().toString();
        int fee = Integer.parseInt(binding.addProductFee.getText().toString());



        FoodModel foodModel = new FoodModel(id,title,productImage,desc,category,fee,
                0,false);
        loadingDialog.setTitle("Data dialog");
        loadingDialog.setMessage("please wait...");
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        firestore.collection("food").add(foodModel)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
                    }
                    loadingDialog.dismiss();
                });
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null){
                    if (data.getData() != null) {
                        loadingDialog.setTitle("Image dialog");
                        loadingDialog.setMessage("please wait...");
                        loadingDialog.setCancelable(false);
                        loadingDialog.show();

                        Uri uri = data.getData();
                        //binding.setImage.setImageURI(uri);

                        imageToFirebase(uri);
                    }


                }

            }
    );

    private void imageToFirebase(Uri uri) {

        uploadProfile.child("food").child(id).putFile(uri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        uploadProfile.child("food").child(id).getDownloadUrl().
                                addOnSuccessListener(uri1 -> {
                                    String url = uri1.toString();
                                    productImage = url;
                                    Glide.with(this).load(url)
                                            .placeholder(R.drawable.ic_add_photo)
                                            .into(binding.addProductImage);
                                    loadingDialog.dismiss();

                                });

                    }else {
                        Toast.makeText(getApplicationContext(),
                                task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void showProductsData() {

        GridLayoutManager manager = new GridLayoutManager(this,2);
        ArrayList<FoodModel> foodList = new ArrayList<>();

        firestore.collection("food").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        foodList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()){
                            FoodModel foodModel = document.toObject(FoodModel.class);
                            foodList.add(foodModel);
                            adapter = new AdminFoodAdapter(this,foodList,this);
                            binding.addProductRv.setLayoutManager(manager);
                            binding.addProductRv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    @Override
    public void onFoodClick(int position, FoodModel model) {
        binding.addProductTitle.setText(model.getTitle());
        Glide.with(this).load(model.getPic()).into(binding.addProductImage);
        binding.addProductDesc.setText(model.getDescription());
        binding.addProductFee.setText(model.getFee()+"");
        binding.addProductCategoryTxt.setVisibility(View.VISIBLE);
        binding.addProductCategory.setVisibility(View.GONE);
        binding.addProductCategoryTxt.setText(model.getCategoryName());
        binding.addProductSave.setText("Update Product");

        binding.addProductImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            launcher.launch(intent);
        });

        binding.addProductSave.setOnClickListener(v -> {
            String title = binding.addProductTitle.getText().toString();
            String fee = binding.addProductFee.getText().toString();
            String desc = binding.addProductDesc.getText().toString();
            model.setTitle(title);
            model.setDescription(desc);
            model.setPic(productImage);
            model.setFee(Double.parseDouble(fee));
            loadingDialog.setTitle("Data dialog");
            loadingDialog.setMessage("please wait...");
            loadingDialog.setCancelable(false);
            loadingDialog.show();
            firestore.collection("food").document(model.getId()).set(model)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
                        }
                        loadingDialog.dismiss();
                    });
        });

        binding.addProductDelete.setOnClickListener(v -> {
            if (model.getId().isEmpty()){
                Toast.makeText(this, "choose a product", Toast.LENGTH_SHORT).show();
            }else {
                firestore.collection("food").document(model.getId()).delete()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                binding.addProductDesc.setText("");
                                binding.addProductTitle.setText("");
                                binding.addProductFee.setText("");
                                binding.addProductCategoryTxt.setText("");
                                Glide.with(this).load("").into(binding.addProductImage);
                            }
                        });
            }
        });

    }
}