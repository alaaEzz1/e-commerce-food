package com.elmohandes.e_comercefood.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.adapters.AdminCategoryAdapter;
import com.elmohandes.e_comercefood.adapters.CategoryAdapter;
import com.elmohandes.e_comercefood.adapters.listeners.CategoryListener;
import com.elmohandes.e_comercefood.databinding.ActivityControlCategoryBinding;
import com.elmohandes.e_comercefood.helpers.FirebaseHelper;
import com.elmohandes.e_comercefood.models.CategoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ControlCategoryActivity extends AppCompatActivity implements CategoryListener {

    ActivityControlCategoryBinding binding;
    FirebaseHelper helper = new FirebaseHelper();
    String url;
    ProgressDialog loadingDialog;
    ImageView preview;
    StorageReference uploadPicture;
    FirebaseFirestore firestore;
    AdminCategoryAdapter adapter;
    ArrayList<CategoryModel> category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_category);

        binding = ActivityControlCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadingDialog = new ProgressDialog(this);
        firestore = FirebaseFirestore.getInstance();
        uploadPicture = FirebaseStorage.getInstance().getReference();

        binding.controlCategoryAdd.setOnClickListener(v -> addCategoryToFirebase());

        category = new ArrayList<>();
        adapter = new AdminCategoryAdapter(this,category,this);
        binding.controlCategoryRv.setAdapter(adapter);

        addCategoryToRv();

    }

    private void addCategoryToRv() {

        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL
                ,false);
        binding.controlCategoryRv.setLayoutManager(manager);

        firestore.collection("categories").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()){
                            CategoryModel model = document.toObject(CategoryModel.class);
                            category.add(model);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

    }

    private void addCategoryToFirebase() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_category_dialog);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);

        EditText catName = dialog.findViewById(R.id.add_category_name);
        AppCompatButton imgButton = dialog.findViewById(R.id.add_category_img);
        AppCompatButton saveButton = dialog.findViewById(R.id.add_category_save);
        preview = dialog.findViewById(R.id.add_category_url);

        imgButton.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            launcher.launch(intent);
        });

        saveButton.setOnClickListener(v -> {
            helper.addCategoryToFireStore(catName,getApplicationContext(),loadingDialog,url);
            dialog.dismiss();
        });

        dialog.show();

    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                Intent data = result.getData();
                if (data != null){
                    if (data.getData() != null) {
                        Uri uri = data.getData();
                        loadingDialog.setTitle("Image dialog");
                        loadingDialog.setMessage("please wait...");
                        loadingDialog.setCancelable(false);
                        loadingDialog.show();
                        //binding.setImage.setImageURI(uri);
                        imageCatToFirebase(uri, this,loadingDialog);
                    }
                }
            }
    );

    public void imageCatToFirebase(Uri uri, Context context, ProgressDialog dialog) {
        String catId = firestore.collection("categories").document().getId();
        uploadPicture.child("categories").child(catId).putFile(uri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        uploadPicture.child("categories").child(catId).getDownloadUrl().
                                addOnSuccessListener(uri1 -> {
                                    url = uri1.toString();
                                    dialog.dismiss();
                                    Glide.with(this).load(url).into(preview);
                                });

                    }else {
                        Toast.makeText(context, task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

    }

    @Override
    public void onCategoryClick(int position, CategoryModel model) {

        String catName = binding.controlCategoryName.getText().toString();
        binding.controlCategoryName.setText(model.getTitle());
        Glide.with(this).load(model.getPic()).into(binding.controlCategoryUrl);

        binding.controlCategoryImg.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            launcher.launch(intent);
        });

        binding.controlCategoryUpdate.setOnClickListener(v -> {
            model.setTitle(catName);
            model.setPic(url);
            firestore.collection("categories").document(model.getId()).set(model)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(this, "updated successfully",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        binding.controlCategoryDelete.setOnClickListener(v -> {
            loadingDialog.setTitle("Category dialog");
            loadingDialog.setMessage("please wait...");
            loadingDialog.setCancelable(false);
            loadingDialog.show();
            firestore.collection("categories").document(model.getId()).delete()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "deleted successfully",
                                    Toast.LENGTH_SHORT).show();
                            binding.controlCategoryName.setText("");
                            Glide.with(this).load("").into(binding.controlCategoryUrl);
                            loadingDialog.dismiss();
                            adapter.notifyDataSetChanged();
                        }
                    });
        });

    }
}