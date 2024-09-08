package com.proj.placementapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StudentLoginActivity extends Activity {

    private EditText etStudentId, etStudentPass;
    private Button btnLoginStudent;
    private static final String LOGIN_URL = "http://"+Config.ipAddress+"/PlacementApp/student_login.php";

    private TextView tvRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_login);

        // Initialize EditText and Button
        etStudentId = findViewById(R.id.etStudentId);
        etStudentPass = findViewById(R.id.etStudentPass);
        btnLoginStudent = findViewById(R.id.btnLoginStudent);


        tvRegister = findViewById(R.id.tvRegister);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg = new Intent(StudentLoginActivity.this, StudentRegisterActivity.class);
                startActivity(reg);
            }
        });


        // Set OnClickListener for Login Button
        btnLoginStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform login authentication
                final String studentId = etStudentId.getText().toString().trim();
                final String password = etStudentPass.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success) {
                                        Toast.makeText(StudentLoginActivity.this, "success", Toast.LENGTH_SHORT).show();

                                        // Login successful, navigate to student dashboard
                                        Intent dashboard = new Intent(StudentLoginActivity.this, StudentDashboardActivity.class);
                                        dashboard.putExtra("PHONE",studentId);
                                        startActivity(dashboard);
                                        finish(); // Finish this activity so user cannot go back to login screen
                                    } else {
                                        // Login failed, display error message
                                        Toast.makeText(StudentLoginActivity.this, "Invalid credentials, please try again", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(StudentLoginActivity.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle errors
                                Toast.makeText(StudentLoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", studentId);
                        params.put("password", password);
                        return params;
                    }
                };

                // Add the request to the RequestQueue
                Volley.newRequestQueue(StudentLoginActivity.this).add(stringRequest);
            }
        });
    }
}
