package com.example.bloodhub;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class SetStatusActivity extends AppCompatActivity {

    private Button available, notAvailable, backButton;
    private ImageView availableImage, notAvailableImage;
    private DocumentReference documentReference;
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_set_status);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        userId = firebaseAuth.getCurrentUser().getUid();

        documentReference = firestore.collection("users").document(userId);

        available = findViewById(R.id.available);
        notAvailable = findViewById(R.id.notavailable);
        availableImage = findViewById(R.id.availableimage);
        notAvailableImage = findViewById(R.id.notavailableimage);
        backButton = findViewById(R.id.backbutton);

        available.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SetStatusActivity.this, "Available for blood donation", Toast.LENGTH_SHORT).show();
                availableImage.setVisibility(View.VISIBLE);
                notAvailableImage.setVisibility(View.INVISIBLE);
                documentReference.update("available", true);
            }
        });

        notAvailable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SetStatusActivity.this, "Not available for blood donation", Toast.LENGTH_SHORT).show();
                setVisible(true);
                availableImage.setVisibility(View.INVISIBLE);
                notAvailableImage.setVisibility(View.VISIBLE);
                documentReference.update("available", false);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(SetStatusActivity.this, HomepageActivity.class));
                finish();
            }
        });

    }
}