package com.elmohandes.e_comercefood.ui;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.adapters.CategoryAdapter;
import com.elmohandes.e_comercefood.adapters.PopularAdapter;
import com.elmohandes.e_comercefood.databinding.ActivityMainBinding;
import com.elmohandes.e_comercefood.helpers.SettingsHelper;
import com.elmohandes.e_comercefood.models.CategoryModel;
import com.elmohandes.e_comercefood.models.ControlOfferModel;
import com.elmohandes.e_comercefood.models.FoodModel;
import com.elmohandes.e_comercefood.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    RecyclerView.Adapter adapter , popularAdapter;
    StorageReference uploadProfile;
    FirebaseFirestore firestore;
    String url;
    ImageView settingsImgProfile;
    private String uid,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //String fullName = getIntent().getStringExtra("full_name");
        binding.mainTxtHi.setText("Hello "+name);

        firestore = FirebaseFirestore.getInstance();

        //String imgUrl = getIntent().getStringExtra("url");
        Glide.with(this).load(url).into(binding.mainImgProfile);

        uploadProfile = FirebaseStorage.getInstance().getReference();

        loadingOffer();

        addCategoryToRv();
        addPopularFoodToRv();

        FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                String token = task.getResult();
                                Log.d("token" , token);
                            }
                        });

        binding.mainAppbarSettings.setOnClickListener(v -> loadingSettings());
        binding.mainAppbarSupport.setOnClickListener(v -> loadingSupport());

        binding.mainBtnAddToCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this,CartActivity.class);
            startActivity(intent);
        });

        binding.mainAppbarProfile.setOnClickListener(v -> loadingProfile());

    }

    private void loadingProfile() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.profile_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        ImageView img = dialog.findViewById(R.id.profile_image);
        TextView txt_name = dialog.findViewById(R.id.profile_name);
        TextView txt_email = dialog.findViewById(R.id.profile_email);
        TextView txt_phone = dialog.findViewById(R.id.profile_phone);
        AppCompatButton btn_update = dialog.findViewById(R.id.profile_logout);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        firestore.collection("users").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            if(document.getId().equals(uid)){
                                Users users = document.toObject(Users.class);
                                txt_name.setText(users.getFullName());
                                txt_phone.setText(users.getPhone());
                                txt_email.setText(users.getEmail());

                                Glide.with(this).load(users.getImgUrl())
                                        .placeholder(R.drawable.ic_picture).into(img);
                            }
                        }
                    }
                });

        btn_update.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this,IntroActivity.class);
            startActivity(intent);
            finish();
        });

        dialog.show();
    }

    private void loadingOffer() {

        firestore.collection("offer").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            ControlOfferModel model = document.toObject(ControlOfferModel.class);
                            if (model.getId().equals("123321")){
                                binding.mainTxtOfferTitle.setText(model.getTitle());
                                String period = model.getStartDate() + " to "+ model.getEndDate();
                                binding.mainTxtHistory.setText(period);
                                Glide.with(this).load(model.getImgUrl())
                                        .placeholder(R.drawable.ic_picture)
                                        .into(binding.mainOfferImage);
                            }
                        }
                    }
                });

    }

    private void loadingSupport() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.support_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        dialog.show();

    }

    private void loadingSettings() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.settings_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        ImageView img = dialog.findViewById(R.id.settings_image);
        EditText et_name = dialog.findViewById(R.id.settings_name);
        TextView txt_email = dialog.findViewById(R.id.settings_email);
        EditText et_password = dialog.findViewById(R.id.settings_password);
        EditText et_phone = dialog.findViewById(R.id.settings_phone);
        AppCompatButton btn_update = dialog.findViewById(R.id.settings_update);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        settingsImgProfile = img;

        SettingsHelper.retrieveData(uid,img,et_name,txt_email,et_password,et_phone,this
                ,launcher);
        btn_update.setOnClickListener(v -> {
            SettingsHelper.updateData(uid,url,img,et_name,txt_email,et_password,et_phone,this);
        });

        dialog.show();
    }


    private void addPopularFoodToRv() {

        String des_1 = "Mozzarella Cheese, Mushroom, Olive, Pepperoni, Pizza Sauce";
        String desc_2 = "Green Pepper, Mozzarella Cheese, Mushroom, Olive, Onion, Pizza Sauce, Tomato";

        LinearLayoutManager manager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL
                ,false);
        ArrayList<FoodModel> foodList = new ArrayList<>();
        foodList.add(new FoodModel("pepperoni pizza","R.mipmap.ic_pizza", des_1,35));
        foodList.add(new FoodModel("Cheese Burger","R.mipmap.ic_burger",
                "Cheese Burger description",30));
        foodList.add(new FoodModel("Vegetable pizza","R.mipmap.ic_pizza2", desc_2,35));
        foodList.add(new FoodModel("Chicken and rice","R.mipmap.ic_chicken",
                "Chicken pizza description",45));

        firestore.collection("food").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        foodList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()){
                            FoodModel foodModel = document.toObject(FoodModel.class);
                            foodList.add(foodModel);
                            popularAdapter = new PopularAdapter(this,foodList);
                            binding.mainRvPopular.setLayoutManager(manager);
                            binding.mainRvPopular.setAdapter(popularAdapter);
                            popularAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void addCategoryToRv() {

        LinearLayoutManager manager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL
                ,false);
        binding.mainRvCategory.setLayoutManager(manager);
        ArrayList<CategoryModel> category = new ArrayList<>();

        firestore.collection("categories").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            CategoryModel model = document.toObject(CategoryModel.class);
                            category.add(model);
                            adapter = new CategoryAdapter(this,category);
                            binding.mainRvCategory.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null){
                    if (data.getData() != null) {

                        Uri uri = data.getData();
                        //binding.setImage.setImageURI(uri);

                        imageToFirebase(uri);
                    }


                }

            }
    );

    private void imageToFirebase(Uri uri) {
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        uploadProfile.child("profile").child(uid).putFile(uri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        uploadProfile.child("profile").child(uid).getDownloadUrl().
                                addOnSuccessListener(uri1 -> {
                                    String url = uri1.toString();
                                    this.url = url;
                                    Glide.with(this).load(url).into(settingsImgProfile);
                                    saveToDatabase(url);

                                });

                    }else {
                        Toast.makeText(getApplicationContext(),
                                task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void saveToDatabase(String url) {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            if(document.getId().equals(uid)){
                                Users users = document.toObject(Users.class);
                                users.setImgUrl(url);
                                firestore.collection("users").document(uid).set(users)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()){
                                                Log.d("upload_pic","done");
                                            }
                                        });
                            }
                        }
                    }
                });

    }

    public void showData(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            if(document.getId().equals(uid)){
                                Users users = document.toObject(Users.class);
                                name = users.getFullName();
                                url = users.getImgUrl();
                                binding.mainTxtHi.setText("Hello " + name);
                                Glide.with(this).load(users.getImgUrl())
                                        .into(binding.mainImgProfile);
                            }
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        showData();

    }
}