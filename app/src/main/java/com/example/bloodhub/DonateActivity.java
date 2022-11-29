package com.example.bloodhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DonateActivity extends AppCompatActivity {

    private Button backButton;
    private ImageView copy;
    private TextView phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        backButton = findViewById(R.id.backbutton);
        copy = findViewById(R.id.copy);
        phone = findViewById(R.id.phone);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
//                startActivity(new Intent(DonateActivity.this, HomepageActivity.class));
            }
        });

        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("TextView", phone.getText().toString());
                clipboardManager.setPrimaryClip(clip);

                Toast.makeText(DonateActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }
}