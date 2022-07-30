package com.elmohandes.e_comercefood.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.databinding.ActivitySignupBinding;
import com.elmohandes.e_comercefood.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    ProgressDialog dialog;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        binding.signupBtnRegister.setOnClickListener(v -> createAccountWithEmail());

        binding.signupBtnLogin.setOnClickListener(v -> finish());

    }

    private void createAccountWithEmail() {
        String fullName =  binding.signupInputFullName.getText().toString();
        String email =  binding.signupInputEmail.getText().toString();
        String phone =  binding.signupInputPhoneNumber.getText().toString();
        String password =  binding.signupInputPassword.getText().toString();
        if (fullName.isEmpty()){
            Toast.makeText(this, "full name is needed", Toast.LENGTH_SHORT).show();
        }else if (email.isEmpty()){
            Toast.makeText(this, "email is needed", Toast.LENGTH_SHORT).show();
        }
        else if (phone.isEmpty()){
            Toast.makeText(this, "mobile phone is needed", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "email is not valid", Toast.LENGTH_SHORT).show();
        }
        else if (password.isEmpty()){
            Toast.makeText(this, "password is needed", Toast.LENGTH_SHORT).show();
        }
        else {
            auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            dialog.setTitle("Signup dialog");
                            dialog.setMessage("please wait...");
                            dialog.setCancelable(false);
                            String userId = auth.getCurrentUser().getUid();
                            storeDataInFireStore(userId,fullName,email,phone,password);
                        }
                    });
        }
    }

    private void storeDataInFireStore(String userId, String fullName, String email,
                                      String phone, String password) {
        dialog.show();
        Users users = new Users(userId,fullName,email,phone,password);
        firestore.collection("users").document(userId).set(users)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "user added", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    binding.signupInputPassword.setText("");
                    binding.signupInputEmail.setText("");
                    binding.signupInputPhoneNumber.setText("");
                    binding.signupInputFullName.setText("");
                }).addOnFailureListener(e -> {
                    dialog.dismiss();
            Toast.makeText(this, "please connect to internet", Toast.LENGTH_SHORT).show();
                });



    }
}