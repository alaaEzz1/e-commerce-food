package com.elmohandes.e_comercefood.helpers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.elmohandes.e_comercefood.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class SettingsHelper {

    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static void retrieveData(String uid , ImageView img, EditText fullName, TextView email,
                                    EditText password, EditText phone, Context context,
                                    ActivityResultLauncher<Intent> launcher){
        firestore.collection("users").get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getId().equals(uid)){
                                Users users = document.toObject(Users.class);
                                //Log.d("TAG", users.getFullName() + " => " + users.getEmail());
                                fullName.setText(users.getFullName());
                                email.setText(users.getEmail());
                                password.setText(users.getPassword());
                                phone.setText(users.getPhone());
                                Glide.with(context).load(users.getImgUrl()).into(img);
                                img.setOnClickListener(view -> {
                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    intent.setType("image/*");
                                    launcher.launch(intent);

                                });
                            }
                        }
                    } else {
                        Log.w("TAG", "Error getting documents.", task.getException());
                    }

                });
    }



    public static void updateData(String uid, String url, ImageView img, EditText et_name,
                                  TextView txt_email, EditText et_password,
                                  EditText et_phone,Context context) {

        String name = et_name.getText().toString();
        String email = txt_email.getText().toString();
        String password = et_password.getText().toString();
        String phone = et_phone.getText().toString();

        Users users = new Users(uid,name,email,phone,password,url,false);
        firestore.collection("users").document(uid).set(users)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        Toast.makeText(context, "updated", Toast.LENGTH_SHORT).show();
                    }
                });


    }
}
