package com.proj.placementapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class AcademicDetailsActivity extends Activity {

    private EditText etName, etPhone, etSemester, etYear, etPercentage, etCGPL, etUsn, etBranch;
    private Button btnSubmit;
    private String url = Config.academicDetails;

    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.academic_details);

        Intent i = getIntent();
        phoneNumber = i.getStringExtra("phoneNumber");



        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etSemester = findViewById(R.id.etSemester);
        etYear = findViewById(R.id.etYear);
        etPercentage = findViewById(R.id.etPercentage);
        etCGPL = findViewById(R.id.etCGPL);
        etUsn = findViewById(R.id.etUsn);
        etBranch = findViewById(R.id.etBranch);
        btnSubmit = findViewById(R.id.btnSubmit);

        etPhone.setText(phoneNumber);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer();
            }
        });
    }

    private void sendDataToServer() {
        final String name = etName.getText().toString().trim();
        final String phone = etPhone.getText().toString().trim();
        final String usn = etUsn.getText().toString().trim();
        final String branch = etBranch.getText().toString().trim();
        final String semester = etSemester.getText().toString().trim();
        final String year = etYear.getText().toString().trim();
        final String percentage = etPercentage.getText().toString().trim();
        final String cgpl = etCGPL.getText().toString().trim();

        // Check if any field is empty
        if (name.isEmpty() || phone.isEmpty() || usn.isEmpty() || branch.isEmpty() || semester.isEmpty() ||
                year.isEmpty() || percentage.isEmpty() || cgpl.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Details Uploaded Successfully", Toast.LENGTH_SHORT).show());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("phone", phone);
                params.put("usn", usn);
                params.put("branch", branch);
                params.put("semester", semester);
                params.put("year", year);
                params.put("percentage", percentage);
                params.put("cgpl", cgpl);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }


}

