package com.example.skype;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SettingsActivity extends AppCompatActivity {

    private Button saveButton;
    private EditText userNameEt;
    private EditText bioEt;
    private ImageView profileImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        saveButton = findViewById(R.id.save_settings_btn);
        userNameEt = findViewById(R.id.username_settings);
        bioEt = findViewById(R.id.bio_settings);
        profileImageView = findViewById(R.id.settings_profile_image);
    }
}