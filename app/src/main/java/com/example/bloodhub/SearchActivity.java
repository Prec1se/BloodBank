package com.example.bloodhub;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.bloodhub.Adapter.UserAdapter;
import com.example.bloodhub.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    String[] locations = {"Dhaka", "Barisal", "Chittagong", "Rangpur", "Rajshahi", "Khulna", "Sylhet"};
    String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+" , "AB-", "O+", "O-"};

    private ArrayAdapter<String> bgItems, lItems;
    private AutoCompleteTextView bloodGroup, location;
    private ImageButton backButton, searchButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firestore;
    private DocumentReference documentReference;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<User> userList;
    private UserAdapter userAdapter;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        backButton = findViewById(R.id.backbutton);
        searchButton = findViewById(R.id.search);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(SearchActivity.this, HomepageActivity.class));
                finish();
            }
        });

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        bloodGroup = findViewById(R.id.bloodgroup);
        location = findViewById(R.id.location);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(SearchActivity.this, userList);

        recyclerView.setAdapter(userAdapter);

        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        bgItems = new ArrayAdapter<>(this, R.layout.list_item, bloodGroups);
        lItems = new ArrayAdapter<>(this, R.layout.list_item, locations);

        bloodGroup.setAdapter(bgItems);
        location.setAdapter(lItems);

        userId = firebaseAuth.getCurrentUser().getUid();

        bloodGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String item = parent.getItemAtPosition(position).toString();
//                Toast.makeText(getApplicationContext(), "You won't be able to change your blood group later", Toast.LENGTH_SHORT).show();
            }
        });

        location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
//                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        documentReference = firestore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                readUsers();
            }
        });
    }

    private void readUsers() {

        String bg, l;
        bg = bloodGroup.getText().toString();
        l = location.getText().toString();

        if(bg.isEmpty()) {
            bloodGroup.setError("Select a blood group");
            bloodGroup.requestFocus();
            return;
        }

        if(l.isEmpty()) {
            location.setError("Select a location");
            location.requestFocus();
            return;
        }

        DocumentReference reference = firestore.collection("users").document(userId);
        Query query = reference.getParent()
                .whereEqualTo("bloodgroup", bg)
                .whereEqualTo("location", l);
        query.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                userList.clear();
                for(DocumentSnapshot documentSnapshot : value.getDocuments()) {
                    User user = new User();
                    user.setAvailable(true);
                    user.setBloodgroup(documentSnapshot.get("bloodgroup").toString());
                    user.setEmail(documentSnapshot.get("email").toString());
                    user.setLocation(documentSnapshot.get("division").toString());
                    user.setPhonenumber(documentSnapshot.get("phone").toString());
                    user.setName(documentSnapshot.get("name").toString());
                    userList.add(user);
//                    Toast.makeText(HomepageActivity.this, "User available", Toast.LENGTH_SHORT).show();
                }
                userAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if(userList.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "No available users", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}