package com.elmohandes.e_comercefood.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.adapters.UsersAdapter;
import com.elmohandes.e_comercefood.databinding.ActivityAdminBinding;
import com.elmohandes.e_comercefood.models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    ActivityAdminBinding binding;
    FirebaseFirestore firestore;
    StorageReference uploadPicture;
    ProgressDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadingDialog = new ProgressDialog(this);

        firestore = FirebaseFirestore.getInstance();
        uploadPicture = FirebaseStorage.getInstance().getReference();

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            Intent intent = new Intent(AdminActivity.this,IntroActivity.class);
            startActivity(intent);
            finish();
        }

        binding.adminShowMain.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this,MainActivity.class);
            startActivity(intent);
        });

        binding.adminControlCategory.setOnClickListener(v -> controlWithCategory());

        binding.adminShowUsersDetails.setOnClickListener(v -> showMyUsers());

        binding.adminCreateProduct.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this,AddProductsActivity.class);
            startActivity(intent);
        });

        binding.adminCreateControlOffer.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this,ControlOfferActivity.class);
            startActivity(intent);
        });

        binding.adminControlNotification.setOnClickListener(v -> {
            Intent intent = new Intent(AdminActivity.this,
                    ControlNotificationsActivity.class);
            startActivity(intent);
        });

    }

    private void controlWithCategory() {
        Intent intent = new Intent(this,ControlCategoryActivity.class);
        startActivity(intent);
    }

    private void showMyUsers() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.show_users_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        RecyclerView rv = dialog.findViewById(R.id.show_users_rv);
        AppCompatButton close = dialog.findViewById(R.id.show_users_close);

        List<Users> usersList = new ArrayList<>();
        firestore.collection("users").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        usersList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()){
                            Users users = document.toObject(Users.class);
                            usersList.add(users);
                            UsersAdapter usersAdapter = new UsersAdapter(this,usersList);
                            rv.setLayoutManager(new LinearLayoutManager(this));
                            rv.setAdapter(usersAdapter);
                            usersAdapter.notifyDataSetChanged();
                        }
                    }
                });

        close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }



    @Override
    protected void onResume() {
        super.onResume();

        if (FirebaseAuth.getInstance().getCurrentUser() == null){
            finish();
        }

    }
}