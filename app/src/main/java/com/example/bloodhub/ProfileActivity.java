package com.example.bloodhub;

import static com.example.bloodhub.HomepageActivity.getBitmapFromURL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView name, email, bloodGroup, phoneNumber, location;
    private CircleImageView profileImage;
    private Button backButton;
    private Uri resultUri;
    private FirebaseFirestore fstore;
    private FirebaseAuth firebaseAuth;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Profile");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        bloodGroup = findViewById(R.id.bloodGroup);
        phoneNumber = findViewById(R.id.phone);
        location = findViewById(R.id.location);
        backButton = findViewById(R.id.backButton);
        profileImage = findViewById(R.id.profile_image);
        fstore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name.setText(value.getString("name"));
                email.setText(value.getString("email"));
                bloodGroup.setText(value.getString("bloodgroup"));
                phoneNumber.setText(value.getString("phone"));
                location.setText(value.getString("division"));

//                profileImage.setImageBitmap(getBitmapFromURL(value.getString("profilepictureurl")));
//                if(value.hasChild("profilepictureurl")) {
//                        String imageUrl = snapshot.child("profilepictureurl").getValue().toString();
//                        Glide.with(getApplicationContext()).load(imageUrl).into(nav_profile_image);
//                    }
//                    else {
//                        nav_profile_image.setImageResource(R.drawable.profile);
//                    }
//
//                    String phoneNumber = snapshot.child("phone").getValue().toString();
//                    nav_phone_number.setText(phoneNumber);
            }
        });

//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
//                .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()) {
//                    name.setText(snapshot.child("name").getValue().toString());
//                    email.setText(snapshot.child("email").getValue().toString());
//                    bloodGroup.setText(snapshot.child("bloodgroup").getValue().toString());
//                    phoneNumber.setText(snapshot.child("phone").getValue().toString());
//
//                    String imageUrl = snapshot.child("profilepictureurl").getValue().toString();
//                    Glide.with(getApplicationContext()).load(imageUrl).into(profileImage);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//        profileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, 1);
//            }
//        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ProfileActivity.this, HomepageActivity.class);
//                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showCustomDialog() {
        final Dialog dialog = new Dialog(ProfileActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
            resultUri = data.getData();
            profileImage.setImageURI(resultUri);
        }
    }
}