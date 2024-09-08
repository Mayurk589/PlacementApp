package com.proj.placementapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.Iterator;

public class StudentDetails extends Activity implements View.OnClickListener {

    Button btnViewAcademics, btnViewContacts, btnViewEducation, btnViewResume, btnViewInter, btnViewOffer, btnBack;
    String usn,phone;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_details);

        Intent i = getIntent();
        usn = i.getStringExtra("usn");

        Intent p = getIntent();
        phone = p.getStringExtra("phone");

        btnViewAcademics = findViewById(R.id.btnViewAcademics);
        btnViewContacts = findViewById(R.id.btnViewContacts);
        btnViewEducation = findViewById(R.id.btnViewEducation);
        btnViewResume = findViewById(R.id.btnViewResume);
        btnViewInter = findViewById(R.id.btnViewInter);
        btnViewOffer = findViewById(R.id.btnViewOffer);



        btnBack = findViewById(R.id.btnBack);

        btnViewAcademics.setOnClickListener(this);
        btnViewContacts.setOnClickListener(this);
        btnViewEducation.setOnClickListener(this);
        btnViewResume.setOnClickListener(this);
        btnViewInter.setOnClickListener(this);
        btnViewOffer.setOnClickListener(this);

        btnBack.setOnClickListener(this);

        requestQueue = Volley.newRequestQueue(this); // Initialize the request queue
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnViewAcademics:
                String accUrl = "http://" + Config.ipAddress + "/PlacementApp/getAcademicDetails.php?usn=" + usn;
                fetchAndShowDetails(accUrl, "Academic Details");
                break;

            case R.id.btnViewContacts:
                String conUrl = "http://" + Config.ipAddress + "/PlacementApp/getContactDetails.php?usn=" + usn;
                fetchAndShowDetails(conUrl, "Contact Details");
                break;

            case R.id.btnViewEducation:
                String eduUrl = "http://" + Config.ipAddress + "/PlacementApp/getEducationalInfo.php?usn=" + usn;
                fetchAndShowDetails(eduUrl, "Educational Details");
                break;

            case R.id.btnViewResume:
                String url = "http://" + Config.ipAddress + "/PlacementApp/getResume.php?usn=" + usn;
                Toast.makeText(StudentDetails.this, "Opening Resume", Toast.LENGTH_SHORT).show();
                Intent displayPdfIntent = new Intent(StudentDetails.this, displayPdf.class);
                displayPdfIntent.putExtra("url", url);
                startActivity(displayPdfIntent);
                break;

            case R.id.btnViewInter:
                String url1 = "http://" + Config.ipAddress + "/PlacementApp/getInterview.php?usn=" + usn;
                Toast.makeText(StudentDetails.this, "Opening Interview Experience", Toast.LENGTH_SHORT).show();
                Intent displayPdfIntent1 = new Intent(StudentDetails.this, displayPdf.class);
                displayPdfIntent1.putExtra("url", url1);
                startActivity(displayPdfIntent1);
                break;


            case R.id.btnViewOffer:
                String url2 = "http://" + Config.ipAddress + "/PlacementApp/getOffer.php?phone=" + phone;
                Toast.makeText(StudentDetails.this, "Opening Offer Letter", Toast.LENGTH_SHORT).show();
                Intent displayPdfIntent2 = new Intent(StudentDetails.this, displayPdf.class);
                displayPdfIntent2.putExtra("url", url2);
                startActivity(displayPdfIntent2);
                break;

            case R.id.btnBack:
                Intent logout = new Intent(StudentDetails.this, ViewStudents.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(logout);
                finish(); // Finish the current activity after logout
                break;
        }
    }

    private void fetchAndShowDetails(String url, String dialogTitle) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showDetailsDialog(response, dialogTitle);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showErrorDialog("Failed to fetch data. Please try again.");
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);
    }

    private void showDetailsDialog(JSONObject details, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        // Build the message to display based on the JSON response
        StringBuilder message = new StringBuilder();


        Iterator<String> keys = details.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            message.append(key).append(": ").append(details.optString(key)).append("\n");
        }


        builder.setMessage(message.toString());
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
