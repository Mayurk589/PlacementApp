package com.proj.placementapp;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class TPODashboardActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tpo_dashboard);

        // Initialize Buttons
        Button btnViewStudentDetails = findViewById(R.id.btnViewStudentDetails);
        Button btnCreateEvent = findViewById(R.id.btnCreateEvent);
        Button btnExport = findViewById(R.id.btnExport);
        Button btnLogout = findViewById(R.id.btnLogout);

        // Set OnClickListener for View Student Details Button
        btnViewStudentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event for View Student Details Button
                Toast.makeText(TPODashboardActivity.this, "View Student Details clicked", Toast.LENGTH_SHORT).show();
                // Add your logic to navigate to View Student Details activity
                Intent stdDetails = new Intent(TPODashboardActivity.this, ViewStudents.class);
                startActivity(stdDetails);
            }
        });

        // Set OnClickListener for Create Event Button
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event for Create Event Button
                Toast.makeText(TPODashboardActivity.this, "Create Event clicked", Toast.LENGTH_SHORT).show();
                // Add your logic to navigate to Create Event activity
                Intent createEvent = new Intent(TPODashboardActivity.this, CreateEvent.class);
                startActivity(createEvent);
            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event for Create Event Button
                Toast.makeText(TPODashboardActivity.this, "Export Button clicked", Toast.LENGTH_SHORT).show();
                // Add your logic to navigate to Create Event activity
                Intent createEvent = new Intent(TPODashboardActivity.this, Tpo_export_activity.class);
                startActivity(createEvent);
            }
        });
        // Set OnClickListener for Logout Button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click event for Logout Button
                Toast.makeText(TPODashboardActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show();

                Intent logout = new Intent(TPODashboardActivity.this, TPOLoginActivity.class);
                startActivity(logout);
            }
        });
    }
}
