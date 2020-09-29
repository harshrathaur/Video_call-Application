package com.example.skype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

   private  RecyclerView notificationList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        notificationList  = findViewById(R.id.notification_list);
        notificationList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder
    {
        TextView userNameTxt;
        Button acceptbtn,cancelBtn;
        ImageView profileImageView;
        RelativeLayout cardView;


        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            userNameTxt = itemView.findViewById(R.id.name_notification);
            acceptbtn = itemView.findViewById(R.id.request_accept_btn);
            cancelBtn =itemView. findViewById(R.id.request_decline_btn);
            cardView = itemView.findViewById(R.id.card_View);
            profileImageView =itemView.findViewById(R.id.image_notification);
        }
    }
}