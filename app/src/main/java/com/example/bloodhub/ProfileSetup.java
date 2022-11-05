package com.example.bloodhub;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.lang.reflect.Array;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileSetup extends AppCompatActivity {

    String[] locations = {"Dhaka", "Barisal", "Chittagong", "Rangpur", "Rajshahi", "Khulna", "Sylhet"};
    String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+" , "AB-", "O+", "O-"};
    private ArrayAdapter<String> bgItems, lItems;
    private AutoCompleteTextView bloodGroup, location;
    private Button signup;
    private EditText name, number;
    private CircleImageView profileImage;
    private Uri resultUri;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);


        signup = findViewById(R.id.signup);
        bloodGroup = findViewById(R.id.bloodGroup);
        location = findViewById(R.id.location);
        profileImage = findViewById(R.id.profile_image);

        mAuth = FirebaseAuth.getInstance();

        bgItems = new ArrayAdapter<>(this, R.layout.list_item, bloodGroups);
        lItems = new ArrayAdapter<>(this, R.layout.list_item, locations);

        bloodGroup.setAdapter(bgItems);
        location.setAdapter(lItems);

        bloodGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "You won't be able to change your blood group later", Toast.LENGTH_SHORT).show();
            }
        });

        location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String bg = bloodGroup.getText().toString();
                final String place = location.getText().toString();
                Intent intent = new Intent(ProfileSetup.this, HomepageActivity.class);
                startActivity(intent);
                finish();
            }
        });
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