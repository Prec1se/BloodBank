package com.example.bloodhub;

import static android.content.ContentValues.TAG;
import static com.example.bloodhub.SplashScreenActivity.setWindowFlag;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bloodhub.Adapter.UserAdapter;
import com.example.bloodhub.Model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomepageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView nav_view;

    private CircleImageView nav_profile_image;
    private TextView nav_name, nav_email, nav_bloodGroup, nav_phone_number, nav_location;
    String userId;
//    MenuItem logout;

    private DatabaseReference userRef;
    private FirebaseFirestore fstore;
    private FirebaseAuth firebaseAuth;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private List<User> userList;
    private UserAdapter userAdapter;
    private Switch aSwitch;
    private boolean available;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
//
        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_homepage);

        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("BloodHub");

        drawerLayout = findViewById(R.id.drawerLayout);
        nav_view = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HomepageActivity.this, drawerLayout,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
//
//        logout = findViewById(R.id.logout);
//        logout.setOnMenuItemClickListener((MenuItem.OnMenuItemClickListener) menuItem -> {
//            FirebaseAuth.getInstance().signOut();
//            startActivity(new Intent(HomepageActivity.this,LoginActivity.class));
//            return true;
//        });

        nav_view.setNavigationItemSelectedListener(this);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(HomepageActivity.this, userList);

        recyclerView.setAdapter(userAdapter);

        fstore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        userId = firebaseAuth.getCurrentUser().getUid();



        nav_profile_image = nav_view.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_name = nav_view.getHeaderView(0).findViewById(R.id.nav_user_name);
        nav_email = nav_view.getHeaderView(0).findViewById(R.id.nav_user_email);
        nav_bloodGroup = nav_view.getHeaderView(0).findViewById(R.id.nav_user_bloodGroup);
        aSwitch = findViewById(R.id.availabilitySwitch);


        DocumentReference documentReference = fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                nav_name.setText(value.getString("name"));
                nav_email.setText(value.getString("email"));
                nav_bloodGroup.setText(value.getString("bloodgroup"));
                available = value.getBoolean("available");
                Log.d("Bloodhub*****", "onEvent: " + available);
                aSwitch.setChecked(available);
//                nav_profile_image.setImageBitmap(getBitmapFromURL(value.getString("profilepictureurl")));
//                if(value.hasChild("profilepictureurl")) {
//                        String imageUrl = snapshot.child("profilepictureurl").getValue().toString();
//                        Glide.with(getApplicationContext()).load(imageUrl).into(nav_profile_image);
//                    }
//                    else {
//                        nav_profile_image.setImageResource(R.drawable.profile);
//                    }
//

                    readUsers();
            }
        });

//        aSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                available = !available;
//                aSwitch.setChecked(available);
//                documentReference.update("available", available);
//            }
//        });

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

//                available = aSwitch.isChecked();
//                aSwitch.setChecked(!available);
//                available = !available;
//                documentReference.update("available", available);
                Log.d("BloodHub*****", "onCheckedChanged: ");
//                if(b) {
//                    Toast.makeText(HomepageActivity.this, "Available for blood donation", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(HomepageActivity.this, "Not available for blood donation", Toast.LENGTH_SHORT).show();
//                }
            }
        });

//        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
//                FirebaseAuth.getInstance().getCurrentUser().getUid()
//        );
//
//
//        userRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()) {
//                    String name = snapshot.child("name").getValue().toString();
//                    nav_name.setText(name);
//
//                    String email = snapshot.child("email").getValue().toString();
//                    nav_email.setText(email);
//
//                    String bloodGroup = snapshot.child("bloodgroup").getValue().toString();
//                    nav_bloodGroup.setText(bloodGroup);
//
//                    if(snapshot.hasChild("profilepictureurl")) {
//                        String imageUrl = snapshot.child("profilepictureurl").getValue().toString();
//                        Glide.with(getApplicationContext()).load(imageUrl).into(nav_profile_image);
//                    }
//                    else {
//                        nav_profile_image.setImageResource(R.drawable.profile);
//                    }
//
//                    String phoneNumber = snapshot.child("phone").getValue().toString();
//                    nav_phone_number.setText(phoneNumber);
//
//                    String location = snapshot.child("location").getValue().toString();
//                    nav_location.setText(location);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

    }

    private void readUsers() {
        DocumentReference reference = fstore.collection("users").document(userId);
        Query query = reference.getParent().whereEqualTo("available", true);
        query.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                userList.clear();
                for(DocumentSnapshot documentSnapshot : value.getDocuments()) {
                    if(documentSnapshot.getId() == userId)
                        continue;
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
                    Toast.makeText(HomepageActivity.this, "No available users", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
//

    public static Bitmap getBitmapFromURL(String src) {
        try {
//            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
//            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
//            Log.e("Exception",e.getMessage());
            return null;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(HomepageActivity.this, ProfileActivity.class));
//                finish();
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomepageActivity.this, LoginActivity.class));
//                finish();
                break;
            case R.id.status:
                startActivity(new Intent(HomepageActivity.this, SetStatusActivity.class));
//                finish();
                break;
            case R.id.search:
                startActivity(new Intent(HomepageActivity.this, SearchActivity.class));
//                finish();
                break;
            case R.id.help:
                startActivity(new Intent(HomepageActivity.this, DonateActivity.class));
//                finish();
                break;
            case R.id.feedback:
                sendFeedback();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    private void sendFeedback() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        String UriText = "mailto:" + Uri.encode("imainul494@gmail.com") + "?subject=" + Uri.encode("") + Uri.encode("");
        Uri uri = Uri.parse(UriText);
        intent.setData(uri);
        startActivity(Intent.createChooser(intent,"send email"));
    }
}