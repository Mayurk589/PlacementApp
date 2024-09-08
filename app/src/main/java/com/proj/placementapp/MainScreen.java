package com.proj.placementapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainscreen);

        // Get references to all boxes
        LinearLayout box1 = findViewById(R.id.box1);
        LinearLayout box2 = findViewById(R.id.box2);
        LinearLayout box3 = findViewById(R.id.box3);
        LinearLayout box4 = findViewById(R.id.box4);

        // Set click listeners for each box

        box1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click on Admin box
                startActivity(new Intent(MainScreen.this, EventsActivity.class));
            }
        });


        box2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click on Student box
                startActivity(new Intent(MainScreen.this, StudentLoginActivity.class));
            }
        });

        box3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click on TPO box
                startActivity(new Intent(MainScreen.this, TPOLoginActivity.class));
            }
        });

        box4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click on Alumni box
                startActivity(new Intent(MainScreen.this, AluminiLoginActivity.class));
            }
        });
    }
}
