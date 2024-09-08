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

public class AluminiLoginActivity extends Activity {

    private EditText etTPOId, etTPOPass;
    private Button btnLoginTPO;
    private static final String LOGIN_URL = "http://"+Config.ipAddress+"/PlacementApp/aluminiLogin.php";

    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alumini_login);

        // Initialize EditText and Button
        etTPOId = findViewById(R.id.etTPOid);
        etTPOPass = findViewById(R.id.etTPOPass);
        btnLoginTPO = findViewById(R.id.btnLoginTPO);

        tvRegister = findViewById(R.id.tvRegister);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reg = new Intent(AluminiLoginActivity.this, AluminiRegisterActivity.class);
                startActivity(reg);
            }
        });

        // Set OnClickListener for Login Button
        btnLoginTPO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform login authentication
                final String tpoId = etTPOId.getText().toString().trim();
                final String password = etTPOPass.getText().toString().trim();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    boolean success = jsonResponse.getBoolean("success");

                                    if (success) {
                                        // Login successful, navigate to TPO dashboard
                                        startActivity(new Intent(AluminiLoginActivity.this, AluminiDashboardActivity.class));
                                        Toast.makeText(AluminiLoginActivity.this, "Success...", Toast.LENGTH_SHORT).show();
                                        finish(); // Finish this activity so user cannot go back to login screen
                                    } else {
                                        // Login failed, display error message
                                        Toast.makeText(AluminiLoginActivity.this, "Invalid credentials, please try again", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(AluminiLoginActivity.this, "JSON Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Handle errors
                                Toast.makeText(AluminiLoginActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", tpoId);
                        params.put("password", password);
                        return params;
                    }
                };

                // Add the request to the RequestQueue
                Volley.newRequestQueue(AluminiLoginActivity.this).add(stringRequest);
            }
        });
    }
}
