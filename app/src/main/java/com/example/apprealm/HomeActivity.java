package com.example.apprealm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apprealm.Firebase.FirebaseActivity;
import com.example.apprealm.RealM.MainActivity;
import com.example.apprealm.Room.roomActivity;

public class HomeActivity extends AppCompatActivity {
    Button btnRealm, btnRoom, btnFb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnFb = findViewById(R.id.btnFb);
        btnRoom = findViewById(R.id.btnRoom);
        btnRealm = findViewById(R.id.btnRealm);

        btnFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FirebaseActivity.class);
                startActivity(intent);
            }
        });
        btnRealm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btnRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, roomActivity.class);
                startActivity(intent);
            }
        });
    }
}