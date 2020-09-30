package com.example.skype;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    private Button saveButton;
    private EditText userNameEt;
    private EditText bioEt;
    private ImageView profileImageView;

    private static int galleryPick = 1;
    private Uri imageUri;

    private String downloadUrl;
     private ProgressDialog progressDialog;
    private DatabaseReference userRef;

    private StorageReference userProfileImageRef ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        saveButton = findViewById(R.id.save_settings_btn);
        userNameEt = findViewById(R.id.username_settings);
        bioEt = findViewById(R.id.bio_settings);
        profileImageView = findViewById(R.id.settings_profile_image);

        progressDialog = new ProgressDialog(this);

        userRef = FirebaseDatabase.getInstance().getReference().child("User");

        userProfileImageRef= FirebaseStorage.getInstance().getReference().child("Profile Images");

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galaryIntent = new Intent();
                galaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galaryIntent.setType("image/*");
                startActivityForResult(galaryIntent, galleryPick);
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
            }
        });
        retrieveUserInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== galleryPick && resultCode==RESULT_OK && data!=null)
        {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
        }
    }

    private void saveUserData()
    {
        final String getUserName = userNameEt.getText().toString() ;
        final String getBio = bioEt.getText().toString() ;

        if(imageUri==null)
        {
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
               if(dataSnapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild("image"))
               {
                saveInfoWithOutImage();
               }

               else
               {
                Toast.makeText(SettingsActivity.this,"Select image first",Toast.LENGTH_SHORT).show();;
               }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        else if(getUserName.equals(""))
        {
            Toast.makeText(this,"User name is mandatory",Toast.LENGTH_SHORT).show();
        }
        else if(getBio.equals(""))
        {
            Toast.makeText(this,"Status is mandatory",Toast.LENGTH_SHORT).show();
        }

        else
        {
            progressDialog.setTitle("Account Settings");
            progressDialog.setMessage("Please Wait.....");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();


            final StorageReference filePath = userProfileImageRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid());

            final UploadTask uploadTask = filePath.putFile(imageUri);
            
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }
                    downloadUrl =filePath.getDownloadUrl().toString();

                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful())
                    {
                        downloadUrl = task.getResult().toString();

                        HashMap<String,Object> profileMap = new HashMap<>();
                        profileMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                        profileMap.put("name",getUserName);
                        profileMap.put("status",getBio);
                        profileMap.put("image",downloadUrl);

                        userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                          updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if(task.isSuccessful())
                                {
                                    Intent intent =new Intent(SettingsActivity.this,ContactsActivity.class);
                                    startActivity(intent);
                                    finish();

                                    Toast.makeText(SettingsActivity.this,"Profile settings updated",Toast.LENGTH_SHORT).show();;

                                }

                            }
                        });
                    }
                }
            });

        }
    }

    private void saveInfoWithOutImage() {
        final String getUserName = userNameEt.getText().toString() ;
        final String getBio = bioEt.getText().toString() ;



        if(getUserName.equals(""))
        {
            Toast.makeText(this,"User name is mandatory",Toast.LENGTH_SHORT).show();
        }
        else if(getBio.equals(""))
        {
            Toast.makeText(this,"Status is mandatory",Toast.LENGTH_SHORT).show();
        }

        else
        {

            progressDialog.setTitle("Account Settings");
            progressDialog.setMessage("Please Wait.....");
            progressDialog.show();

            HashMap<String,Object> profileMap = new HashMap<>();
            profileMap.put("uid",FirebaseAuth.getInstance().getCurrentUser().getUid());
            profileMap.put("name",getUserName);
            profileMap.put("status",getBio);

            userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                    updateChildren(profileMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful())
                    {
                        Intent intent =new Intent(SettingsActivity.this,ContactsActivity.class);
                        startActivity(intent);
                        finish();
                        progressDialog.dismiss();

                        Toast.makeText(SettingsActivity.this,"Profile settings updated",Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
    }

private void retrieveUserInfo()
{
   userRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
           addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        String imageDb = dataSnapshot.child("image").getValue().toString();
                        String userDb = dataSnapshot.child("name").getValue().toString();
                        String bioDb = dataSnapshot.child("status").getValue().toString();

                        userNameEt.setText(userDb);
                        bioEt.setText(bioDb);
                        Picasso.get().load(imageDb).placeholder(R.drawable.profile_image)
                                .into(profileImageView);

                    }
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });
}

}