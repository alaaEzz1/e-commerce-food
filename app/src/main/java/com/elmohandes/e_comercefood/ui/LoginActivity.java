package com.elmohandes.e_comercefood.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.databinding.ActivityLoginBinding;
import com.elmohandes.e_comercefood.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    ProgressDialog dialog;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();

        binding.loginBtnLogin.setOnClickListener(v -> {
            dialog.setTitle("Login dialog");
            dialog.setMessage("please wait...");
            dialog.setCancelable(false);
            getUserData();
        });
        binding.loginBtnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
            startActivity(intent);
        });

    }

    private void getUserData() {

        String email = binding.loginInputEmail.getText().toString();
        String password = binding.loginInputPassword.getText().toString();

        if (email.isEmpty()){
            Toast.makeText(this, "email is needed", Toast.LENGTH_SHORT).show();
        }
        else if (password.isEmpty()){
            Toast.makeText(this, "mobile phone is needed", Toast.LENGTH_SHORT).show();
        }
        else{
            dialog.show();
            auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            if (email.equals("lite5554@gmail.com")){
                                Intent intent = new Intent(this,AdminActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(this, "successfully",
                                        Toast.LENGTH_SHORT).show();
                                sendDataToMain();
                            }
                        }else {
                            dialog.dismiss();
                            Toast.makeText(this,
                                    "Email or Password is not accurate try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    private void sendDataToMain() {
        dialog.dismiss();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("users").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Users users = document.toObject(Users.class);
                            //Log.d("TAG", users.getFullName() + " => " + users.getEmail());
                            Intent intent;
                            if (users.getEmail().equals("lite5554@gmail.com")){
                                intent = new Intent(getApplicationContext(), AdminActivity.class);
                            }else {
                                intent = new Intent(getApplicationContext(), MainActivity.class);
                            }
                            intent.putExtra("full_name",users.getFullName());
                            intent.putExtra("url",users.getImgUrl());
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Log.w("TAG", "Error getting documents.", task.getException());
                    }
                });

    }

}