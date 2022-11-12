package com.example.bloodhub;

import static android.content.ContentValues.TAG;
import static com.example.bloodhub.SplashScreenActivity.setWindowFlag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    String[] locations = {"Dhaka", "Barisal", "Chittagong", "Rangpur", "Rajshahi", "Khulna", "Sylhet"};
    String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+" , "AB-", "O+", "O-"};
    private ArrayAdapter<String> bgItems, lItems;
    private AutoCompleteTextView bloodGroup, location;
    private TextView backButton;
    private Button register;
    private EditText name, phoneNumber, email, password, confirmPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;
    private CircleImageView profileImage;
    private Uri resultUri, filePath;
    private final int PICK_IMAGE_REQUEST = 22;
    private FirebaseFirestore fstore;
    private String userID;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_registration);

        name = findViewById(R.id.name);
        phoneNumber = findViewById(R.id.phoneNumber);
        backButton = findViewById(R.id.backButton);
        bloodGroup = findViewById(R.id.bloodGroup);
        location = findViewById(R.id.location);
        profileImage = findViewById(R.id.profile_image);
        register = findViewById(R.id.registerButton);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

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
                SelectImage();
                uploadImage();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        getSupportActionBar().setTitle("Sign up");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

    }

//    @SuppressLint("NonConstantResourceId")
//    @Override
//    public void onClick(View view) {
//        if(view.getId() == R.id.registerButton)
//                createAccount();
//        return;
//    }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getSupportActionBar().hide();
//
//        setContentView(R.layout.activity_registration);
//
//        backButton = findViewById(R.id.backButton);
//        bloodGroup = findViewById(R.id.bloodGroup);
//        location = findViewById(R.id.location);
//        profileImage = findViewById(R.id.profile_image);
//        register = findViewById(R.id.registerButton);
//        email = findViewById(R.id.registerEmail);
//        password = findViewById(R.id.password);
//        confirmPassword = findViewById(R.id.confirmPassword);
//        mAuth = FirebaseAuth.getInstance();
//        fstore = FirebaseFirestore.getInstance();
//
//        bgItems = new ArrayAdapter<>(this, R.layout.list_item, bloodGroups);
//        lItems = new ArrayAdapter<>(this, R.layout.list_item, locations);
//
//        bloodGroup.setAdapter(bgItems);
//        location.setAdapter(lItems);
//
//        bloodGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                String item = parent.getItemAtPosition(position).toString();
//                Toast.makeText(getApplicationContext(), "You won't be able to change your blood group later", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        location.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String item = parent.getItemAtPosition(position).toString();
//                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        profileImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent, 1);
//            }
//        });
//        backButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
//                startActivity(intent);
//            }
//        });
//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String n = name.getText().toString().trim();
//                String pn = phoneNumber.getText().toString().trim();
//                String e = email.getText().toString().trim();
//                String p = password.getText().toString();
//                String cp = confirmPassword.getText().toString();
//                if(TextUtils.isEmpty(n)) {
//                    name.setError("Name is required");
//                    return;
//                }
//                if(TextUtils.isEmpty(pn)) {
//                    phoneNumber.setError("Phone Number is required");
//                    return;
//                }
//                if(TextUtils.isEmpty(e)) {
//                    email.setError("An email address is required!");
//                    return;
//                }
//                if(TextUtils.isEmpty(p)) {
//                    password.setError("You can't sign up without a password!");
//                    return;
//                }
//                if(p != cp) {
//                    confirmPassword.setError("Passwords don't match");
//                    return;
//                }
//                mAuth.createUserWithEmailAndPassword(e, p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(!task.isSuccessful()) {
//                            String error = task.getException().toString();
//                            Toast.makeText(RegistrationActivity.this, "Error: " + error, Toast.LENGTH_SHORT).show();
//                        }
//                        else {
//                            String currentUserId = mAuth.getCurrentUser().getUid();
//                            DocumentReference documentReference = fstore.collection("users").document(currentUserId);
//                            Map<String,Object>user = new HashMap<>();
//                            user.put("name",name);
//                            user.put("email",email);
//                            user.put("phone",phoneNumber);
//
//                            documentReference.set(user).addOnSuccessListener(unused ->
//                                            Log.d(TAG, "user profile is created for " + currentUserId))
//                                    .addOnFailureListener(e ->
//                                            Log.d(TAG, e.toString()));
//
////                            userDatabaseRef = FirebaseDatabase.getInstance().getReference()
////                                    .child("User").child(currentUserId);
////                            HashMap userInfo = new HashMap();
////                            userInfo.put("Email", email);
////                            userInfo.put("Name", name);
////                            userInfo.put("Phone Number", phoneNumber);
////
////                            userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
////                                @Override
////                                public void onComplete(@NonNull Task task) {
////                                    if(task.isSuccessful()) {
////                                        Toast.makeText(RegistrationActivity.this, "Data Set Successfully", Toast.LENGTH_SHORT).show();
////                                    }
////                                    else {
////                                        Toast.makeText(RegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
////                                    }
////                                    finish();
////                                }
////                            });
//
//                            if(resultUri != null) {
//                                final StorageReference filePath = FirebaseStorage.getInstance().getReference()
//                                        .child("profile images").child(currentUserId);
//                                Bitmap bitmap = null;
//
//                                try {
//                                    bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
//                                }catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//                                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
//                                byte[] data = byteArrayOutputStream.toByteArray();
//                                UploadTask uploadTask = filePath.putBytes(data);
//
//                                uploadTask.addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(RegistrationActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                                    @Override
//                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                        if(taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {
//                                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
//                                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                                @Override
//                                                public void onSuccess(Uri uri) {
//                                                    String imageUrl = uri.toString();
//                                                    Map newImageMap = new HashMap();
//                                                    newImageMap.put("profilepictureurl", imageUrl);
//
//                                                    userDatabaseRef.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task task) {
//                                                            if(task.isSuccessful()) {
//                                                                Toast.makeText(RegistrationActivity.this, "Image url added to database successfully", Toast.LENGTH_SHORT).show();
//                                                            }
//                                                            else {
//                                                                Toast.makeText(RegistrationActivity.this, "Error: " + task.getException(), Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        }
//                                                    });
////                                                    finish();
//                                                }
//                                            });
//                                        }
//                                    }
//                                });
//
////                                Intent intent = new Intent(RegistrationActivity.this, HomepageActivity.class);
////                                startActivity(intent);
////                                finish();
//
//                            }
//                            Intent intent = new Intent(RegistrationActivity.this, HomepageActivity.class);
//                            startActivity(intent);
//                            finish();
//
//                        }
//                    }
//                });
////                Intent inten
//            }
//        });
//    }

    private void createAccount() {

        String n, p, e, pn, cp, bg, l, pp;

        n = name.getText().toString().trim();
        p = password.getText().toString().trim();
        e = email.getText().toString().trim();
        pn = phoneNumber.getText().toString().trim();
        cp = confirmPassword.getText().toString().trim();
        bg = bloodGroup.getText().toString();
        l = location.getText().toString();


        if (n.isEmpty()) {
            name.setError("Enter a First Name");
            name.requestFocus();
            return;
        }

        if (pn.isEmpty()) {
            phoneNumber.setError("Enter a Phone Number");
            phoneNumber.requestFocus();
            return;
        }

        if (e.isEmpty()) {
            email.setError("Enter Your Email Address");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
            email.setError("Provide a Valid Email Address");
            email.requestFocus();
            return;
        }

        if (p.isEmpty()) {
            password.setError("Enter a Password");
            password.requestFocus();
            return;
        }

        if (p.length() < 6) {
            password.setError("Password must contain at least 6 characters");
            password.requestFocus();
            return;
        }

        if (!cp.equals(p)) {
            confirmPassword.setError("Passwords does not match");
            confirmPassword.requestFocus();
            return;
        }

        final ProgressDialog pd = new ProgressDialog(RegistrationActivity.this);
        pd.setMessage("Please wait...");
        pd.show();

        mAuth.createUserWithEmailAndPassword(e, p)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        assert firebaseUser != null;

//                        firebaseUser.sendEmailVerification().addOnCompleteListener(task1 -> {
//                            if(task1.isSuccessful()){
//
//                                Toast.makeText(RegistrationActivity.this, "Check email to verify your account", Toast.LENGTH_LONG).show();
//                                //                        redirect to login page
//                                FirebaseAuth.getInstance().signOut();
//                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
//                                finish();
//                            }
//                            else
//                            {
//                                // email not sent, so display message and restart the activity or do whatever you wish to do
//                                Toast.makeText(RegistrationActivity.this, task1.getException().getMessage(), Toast.LENGTH_LONG).show();
//                                //restart this activity
//                                overridePendingTransition(0, 0);
//                                finish();
//                                overridePendingTransition(0, 0);
//                                startActivity(getIntent());
//                            }
//                        });

                        userID = mAuth.getCurrentUser().getUid();

                        DocumentReference documentReference = fstore.collection("users").document(userID);
                        Map<String,Object>user = new HashMap<>();
                        user.put("name",n);
                        user.put("email",e);
                        user.put("phone",pn);
                        user.put("bloodgroup", bg);
                        user.put("division", l);
                        user.put("available", false);

                        documentReference.set(user).addOnSuccessListener(unused ->
                                        Log.d(TAG, "user profile is created for " + userID))
                                .addOnFailureListener(ex ->
                                        Log.d(TAG, ex.toString()));
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                    } else {
                        Toast.makeText(RegistrationActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    pd.dismiss();

                });
    }

    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(RegistrationActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(RegistrationActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                profileImage.setImageBitmap(bitmap);
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }
}