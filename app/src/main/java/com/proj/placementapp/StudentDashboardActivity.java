package com.proj.placementapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StudentDashboardActivity extends Activity {

    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard);

        Intent i = getIntent();
        phoneNumber = i.getStringExtra("PHONE");

        Button academicDetailsButton = findViewById(R.id.btnAcademicDetails);
        Button contactDetailsButton = findViewById(R.id.btnContactDetails);
        Button educationalDetailsButton = findViewById(R.id.btnEducationalDetails);
        Button resumeButton = findViewById(R.id.btnResume);

        Button feedbackButton = findViewById(R.id.btnFeedBack);
        Button placementDetailsButton = findViewById((R.id.btnPlacementDetails));

        Button logoutButton = findViewById(R.id.btnLogout);

        // Set onClickListeners for each button
        academicDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(AcademicDetailsActivity.class);
            }
        });

        contactDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(ContactDetailsActivity.class);
            }
        });

        educationalDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchActivity(EducationalDetailsActivity.class);
            }
        });

        resumeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launchActivity(ResumeActivity.class);

                Intent upload = new Intent(StudentDashboardActivity.this, UploadResumeActivity.class);
                upload.putExtra("phoneNumber", phoneNumber);
                startActivity(upload);
            }
        });

        feedbackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launchActivity(ResumeActivity.class);

                Intent feedback = new Intent(StudentDashboardActivity.this, Experience_Student.class);
                feedback.putExtra("phoneNumber", phoneNumber);
                startActivity(feedback);
            }
        });

        placementDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launchActivity(PlacementDetailsActivity.class);

            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logout action here
                Intent logout = new Intent(StudentDashboardActivity.this, StudentLoginActivity.class);
                startActivity(logout);
                finish();
            }
        });

    }

    // Method to launch activities
    private void launchActivity(Class<?> cls) {
        Intent intent = new Intent(StudentDashboardActivity.this, cls);
        intent.putExtra("phoneNumber", phoneNumber); // Pass phone number to the next activity
        startActivity(intent);
    }

}

