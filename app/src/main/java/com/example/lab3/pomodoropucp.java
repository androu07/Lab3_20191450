package com.example.lab3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class pomodoropucp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoropucp);

        Intent intent = getIntent();
        String userid = intent.getStringExtra("user_id");
        String userFullname = intent.getStringExtra("user_fullname");
        String userEmail = intent.getStringExtra("user_email");
        String userGender = intent.getStringExtra("user_gender");

        TextView userNameTextView = findViewById(R.id.user_name);
        TextView userEmailTextView = findViewById(R.id.user_email);
        ImageView userIconImageView = findViewById(R.id.user_icon);

        userNameTextView.setText(userFullname);
        userEmailTextView.setText(userEmail);
        if (userGender.equalsIgnoreCase("male")) {
            userIconImageView.setImageResource(R.drawable.hombre);
        } else if (userGender.equalsIgnoreCase("female")) {
            userIconImageView.setImageResource(R.drawable.mujer);
        }
    }
}