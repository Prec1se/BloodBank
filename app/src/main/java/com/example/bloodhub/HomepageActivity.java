package com.example.bloodhub;

import static com.example.bloodhub.SplashScreenActivity.setWindowFlag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomepageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private NavigationView nav_view;

    private CircleImageView nav_profile_image;
    private TextView nav_name, nav_email, nav_bloodGroup, nav_phone_number, nav_location;
//    MenuItem logout;

    private DatabaseReference userRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
//
//        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
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

        nav_profile_image = nav_view.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_name = nav_view.getHeaderView(0).findViewById(R.id.nav_user_name);
        nav_email = nav_view.getHeaderView(0).findViewById(R.id.nav_user_email);
        nav_bloodGroup = nav_view.getHeaderView(0).findViewById(R.id.nav_user_bloodGroup);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );


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
//
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.profile:
                startActivity(new Intent(HomepageActivity.this, ProfileActivity.class));
                finish();
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(HomepageActivity.this, LoginActivity.class));
                finish();

        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}