package com.proj.placementapp;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class
AluminiDashboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alumini_dashboard_activity);

        // Initialize Buttons
        Button btnCreateEvents = findViewById(R.id.createEventButton);



        btnCreateEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event for Create Event Button
                Toast.makeText(AluminiDashboardActivity.this, "Create Event clicked", Toast.LENGTH_SHORT).show();
                // Add your logic to navigate to Create Event activity
                Intent createEvent = new Intent(AluminiDashboardActivity.this, AluminiEventActivity.class);
                startActivity(createEvent);
            }
        });


    }
}
