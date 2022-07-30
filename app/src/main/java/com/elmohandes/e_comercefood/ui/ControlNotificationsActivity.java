package com.elmohandes.e_comercefood.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.elmohandes.e_comercefood.R;
import com.elmohandes.e_comercefood.databinding.ActivityControlNotificationsBinding;
import com.elmohandes.e_comercefood.notification.VolleyMessaging;
import com.google.firebase.messaging.FirebaseMessaging;

public class ControlNotificationsActivity extends AppCompatActivity {

    ActivityControlNotificationsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_notifications);

        binding = ActivityControlNotificationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String topic ="food";
        FirebaseMessaging.getInstance().subscribeToTopic(topic);
        VolleyMessaging messaging = new VolleyMessaging();

        binding.notificationSend.setOnClickListener(v -> {
            String title = binding.notificationTitle.getText().toString();
            String message = binding.notificationMessage.getText().toString();

            if (!title.equals("") && !message.equals("")){
                messaging.pushNotification(topic,title,message,this);
                binding.notificationTitle.setText("");
                binding.notificationMessage.setText("");
                Toast.makeText(this, "done", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this, "title or message is empty",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
}