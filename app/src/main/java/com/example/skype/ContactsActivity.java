package com.example.skype;



import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContactsActivity extends AppCompatActivity {

    BottomNavigationView navView;
    RecyclerView myContactList;
    ImageView findPeopleBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        findPeopleBtn = findViewById(R.id.find_people);
        myContactList = findViewById(R.id.contact_list);
        myContactList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        findPeopleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFindPeopleActivity = new Intent(ContactsActivity.this, FindPeopleActivity.class);
                startActivity(intentFindPeopleActivity);
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch(menuItem.getItemId()){
                case R.id.navigation_home:
                    Intent intentHome = new Intent(ContactsActivity.this, ContactsActivity.class);
                    startActivity(intentHome);
                    break;

                case R.id.navigation_settings:
                    Intent intentSettings = new Intent(ContactsActivity.this,SettingsActivity.class);
                    startActivity(intentSettings);
                    break;

                case R.id.navigation_notifications:
                    Intent intentNotifications = new Intent(ContactsActivity.this,NotificationActivity.class);
                    startActivity(intentNotifications);
                    break;

                case R.id.navigation_logout:
                    FirebaseAuth.getInstance().signOut();
                    Intent intentLogout = new Intent(ContactsActivity.this,RegistrationActivity.class);
                    startActivity(intentLogout);
                    finish();
                    break;


            }
            return false;
        }
    };
}
