package com.elmohandes.e_comercefood.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.databinding.ActivityControlOfferBinding;
import com.elmohandes.e_comercefood.models.ControlOfferModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ControlOfferActivity extends AppCompatActivity {

    ActivityControlOfferBinding binding;
    FirebaseFirestore firestore;
    StorageReference uploadPicture;
    ProgressDialog loadingDialog;
    String url,id;
    int year,month,day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_offer);

        binding = ActivityControlOfferBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadingDialog = new ProgressDialog(this);

        firestore = FirebaseFirestore.getInstance();
        uploadPicture = FirebaseStorage.getInstance().getReference();
        id = "123321";

        firestore.collection("offer").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            ControlOfferModel model = document.toObject(ControlOfferModel.class);
                            if (model.getId().equals(id)){
                                binding.offerTitle.setText(model.getTitle());
                                binding.offerStartDateResult.setText(model.getStartDate());
                                binding.offerEndDateResult.setText(model.getEndDate());
                                binding.offerSave.setText("Update Offer");
                                url = model.getImgUrl();
                                Glide.with(this).load(model.getImgUrl())
                                        .placeholder(R.drawable.ic_picture).into(binding.offerImage);
                            }
                        }
                    }
                });

        Glide.with(this).load(url).placeholder(R.drawable.ic_picture).into(binding.offerImage);

        year = Calendar.getInstance().get(Calendar.YEAR);
        month = Calendar.getInstance().get(Calendar.MONTH);
        day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        binding.offerImage.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            launcher.launch(intent);
        });
        
        binding.offerStartDate.setOnClickListener(v -> onStartDate());

        binding.offerEndDate.setOnClickListener(v -> onEndDate());

        binding.offerSave.setOnClickListener(v -> saveToDatabase());

        binding.offerShowOffer.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        });

    }

    private void saveToDatabase() {
        String title = binding.offerTitle.getText().toString();
        String startDate = binding.offerStartDateResult.getText().toString();
        String endDate = binding.offerEndDateResult.getText().toString();

        loadingDialog.setTitle("Offer dialog");
        loadingDialog.setMessage("please wait...");
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        ControlOfferModel model = new ControlOfferModel(id,title,startDate,endDate,url);
        firestore.collection("offer").document(id).set(model)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        loadingDialog.dismiss();
                        Toast.makeText(ControlOfferActivity.this,
                                "saved", Toast.LENGTH_SHORT).show();
                    }else {
                        loadingDialog.dismiss();
                        Toast.makeText(ControlOfferActivity.this,
                                "error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void onEndDate() {
        DatePickerDialog dialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Dialog_MinWidth, (view, year, newMonth, dayOfMonth) -> {
            String date =convertDate(year,newMonth,dayOfMonth);
            binding.offerEndDateResult.setText(date);
        }
                , year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void onStartDate() {
        DatePickerDialog dialog = new DatePickerDialog(this,
                android.R.style.Theme_Holo_Dialog_MinWidth, (view, year, newMonth, dayOfMonth) -> {
            String date =convertDate(year,newMonth,dayOfMonth);
            binding.offerStartDateResult.setText(date);
                }
                , year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    private String convertDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,dayOfMonth);
        Date date = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM");
        String result = dateFormat.format(date);
        return result;
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
            });

    public void imageCatToFirebase(Uri uri, Context context, ProgressDialog dialog) {
        uploadPicture.child("offer").child(id).putFile(uri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        uploadPicture.child("offer").child(id).getDownloadUrl().
                                addOnSuccessListener(uri1 -> {
                                    url = uri1.toString();
                                    dialog.dismiss();
                                    Glide.with(this).load(url)
                                            .placeholder(R.drawable.ic_add_photo)
                                            .into(binding.offerImage);
                                });

                    }else {
                        Toast.makeText(context, task.getException().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

    }

}