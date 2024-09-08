package com.proj.placementapp;


import android.app.Activity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StudentRegisterActivity extends Activity {

    private EditText etRegName, etRegPhone, etRegPassword, etRegEmail, etRegPlace,etRegUSN, etOtp;
    private Button btnRegRegister;
    private static final String REGISTER_URL = "http://"+Config.ipAddress+"/PlacementApp/register_student.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Initialize EditText and Button
        etRegName = findViewById(R.id.etRegName);
        etRegPhone = findViewById(R.id.etRegPhone);
        etRegPassword = findViewById(R.id.etRegPassword);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPlace = findViewById(R.id.etRegPlace);
        etRegUSN = findViewById(R.id.etRegUSN);

        btnRegRegister = findViewById(R.id.btnRegRegister);


        // Set OnClickListener for Register Button
        btnRegRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform registration
                final String name = etRegName.getText().toString().trim();
                final String phone = etRegPhone.getText().toString().trim();
                final String password = etRegPassword.getText().toString().trim();
                final String email = etRegEmail.getText().toString().trim();
                final String place = etRegPlace.getText().toString().trim();
                final String usn = etRegUSN.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success) {
                                        // Registration successful
                                        Toast.makeText(StudentRegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                        // Redirect to login activity or dashboard
                                    } else {
                                        // Registration failed
                                        Toast.makeText(StudentRegisterActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(StudentRegisterActivity.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle errors
                                Toast.makeText(StudentRegisterActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("name", name);
                        params.put("phone", phone);
                        params.put("password", password);
                        params.put("email", email);
                        params.put("place", place);
                        params.put("usn", usn);
                        return params;
                    }
                };

                // Add the request to the RequestQueue
                Volley.newRequestQueue(StudentRegisterActivity.this).add(stringRequest);
            }
        });

        // Set OnClickListener for Get OTP Button

    }
}
