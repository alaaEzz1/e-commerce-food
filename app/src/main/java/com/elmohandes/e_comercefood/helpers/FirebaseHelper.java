package com.elmohandes.e_comercefood.helpers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.elmohandes.e_comercefood.models.CategoryModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class FirebaseHelper {

    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public void addCategoryToFireStore(EditText name, Context context, ProgressDialog loadingDialog, String url){
        String data = name.getText().toString();
        loadingDialog.setTitle("Image dialog");
        loadingDialog.setMessage("please wait...");
        loadingDialog.setCancelable(false);
        loadingDialog.show();
        String catId = firestore.collection("categories").document().getId();
        CategoryModel model = new CategoryModel();
        model.setTitle(data);
        model.setPic(url);
        model.setId(catId);


        firestore.collection("categories").document(catId).set(model)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(context, "done", Toast.LENGTH_SHORT).show();

                    }
                });
        loadingDialog.dismiss();
    }



}
